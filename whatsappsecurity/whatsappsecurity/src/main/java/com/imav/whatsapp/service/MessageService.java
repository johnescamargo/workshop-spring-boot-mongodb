package com.imav.whatsapp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.MessageInitDTO;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;

@Service
public class MessageService {

	private static Gson GSON = new Gson();

	@Autowired
	private HttpMessageService messageHttpService;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private DBMessageResource dbMessageResource;

	@Autowired
	private MessageButtonReplyService buttonReplyService;

	public void sendMessage(String msg, int ownerOfMessage) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			MessageModel message = new MessageModel();
			message = GSON.fromJson(msg, MessageModel.class);
			String messageTo = message.getTo();
			message.setTo(messageTo);

			String jsonMessage = GSON.toJson(message, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String id = hashMap.get("id");
				ImavMessage mess = dbMessageResource.saveImavMessageIntoDatabase(message, null, id);

				websocketService.convertMessageSendChat(mess, ownerOfMessage);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean sendMessageInitial(MessageInitDTO obj, String msg) {

		try {

			HashMap<String, String> hashMap = new HashMap<>();
			MessageModel message = new MessageModel();
			String name = obj.getName();

			message.setTo("55" + obj.getPhone());
			message.text.setBody(msg);

			String jsonMessage = GSON.toJson(message, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);// Sending Message

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("id");
				dbMessageResource.saveImavMessageIntoDatabase(message, name, idWamid);
				websocketService.convertMessageSend(message, idWamid);

				buttonReplyService.sendButtonReplyYesNo("55" + obj.getPhone());
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

//	public Object sendMessageReply(String from, String msg) {
//
//		Object objResp = new Object();
//
//		MessageModel message = new MessageModel();
//
//		message.setTo(from);
//		message.text.setBody(msg);
//
//		String jsonMessage = GSON.toJson(message, MessageModel.class);
//		objResp = messageHttpService.sendMessage(jsonMessage);
//
//		return objResp;
//	}

}