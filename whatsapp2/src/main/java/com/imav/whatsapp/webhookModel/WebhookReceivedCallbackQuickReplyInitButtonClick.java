package com.imav.whatsapp.webhookModel;

import java.util.ArrayList;

public class WebhookReceivedCallbackQuickReplyInitButtonClick {

	public String object;
	public ArrayList<Entry> entry = new ArrayList<>();

	public WebhookReceivedCallbackQuickReplyInitButtonClick() {

	}

	public WebhookReceivedCallbackQuickReplyInitButtonClick(String object, ArrayList<Entry> entry) {
		this.object = object;
		this.entry = entry;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public ArrayList<Entry> getEntry() {
		return entry;
	}

	public void setEntry(ArrayList<Entry> entry) {
		this.entry = entry;
	}

	@Override
	public String toString() {
		return "WebhookReceivedInteractionButtonReply [object=" + object + ", entry=" + entry + "]";
	}

	public class Button {
		public String payload;
		public String text;

		public Button(String payload, String text) {

			this.payload = payload;
			this.text = text;
		}

		public Button() {

		}

		public String getPayload() {
			return payload;
		}

		public void setPayload(String payload) {
			this.payload = payload;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "Button [payload=" + payload + ", text=" + text + "]";
		}

	}

	public class Change {
		public Value value = new Value();
		public String field;

		public Change(Value value, String field) {
			this.value = value;
			this.field = field;
		}

		public Change() {

		}

		public Value getValue() {
			return value;
		}

		public void setValue(Value value) {
			this.value = value;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		@Override
		public String toString() {
			return "Change [value=" + value + ", field=" + field + "]";
		}

	}

	public class Contact {
		public Profile profile = new Profile();
		public String wa_id;

		public Contact() {

		}

		public Contact(Profile profile, String wa_id) {

			this.profile = profile;
			this.wa_id = wa_id;
		}

		public Profile getProfile() {
			return profile;
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}

		public String getWa_id() {
			return wa_id;
		}

		public void setWa_id(String wa_id) {
			this.wa_id = wa_id;
		}

		@Override
		public String toString() {
			return "Contact [profile=" + profile + ", wa_id=" + wa_id + "]";
		}

	}

	public class Context {
		public String from;
		public String id;

		public Context() {

		}

		public Context(String from, String id) {
			this.from = from;
			this.id = id;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "Context [from=" + from + ", id=" + id + "]";
		}

	}

	public class Entry {
		public String id;
		public ArrayList<Change> changes = new ArrayList<>();

		public Entry() {

		}

		public Entry(String id, ArrayList<Change> changes) {

			this.id = id;
			this.changes = changes;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public ArrayList<Change> getChanges() {
			return changes;
		}

		public void setChanges(ArrayList<Change> changes) {
			this.changes = changes;
		}

		@Override
		public String toString() {
			return "Entry [id=" + id + ", changes=" + changes + "]";
		}

	}

	public class Message {
		public Context context;
		public String from;
		public String id;
		public String timestamp;
		public String type;
		public Button button = new Button();

		public Message() {

		}

		public Message(Context context, String from, String id, String timestamp, String type, Button button) {

			this.context = context;
			this.from = from;
			this.id = id;
			this.timestamp = timestamp;
			this.type = type;
			this.button = button;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Button getButton() {
			return button;
		}

		public void setButton(Button button) {
			this.button = button;
		}

		@Override
		public String toString() {
			return "Message [context=" + context + ", from=" + from + ", id=" + id + ", timestamp=" + timestamp
					+ ", type=" + type + ", button=" + button + "]";
		}

	}

	public class Metadata {
		public String display_phone_number;
		public String phone_number_id;

		public Metadata(String display_phone_number, String phone_number_id) {
			this.display_phone_number = display_phone_number;
			this.phone_number_id = phone_number_id;
		}

		public Metadata() {

		}

		public String getDisplay_phone_number() {
			return display_phone_number;
		}

		public void setDisplay_phone_number(String display_phone_number) {
			this.display_phone_number = display_phone_number;
		}

		public String getPhone_number_id() {
			return phone_number_id;
		}

		public void setPhone_number_id(String phone_number_id) {
			this.phone_number_id = phone_number_id;
		}

		@Override
		public String toString() {
			return "Metadata [display_phone_number=" + display_phone_number + ", phone_number_id=" + phone_number_id
					+ "]";
		}

	}

	public class Profile {
		public String name;

		public Profile(String name) {
			this.name = name;
		}

		public Profile() {

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Profile [name=" + name + "]";
		}

	}

	public class Value {
		public String messaging_product;
		public Metadata metadata = new Metadata();
		public ArrayList<Contact> contacts = new ArrayList<>();;
		public ArrayList<Message> messages = new ArrayList<>();;

		public Value() {

		}

		public Value(String messaging_product, Metadata metadata, ArrayList<Contact> contacts,
				ArrayList<Message> messages) {

			this.messaging_product = messaging_product;
			this.metadata = metadata;
			this.contacts = contacts;
			this.messages = messages;
		}

		public String getMessaging_product() {
			return messaging_product;
		}

		public void setMessaging_product(String messaging_product) {
			this.messaging_product = messaging_product;
		}

		public Metadata getMetadata() {
			return metadata;
		}

		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}

		public ArrayList<Contact> getContacts() {
			return contacts;
		}

		public void setContacts(ArrayList<Contact> contacts) {
			this.contacts = contacts;
		}

		public ArrayList<Message> getMessages() {
			return messages;
		}

		public void setMessages(ArrayList<Message> messages) {
			this.messages = messages;
		}

		@Override
		public String toString() {
			return "Value [messaging_product=" + messaging_product + ", metadata=" + metadata + ", contacts=" + contacts
					+ ", messages=" + messages + "]";
		}

	}

}
