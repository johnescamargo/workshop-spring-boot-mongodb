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
import com.imav.whatsapp.dto.MessageInitDto;
import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.ImageDb;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.ButtonReply;
import com.imav.whatsapp.model.ImageResponse;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.ImageDbRepository;
import com.imav.whatsapp.resource.DBImavMessageButtonReplyResource;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpImageService;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedMediaMessageWithImage;

@Service
public class MessageButtonReplyService {

	private Logger logger = LogManager.getLogger(MessageButtonReplyService.class);
	
	private static Gson GSON = new Gson();

	@Autowired
	private HttpMessageService httpMessageService;
	
	@Autowired
	private HttpImageService httpImageService;

	@Autowired
	private DBImavMessageButtonReplyResource dbOurButtonReplyResource;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private DBMessageResource dbMessageResource;

	@Autowired
	private ConfirmationResponseRepository confirmationRespository;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ImageDbRepository imageDbRepository;

	public void sendButtonReplyYesNo(MessageInitDto dto) {

		String json = "";
		HashMap<String, String> hashMap = new HashMap<>();
		String from = dto.getPhone();

		boolean limit = checkTimelimit(from);

		try {

			if (!limit) {

				String path = ("/json/ButtonReplyYesNo.json");

				json = readJsonFiles(path);

				ButtonReply button = new ButtonReply();

				button = GSON.fromJson(json, ButtonReply.class);
				button.setTo(dto.getPhone());
				String jsonMessage = GSON.toJson(button, ButtonReply.class);

				String response = httpMessageService.sendMessage(jsonMessage);

				hashMap = messageUtil.checkResponseMessageSent(response);
				String resp = hashMap.get("resp");

				if (resp.equals("success")) {
					String idWamid = hashMap.get("idWamid");
					dbOurButtonReplyResource.saveImavMessageButtonReplyIntoDatabase(button, idWamid);
					websocketService.convertMessageButtonSend(button, idWamid);
					saveListOfConfirmationResponse(dto, idWamid);
				} else {
					String timestamp = hashMap.get("timestamp");
					System.out.println("Message Not sent - timestamp: " + timestamp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendButtonResponse(String from) {

		HashMap<String, String> hashMap = new HashMap<>();
		boolean limit = checkTimelimit(from);

		try {

			if (!limit) {
				String path = ("/json/MessageResponse.json");

				String json = readJsonFiles(path);

				ButtonReply button = new ButtonReply();

				button = GSON.fromJson(json, ButtonReply.class);
				button.setTo(from);
				String jsonMessage = GSON.toJson(button, ButtonReply.class);

				String response = httpMessageService.sendMessage(jsonMessage);

				hashMap = messageUtil.checkResponseMessageSent(response);
				String resp = hashMap.get("resp");

				if (resp.equals("success")) {
					String idWamid = hashMap.get("idWamid");
					dbOurButtonReplyResource.saveImavMessageButtonReplyIntoDatabase(button, idWamid);
					websocketService.convertMessageButtonSend(button, idWamid);
				} else {
					System.out.println("Message not sent");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("sendButtonResponse error *****");
			logger.info(e);
		}
	}

	public void sendButtonResponseWorkOff(String from) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			String path = ("/json/MessageResponseWorkOff.json");

			String json = readJsonFiles(path);

			ButtonReply button = new ButtonReply();

			button = GSON.fromJson(json, ButtonReply.class);
			button.setTo(from);
			String jsonMessage = GSON.toJson(button, ButtonReply.class);

			String response = httpMessageService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				dbOurButtonReplyResource.saveImavMessageButtonReplyIntoDatabase(button, idWamid);
				websocketService.convertMessageButtonSend(button, idWamid);
			} else {
				System.out.println("Message not sent");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("sendButtonResponse error *****");
			logger.info(e);
		}
	}

	public void messageZeroResponse(String from, String name) {

		String json = "";
		int ownerOfMessage = 0;
		HashMap<String, String> hashMap = new HashMap<>();
		boolean limit = checkTimelimit(from);

		try {

			if (limit) {

				String path = ("/json/MessageZeroResponse.json");

				json = readJsonFiles(path);

				MessageModel message = new MessageModel();

				message = GSON.fromJson(json, MessageModel.class);
				message.setTo(from);
				String jsonMessage = GSON.toJson(message, MessageModel.class);

				String response = httpMessageService.sendMessage(jsonMessage);
				hashMap = messageUtil.checkResponseMessageSent(response);
				String resp = hashMap.get("resp");

				if (resp.equals("success")) {
					String idWamid = hashMap.get("idWamid");
					ImavMessage mess = dbMessageResource.saveImavMessageIntoDatabase(message, name, idWamid, false);
					websocketService.convertMessageSendChat(mess, ownerOfMessage);
				} else {
					String timestamp = hashMap.get("timestamp");
					System.out.println("Message Not sent - timestamp: " + timestamp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("messageZeroResponse error *****");
			logger.info(e);
		}

	}

	public void messageNoAudio(String from, String name) {

		String json = "";
		int ownerOfMessage = 0;
		HashMap<String, String> hashMap = new HashMap<>();
		boolean limit = checkTimelimit(from);

		try {

			if (!limit) {
				String path = ("/json/MessageNoAudio.json");

				json = readJsonFiles(path);

				MessageModel message = new MessageModel();

				message = GSON.fromJson(json, MessageModel.class);
				message.setTo(from);
				String jsonMessage = GSON.toJson(message, MessageModel.class);

				String response = httpMessageService.sendMessage(jsonMessage);
				hashMap = messageUtil.checkResponseMessageSent(response);
				String resp = hashMap.get("resp");

				if (resp.equals("success")) {
					String idWamid = hashMap.get("idWamid");
					ImavMessage mess = dbMessageResource.saveImavMessageIntoDatabase(message, name, idWamid, false);
					websocketService.convertMessageSendChat(mess, ownerOfMessage);
				} else {
					String timestamp = hashMap.get("timestamp");
					System.out.println("Message Not sent - timestamp: " + timestamp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("messageNoText error *****");
			logger.info(e);
		}

	}

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

		confirmationRespository.save(obj);
	}

	public String readJsonFiles(String path) {

		String json = "";

		try (InputStream inputStream = getClass().getResourceAsStream(path);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	private boolean checkTimelimit(String phone) {
		Customer customer = new Customer();

		try {

			// Check if customer exists
			boolean exist = customerRepository.existsById(phone);
			if (exist) {
				// get customer
				customer = customerRepository.findByPhoneNumber(phone);
			}

			// check if limit is less than 7200 2 hours calc
			long timelimit = Long.parseLong(customer.getC_timelimit());
			long timestamp = System.currentTimeMillis() / 1000;

			long result = timestamp - timelimit;

			// if so return true and save message into DB, websocket etc
			// 3600 seg = 1h
			// 600 = 10min
			if (result <= 600) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("checkTimelimit error");
			return false;
		}

		return false;
	}

	public void getImage(String obj, String type) {
		WebhookReceivedMediaMessageWithImage img = new WebhookReceivedMediaMessageWithImage();
		ImageResponse imageResp = new ImageResponse();

		try {
			
			img = GSON.fromJson(obj, WebhookReceivedMediaMessageWithImage.class);
			String idImage = img
					.getEntry().get(0)
					.getChanges().get(0)
					.getValue()
					.getMessages().get(0)
					.getImage()
					.getId();
			
			// Text from image
			String caption = img
					.getEntry().get(0)
					.getChanges().get(0)
					.getValue()
					.getMessages().get(0)
					.getImage()
					.getCaption();
			
			// Customer's phone number from image WebHook
			String phone = img
					.getEntry().get(0)
					.getChanges().get(0)
					.getValue()
					.getContacts().get(0)
					.getWa_id();
			
			String idWamid = img
					.getEntry().get(0)
					.getChanges().get(0)
					.getValue()
					.getMessages().get(0)
					.getId();
			
			imageResp = httpImageService.getImageUrl(idImage);
			byte[] content = httpImageService.getImage(imageResp);
			
			// TODO
			// make a statement for errors 
			saveImageDb(imageResp, caption, idImage, content, phone, idWamid, type);
						
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.info("Image  and caption not saved... " + Long.toString(System.currentTimeMillis() / 1000));			
		}

	}
	
	private void saveImageDb (ImageResponse imageResp, String caption, String idImage, byte[] content, String phone, String idWamid, String type) {
		ImageDb imgDb = new ImageDb();
				
		try {
			
			imgDb.setCaption(caption);
			imgDb.setId(idImage);
			imgDb.setMimeType(imageResp.getMime_type());
			imgDb.setTimestamp(Long.toString(System.currentTimeMillis() / 1000));
			imgDb.setContent(content);;
			imgDb.setPhone(phone);
			imgDb.setIdWamid(idWamid);
			imgDb.setType(type);
			
			imageDbRepository.save(imgDb);
			websocketService.convertImageSendChat(imgDb);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}