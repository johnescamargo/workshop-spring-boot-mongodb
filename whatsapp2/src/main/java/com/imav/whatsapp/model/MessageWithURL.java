package com.imav.whatsapp.model;

public class MessageWithURL {

	public String messaging_product;
	public String to;
	public Text text = new Text();

	public MessageWithURL() {
		messaging_product = "whatsapp";
	}

	public String getMessaging_product() {
		return messaging_product;
	}

	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
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
		return "MessageWithURL [messaging_product=" + messaging_product + ", to=" + to + ", text=" + text + "]";
	}

	public class Text {
		public boolean preview_url;
		public String body;


		public Text() {
			this.preview_url = true;
		}

		public boolean isPreview_url() {
			return preview_url;
		}

		public void setPreview_url(boolean preview_url) {
			this.preview_url = preview_url;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		@Override
		public String toString() {
			return "Text [preview_url=" + preview_url + ", body=" + body + "]";
		}

	}

}
