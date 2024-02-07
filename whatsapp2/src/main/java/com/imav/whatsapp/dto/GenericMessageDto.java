package com.imav.whatsapp.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericMessageDto {

	private String id;

	private int ownerOfMessage;

	private Integer status;

	private String name;

	private String phone_from;

	private String text;

	private String type;

	private String timestamp;

	private String idWamid;

	private String mimeType;

	private byte[] content;

	private List<Button> buttons = new ArrayList<>();

	public GenericMessageDto() {

	}

	public GenericMessageDto(String id, int ownerOfMessage, Integer status, String name, String phone_from, String text,
			String type, String timestamp, String mimeType, String idWamid, byte[] content, List<Button> buttons) {
		this.id = id;
		this.ownerOfMessage = ownerOfMessage;
		this.status = status;
		this.name = name;
		this.phone_from = phone_from;
		this.text = text;
		this.type = type;
		this.timestamp = timestamp;
		this.buttons = buttons;
		this.idWamid = idWamid;
		this.content = content;
		this.mimeType = mimeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOwnerOfMessage() {
		return ownerOfMessage;
	}

	public void setOwnerOfMessage(int ownerOfMessage) {
		this.ownerOfMessage = ownerOfMessage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getIdWamid() {
		return idWamid;
	}

	public void setIdWamid(String idWamid) {
		this.idWamid = idWamid;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "GenericMessageDto [id=" + id + ", ownerOfMessage=" + ownerOfMessage + ", status=" + status + ", name="
				+ name + ", phone_from=" + phone_from + ", text=" + text + ", type=" + type + ", timestamp=" + timestamp
				+ ", idWamid=" + idWamid + ", mimeType=" + mimeType + ", content=" + Arrays.toString(content)
				+ ", buttons=" + buttons + "]";
	}

	public static class Button {

		private String id;
		private String type;
		private String title;

		public Button() {

		}

		public Button(String id, String type, String title) {
			this.id = id;
			this.type = type;
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

}
