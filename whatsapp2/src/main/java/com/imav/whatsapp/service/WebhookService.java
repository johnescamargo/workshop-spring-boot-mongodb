package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.resource.DBCustomerResource;
import com.imav.whatsapp.util.WeekUtil;
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

	@Autowired
	private SendWebSocketService websocket;

	@Autowired
	private WeekUtil weekUtil;
	

	public void setWebhookClass(String type, String obj) {

		boolean response = false;
		String from = "";
		String name = "";

		if (!type.equals("status")) {
			WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
			messageReceived = GSON.fromJson(obj, WebhookReceivedTextMessage.class);
			from = messageReceived
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getFrom();
			
			name = messageReceived
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getContacts()
					.get(0)
					.getProfile()
					.getName();
		}

		switch (type) {

		case "text":
			resource.checkNewCustomer(obj);

			response = weekUtil.checkIfDayIsOff();
			if (response) {
				message.sendButtonResponse(from);
			} else {
				message.sendButtonResponseWorkOff(from);
			}
			websocket.showCustomerToWebSocket();
			break;
			
		case "image":
			response = weekUtil.checkIfDayIsOff();
			
			if (response) {
				message.getImage(obj, type);
			} else {
				message.getImage(obj, type);
				message.sendButtonResponseWorkOff(from);
			}
			
			websocket.showCustomerToWebSocket();
			break;
			
		case "reaction":
//			response = weekUtil.checkIfDayIsOff();
//			
//			if (response) {
//				message.getImage(obj, type);
//			} else {
//				message.getImage(obj, type);
//				message.sendButtonResponseWorkOff(from);
//			}
			
			websocket.showCustomerToWebSocket();
			break;
			
		case "sticker":
//			response = weekUtil.checkIfDayIsOff();
//			
//			if (response) {
//				message.getImage(obj, type);
//			} else {
//				message.getImage(obj, type);
//				message.sendButtonResponseWorkOff(from);
//			}
			
			websocket.showCustomerToWebSocket();
			break;
			
		case "unknown":
//			response = weekUtil.checkIfDayIsOff();
//			
//			if (response) {
//				message.getImage(obj, type);
//			} else {
//				message.getImage(obj, type);
//				message.sendButtonResponseWorkOff(from);
//			}
			
			websocket.showCustomerToWebSocket();
			break;
			
		case "audio":
			message.messageNoAudio(from, name);
			button.sendResponseToAudio(from);
			websocket.showCustomerToWebSocket();
			break;
			
		case "interactive":
			button.sendResponseToButtonClicked(obj, from);
			websocket.showCustomerToWebSocket();
			break;
			
		case "button":
			button.sendResponseToInitButtonClicked(obj, from);
			websocket.showCustomerToWebSocket();
			break;
			
		case "location":
			button.saveInteractiveButtonLocation(obj, from);
			location.sendLocation(from);
			websocket.showCustomerToWebSocket();
			break;
			
		case "status":
			statusesService.checkTypeOfStatus(obj);
			break;

		default:
			websocket.showCustomerToWebSocket();
			break;
		}

		websocket.showCustomerToWebSocket();
	}

}
