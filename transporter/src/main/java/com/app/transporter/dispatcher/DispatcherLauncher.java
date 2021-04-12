package com.app.transporter.dispatcher;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;

/**
 * Hello world!
 *
 */
public class DispatcherLauncher {

	public static ActorSystem<Void> system;

	public static void main(String[] args) throws Exception {
		
		system = ActorSystem.create(Behaviors.empty(), "routes");
		final Http http = Http.get(system);
		Dispatcher dispatcher = new Dispatcher(system);

		// createServer and bind the handler for the requests
		http.newServerAt("localhost", 8080).bind(dispatcher.getDispatcherHandler());
	}
}
