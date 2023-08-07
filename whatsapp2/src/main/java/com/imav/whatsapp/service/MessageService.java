package com.imav.whatsapp.service;

import java.time.LocalDate;
import java.util.HashMap;

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

@Service
public class MessageService {

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
	private DBImavMessageButtonReplyResource dbOurButtonReplyResource;
	
	@Autowired
	private ConfirmationResponseRepository confirmationRespository;

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

				Customer customer = customerRepository.findByPhoneNumber(messageTo);
				customer.setTalk(false);
				customerRepository.save(customer);// Update customer

				websocketService.convertMessageSendChat(mess, ownerOfMessage);
				talkRepository.deleteByPhone(messageTo);
				websocketService.updateWantsToTalk();
				websocketService.showCustomerToWebSocket();

			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean sendInitMessage(MessageInitDto dto) {

		try {
			
			String response = "";
			HashMap<String, String> hashMap = new HashMap<>();
		
			InitMessageTemplate template = messageUtil.messageInitOrganizer(dto);
			
			String jsonMessage = GSON.toJson(template, InitMessageTemplate.class);
			System.out.println(jsonMessage);
			response = messageHttpService.sendMessage(jsonMessage);// Sending Message

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				
				// save new customer
				saveNewCustomer(dto.getPhone(), dto.getName());
				
				// Simulate and Save init-message-template
				ImavButtonMessage buttonMessage = messageUtil.setImavButtonMessage(dto, idWamid);
				
				dbOurButtonReplyResource.saveImavButtonMessageIntoDb(buttonMessage, idWamid);
				websocketService.convertMessageButtonInitSend(buttonMessage);
				saveListOfConfirmationResponse(dto, idWamid);

				websocketService.showCustomerToWebSocket();
			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

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
				dbMessageResource.saveImavMessageIntoDatabase(message, name, idWamid, true);
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
		}
		return true;
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
	
	public void saveNewCustomer(String phoneNumber, String name) {
		boolean resp = false;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		
		try {
			
			resp = customerRepository.existsByPhoneNumber(phoneNumber);
			
			if(!resp) {
				Customer customer = new Customer();
				customer.setC_phone_number(phoneNumber);
				customer.setC_name(name);
				customer.setC_timestamp(timestamp);
				customer.setTalk(false);
				
				customerRepository.save(customer);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		
	}

}
