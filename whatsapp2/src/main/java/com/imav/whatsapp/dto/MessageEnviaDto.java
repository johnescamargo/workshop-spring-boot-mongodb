package com.imav.whatsapp.dto;

import java.util.List;

public class MessageEnviaDto {

	private String internacionalCode;
	private String phone;
	private String name;
	private List<String> messages;

	public MessageEnviaDto(String internacionalCode, String phone, String name, List<String> messages) {
		this.internacionalCode = internacionalCode;
		this.phone = phone;
		this.name = name;
		this.messages = messages;
	}

	public MessageEnviaDto() {

	}

	public String getInternacionalCode() {
		return internacionalCode;
	}

	public void setInternacionalCode(String internacionalCode) {
		this.internacionalCode = internacionalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
