package com.imav.whatsapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.resource.DBStatusesResource;
import com.imav.whatsapp.webhookModel.WebhookStatusDelivered;
import com.imav.whatsapp.webhookModel.WebhookStatusRead;
import com.imav.whatsapp.webhookModel.WebhookStatusSent;

@Service
public class StatusesService {

	private static Gson GSON = new Gson();

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private DBStatusesResource statusesResource;

	public void checkTypeOfStatus(String json) {

		JSONObject obj = new JSONObject(json);
		JSONArray arr = new JSONArray();
		String status = "";

		try {

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("statuses");
			obj = arr.getJSONObject(0);
			status = obj.getString("status");

			switch (status) {
			case "sent":
				setSent(json);
				websocketService.updateWebsocket();
				break;
			case "delivered":
				setDelivered(json);
				websocketService.updateWebsocket();
				break;
			case "read":
				setRead(json);
				websocketService.updateWebsocket();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - another type of status has been found...");
		}
	}

	public void setSent(String obj) {
		WebhookStatusSent sent = new WebhookStatusSent();
		sent = GSON.fromJson(obj, WebhookStatusSent.class);
		int statusCode = 1;

		String idWamid = sent.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0).getId();
		String phone = sent.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0).getRecipient_id();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		statusesResource.findIdWamid(phone, idWamid, statusCode);
	}

	public void setDelivered(String obj) {
		WebhookStatusDelivered delivered = new WebhookStatusDelivered();
		delivered = GSON.fromJson(obj, WebhookStatusDelivered.class);
		int statusCode = 2;

		String idWamid = delivered.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0).getId();
		String phone = delivered.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0)
				.getRecipient_id();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		statusesResource.findIdWamid(phone, idWamid, statusCode);
	}

	public void setRead(String obj) {
		WebhookStatusRead read = new WebhookStatusRead();
		read = GSON.fromJson(obj, WebhookStatusRead.class);
		int statusCode = 3;

		String idWamid = read.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0).getId();
		String phone = read.getEntry().get(0).getChanges().get(0).getValue().getStatuses().get(0).getRecipient_id();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		statusesResource.findIdWamid(phone, idWamid, statusCode);
	}

}
