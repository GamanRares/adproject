package com.app.transporter.servers;


import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.app.transporter.db.entities.Courier;
import com.app.transporter.db.entities.Package;
import com.app.transporter.db.repositories.CourierRepository;
import com.app.transporter.db.repositories.PackageRepository;
import com.app.transporter.db.repositories.UniversalRepository;
import com.app.transporter.utility.ServerInfo;
import com.app.transporter.utility.TMessage;
import com.google.inject.Inject;

import akka.actor.typed.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;

//TODO: constant the dispatcher address
public class TransporterServer extends AllDirectives {
	
	@Inject private UniversalRepository universalRepo;
	@Inject private CourierRepository courierRepo;
	@Inject private PackageRepository packageRepo;
	
	//Server consumables
	public ActorSystem<Void> system;
	public ServerInfo serverInfo;
	
	public TransporterServer(ActorSystem<Void> system, ServerInfo serverInfo) {
		this.system = system;
		this.serverInfo = serverInfo;
	}

	public Route routeTree() {
		return concat(

				path("dbSchema", retriveAllSchema),
				
				path("login", login()),

				get(GET()),

				post(POST()),

				post(DELETE()),

				put(PUT())
				
				);

	}
	
	//TODO:add pass in db and role
	private Supplier<Route> login() {
		return () -> parameter("username", username ->
        			 parameter("password", password -> { return complete("sad"); }));
	}
	
	
	private Supplier<Route> POST() {
		return update(true);
	}
	
	private Supplier<Route> PUT() {
		return update(false);
	}
	
	private Supplier<Route> update(boolean statusMessage) {
		return () -> concat(
				path("auth",() -> entity(CourierRepository.unmarshaller, register(statusMessage))),
				path("packages",() -> entity(PackageRepository.unmarshaller, savePackage(statusMessage)))
				);
	}
	
	
	private Supplier<Route> DELETE() {
		return null;
	}
	
	private Supplier<Route> GET() {
		return () -> concat(
				path("packages", getPackageByID),
				path("packages", getPackages),
				path("couriers", getCourierByID),
				path("couriers", getCouriers)
				);
	}

	
	// Couriers
	private Function<Courier, Route> register(boolean updateStatus) {
		return courier -> {
			courierRepo.save(courier);
			TMessage message = new TMessage(serverInfo.getDomainAddress(), courierRepo.toJSON(courier), updateStatus);
			return complete(StatusCodes.OK, message, Jackson.marshaller());
		};
	}
	
	private Supplier<Route> getCouriers = () -> {
		String couriers = courierRepo.extractAllRowsToJSON();
		TMessage tm = new TMessage(serverInfo.getDomainAddress(), couriers, false);
		return complete(StatusCodes.OK, tm, Jackson.<TMessage>marshaller());
	};

	private Supplier<Route> getCourierByID = () -> parameter(StringUnmarshallers.INTEGER, "id", id -> {
		var pack = courierRepo.searchById(id).get();
		TMessage message = new TMessage(serverInfo.getDomainAddress(), courierRepo.toJSON(pack), false);
		return complete(StatusCodes.OK, message, Jackson.marshaller());
	});
	
	
	// Packages
	private Function<Package, Route> savePackage(boolean updateStatus) {
		return pack -> {
			packageRepo.save(pack);
			TMessage message = new TMessage(serverInfo.getDomainAddress(), packageRepo.toJSON(pack), updateStatus);
			return complete(StatusCodes.OK, message, Jackson.marshaller());
		};
	}
	
	private Supplier<Route> getPackages = () -> {
		String packages = packageRepo.extractAllRowsToJSON();
		TMessage tm = new TMessage(serverInfo.getDomainAddress(), packages, false);
		return complete(StatusCodes.OK, tm, Jackson.<TMessage>marshaller());
	};
	
	
	private Supplier<Route> getPackageByID = () -> parameter("id", id -> {
		var pack = packageRepo.searchById(Integer.parseInt(id)).get();
		TMessage message = new TMessage(serverInfo.getDomainAddress(), packageRepo.toJSON(pack), false);
		return complete(StatusCodes.OK, message, Jackson.marshaller());
	});
	
	//Server registration
	private Consumer<TMessage> syncServer() {
		return message -> {
			universalRepo.insertAllDBSchema(message.body);
		};
	}
	
	Supplier<Route> retriveAllSchema = () -> {
		String dbSchema = universalRepo.createDBSchema();
		TMessage tm = new TMessage(serverInfo.getDomainAddress(), dbSchema, true);
		return complete(StatusCodes.OK, tm, Jackson.<TMessage>marshaller());
	};

	//TODO: display a message when the registration is done in the dispatcher and after that here
	/**
	 * Creates an request to the dispatcher
	 */
	public void registerServerToDispatcher() {
		String hardcoded = "http://localhost:8080/register";
		Query serverInfoQuery = Query.create(serverInfo.resistrationQuerry());
		Uri dispatcherURI = Uri.create(hardcoded).query(serverInfoQuery);
		HttpRequest request = HttpRequest.create(dispatcherURI.toString());
		Http.get(system).singleRequest(request).thenAccept(response -> {
			CompletionStage<TMessage> unmarshal = Jackson.unmarshaller(TMessage.class).unmarshal(response.entity(), system);
			unmarshal.thenAccept(syncServer());
		});
	}

}
