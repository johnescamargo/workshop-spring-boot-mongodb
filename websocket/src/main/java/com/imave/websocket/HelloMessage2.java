package com.imave.websocket;

public class HelloMessage2 {

	private String name;
	private String message;

	public HelloMessage2() {
	}

	public HelloMessage2(String name, String message) {
		this.name = name;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
