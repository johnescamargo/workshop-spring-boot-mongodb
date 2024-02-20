package com.imav.whatsapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.imav.whatsapp.configuration.WebSocketSessionHandler;
import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.dto.GenericMessageDto;
import com.imav.whatsapp.dto.GenericMessageDto.Button;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.CustomerMessage;
import com.imav.whatsapp.entity.ImavButton;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavLocationMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.ButtonReply;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.repository.ImavButtonRepository;
import com.imav.whatsapp.webhookModel.WebhookLocation;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class SendWebSocketService {

	private static WebSocketSessionHandler CLIENTONESESSIONHANDLER = new WebSocketSessionHandler();

	@Autowired
	private ImavButtonRepository buttonRepo;

	public StompSession stompConnection() {

		StompSession session = null;

		try {
			WebSocketClient client = new StandardWebSocketClient();
			WebSocketStompClient stompClient = new WebSocketStompClient(client);
			stompClient.setMessageConverter(new MappingJackson2MessageConverter());
			CompletableFuture<StompSession> sessionAsync = stompClient
					.connectAsync("ws://localhost:5000/websocket-server", CLIENTONESESSIONHANDLER);

			session = sessionAsync.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return session;
	}

	public void sendMessageToWebSocket(GenericMessageDto obj) {
		StompSession session = stompConnection();
		session.subscribe("/topic/message", CLIENTONESESSIONHANDLER);
		session.send("/app/process-message", obj);
	}

	public void sendCustomersToWebSocket(List<CustomerDto> objs) {
		StompSession session = stompConnection();
		session.subscribe("/topic/customers", CLIENTONESESSIONHANDLER);
		session.send("/app/customers", objs);
	}

	public void sendAllMesssagesToWebSocket(List<GenericMessageDto> messages) {
		StompSession session = stompConnection();
		session.subscribe("/topic/customers", CLIENTONESESSIONHANDLER);
		session.send("/app/process-messages", messages);
	}

	public void convertCustomerToDto(List<Customer> customers) {
		List<CustomerDto> dtos = new ArrayList<>();

		for (int i = 0; i < customers.size(); i++) {
			CustomerDto dto = new CustomerDto();
			dto.setName(customers.get(i).getC_name());
			dto.setPhone_number(customers.get(i).getC_phone_number());
			dto.setTimestamp(Integer.valueOf(customers.get(i).getC_timestamp()));
			dtos.add(dto);
		}

		Collections.sort(dtos, Comparator.comparing(CustomerDto::getTimestamp));
		Collections.reverse(dtos);

		sendCustomersToWebSocket(dtos);
	}

	public void convertAllMessageToDto(List<CustomerMessage> customerMessages, List<ImavMessage> imavMessages,
			List<ImavButtonMessage> imavButtonMessages, List<ImavLocationMessage> imavLocationMessages) {
		List<GenericMessageDto> dtos = new ArrayList<>();

		if (!customerMessages.isEmpty()) {
			for (int i = 0; i < customerMessages.size(); i++) {
				GenericMessageDto dto = new GenericMessageDto();

				dto.setOwnerOfMessage(1);
				dto.setName(customerMessages.get(i).getM_name());
				dto.setPhone_from(customerMessages.get(i).getM_phone_from());
				dto.setStatus(customerMessages.get(i).getM_status());
				dto.setText(customerMessages.get(i).getM_text());
				dto.setTimestamp(customerMessages.get(i).getM_timestamp());
				dto.setType(customerMessages.get(i).getM_type());
				dto.setIdWamid(customerMessages.get(i).getIdWamid());

				dtos.add(dto);
			}
		}

		if (!imavMessages.isEmpty()) {
			for (int i = 0; i < imavMessages.size(); i++) {
				GenericMessageDto dto = new GenericMessageDto();

				dto.setOwnerOfMessage(0);
				dto.setId(Integer.toString((int) imavMessages.get(i).getId()));
				dto.setName("IMAV");
				dto.setPhone_from(imavMessages.get(i).getTo());
				dto.setStatus(imavMessages.get(i).getStatus());
				dto.setText(imavMessages.get(i).getText());
				dto.setTimestamp(imavMessages.get(i).getTimestamp());
				dto.setType(imavMessages.get(i).getType());
				dto.setIdWamid(imavMessages.get(i).getIdWamid());


				dtos.add(dto);
			}
		}

		if (!imavButtonMessages.isEmpty()) {
			for (int i = 0; i < imavButtonMessages.size(); i++) {
				GenericMessageDto dto = new GenericMessageDto();
				List<GenericMessageDto.Button> buttons = new ArrayList<>();

				dto.setOwnerOfMessage(0);
				dto.setId(Integer.toString((int) imavButtonMessages.get(i).getId()));
				dto.setName("IMAV");
				dto.setPhone_from(imavButtonMessages.get(i).getB_to());
				dto.setStatus(imavButtonMessages.get(i).getB_status());
				dto.setText(imavButtonMessages.get(i).getB_text());
				dto.setTimestamp(imavButtonMessages.get(i).getB_timestamp());
				dto.setType(imavButtonMessages.get(i).getB_type());
				dto.setIdWamid(imavButtonMessages.get(i).getIdWamid());


				long id = imavButtonMessages.get(i).getId();
				List<ImavButton> ourButtons = new ArrayList<>();
				ourButtons = buttonRepo.findAllByImavButtonMessageId(id);

				for (int j = 0; j < ourButtons.size(); j++) {
					GenericMessageDto.Button button = new Button();

					if (id == ourButtons.get(j).getImavButtonMessage().getId()) {
						button.setId(ourButtons.get(j).getB_id());
						button.setTitle(ourButtons.get(j).getB_title());
						button.setType(ourButtons.get(j).getB_type());
						buttons.add(button);
					}
				}

				dto.setButtons(buttons);

				dtos.add(dto);
			}
		}

		if (!imavLocationMessages.isEmpty()) {
			for (int i = 0; i < imavLocationMessages.size(); i++) {
				GenericMessageDto dto = new GenericMessageDto();

				dto.setOwnerOfMessage(0);
				dto.setId(Integer.toString((int) imavLocationMessages.get(i).getId()));
				dto.setName("IMAV");
				dto.setPhone_from(imavLocationMessages.get(i).getL_to());
				dto.setStatus(imavLocationMessages.get(i).getL_status());
				dto.setText(imavLocationMessages.get(i).getL_address());
				dto.setTimestamp(imavLocationMessages.get(i).getTimestamp());
				dto.setType(imavLocationMessages.get(i).getL_type());
				dto.setIdWamid(imavLocationMessages.get(i).getIdWamid());


				dtos.add(dto);
			}
		}

		Collections.sort(dtos, Comparator.comparing(GenericMessageDto::getTimestamp));

		sendAllMesssagesToWebSocket(dtos);
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

	public void convertMessageFromOutsideSend(WebhookReceivedTextMessage message, int code) {
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

}