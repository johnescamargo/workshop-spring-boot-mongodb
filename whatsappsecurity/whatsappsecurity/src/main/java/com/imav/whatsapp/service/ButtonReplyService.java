package com.imav.whatsapp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.ButtonReplyUtil;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;

@Service
public class ButtonReplyService {

	private static Gson GSON = new Gson();
	@Autowired
	private HttpMessageService messageHttpService;

	@Autowired
	private ButtonReplyUtil util;

	@Autowired
	private DBMessageResource dbMessageResource;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private LocationReplyService locationService;

	public void sendResponseToButtonClicked(String obj, String from) {

		WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
		MessageWithURL url = new MessageWithURL();
		MessageWithURL url2 = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getInteractive().getButton_reply().getId();

			switch (id) {
			case "button-sim":
				messageResponse(obj, util.setButtonYes());
				url.setTo(from);
				url.text.setBody(util.setButtonYes());
				sendImavResponseURL(url);

				url2.setTo(from);
				url2.text.setBody(util.setButtonWhatsapp());
				sendImavResponseURL(url2);

				break;

			case "button-nao":
				messageResponse(obj, util.setButtonNo());
				url.setTo(from);
				url.text.setBody(util.setButtonNo());
				sendImavResponseURL(url);

				break;

			case "button-telefone":
				messageResponse(obj, util.setButtonTelephone());
				url.setTo(from);
				url.text.setBody(util.setButtonTelephone());
				sendImavResponseURL(url);

				break;

			case "button-whatsapp":
				messageResponse(obj, util.setButtonWhatsapp());
				url.setTo(from);
				url.text.setBody(util.setButtonWhatsapp());
				sendImavResponseURL(url);

				break;

			case "button-localizacao":
				saveInteractiveMessageFromCustomer(buttonClicked, from);

				String respLocation = locationService.sendLocation(from);
				messageHttpService.sendMessage(respLocation);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendImavResponseURL(MessageWithURL url) {

		HashMap<String, String> hashMap = new HashMap<>();
		String jsonMessage = GSON.toJson(url, MessageWithURL.class);
		String response = messageHttpService.sendMessage(jsonMessage);

		hashMap = messageUtil.checkResponseMessageSent(response);
		String resp = hashMap.get("resp");

		if (resp.equals("success")) {
			String idWamid = hashMap.get("resp");
			dbMessageResource.saveImavMessageURlIntoDatabase(url, idWamid);
			websocketService.convertMessageUrlSend(url, idWamid);
		} else {
			String timestamp = hashMap.get("timestamp");
			System.out.println("Message Not sent - timestamp: " + timestamp);
		}
	}

	private void messageResponse(String obj, String msgBody) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
			MessageModel messageModel = new MessageModel();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			// extract phone number from the WebHook payload
			String from = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getFrom();

			messageModel.setTo(from);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				saveInteractiveMessageFromCustomer(buttonClicked, from);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveInteractiveMessageFromCustomer(WebhookReceivedCallbackQuickReplyButtonClick obj, String from) {
		try {
			Customer customer = dbMessageResource.getCustomerObject(from);

			dbMessageResource.saveInteractiveReplyIntoDatabase(obj, customer);

			websocketService.convertInteractiveFromOutside(obj, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveInteractiveButtonLocation(String obj, String from) {
		try {
			Customer customer = dbMessageResource.getCustomerObject(from);

			WebhookReceivedCallbackQuickReplyButtonClick interactive = dbMessageResource
					.saveInteractiveReplyIntoDatabase(obj, customer);

			websocketService.convertInteractiveFromOutside(interactive, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
