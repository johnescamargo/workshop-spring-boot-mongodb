package com.imav.whatsapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.resource.DBCustomerResource;
import com.imav.whatsapp.webhookModel.WebhookMessageModel;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class WebhookService {

	private static Gson GSON = new Gson();

	@Autowired
	private ButtonReplyService button;

	@Autowired
	private MessageButtonReplyService message;

	@Autowired
	private LocationReplyService location;

	@Autowired
	private DBCustomerResource resource;

	@Autowired
	private StatusesService statusesService;

	public boolean checkPostWebhook(WebhookMessageModel message) {
		if (!message.getEntry().isEmpty() && !message.getEntry().get(0).getChanges().isEmpty()
				&& !message.getEntry().get(0).getChanges().get(0).getValue().getMessages().isEmpty()) {
			return true;
		} else {

			return false;
		}
	}

	/**
	 * Obtain a JSON file and send it to client "json/ButtonReplyYesNo.json"
	 */
	public void setJsonReply(String strPath) {

		try {
			Path fileName = Path.of(strPath);
			String json;
			json = Files.readString(fileName);
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * List of WebHook Messages Classes ReceivedTextMessage
	 * ReceivedTextMessageWithShowSecurityNotifications
	 * ReceivedTextMessageWithReaction ReceivedTextMessageWithImage
	 * ReceivedTextMessageWithSticker ReceivedUnknownMessages
	 * ReceivedCallbackFromQuickReplyButtonClick
	 * 
	 * @param json
	 * @return
	 */
	public String findTypeOfWebhookMessage(String json) {

		String type = "";

		try {

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("messages");
			obj = arr.getJSONObject(0);
			type = obj.getString("type");

		} catch (Exception e) {
			System.out.println("Find other type of  webhook");
			type = findOtherTypesOfWebhookMessage(json);
		}

		return type;
	}

	/**
	 * TODO
	 * 
	 * @param str
	 * @return
	 */
	public String findOtherTypesOfWebhookMessage(String str) {

		String type = "not found";

		// example of another type of Class
		int field = str.lastIndexOf("\"latitude\"");
		if (field > 0) {
			type = "location";
			System.out.println(type);
			return type;
		}

		field = str.lastIndexOf("\"statuses\"");
		if (field > 0) {
			type = "status";
			return type;
		}

		return type;
	}

	/**
	 * 
	 * @param type
	 * @param obj
	 */
	public void setWebhookClass(String type, String str) {

		WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
		messageReceived = GSON.fromJson(str, WebhookReceivedTextMessage.class);
		String from = "";
		String name = "";

		if (!type.equals("status")) {
			from = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getFrom();
			name = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile()
					.getName();
		}

		switch (type) {
		case "text":
			resource.checkNewCustomer(str);
			message.sendButtonResponse(from);
			break;
		case "image":
			message.messageNoText(from, name);
			break;
		case "reaction":
			message.messageNoText(from, name);
			break;
		case "sticker":
			message.messageNoText(from, name);
			break;
		case "unknown":
			message.messageNoText(from, name);
			break;
		case "interactive":
			button.sendResponseToButtonClicked(str, from);
			break;
		case "location":
			button.saveInteractiveButtonLocation(str, from);
			location.sendLocation(from);
			break;
		case "status":
			statusesService.checkTypeOfStatus(str);
			break;

		default:
			break;
		}
	}

}
