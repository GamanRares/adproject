package com.app.transporter.db.entities;

public class Courier {
	
	public Integer id;
	public String name;
	public String password;

	public Courier() {
		id = 0;
		name = "";
		password = "";
	}

	public Courier(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

}
