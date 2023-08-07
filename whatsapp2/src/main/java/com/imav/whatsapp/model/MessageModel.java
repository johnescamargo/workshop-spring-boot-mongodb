package com.imav.whatsapp.model;

public class MessageModel {

	public String messaging_product;
	public String to;
	public Text text;

	public MessageModel() {
		text = new Text();
		messaging_product = "whatsapp";
	}

	public String getMessaging_product() {
		return messaging_product;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "MessageModel [messaging_product=" + messaging_product + ", to=" + to + ", text=" + text + "]";
	}

	public class Text {
		public String body;

		public Text() {
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		@Override
		public String toString() {
			return "Text [body=" + body + "]";
		}

	}
}
