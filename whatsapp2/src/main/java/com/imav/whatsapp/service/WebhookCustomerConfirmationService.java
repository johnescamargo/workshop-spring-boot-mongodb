package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.textmode.TextConfirmationStep;

@Service
public class WebhookCustomerConfirmationService {

	@Autowired
	private ButtonReplyService button;

	@Autowired
	private MessageButtonReplyService message;

	@Autowired
	private LocationReplyService location;

	@Autowired
	private SendWebSocketService websocket;

	@Autowired
	private TextConfirmationStep step;

	public void setWebhookConfirmation(String type, String obj, String phone, String name) {

		switch (type) {

		case "text":
			step.checkStepText(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "image":
			message.getImage(obj, type);
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
			button.sendResponseToConfirmationButtonClicked(obj, phone);
			websocket.showCustomerToWebSocket();
			break;

		case "button":
			button.sendResponseToInitButtonClicked(obj, phone);
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
