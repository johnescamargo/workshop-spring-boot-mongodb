package com.imav.whatsapp.dto;

public class MessageDto {

	private String id;

	private Integer status;

	private String name;

	private String phone_from;

	private String text;

	private String type;

	private String timestamp;

	public MessageDto() {

	}

	public MessageDto(String id, Integer status, String name, String phone_from, String text, String type,
			String timestamp) {
		this.id = id;
		this.status = status;
		this.name = name;
		this.phone_from = phone_from;
		this.text = text;
		this.type = type;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_from() {
		return phone_from;
	}

	public void setPhone_from(String phone_from) {
		this.phone_from = phone_from;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MessageDto [id=" + id + ", status=" + status + ", name=" + name + ", phone_from=" + phone_from
				+ ", text=" + text + ", type=" + type + ", timestamp=" + timestamp + "]";
	}

}
