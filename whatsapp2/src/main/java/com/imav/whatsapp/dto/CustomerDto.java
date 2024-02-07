package com.imav.whatsapp.dto;

public class CustomerDto {

	private String phone_number;

	private String name;

	private int timestamp;

	private boolean talk;

	public CustomerDto(String phone_number, String name, int timestamp, boolean talk) {
		this.phone_number = phone_number;
		this.name = name;
		this.timestamp = timestamp;
		this.talk = talk;
	}

	public CustomerDto() {

	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isTalk() {
		return talk;
	}

	public void setTalk(boolean talk) {
		this.talk = talk;
	}

	@Override
	public String toString() {
		return "CustomerDto [phone_number=" + phone_number + ", name=" + name + ", timestamp=" + timestamp + ", talk="
				+ talk + "]";
	}

}
