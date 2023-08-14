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
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.webhookModel.WebhookLocation;

@Service
public class LocationReplyService {

	private static Gson GSON = new Gson();

	@Autowired
	private HttpMessageService httpMessageService;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private DBMessageResource dbMessageResource;

	public String sendLocation(String from)  {

		String json = "";
		HashMap<String, String> hashMap = new HashMap<>();

		try {

			try (InputStream inputStream = getClass().getResourceAsStream("/json/location.json");
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}

			WebhookLocation location = new WebhookLocation();

			location = GSON.fromJson(json, WebhookLocation.class);

			location.setTo(from);

			json = GSON.toJson(location, WebhookLocation.class);

			String response = httpMessageService.sendMessage(json);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				dbMessageResource.saveLocationIntoDatabase(location, from, idWamid);
				websocketService.convertLocationSend(location, 0, idWamid);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;

	}

}
