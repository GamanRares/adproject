package com.app.transporter.db.entities;

public class Package {
	
	public Integer id;
	
	public String title;

	public Package() {
		id = -1;
		title = "";
	}

	public Package(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	
}
