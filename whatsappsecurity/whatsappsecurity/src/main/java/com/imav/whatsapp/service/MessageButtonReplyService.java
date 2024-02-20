package com.imav.whatsapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.ButtonReply;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.resource.DBImavMessageButtonReplyResource;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;

@Service
public class MessageButtonReplyService {

	private static Gson GSON = new Gson();

	@Autowired
	private HttpMessageService httpMessageService;

	@Autowired
	private DBImavMessageButtonReplyResource dbOurButtonReplyResource;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private DBMessageResource dbMessageResource;

	public void sendButtonReplyYesNo(String from) {

		String json = "";
		HashMap<String, String> hashMap = new HashMap<>();

		try {

			String path = ("/json/ButtonReplyYesNo.json");

			json = readJsonFiles(path);

			ButtonReply button = new ButtonReply();

			button = GSON.fromJson(json, ButtonReply.class);
			button.setTo(from);
			String jsonMessage = GSON.toJson(button, ButtonReply.class);

			String response = httpMessageService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("id");
				dbOurButtonReplyResource.saveImavMessageButtonReplyIntoDatabase(button, idWamid);
				websocketService.convertMessageButtonSend(button, idWamid);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendButtonResponse(String from) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			String path = ("/json/MessageResponse.json");

			String json = readJsonFiles(path);

			ButtonReply button = new ButtonReply();

			button = GSON.fromJson(json, ButtonReply.class);
			button.setTo(from);
			String jsonMessage = GSON.toJson(button, ButtonReply.class);

			String response = httpMessageService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("id");
				dbOurButtonReplyResource.saveImavMessageButtonReplyIntoDatabase(button, idWamid);

				websocketService.convertMessageButtonSend(button, idWamid);

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void messageNoText(String from, String name) {

		String json = "";
		int ownerOfMessage = 0;
		HashMap<String, String> hashMap = new HashMap<>();

		try {

			String path = ("/json/MessageNoText.json");

			json = readJsonFiles(path);

			MessageModel message = new MessageModel();

			message = GSON.fromJson(json, MessageModel.class);
			message.setTo(from);
			String jsonMessage = GSON.toJson(message, MessageModel.class);

			String response = httpMessageService.sendMessage(jsonMessage);
			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("id");
				ImavMessage mess = dbMessageResource.saveImavMessageIntoDatabase(message, name, idWamid);
				websocketService.convertMessageSendChat(mess, ownerOfMessage);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String readJsonFiles(String path) {

		String json = "";

		try (InputStream inputStream = getClass().getResourceAsStream(path);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	/*
	 * // It is not being used - Only a test , check the body of some future WebHook
	 * public void checkTypeOfMessage(String obj) { try { WebhookMessageModel
	 * message = new WebhookMessageModel(); message = GSON.fromJson(obj,
	 * WebhookMessageModel.class);
	 * 
	 * String text =
	 * message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
	 * .getText() String resp =
	 * message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
	 * .getText() .getBody(); Get json message and phone number and send Message to
	 * method send Method MessageModel } catch (Exception e) { e.printStackTrace();
	 * } }
	 */

}
