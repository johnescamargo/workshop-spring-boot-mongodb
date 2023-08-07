package com.imav.whatsapp.webhookModel;

import java.util.ArrayList;

public class WebhookStatusSent {

	private String object;
	private ArrayList<Entry> entry = new ArrayList<>();

	public WebhookStatusSent() {

	}

	public WebhookStatusSent(String object, ArrayList<Entry> entry) {
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
		return "WebhookStatusSent [object=" + object + ", entry=" + entry + "]";
	}

	public class Change {
		private Value value = new Value();
		private String field;

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

	public class Conversation {
		private String id;
		private String expiration_timestamp;
		private Origin origin = new Origin();

		public Conversation(String id, String expiration_timestamp, Origin origin) {
			this.id = id;
			this.expiration_timestamp = expiration_timestamp;
			this.origin = origin;
		}

		public Conversation() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getExpiration_timestamp() {
			return expiration_timestamp;
		}

		public void setExpiration_timestamp(String expiration_timestamp) {
			this.expiration_timestamp = expiration_timestamp;
		}

		public Origin getOrigin() {
			return origin;
		}

		public void setOrigin(Origin origin) {
			this.origin = origin;
		}

		@Override
		public String toString() {
			return "Conversation [id=" + id + ", expiration_timestamp=" + expiration_timestamp + ", origin=" + origin
					+ "]";
		}

	}

	public class Entry {
		private String id;
		private ArrayList<Change> changes = new ArrayList<>();

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

	public class Metadata {
		private String display_phone_number;
		private String phone_number_id;

		public Metadata() {

		}

		public Metadata(String display_phone_number, String phone_number_id) {

			this.display_phone_number = display_phone_number;
			this.phone_number_id = phone_number_id;
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

	public class Origin {
		private String type;

		public Origin(String type) {

			this.type = type;
		}

		public Origin() {

		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "Origin [type=" + type + "]";
		}

	}

	public class Pricing {
		private boolean billable;
		private String pricing_model;
		private String category;

		public Pricing() {

		}

		public Pricing(boolean billable, String pricing_model, String category) {
			this.billable = billable;
			this.pricing_model = pricing_model;
			this.category = category;
		}

		public boolean isBillable() {
			return billable;
		}

		public void setBillable(boolean billable) {
			this.billable = billable;
		}

		public String getPricing_model() {
			return pricing_model;
		}

		public void setPricing_model(String pricing_model) {
			this.pricing_model = pricing_model;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		@Override
		public String toString() {
			return "Pricing [billable=" + billable + ", pricing_model=" + pricing_model + ", category=" + category
					+ "]";
		}

	}

	public class Status {
		private String id;
		private String status;
		private String timestamp;
		private String recipient_id;
		private Conversation conversation = new Conversation();
		private Pricing pricing = new Pricing();

		public Status(String id, String status, String timestamp, String recipient_id, Conversation conversation,
				Pricing pricing) {

			this.id = id;
			this.status = status;
			this.timestamp = timestamp;
			this.recipient_id = recipient_id;
			this.conversation = conversation;
			this.pricing = pricing;
		}

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

		public Conversation getConversation() {
			return conversation;
		}

		public void setConversation(Conversation conversation) {
			this.conversation = conversation;
		}

		public Pricing getPricing() {
			return pricing;
		}

		public void setPricing(Pricing pricing) {
			this.pricing = pricing;
		}

		@Override
		public String toString() {
			return "Status [id=" + id + ", status=" + status + ", timestamp=" + timestamp + ", recipient_id="
					+ recipient_id + ", conversation=" + conversation + ", pricing=" + pricing + "]";
		}

	}

	public class Value {
		private String messaging_product;
		private Metadata metadata = new Metadata();
		private ArrayList<Status> statuses = new ArrayList<>();

		public Value() {

		}

		public Value(String messaging_product, Metadata metadata, ArrayList<Status> statuses) {
			this.messaging_product = messaging_product;
			this.metadata = metadata;
			this.statuses = statuses;
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

		@Override
		public String toString() {
			return "Value [messaging_product=" + messaging_product + ", metadata=" + metadata + ", statuses=" + statuses
					+ "]";
		}

	}

}
