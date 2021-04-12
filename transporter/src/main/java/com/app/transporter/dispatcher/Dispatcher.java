package com.app.transporter.dispatcher;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import com.app.transporter.utility.ServerInfo;
import com.app.transporter.utility.TMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.typed.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.japi.function.Function;


/**
 * <h2>Requests controller responsibilities:</h2>
 * <br>Cache existing servers. When a new server is added the dispatcher ensures that the server has the database synchronized with the rest of the servers.
 * <br>Forwards requests to the nearest server based on longitude and latitude.
 * Decides based on the received message if the request should be passed to the remaining servers for synchronization.
 * <br>A simple scenario: 
 * <br>client -> request(POST,package) -> dispatcher 
 * <br>dispatcher -> request(POST,package) -> nearest server
 * <br>nearest server -> response(POST,TMessage) -> dispatcher
 * <br>dispatcher -> response(POST,TMessage.body) -> client
 * <br>dispatcher -> request(PUT,package) -> [rest of the servers]
 * <br>The last step represents the synchronization of the servers.
 * @author Victor
 *
 */
public class Dispatcher extends AllDirectives {
	

	private final ActorSystem<Void> system;
	
	//        'host:port' -> serverInfo
	private Map<String, ServerInfo> availableServers = new LinkedHashMap<>();

	public Dispatcher(ActorSystem<Void> system) {
		this.system = system;
	}

	/**
	 * Dispatch handler. <br>
	 * /register - server registration
	 * <br>Rest of requests are forwarded to closest server.
	 * <br>The closest server will respond with the answer and the dispatcher is responsible for returning the JSON information to client.
	 * <br>POST requests are also forwarded to the rest of the cached servers for synchronization.
	 * @return forwards the nearest server response to the client.
	 */
	public Function<HttpRequest, CompletionStage<HttpResponse>> getDispatcherHandler() {
		return request -> {
			Uri uri = request.getUri();
			String attributes = uri.query().toString();
			String query = !attributes.isEmpty() ? "?" + attributes : "";
			String queryPath = uri.getPathString() + query;

			// Server registration request
			if (queryPath.contains("/register")) {
				cacheServer(uri);

				// Create full synchronization of the DB for the new server
				if (availableServers.size() > 1) {
					return syncAllDataForTheNewServer();
				}
				return null;
			}

			//Basic requests
			final ServerInfo closestServer = getNearestServer();
			final String link = closestServer.getRequestLink(queryPath);
			HttpRequest requestToServer = HttpRequest.create(link)
					.withHeaders(request.getHeaders())
					.withMethod(request.method())
					.withEntity(request.entity());
			
			//Forward the request to the closest server and get the rough response.
			var roughResponse = Http.get(system).singleRequest(requestToServer);
			
			//Filter the response for client by extracting the 'body' from the TMessage.
			//Steps: 1. transform the response into a future message (TMessage)
			//       2. return to the client just the body of the available message
			//       3. sync the rest of the servers
			var filteredResponse = roughResponse.thenCompose(httpResponse -> {
				var message = Jackson.unmarshaller(TMessage.class).unmarshal(httpResponse.entity(), system);
				return message.thenApply(manageResponse(requestToServer,closestServer));
			});
			
			return filteredResponse;
		};
	}
	
	/**
	 * Retrieve the DBSchema with all data from queue and forward it to the new server.
	 * @return
	 */
	CompletionStage<HttpResponse> syncAllDataForTheNewServer() {
		var firstServer = availableServers.values().stream().findFirst().get();
		String link = firstServer.getRequestLink("/dbSchema");
		HttpRequest requestToServer = HttpRequest.GET(link);
		return Http.get(system).singleRequest(requestToServer).thenCompose(httpResponse -> {
			var ftMessage = Jackson.unmarshaller(TMessage.class).unmarshal(httpResponse.entity(), system);
			return ftMessage.thenApply(ms -> {
				try {
					String writeValueAsString = new ObjectMapper().writeValueAsString(ms);
					return HttpResponse.create().withEntity(ContentTypes.APPLICATION_JSON, writeValueAsString);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return null;
			});
		});
	}
	
	/**
	 * After receiving the JSON response from the nearest server return the 'body - response' to the client.
	 * <BR>In parallel (asynchronous) the rest of the available servers gets synchronized. 
	 * @param oldRequest
	 * @param closestServer
	 * @return
	 */
	private java.util.function.Function<TMessage,HttpResponse> manageResponse(HttpRequest oldRequest, ServerInfo closestServer){
		return  messesage -> {
			runAsync(() -> synchronizeServers(messesage, oldRequest, closestServer));
			return HttpResponse.create().withEntity(ContentTypes.APPLICATION_JSON, messesage.body);
		};
	}
	
	private void synchronizeServers(TMessage message, HttpRequest oldRequest, ServerInfo closestServer) {
			boolean informServers = message.updateStatus;
			if (informServers) {
				var restOfServers = availableServers.values().stream()
						.filter(svr -> !svr.compareServerInfo(closestServer))
						.collect(toList());
				
				//create PUT requests for the servers - alternatively POST ca be used
				for (var server : restOfServers) {
					String queryPath = oldRequest.getUri().getPathString();
					String link = server.getRequestLink(queryPath);
					HttpRequest requestToServer = HttpRequest
							.PUT(link)
							.withHeaders(oldRequest.getHeaders())
							.withEntity(oldRequest.entity());
					Http.get(system).singleRequest(requestToServer);
				}
			}
	}

	//TODO: cache maybe the client and also the location
	//TODO: this is wrong for not just extract the first server
	//TODO: create a method or something some formula ffs my life
	private ServerInfo getNearestServer() {
		return availableServers.values().stream().findFirst().get();
	}
	
	private void cacheServer(Uri uri) {
		Query query = uri.query();
		String host = query.getOrElse("host", "");
		String port = query.getOrElse("port", "");
		String address = host + ":" + port;
		if (!availableServers.containsKey(address)) {
			Long longitude = Long.parseLong(query.getOrElse("longitude", "0"));
			Long latitude = Long.parseLong(query.getOrElse("latitude", "0"));
			ServerInfo server = new ServerInfo(host, port, longitude, latitude);
			availableServers.put(address, server);
		}
	}

}
