package com.imav.whatsapp.model;

public class ResponseMessageSent {

	private String id;
	private boolean sent;

	public ResponseMessageSent() {

	}

	public ResponseMessageSent(String id, boolean sent) {
		this.id = id;
		this.sent = sent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	@Override
	public String toString() {
		return "ResponseMessageSent [id=" + id + ", sent=" + sent + "]";
	}

}
