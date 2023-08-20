package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.resource.DBCustomerResource;
import com.imav.whatsapp.util.CustomerUtil;

@Service
public class WebhookCustomerTalkingService {

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
	private CustomerUtil customerUtil;

	public void setWebhookTalking(String type, String obj, String phone, String name) {

		switch (type) {

		case "text":
			resource.saveCustomerMessage(obj);
			customerUtil.updateOnlyCustomerWantToTalk(phone);
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
			//button.sendResponseToAudio(from);
			websocket.showCustomerToWebSocket();
			break;

		case "interactive":
			button.sendResponseToButtonClicked(obj, phone);
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
