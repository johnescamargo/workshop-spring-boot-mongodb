package com.imav.whatsapp.dto;

import java.util.List;

public class MessageInitDTO {

	private String id;
	private String date;
	private String time;
	private String name;
	private String phone;
	private String doctor;
	private String service;
	private List<String> messages;

	public MessageInitDTO(String date, String time, String name, String phone, String doctor, String service,
			List<String> messages) {
		this.date = date;
		this.time = time;
		this.name = name;
		this.phone = phone;
		this.doctor = doctor;
		this.service = service;
		this.messages = messages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getdoctor() {
		return doctor;
	}

	public void setdoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getservice() {
		return service;
	}

	public void setservice(String service) {
		this.service = service;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> message) {
		this.messages = message;
	}

	@Override
	public String toString() {
		return "MessageDTO [id=" + id + ", date=" + date + ", time=" + time + ", name=" + name + ", phone=" + phone
				+ ", doctor=" + doctor + ", service=" + service + ", messages=" + messages + "]";
	}

}
