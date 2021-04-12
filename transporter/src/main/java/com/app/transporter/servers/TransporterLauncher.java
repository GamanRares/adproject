package com.app.transporter.servers;

import org.dalesbred.Database;

import com.app.transporter.utility.DBBinder;
import com.app.transporter.utility.ServerInfo;
import com.google.inject.Guice;
import com.google.inject.Injector;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;

/**
 * Hello world!
 *
 */
public class TransporterLauncher extends AllDirectives {


	public static void main(String[] args) throws Exception {
		final Database db = Database.forUrlAndCredentials("jdbc:mysql://localhost:3306/firstdb", "abcuser", "abcpassword");
		Injector injector = Guice.createInjector(new DBBinder(db));
		ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "routes");
		final Http http = Http.get(system);
		ServerInfo serverInfo = new ServerInfo("localhost", "8090", 10, 10);
		TransporterServer transporterServer = new TransporterServer(system, serverInfo);
		injector.injectMembers(transporterServer);
		http.newServerAt("localhost", 8090).bind(transporterServer.routeTree());
		transporterServer.registerServerToDispatcher();
	}

}
