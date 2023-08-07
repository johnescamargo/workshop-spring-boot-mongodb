package com.imav.whatsapp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.ButtonReplyUtil;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyInitButtonClick;

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

	@Autowired
	private ConfirmationResponseRepository confirmationResponseRepository;

	public void sendResponseToButtonClicked(String obj, String from) {

		WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
		MessageWithURL url = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getInteractive().getButton_reply().getId();

			switch (id) {
			case "button-telefone":
				sendMessageResponse(obj, util.setButtonTelephone(), "Sem resposta");

				break;

			case "button-whatsapp":
				url.setTo(from);
				url.text.setBody(util.setButtonWhatsapp());
				saveInteractiveMessageFromCustomer(buttonClicked, from);
				sendImavResponseURL(url);

				break;

			case "button-localizacao":
				saveInteractiveMessageFromCustomer(buttonClicked, from);
				locationService.sendLocation(from);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Interactive button failed");
		}
	}
	
	
	public void sendResponseToInitButtonClicked(String obj, String from) {

		WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();
		MessageWithURL url2 = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			String id = buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getButton()
					.getPayload();

			switch (id) {
			case "SIM":
				sendButtonResponse(obj, util.setButtonYes(), "SIM");

//				url2.setTo(from);
//				url2.text.setBody(util.setButtonWhatsapp());
//				sendImavResponseURL(url2);

				break;

			case "REMARCAR":// Button REMARCAR
				sendButtonResponse(obj, util.setButtonNo(), "NÃO");

				url2.setTo(from);
				url2.text.setBody(util.setButtonWhatsapp());
				sendImavResponseURL(url2);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Quickly response button failed");
		}
	}

	public void sendResponseToAudio(String from) {

		MessageWithURL url = new MessageWithURL();

		try {
			url.setTo(from);
			url.text.setBody(util.setButtonWhatsapp());
			sendImavResponseURL(url);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Audio failed");
		}
	}

	public void sendImavResponseURL(MessageWithURL url) {

		HashMap<String, String> hashMap = new HashMap<>();
		String jsonMessage = GSON.toJson(url, MessageWithURL.class);
		String response = messageHttpService.sendMessage(jsonMessage);

		hashMap = messageUtil.checkResponseMessageSent(response);
		String resp = hashMap.get("resp");

		if (resp.equals("success")) {
			String idWamid = hashMap.get("idWamid");
			dbMessageResource.saveImavMessageURlIntoDatabase(url, idWamid);
			websocketService.convertMessageUrlSend(url, idWamid);
		} else {
			String timestamp = hashMap.get("timestamp");
			System.out.println("Message Not sent - timestamp: " + timestamp);
		}
	}

	private void sendMessageResponse(String obj, String msgBody, String respYesOrNo) {

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
				String idWamid = hashMap.get("idWamid");
				String name = "IMAV";
				dbMessageResource.saveImavMessageIntoDatabase(messageModel, name, idWamid, false);
				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("NÃO")) {
					String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
							.getContext().getId();
					updateConfirmationResponse(id, respYesOrNo);
				}

			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendButtonResponse(String obj, String msgBody, String respYesOrNo) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();
			MessageModel messageModel = new MessageModel();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			// extract phone number from the WebHook payload
			String from = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getFrom();

			messageModel.setTo(from);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				saveButtonMessageFromCustomer(buttonClicked, from);
				String idWamid = hashMap.get("idWamid");
				String name = "IMAV";
				dbMessageResource.saveImavMessageIntoDatabase(messageModel, name, idWamid, false);
				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("NÃO")) {
					String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
							.getContext().getId();
					updateConfirmationResponse(id, respYesOrNo);
				}

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
	
	public void saveButtonMessageFromCustomer(WebhookReceivedCallbackQuickReplyInitButtonClick obj, String from) {
		try {
			Customer customer = dbMessageResource.getCustomerObject(from);

			dbMessageResource.saveButtonReplyIntoDatabase(obj, customer);

			websocketService.convertButtonFromOutside(obj, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateConfirmationResponse(String idWamid, String respYesOrNo) {
		ConfirmationResponse confirmation = new ConfirmationResponse();

		boolean resp = confirmationResponseRepository.existsByIdWamid(idWamid);

		if (resp) {
			confirmation = confirmationResponseRepository.findById_Wamid(idWamid);

			System.out.println(confirmation);

			confirmation.setResponse(respYesOrNo);
			System.out.println(confirmation);

			confirmationResponseRepository.save(confirmation);
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
