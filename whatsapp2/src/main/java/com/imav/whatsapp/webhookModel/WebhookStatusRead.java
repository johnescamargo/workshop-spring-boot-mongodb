package com.imav.whatsapp.webhookModel;

import java.util.ArrayList;

public class WebhookStatusRead {

	public String object;
	public ArrayList<Entry> entry = new ArrayList<>();

	public WebhookStatusRead() {

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

	public void setEntry(Entry entry) {
		this.entry.add(entry);
	}

	@Override
	public String toString() {
		return "WebhookStatusRead [object=" + object + ", entry=" + entry + "]";
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

	public class Status {
		public String id;
		public String status;
		public String timestamp;
		public String recipient_id;

		public Status() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getRecipient_id() {
			return recipient_id;
		}

		public void setRecipient_id(String recipient_id) {
			this.recipient_id = recipient_id;
		}

		@Override
		public String toString() {
			return "Status [id=" + id + ", status=" + status + ", timestamp=" + timestamp + ", recipient_id="
					+ recipient_id + "]";
		}

	}

	public class Value {
		public String messaging_product;
		public Metadata metadata;
		public ArrayList<Status> statuses = new ArrayList<>();

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

		public ArrayList<Status> getStatuses() {
			return statuses;
		}

		public void setStatuses(ArrayList<Status> statuses) {
			this.statuses = statuses;
		}
		
		public void setStatuses(Status statuses) {
			this.statuses.add(statuses);
		}

		@Override
		public String toString() {
			return "Value [messaging_product=" + messaging_product + ", metadata=" + metadata + ", statuses=" + statuses
					+ "]";
		}

	}

}
