package com.imav.whatsapp.model;

import java.util.ArrayList;
import java.util.List;

public class ButtonReply {

	private String messaging_product;
	private String recipient_type;
	private String to;
	private String type;
	public Interactive interactive;

	public ButtonReply() {
		new Interactive();
		messaging_product = "whatsapp";
		recipient_type = "individual";
		type = "interactive";
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Interactive getInteractive() {
		return interactive;
	}

	public void setInteractive(Interactive interactive) {
		this.interactive = interactive;
	}

	@Override
	public String toString() {
		return "ButtonReply [messaging_product=" + messaging_product + ", recipient_type=" + recipient_type + ", to="
				+ to + ", type=" + type + ", interactive=" + interactive + "]";
	}

	public class Interactive {
		public String type;
		public Body body;
		public Action action;

		public Interactive() {
			new Body();
			new Action();
			this.type = "button";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Body getBody() {
			return body;
		}

		public void setBody(Body body) {
			this.body = body;
		}

		public Action getAction() {
			return action;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		@Override
		public String toString() {
			return "Interactive [type=" + type + ", body=" + body + ", action=" + action + "]";
		}

	}

	public class Body {

		public String text;

		public Body() {

		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "Body [text=" + text + "]";
		}

	}

	public class Action {
		public List<Button> buttons = new ArrayList<>();

		public Action() {
		}

		public List<Button> getButtons() {
			return buttons;
		}

		public void setButtons(List<Button> buttons) {
			this.buttons = buttons;
		}

		@Override
		public String toString() {
			return "Action [buttons=" + buttons + "]";
		}

	}

	public class Button {
		public String type;
		public Reply reply;

		public Button() {
			type = "reply";
			new Reply();
		}

		public String getType() {
			return type;
		}

		public Reply getReply() {
			return reply;
		}

		public void setReply(Reply reply) {
			this.reply = reply;
		}

		@Override
		public String toString() {
			return "Button [type=" + type + ", reply=" + reply + "]";
		}

	}

	public class Reply {
		public String id;
		public String title;

		public Reply() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return "Reply [id=" + id + ", title=" + title + "]";
		}
	}
}
