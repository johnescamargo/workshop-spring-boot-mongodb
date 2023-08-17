package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.textmode.TextConfirmationStep;
import com.imav.whatsapp.util.CustomerUtil;

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

	@Autowired
	private CustomerUtil customerUtil;

	public void setWebhookConfirmation(String type, String obj, String phone, String name) {

		int resp = customerUtil.getCustomerStep(phone);

		if (resp == 1) {
			webhoohConfirmationStep1(type, obj, phone, name);
		} else if (resp == 2) {
			webhoohConfirmationStep2(type, obj, phone, name);
		}

	}

	public void webhoohConfirmationStep1(String type, String obj, String phone, String name) {
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
			button.sendResponseToInitButtonClickedStep1(obj, phone);
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

	public void webhoohConfirmationStep2(String type, String obj, String phone, String name) {
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
			button.sendResponseToInitButtonClickedStep1(obj, phone);
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
