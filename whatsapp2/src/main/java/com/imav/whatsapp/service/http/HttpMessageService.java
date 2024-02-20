package com.imav.whatsapp.service.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.model.SuccessfullResponse;
import com.imav.whatsapp.service.WhatsappTokens;

@Service
public class HttpMessageService {

	private Logger logger = LogManager.getLogger(HttpMessageService.class);

	@Autowired
	private WhatsappTokens whatsappTokens;

	public String sendMessage(String jsonMessage) {

		logger.info("Sending Message POST - HTTP .........");

		String resp = sendSuccess();
		//String resp = "";

		try {

//			URL url = new URL(
//					"https://graph.facebook.com/v15.0/" + whatsappTokens.getFromPhoneNumberId() + "/messages");
//			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setRequestMethod("POST");
//
//			httpConn.setRequestProperty("Authorization", "Bearer " + whatsappTokens.getAccessToken());
//			httpConn.setRequestProperty("Content-Type", "application/json");
//
//			httpConn.setDoOutput(true);
//			OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
//			writer.write(jsonMessage);
//			writer.flush();
//			writer.close();
//			httpConn.getOutputStream().close();
//
//			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
//					: httpConn.getErrorStream();
//			try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
//				String response = s.hasNext() ? s.next() : "";
//				System.out.println(response);
//				logger.info("Receiving response after sending data https:");
//				logger.info(response);
//				resp = response;
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resp;

	}

	// Only for test purpose
	public String sendSuccess() {

		String json = "";

		try {

			try (InputStream inputStream = getClass().getResourceAsStream("/json/success.json");
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}

			Gson GSON = new Gson();

			SuccessfullResponse messageResp = GSON.fromJson(json, SuccessfullResponse.class);

			json = GSON.toJson(messageResp, SuccessfullResponse.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
