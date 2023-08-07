package com.imav.whatsapp.model;

import java.util.ArrayList;
import java.util.List;

public class SuccessfullResponse {

	private String messaging_product;

	private List<Contact> contacts = new ArrayList<>();

	private List<Message> messages = new ArrayList<>();

	public SuccessfullResponse() {

	}

	public SuccessfullResponse(String messaging_product, List<Contact> contacts, List<Message> messages) {
		this.messaging_product = messaging_product;
		this.contacts = contacts;
		this.messages = messages;
	}

	public String getMessaging_product() {
		return messaging_product;
	}

	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "ResponseMessageSent [messaging_product=" + messaging_product + ", contacts=" + contacts + ", messages="
				+ messages + "]";
	}

	public class Contact {

		private String input;
		private String wa_id;

		public Contact() {
			
		}

		public Contact(String input, String wa_id) {
			this.input = input;
			this.wa_id = wa_id;
		}

		public String getInput() {
			return input;
		}

		public void setInput(String input) {
			this.input = input;
		}

		public String getWa_id() {
			return wa_id;
		}

		public void setWa_id(String wa_id) {
			this.wa_id = wa_id;
		}

		@Override
		public String toString() {
			return "Contact [input=" + input + ", wa_id=" + wa_id + "]";
		}

	}

	public class Message {
		private String id;

		public Message() {

		}

		public Message(String id) {
			this.id = id;
		}

		public String getWa_id() {
			return id;
		}

		public void setWa_id(String wa_id) {
			this.id = wa_id;
		}

		@Override
		public String toString() {
			return "Message [wa_id=" + id + "]";
		}

	}

}
