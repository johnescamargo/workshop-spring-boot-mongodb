package com.imav.whatsapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WebhookUtil {

	private Logger logger = LogManager.getLogger(WebhookUtil.class);

	public String findTypeOfWebhook(String json) {

		String type = "";

		try {

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("messages");
			obj = arr.getJSONObject(0);
			type = obj.getString("type");

		} catch (Exception e) {
			type = findOtherTypesOfWebhookMessage(json);
			logger.info(type);
		}

		return type;
	}
	
	public String getPhoneNumber(String json) {

		String phone = "";

		try {

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("contacts");
			obj = arr.getJSONObject(0);
			phone = obj.getString("wa_id");

		} catch (Exception e) {
			phone = findOtherTypesOfWebhookMessage(json);
			logger.info("Another type of  webhook has been found: " + phone);
		}

		return phone;
	}
	
	public String getName(String json) {

		String name = "";

		try {

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("contacts");
			obj = arr.getJSONObject(0).getJSONObject("profile");;
			name = obj.getString("name");

		} catch (Exception e) {
			name = findOtherTypesOfWebhookMessage(json);
			logger.info("Another type of  webhook has been found: " + name);
		}

		return name;
	}

	public String findOtherTypesOfWebhookMessage(String str) {

		String type = "not found";

		// example of another type of Class
		int field = str.lastIndexOf("\"latitude\"");
		if (field > 0) {
			type = "location";
			// System.out.println(type);
			return type;
		}

		field = str.lastIndexOf("\"statuses\"");
		if (field > 0) {
			type = "status";
			return type;
		}
		
		
		field = str.lastIndexOf("\"errors\"");
		if (field > 0) {
			type = "errors";
			return type;
		}

		return type;
	}

}
