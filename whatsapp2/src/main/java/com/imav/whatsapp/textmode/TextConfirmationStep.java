package com.imav.whatsapp.textmode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.ai.MessageAI;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.ButtonReplyService;
import com.imav.whatsapp.service.MessageButtonReplyService;
import com.imav.whatsapp.service.MessageService;
import com.imav.whatsapp.service.SendWebSocketService;
import com.imav.whatsapp.util.TextReplyUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class TextConfirmationStep {

	private static Gson GSON = new Gson();

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private MessageButtonReplyService buttonReplyService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private DBMessageResource messageResource;

	@Autowired
	private TextReplyUtil textReplyUtil;

	@Autowired
	private ButtonReplyService buttonReply;
	
	@Autowired
	private SendWebSocketService websocket;
		

	@Autowired
	private MessageAI messageAi;

	public void checkStepText(String obj, String phone) {

		int step = getCustomerStep(phone);

		switch (step) {

		case 1:
			textConfirmation1(obj, phone);
			break;

		case 2:
			textConfirmation2(obj, phone);
			break;

		default:

			break;
		}

	}
	
	public void checkStepTextDayOff(String obj, String phone) {

		
		int step = getCustomerStep(phone);

		switch (step) {

		case 1:
			textConfirmation1DayOff(obj, phone);
			break;

		case 2:
			textConfirmation2DayOff(obj, phone);
			break;

		default:

			break;
		}

	}

	public void textConfirmation1(String obj, String phone) {

		String resp = messageAi.analyzeTextYesOrNo(obj);
		Customer customer = getCustomer(phone);
		WebhookReceivedTextMessage text = convertCustomerText(obj);

		if (resp.equals("yes")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);// int = 1 from customer, int = 0 from IMAV
			
			messageService.sendMessageResponse(obj, textReplyUtil.setTextYes(), "SIM");
			
			int step = 0;
			boolean talk = false;
			String timestamp = "0";
			messageService.updateCustomer(phone, step, "normal", timestamp, talk);

		} else if (resp.equals("remarcar")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);// int = 1 from customer, int = 0 from IMAV
			
			String id = 
					text
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getId();
			
			messageService.updateConfirmationResponse(id, "REMARCAR");
			String path = ("/json/button_reply_remarcar.json");
			buttonReplyService.sendButtonResponse(phone, path);
			
			int step = 2;
			boolean talk = false;
			String timestamp = "0";
			messageService.updateCustomer(phone, step, "confirmation", timestamp, talk);

		} else if (resp.equals("void")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);// int = 1 from customer, int = 0 from IMAV
			
			String path = ("/json/message_dont_get_it.json");
			messageService.sendMessageModelImav(phone, null, path);
			
		} else if (resp.equals("cancelar")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);// int = 1 from customer, int = 0 from IMAV
			
			messageService.sendMessageResponse(obj, textReplyUtil.setTextCancel(), "CANCELADO");
			
			int step = 0;
			boolean talk = false;
			String timestamp = "0";
			messageService.updateCustomer(phone, step, "normal", timestamp, talk);
		}
	}
	
	public void textConfirmation1DayOff(String obj, String phone) {

		String resp = messageAi.analyzeTextYesOrNo(obj);
		Customer customer = getCustomer(phone);
		WebhookReceivedTextMessage text = convertCustomerText(obj);

		if (resp.equals("yes")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			messageService.sendMessageResponse(obj, textReplyUtil.setTextYes(), "SIM");
			
			int step = 0;
			boolean talk = false;
			messageService.updateCustomer(phone, step, "normal", "0", talk);

		} else if (resp.equals("remarcar")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			String id = 
					text
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getId();
			
			messageService.updateConfirmationResponse(id, resp);
			String path = ("/json/button_reply_remarcar_off.json");
			buttonReplyService.sendButtonResponse(phone, path);
			
			int step = 0;
			boolean talk = true;
			String timestamp = "0";
			messageService.updateCustomer(phone, step, "normal", timestamp, talk);

		} else if (resp.equals("void")) {

			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			String path = ("/json/message_dont_get_it.json");
			messageService.sendMessageModelImav(phone, null, path);
		}
	}

	public void textConfirmation2(String obj, String phone) {

		String resp = messageAi.analyzeTextTalkOrPhone(obj);
		Customer customer = getCustomer(phone);
		WebhookReceivedTextMessage text = convertCustomerText(obj);
		String name = customer.getName();
		int step = 0;//zero means going back to first step

		if (resp.equals("telefone")) {
			
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			buttonReply.sendMessageResponse(obj, textReplyUtil.setTextTelephone(), "REMARCAR");
			
			boolean talk = false;
			buttonReply.updateCustomer(phone, step, "normal", talk);

		} else if (resp.equals("falar")) {
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			buttonReply.updateCustomerWantToTalk(phone);
			
			buttonReplyService.messageTalkToUsResponse(phone, name);
			
			boolean talk = true;
			buttonReply.updateCustomer(phone, step, "normal", talk);

		} else if (resp.equals("void")) {
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			String path = ("/json/message_tel_or_speak.json");
			messageService.sendMessageModelImav(phone, null, path);
		}
	}
	
	public void textConfirmation2DayOff(String obj, String phone) {

		String resp = messageAi.analyzeTextTalkOrPhone(obj);
		Customer customer = getCustomer(phone);
		WebhookReceivedTextMessage text = convertCustomerText(obj);
		String name = customer.getName();
		int step = 0;//zero means going back to first step

		if (resp.equals("telefone")) {
			
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			buttonReply.sendMessageResponse(obj, textReplyUtil.setTextTelephone(), "REMARCAR");
			
			boolean talk = true;
			buttonReply.updateCustomer(phone, step, "normal", talk);

		} else if (resp.equals("falar")) {
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			buttonReply.updateCustomerWantToTalk(phone);
			buttonReplyService.messageTalkToUsResponse(phone, name);
			
			boolean talk = true;
			buttonReply.updateCustomer(phone, step, "normal", talk);

		} else if (resp.equals("void")) {
			messageResource.saveMessageIntoDatabase(text, customer);
			
			websocket.convertMessageFromCustomer(text, 1);
			
			String path = ("/json/message_tel_or_speak.json");
			messageService.sendMessageModelImav(phone, null, path);
		}
	}

	public int getCustomerStep(String phone) {

		int step = 0;

		try {

			Customer customer = customerRepository.findByPhoneNumber(phone);
			step = customer.getStep();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return step;
	}

	public Customer getCustomer(String phone) {

		Customer customer = new Customer();

		try {

			customer = customerRepository.findByPhoneNumber(phone);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	public WebhookReceivedTextMessage convertCustomerText(String obj) {

		WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();

		try {

			messageReceived = GSON.fromJson(obj, WebhookReceivedTextMessage.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageReceived;
	}

}
