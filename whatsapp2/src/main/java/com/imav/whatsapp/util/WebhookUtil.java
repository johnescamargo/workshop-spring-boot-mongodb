package com.imav.whatsapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WebhookUtil {

	private Logger logger = LogManager.getLogger(WebhookUtil.class);

	public String findTypeOfWebhookMessage(String json) {

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
			logger.info("Another type of  webhook has been found: " + type);
		}

		return type;
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

		return type;
	}

}