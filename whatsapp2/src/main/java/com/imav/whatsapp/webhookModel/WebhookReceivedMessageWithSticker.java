package com.imav.whatsapp.webhookModel;

import java.util.ArrayList;

public class WebhookReceivedMessageWithSticker {

	public String object;
	public ArrayList<Entry> entry = new ArrayList<>();

	public WebhookReceivedMessageWithSticker() {

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

	public void setEntry(Entry entry) {
		this.entry.add(entry);
	}

	public void setEntry(ArrayList<Entry> entry) {
		this.entry = entry;
	}

	@Override
	public String toString() {
		return "WebhookMessageSentUserInitiated [object=" + object + ", entry=" + entry + "]";
	}

	public class Change {
		public Value value;
		public String field;

		public Change() {
			new Value();
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
		public Profile profile;
		public String wa_id;

		public Contact() {
			new Profile();
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

	public class Entry {
		public String id;
		public ArrayList<Change> changes = new ArrayList<>();

		public Entry() {

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
		
		public void setChanges(Change changes) {
			this.changes.add(changes);
		}

		@Override
		public String toString() {
			return "Entry [id=" + id + ", changes=" + changes + "]";
		}

	}

	public class Message {
		public String from;
		public String id;
		public String timestamp;
		public String type;
		public Sticker sticker;
	}

	public class Metadata {
		public String display_phone_number;
		public String phone_number_id;

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

	public class Sticker {
		public String id;
		public boolean animated;
		public String mime_type;
		public String sha256;

		public Sticker() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean isAnimated() {
			return animated;
		}

		public void setAnimated(boolean animated) {
			this.animated = animated;
		}

		public String getMime_type() {
			return mime_type;
		}

		public void setMime_type(String mime_type) {
			this.mime_type = mime_type;
		}

		public String getSha256() {
			return sha256;
		}

		public void setSha256(String sha256) {
			this.sha256 = sha256;
		}

		@Override
		public String toString() {
			return "Sticker [id=" + id + ", animated=" + animated + ", mime_type=" + mime_type + ", sha256=" + sha256
					+ "]";
		}

	}

	public class Value {
		public String messaging_product;
		public Metadata metadata;
		public ArrayList<Contact> contacts = new ArrayList<>();
		public ArrayList<Message> messages = new ArrayList<>();

		public Value() {
			new Metadata();
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

		public void setContacts(Contact contacts) {
			this.contacts.add(contacts);
		}

		public ArrayList<Message> getMessages() {
			return messages;
		}

		public void setMessages(ArrayList<Message> messages) {
			this.messages = messages;
		}

		public void setMessages(Message messages) {
			this.messages.add(messages);
		}

		@Override
		public String toString() {
			return "Value [messaging_product=" + messaging_product + ", metadata=" + metadata + ", contacts=" + contacts
					+ ", messages=" + messages + "]";
		}

	}

}
