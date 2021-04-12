package com.app.transporter.utility;

public class TMessage {

	// http://Domain + host
	public String fullAddress;

	public String body;

	public boolean updateStatus;

	public TMessage() {
		fullAddress = "";
		body = "";
		updateStatus = false;
	}

	public TMessage(String fullAddress, String body, boolean updateStatus) {
		super();
		this.fullAddress = fullAddress;
		this.body = body;
		this.updateStatus = updateStatus;
	}

}
