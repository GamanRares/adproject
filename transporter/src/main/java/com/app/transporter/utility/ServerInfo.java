package com.app.transporter.utility;

import java.util.Map;

public class ServerInfo {
	public String host;
	public String port;
	public long latitude, longitude;

	public ServerInfo(String host, String port, long latitude, long longitude) {
		this.host = host;
		this.port = port;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	//E.g: http://domain.name.com:9090
	public String getDomainAddress() {
		return "http://" + host + ":" + port;
	}
	
	//domain address + queries
	public String getRequestLink(final String query) {
		if (query.startsWith("/")) {
			return getDomainAddress() + query;
		}
		return getDomainAddress() + "/" + query;
	}
	
	public Map<String,String> resistrationQuerry() {
		return Map.of(
				"host", host,
				"port", port,
				"longitude", Long.toString(longitude),
				"latitude", Long.toString(latitude));
				
	}
	
	public boolean compareServerInfo(ServerInfo anotherServer) {
		return this.getDomainAddress().equals(anotherServer.getDomainAddress());
	}
}
