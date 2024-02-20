package com.imav.whatsapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.MessageInitDTO;
import com.imav.whatsapp.model.ErrorResponse;
import com.imav.whatsapp.model.SuccessfullResponse;

@Service
public class MessageUtil {

	private static Gson GSON = new Gson();

	private Logger logger = LogManager.getLogger(MessageUtil.class);

	public HashMap<String, String> checkResponseMessageSent(String response) {

		HashMap<String, String> hashMap = new HashMap<>();
		String resp = checkTypeOfResponse(response);

		if (resp.equals("success")) {
			SuccessfullResponse messageResp = GSON.fromJson(response, SuccessfullResponse.class);
			String messageFrom = messageResp.getContacts().get(0).getWa_id();
			String id = messageResp.getMessages().get(0).getWa_id();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);

			hashMap.put("from", messageFrom);
			hashMap.put("resp", "success");
			hashMap.put("id", id);
			hashMap.put("timestamp", timestamp);

		} else if (response.equals("error")) {
			ErrorResponse messageResp = GSON.fromJson(response, ErrorResponse.class);
			logger.info("Error message sent: " + messageResp);
			hashMap.put("resp", "error");
		} else {
			logger.info("Unknown message...");
			hashMap.put("resp", "error");
		}
		return hashMap;
	}

	public boolean checkPhoneNumber(MessageInitDTO obj) {

		String phone = String.valueOf(obj.getPhone().charAt(2));
		if (phone.equals("9")) {
			return true;
		} else {
			return false;
		}
	}

	public String checkTypeOfResponse(String str) {

		String type = "success";
		int field = str.lastIndexOf("\"error\"");
		if (field > 0) {
			type = "error";
		}
		return type;
	}

	public String messageOrganizer(MessageInitDTO obj) {

		String message = "";

		for (int i = 0; i < obj.getMessages().size(); i++) {

			if (i == 0) {
				message += obj.getMessages().get(i);
				message += " " + obj.getName() + System.lineSeparator();
			} else {
				message += obj.getMessages().get(i) + System.lineSeparator();

				if (i == 1) {
					message += "Data: " + obj.getDate() + System.lineSeparator();
					message += "Hora: " + obj.getTime() + System.lineSeparator();
					message += "Medico: " + obj.getdoctor() + System.lineSeparator();
				}
			}
		}

		List<String> msg = new ArrayList<>();
		msg.add(message);

		obj.getMessages().clear();
		obj.setMessages(msg);

		return message;
	}

}
