package com.imav.whatsapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.MessageEnviaDto;
import com.imav.whatsapp.dto.MessageInitDto;
import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.entity.WantsToTalk;
import com.imav.whatsapp.model.InitMessageTemplate;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.WantsToTalkRepository;
import com.imav.whatsapp.resource.DBImavMessageButtonReplyResource;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class MessageService {

	private Logger logger = LogManager.getLogger(MessageService.class);

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
	private DBImavMessageButtonReplyResource dbImavButtonReplyResource;

	@Autowired
	private ConfirmationResponseRepository confirmationRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private WantsToTalkRepository talkRepository;

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
				String id = hashMap.get("idWamid");
				ImavMessage mess = dbMessageResource.saveImavFrontEndMessageIntoDatabase(message, id);

				updateCustomerWantToTalk(messageTo);
				updateCustomerToNormal(messageTo);

				websocketService.convertMessageSendChat(mess, ownerOfMessage);
				talkRepository.deleteByPhone(messageTo);
				websocketService.updateWantsToTalk();
				websocketService.showCustomerToWebSocket();

			} else {
				String timestamp = hashMap.get("timestamp");
				logger.info("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	public void sendMessageModelImav(String phone, String path) {

		String json = "";
		int ownerOfMessage = 0;
		HashMap<String, String> hashMap = new HashMap<>();

		try {

			json = readJsonFiles(path);

			MessageModel message = new MessageModel();

			message = GSON.fromJson(json, MessageModel.class);
			message.setTo(phone);
			String jsonMessage = GSON.toJson(message, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);
			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				ImavMessage mess = dbMessageResource.saveImavMessageIntoDatabase(message, idWamid);
				websocketService.convertMessageSendChat(mess, ownerOfMessage);
			} else {
				String timestamp = hashMap.get("timestamp");
				logger.info("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Send message to customer Save message into DB Send websocket message Save
	 * confirmation list Save and update customer websocket
	 */
	public boolean sendInitMessageImav(MessageInitDto dto) {

		boolean msgSent = false;

		try {

			HashMap<String, String> hashMap = new HashMap<>();

			// Template IMAV - Initial message
			InitMessageTemplate template = messageUtil.messageInitOrganizer(dto);

			String jsonMessage = GSON.toJson(template, InitMessageTemplate.class);

			String response = messageHttpService.sendMessage(jsonMessage);// Sending Message

			hashMap = messageUtil.checkResponseMessageSent(response);

			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				msgSent = true;
				String idWamid = hashMap.get("idWamid");

				// save or update new customer
				saveNewCustomer(dto.getPhone(), dto.getName());

				// Simulate and Save initial-message-template
				ImavButtonMessage buttonMessage = messageUtil.setImavButtonMessage(dto, idWamid);

				dbImavButtonReplyResource.saveImavButtonMessageIntoDb(buttonMessage, idWamid);
				websocketService.convertMessageButtonInitSend(buttonMessage);

				saveListOfConfirmationResponse(dto, idWamid);
				websocketService.showCustomerToWebSocket();
			} else {
				String timestamp = hashMap.get("timestamp");
				logger.info("Message Not sent - timestamp: " + timestamp);
				msgSent = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
		return msgSent;
	}

	// TODO check everything
	public boolean sendMessageEnvia(MessageEnviaDto dto, String msg) {

		try {

			HashMap<String, String> hashMap = new HashMap<>();
			MessageModel message = new MessageModel();
			String name = dto.getName();
			String phone = dto.getInternacionalCode() + dto.getPhone();
			String response = "";

			message.setTo(phone);
			message.text.setBody(msg);

			String jsonMessage = GSON.toJson(message, MessageModel.class);

			response = messageHttpService.sendMessage(jsonMessage);// Sending Message

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				dbMessageResource.saveImavMessageIntoDatabase(message, idWamid);
				websocketService.convertMessageSend(message, idWamid);

				WantsToTalk talk = new WantsToTalk(name, phone);
				talkRepository.save(talk);
				websocketService.updateWantsToTalk();
				websocketService.showCustomerToWebSocket();
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
		return true;
	}

	public void sendMessageResponse(String obj, String msgBody) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
			MessageModel messageModel = new MessageModel();

			messageReceived = GSON.fromJson(obj, WebhookReceivedTextMessage.class);

			// extract phone number from the WebHook payload
			String phone = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getFrom();

			messageModel.setTo(phone);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				dbMessageResource.saveImavMessageIntoDatabase(messageModel, idWamid);
				websocketService.convertMessageSend(messageModel, idWamid);
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
	}

	/*
	 * Save and create list of initial template message sent per day
	 */
	public void saveListOfConfirmationResponse(MessageInitDto dto, String idWamid) {

		LocalDate currentDate = LocalDate.now();
		String day = Integer.toString(currentDate.getDayOfMonth());
		String month = Integer.toString(currentDate.getMonthValue());
		String year = Integer.toString(currentDate.getYear());
		String date = "";

		if (month.length() < 2) {
			month = "0" + month;
		}

		if (day.length() < 2) {
			day = "0" + day;
		}

		date = day + "/" + month + "/" + year;

		ConfirmationResponse obj = new ConfirmationResponse();
		obj.setDoctor(dto.getDoctor());
		obj.setHour(dto.getTime());
		obj.setIdWamid(idWamid);
		obj.setName(dto.getName());
		obj.setPhoneNumber(dto.getPhone());
		obj.setResponse("Sem resposta");
		obj.setService(dto.getService());
		obj.setServiceDate(dto.getDate());
		obj.setShippingDate(date);
		obj.setStatus(1);
		obj.setUser(dto.getUser());

		confirmationRepository.save(obj);
	}

	/*
	 * Save new Customer after sending template message, or Update customer mode,
	 * timestamp, and step
	 */
	public void saveNewCustomer(String phoneNumber, String name) {
		boolean resp = false;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		try {

			resp = customerRepository.existsByPhoneNumber(phoneNumber);

			if (!resp) {
				Customer customer = new Customer();
				customer.setPhoneNumber(phoneNumber);
				customer.setName(name);
				customer.setTimestamp(timestamp);
				customer.setTalk(false);
				customer.setMode("confirmation");
				customer.setStep(1);

				customerRepository.save(customer);
			} else {
				Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
				customer.setTimestamp(timestamp);
				customer.setMode("confirmation");
				customer.setName(name);
				customer.setStep(1);

				customerRepository.save(customer);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}

	}

	public void updateCustomerToNormal(String phone) {

		Customer customer = new Customer();

		try {

			customer = customerRepository.findByPhoneNumber(phone);
			customer.setMode("normal");

			customerRepository.save(customer);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCustomerWantToTalk(String phone) {

		Customer customer = new Customer();

		try {

			customer = customerRepository.findByPhoneNumber(phone);
			customer.setTalk(false);
			customerRepository.save(customer);// Update customer

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}

	}

	public void updateConfirmationResponse(String idWamid, String reply) {
		ConfirmationResponse confirmation = new ConfirmationResponse();

		boolean resp = confirmationRepository.existsByIdWamid(idWamid);

		if (resp) {

			confirmation = confirmationRepository.findById_Wamid(idWamid);

			confirmation.setResponse(reply);

			confirmationRepository.save(confirmation);
		}

	}

	public void updateCustomer(String phone, int step, String mode, String timelimit, boolean talk) {

		Customer customer = new Customer();

		try {

			customer = customerRepository.findByPhoneNumber(phone);
			customer.setMode(mode);
			customer.setStep(step);
			customer.setTimelimit(timelimit);
			customer.setTalk(talk);

			customerRepository.save(customer);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}

	}

	public String readJsonFiles(String path) {

		String json = "";

		try (InputStream inputStream = getClass().getResourceAsStream(path);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(e);
		}

		return json;
	}

}
