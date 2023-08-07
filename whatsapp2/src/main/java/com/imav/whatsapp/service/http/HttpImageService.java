package com.imav.whatsapp.service.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.model.ImageResponse;
import com.imav.whatsapp.service.WhatsappTokens;

@Service
public class HttpImageService {

	private Logger logger = LogManager.getLogger(HttpImageService.class);

	@Autowired
	private WhatsappTokens whatsappTokens;

	private Gson GSON = new Gson();

	public ImageResponse getImageUrl(String idImage) {

		logger.info("Sending Message GET IMAGE - HTTP .........");

		ImageResponse resp = new ImageResponse();

		try {

			URL url = new URL("https://graph.facebook.com/v17.0/" + idImage);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Authorization", "Bearer " + whatsappTokens.getAccessToken());
			httpConn.connect();

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
				String response = s.hasNext() ? s.next() : "";

				resp = GSON.fromJson(response, ImageResponse.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resp;
	}

	public byte[] getImage(ImageResponse img) {

		String urlImg = img.getUrl();
		InputStream inStream = null;

		try {

			URL url = new URL(urlImg);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Authorization", "Bearer " + whatsappTokens.getAccessToken());

			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConn.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = inputStreamToByte(inStream);

		return data;
	}

	public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
