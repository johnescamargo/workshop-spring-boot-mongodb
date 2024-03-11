package com.imav.whatsapp.service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.imav.whatsapp.configuration.WebSocketSessionHandler;
import com.imav.whatsapp.dto.GenericMessageDto;
import com.imav.whatsapp.dto.GenericMessageDto.Button;
import com.imav.whatsapp.entity.ImageDb;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.ButtonReply;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.webhookModel.WebhookLocation;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyInitButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class SendWebSocketService {

	private static WebSocketSessionHandler CLIENT_SESSION_HANDLER = new WebSocketSessionHandler();

	public StompSession stompConnectionHttp() {

		StompSession session = null;

		try {

			StandardWebSocketClient client = new StandardWebSocketClient();
			WebSocketStompClient stompClient = new WebSocketStompClient(client);
			stompClient.setMessageConverter(new MappingJackson2MessageConverter());
			CompletableFuture<StompSession> sessionAsync = stompClient
					.connectAsync("ws://localhost:5000/websocket-server", CLIENT_SESSION_HANDLER);

			session = sessionAsync.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return session;
	}

	public StompSession stompConnectionTLS() {

		StompSession session = null;

		String websocketUrl = "wss://www.web.login.imav.com.br:5000/websocket-server";

		try {

			String trustStoreFile = "/etc/letsencrypt/live/www.web.login.imav.com.br/cert.jks";
			String trustStorePassword = "123456";
			String keyStoreFile = "/etc/letsencrypt/live/www.web.login.imav.com.br/keystore.p12";
			String keyStorePassword = "12345";

			// Load the trust store
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword.toCharArray());
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustStore);

			// Load the key store
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

			StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
			webSocketClient
					.setUserProperties(Collections.singletonMap("org.apache.tomcat.websocket.SSL_CONTEXT", sslContext));

			WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
			stompClient.setMessageConverter(new MappingJackson2MessageConverter());

			session = stompClient.connectAsync(websocketUrl, CLIENT_SESSION_HANDLER).get();

		} catch (Exception e) {
			e.printStackTrace();

		}

		return session;
	}

	public void sendMessageToWebSocket(GenericMessageDto dto) {
		StompSession session = stompConnectionTLS();
		//StompSession session = stompConnectionHttp();
		session.subscribe("/topic/message", CLIENT_SESSION_HANDLER);
		session.send("/app/process-message", dto);
		session.disconnect();
	}

	public void showCustomerToWebSocket() {
		StompSession session = stompConnectionTLS();
		//StompSession session = stompConnectionHttp();
		session.subscribe("/topic/customers", CLIENT_SESSION_HANDLER);
		session.send("/app/customers", true);
		session.disconnect();
	}

	public void updateWebsocket() {
		StompSession session = stompConnectionTLS();
		//StompSession session = stompConnectionHttp();
		session.subscribe("/topic/messages-customers", CLIENT_SESSION_HANDLER);
		session.send("/app/process-messages-customers", true);
		session.disconnect();
	}

	public void updateWantsToTalk() {
		StompSession session = stompConnectionTLS();
		//StompSession session = stompConnectionHttp();
		session.subscribe("/topic/talks", CLIENT_SESSION_HANDLER);
		session.send("/app/talk", true);
		session.disconnect();
	}

	public void convertMessageSendChat(ImavMessage message, int ownerOfMessage) {
		GenericMessageDto dto = new GenericMessageDto();

		dto.setOwnerOfMessage(ownerOfMessage);
		dto.setId(Integer.toString((int) message.getId()));
		dto.setName("IMAV");
		dto.setPhone_from(message.getTo());
		dto.setStatus(1);
		dto.setText(message.getText());
		dto.setTimestamp(message.getTimestamp());
		dto.setType(message.getType());
		dto.setIdWamid(message.getIdWamid());

		sendMessageToWebSocket(dto);
	}

	public void convertLocationSend(WebhookLocation location, int owner, String idWamid) {
		GenericMessageDto dto = new GenericMessageDto();

		try {

			dto.setOwnerOfMessage(owner);
			dto.setId("any");
			dto.setName("IMAV");
			dto.setPhone_from(location.getTo());
			dto.setStatus(1);
			dto.setText(location.getLocation().getAddress());
			dto.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000)));
			dto.setType(location.getType());
			dto.setIdWamid(idWamid);

		} catch (Exception e) {
			e.printStackTrace();
		}

		sendMessageToWebSocket(dto);
	}

	public void convertMessageFromCustomer(WebhookReceivedTextMessage message, int code) {
		GenericMessageDto dto = new GenericMessageDto();

		dto.setStatus(1);
		dto.setOwnerOfMessage(code);
		dto.setId(message.getEntry().get(0).getId());
		dto.setName(
				message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName());
		dto.setPhone_from(message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id());
		dto.setText(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getText().getBody());
		dto.setTimestamp(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp());
		dto.setType(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		dto.setIdWamid(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		sendMessageToWebSocket(dto);
	}

	public void convertMessageSend(MessageModel message, String idWamid) {
		GenericMessageDto dto = new GenericMessageDto();

		dto.setOwnerOfMessage(0);
		dto.setId("any");
		dto.setName("IMAV");
		dto.setPhone_from(message.getTo());
		dto.setStatus(1);
		dto.setText(message.getText().getBody());
		dto.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000)));
		dto.setType("text");
		dto.setIdWamid(idWamid);

		sendMessageToWebSocket(dto);
	}

	public void convertMessageButtonSend(ButtonReply button, String idWamid) {
		GenericMessageDto dto = new GenericMessageDto();
		List<Button> buttons = new ArrayList<>();

		dto.setOwnerOfMessage(0);
		dto.setId("any");
		dto.setName("IMAV");
		dto.setPhone_from(button.getTo());
		dto.setStatus(1);
		dto.setText(button.getInteractive().getBody().getText());
		dto.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000)));
		dto.setType(button.getInteractive().getType());
		dto.setIdWamid(idWamid);

		for (int i = 0; i < button.getInteractive().getAction().getButtons().size(); i++) {
			GenericMessageDto.Button innerButton = new Button();
			innerButton.setId(button.getInteractive().getAction().getButtons().get(i).getReply().getId());
			innerButton.setTitle(button.getInteractive().getAction().getButtons().get(i).getReply().getTitle());
			innerButton.setType(button.getInteractive().getAction().getButtons().get(i).getType());
			buttons.add(innerButton);
		}

		dto.setButtons(buttons);
		sendMessageToWebSocket(dto);
	}
	
	public void convertMessageButtonInitSend(ImavButtonMessage button) {
		GenericMessageDto dto = new GenericMessageDto();
		List<Button> buttons = new ArrayList<>();

		dto.setOwnerOfMessage(0);
		dto.setId("any");
		dto.setName("IMAV");
		dto.setPhone_from(button.getB_to());
		dto.setStatus(1);
		dto.setText(button.getB_text());
		dto.setTimestamp(button.getB_timestamp());
		dto.setType(button.getB_type());
		dto.setIdWamid(button.getIdWamid());

		for (int i = 0; i < button.getButtons().size(); i++) {
			GenericMessageDto.Button innerButton = new Button();
			innerButton.setId(button.getButtons().get(i).getB_id());
			innerButton.setTitle(button.getButtons().get(i).getB_title());
			innerButton.setType(button.getButtons().get(i).getB_type());
			buttons.add(innerButton);
		}

		dto.setButtons(buttons);
		sendMessageToWebSocket(dto);
	}
	

	public void convertMessageUrlSend(MessageWithURL url, String idWamid) {
		GenericMessageDto dto = new GenericMessageDto();

		try {

			dto.setOwnerOfMessage(0);
			dto.setId("any");
			dto.setName("IMAV");
			dto.setPhone_from(url.getTo());
			dto.setStatus(1);
			dto.setText(url.getText().getBody());
			dto.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000)));
			dto.setType("text");
			dto.setIdWamid(idWamid);

		} catch (Exception e) {
			e.printStackTrace();
		}

		sendMessageToWebSocket(dto);

	}

	public void convertInteractiveFromOutside(WebhookReceivedCallbackQuickReplyButtonClick message, int code) {
		GenericMessageDto dto = new GenericMessageDto();

		dto.setStatus(1);
		dto.setOwnerOfMessage(code);
		dto.setId(message.getEntry().get(0).getId());
		dto.setName(
				message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName());
		dto.setPhone_from(message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id());
		dto.setText(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getInteractive()
				.getButton_reply().getTitle());
		dto.setTimestamp(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp());
		dto.setType(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		dto.setIdWamid(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		sendMessageToWebSocket(dto);
	}
	
	public void convertButtonFromOutside(WebhookReceivedCallbackQuickReplyInitButtonClick message, int code) {
		GenericMessageDto dto = new GenericMessageDto();

		dto.setStatus(1);
		dto.setOwnerOfMessage(code);
		dto.setId(message.getEntry().get(0).getId());
		dto.setName(
				message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName());
		dto.setPhone_from(message.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id());
		dto.setText(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getButton().getText());
		dto.setTimestamp(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp());
		dto.setType(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		dto.setIdWamid(message.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		sendMessageToWebSocket(dto);
	}

	public void convertImageSendChat(ImageDb imgDb) {
		GenericMessageDto dto = new GenericMessageDto();

		try {

			dto.setOwnerOfMessage(0);
			dto.setId(imgDb.getId());
			dto.setName(imgDb.getId());
			dto.setPhone_from(imgDb.getPhone());
			dto.setStatus(1);
			dto.setText(imgDb.getCaption());
			dto.setTimestamp(imgDb.getTimestamp());
			dto.setType(imgDb.getType());
			dto.setIdWamid(imgDb.getIdWamid());
			dto.setMimeType(imgDb.getMimeType());
			dto.setContent(imgDb.getContent());

		} catch (Exception e) {
			e.printStackTrace();
		}

		sendMessageToWebSocket(dto);

	}


}
