package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.resource.DBCustomerResource;
import com.imav.whatsapp.textmode.TextConfirmationStep;

@Service
public class WebhookCustomerNormalDayOffService {

	@Autowired
	private ButtonReplyService button;

	@Autowired
	private MessageButtonReplyService message;

	@Autowired
	private LocationReplyService location;

	@Autowired
	private DBCustomerResource resource;

	@Autowired
	private SendWebSocketService websocket;
	
	@Autowired
	private TextConfirmationStep step;

	public void setWebhookNormalDayOff(String type, String obj, String phone, String name) {

		switch (type) {

		case "text":
			resource.saveCustomerMessage(obj);
			String path = ("/json/message_response_work_off.json");
			message.sendButtonResponse(phone, path);
			websocket.showCustomerToWebSocket();
			break;

		case "image":
			message.getImage(obj, type);
			String pathImage = ("/json/message_response_work_off.json");
			message.sendButtonResponse(phone, pathImage);
			websocket.showCustomerToWebSocket();
			break;

		case "reaction":

			websocket.showCustomerToWebSocket();
			break;

		case "sticker":

			websocket.showCustomerToWebSocket();
			break;

		case "unknown":

			websocket.showCustomerToWebSocket();
			break;

		case "audio":
			message.messageNoAudio(phone, name);
			button.sendResponseToAudio(phone);
			websocket.showCustomerToWebSocket();
			break;

		case "interactive":
			button.sendResponseToButtonClickedDayOff(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "button":
			button.sendResponseToInitButtonClickedDayOff(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "location":
			button.saveInteractiveButtonLocation(obj, phone);
			location.sendLocation(phone);
			websocket.showCustomerToWebSocket();
			break;

		default:
			websocket.showCustomerToWebSocket();
			break;
		}

		websocket.showCustomerToWebSocket();
	}

	public void setWebhookConfirmationDayOff(String type, String obj, String phone, String name) {
		switch (type) {

		case "text":
			step.checkStepTextDayOff(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "image":
			message.getImage(obj, type);
			String pathImage = ("/json/message_response_work_off.json");
			message.sendButtonResponse(phone, pathImage);
			websocket.showCustomerToWebSocket();
			break;

		case "reaction":

			websocket.showCustomerToWebSocket();
			break;

		case "sticker":

			websocket.showCustomerToWebSocket();
			break;

		case "unknown":

			websocket.showCustomerToWebSocket();
			break;

		case "audio":
			message.messageNoAudio(phone, name);
			button.sendResponseToAudio(phone);
			websocket.showCustomerToWebSocket();
			break;

		case "interactive":
			button.sendResponseToButtonClickedDayOff(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "button":
			button.sendResponseToInitButtonClickedDayOff(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "location":
			button.saveInteractiveButtonLocation(obj, phone);
			location.sendLocation(phone);
			websocket.showCustomerToWebSocket();
			break;

		default:
			websocket.showCustomerToWebSocket();
			break;
		}

		websocket.showCustomerToWebSocket();

	}

}
