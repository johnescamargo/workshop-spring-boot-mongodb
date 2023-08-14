package com.imav.whatsapp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.WantsToTalk;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.WantsToTalkRepository;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.http.HttpMessageService;
import com.imav.whatsapp.util.MessageUtil;
import com.imav.whatsapp.util.TextReplyUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyInitButtonClick;

@Service
public class ButtonReplyService {

	private static Gson GSON = new Gson();
	
	@Autowired
	private HttpMessageService messageHttpService;

	@Autowired
	private TextReplyUtil textReplyUtil;

	@Autowired
	private DBMessageResource dbMessageResource;

	@Autowired
	private SendWebSocketService websocketService;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private LocationReplyService locationService;
	
	@Autowired
	private MessageButtonReplyService buttonReplyService;
	
	@Autowired
	private ConfirmationResponseRepository confirmationResponseRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private WantsToTalkRepository wantsToTalkRepository;

	public void sendResponseToButtonClicked(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
		MessageWithURL url = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			String id = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getInteractive()
					.getButton_reply()
					.getId();
			
			switch (id) {
			case "button-telefone":
				sendMessageResponse(obj, textReplyUtil.setTextTelephone(), "Sem resposta");

				break;

			case "button-whatsapp":
				url.setTo(phone);
				url.text.setBody(textReplyUtil.setLinkWhatsapp());
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				sendImavResponseURL(url);

				break;

			case "button-localizacao":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				locationService.sendLocation(phone);

				break;
				
			case "button-falar":
				buttonReplyService.messageTalkToUsResponse(phone, id);

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Interactive button failed");
		}
	}
	
	public void sendResponseToButtonClickedDayOff(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
		MessageWithURL url = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			String id = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getInteractive()
					.getButton_reply()
					.getId();
			
			switch (id) {
			case "button-telefone":
				sendMessageResponse(obj, textReplyUtil.setTextTelephone(), "Sem resposta");

				break;

			case "button-whatsapp":
				url.setTo(phone);
				url.text.setBody(textReplyUtil.setLinkWhatsapp());
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				sendImavResponseURL(url);

				break;

			case "button-localizacao":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				locationService.sendLocation(phone);

				break;
				
			case "button-falar":
				buttonReplyService.messageTalkToUsResponseDayOff(phone, id);

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Interactive button failed");
		}
	}
	
	public void sendResponseToConfirmationButtonClicked(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
		MessageWithURL url = new MessageWithURL();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			String id = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getInteractive()
					.getButton_reply()
					.getId();

			switch (id) {
			case "button-telefone":
				sendMessageResponse(obj, textReplyUtil.setTextTelephone(), "REMARCAR");
				int step = 0;
				boolean talk = false;
				updateCustomer(phone, step, "normal", talk);
				
				break;

			case "button-whatsapp":
				url.setTo(phone);
				url.text.setBody(textReplyUtil.setLinkWhatsapp());
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				sendImavResponseURL(url);

				break;

			case "button-localizacao":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				locationService.sendLocation(phone);

				break;
				
			case "button-falar":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				updateCustomerWantToTalk(phone);
				buttonReplyService.messageTalkToUsResponse(phone, id);
				
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Interactive button failed");
		}
	}
	
	
	public void sendResponseToInitButtonClicked(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();
		
		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			String id = buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getButton()
					.getPayload();

			switch (id) {
			case "SIM":
				sendButtonResponse(obj, textReplyUtil.setTextYes(), "SIM");

				break;

			case "REMARCAR":// Button REMARCAR
				convertWebhookButtonInit(obj, phone, "REMARCAR");
				String path = ("/json/button_reply_remarcar.json");
				buttonReplyService.sendButtonResponse(phone, path);
				
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Quickly response button failed");
		}
	}
	
	public void sendResponseToInitButtonClickedDayOff(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();
		
		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			String id = buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getButton()
					.getPayload();

			switch (id) {
			case "SIM":
				sendButtonResponse(obj, textReplyUtil.setTextYes(), "SIM");

				break;

			case "REMARCAR":// Button REMARCAR
				convertWebhookButtonInitDayOff(obj, phone, "NÃO");
				String path = ("/json/button_reply_remarcar_off.json");
				buttonReplyService.sendButtonResponse(phone, path);
				
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Quickly response button failed");
		}
	}

	public void sendResponseToAudio(String phone) {

		MessageWithURL url = new MessageWithURL();

		try {
			url.setTo(phone);
			url.text.setBody(textReplyUtil.setLinkWhatsapp());
			sendImavResponseURL(url);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Audio failed");
		}
	}

	public void sendImavResponseURL(MessageWithURL url) {

		HashMap<String, String> hashMap = new HashMap<>();
		String jsonMessage = GSON.toJson(url, MessageWithURL.class);
		String response = messageHttpService.sendMessage(jsonMessage);

		hashMap = messageUtil.checkResponseMessageSent(response);
		String resp = hashMap.get("resp");

		if (resp.equals("success")) {
			String idWamid = hashMap.get("idWamid");
			dbMessageResource.saveImavMessageURlIntoDatabase(url, idWamid);
			websocketService.convertMessageUrlSend(url, idWamid);
		} else {
			String timestamp = hashMap.get("timestamp");
			System.out.println("Message Not sent - timestamp: " + timestamp);
		}
	}

	public void sendMessageResponse(String obj, String msgBody, String respYesOrNo) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			WebhookReceivedCallbackQuickReplyButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyButtonClick();
			MessageModel messageModel = new MessageModel();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);

			// extract phone number from the WebHook payload
			String phone = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getFrom();

			messageModel.setTo(phone);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				saveInteractiveMessageFromCustomer(buttonClicked, phone);
				String idWamid = hashMap.get("idWamid");
				String name = "IMAV";
				dbMessageResource.saveImavMessageIntoDatabase(messageModel, name, idWamid, false);
				
				//updateCustomerWantToTalk(phone);
				
				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
					String id = 
							buttonClicked
							.getEntry()
							.get(0)
							.getChanges()
							.get(0)
							.getValue()
							.getMessages()
							.get(0)
							.getContext()
							.getId();
					
					updateConfirmationResponse(id, respYesOrNo);
				}

			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendButtonResponse(String obj, String msgBody, String respYesOrNo) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();
			MessageModel messageModel = new MessageModel();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			// extract phone number from the WebHook payload
			String phone = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getFrom();

			messageModel.setTo(phone);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				saveButtonMessageFromCustomer(buttonClicked, phone);
				String idWamid = hashMap.get("idWamid");								
				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
					String id = 
							buttonClicked
							.getEntry()
							.get(0)
							.getChanges()
							.get(0)
							.getValue()
							.getMessages()
							.get(0)
							.getContext()
							.getId();
					
					updateConfirmationResponse(id, respYesOrNo);
					
					if (respYesOrNo.equals("SIM")) {
						int step = 0;
						boolean talk = false;
						updateCustomer(phone, step, "normal", talk);
					}
					
					if (respYesOrNo.equals("REMARCAR")) {
						int step = 2;
						boolean talk = false;
						updateCustomer(phone, step, "confirmation", talk);
					}
				}

			} else {
				String timestamp = hashMap.get("timestamp");
				System.out.println("Message Not sent - timestamp: " + timestamp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void convertWebhookButtonInit(String obj, String msgBody, String respYesOrNo) {
			
		try {

			WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			// extract phone number from the WebHook payload
			String phone = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getFrom();

			saveButtonMessageFromCustomer(buttonClicked, phone);
			
				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
					String id = 
							buttonClicked
							.getEntry()
							.get(0)
							.getChanges()
							.get(0)
							.getValue()
							.getMessages()
							.get(0)
							.getContext()
							.getId();
					
					if (respYesOrNo.equals("REMARCAR")) {
						int step = 2;
						boolean talk = false;
						updateCustomer(phone, step, "confirmation", talk);
					}
					
					updateConfirmationResponse(id, respYesOrNo);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void convertWebhookButtonInitDayOff(String obj, String msgBody, String respYesOrNo) {
		
		try {

			WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			// extract phone number from the WebHook payload
			String phone = 
					buttonClicked
					.getEntry()
					.get(0)
					.getChanges()
					.get(0)
					.getValue()
					.getMessages()
					.get(0)
					.getFrom();

			saveButtonMessageFromCustomer(buttonClicked, phone);
			
				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("NÃO")) {
					String id = 
							buttonClicked
							.getEntry()
							.get(0)
							.getChanges()
							.get(0)
							.getValue()
							.getMessages()
							.get(0)
							.getContext()
							.getId();
					
					if (respYesOrNo.equals("NÃO")) {
						int step = 0;
						boolean talk = true;
						updateCustomer(phone, step, "normal", talk);
					}
					
					updateConfirmationResponse(id, respYesOrNo);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void saveInteractiveMessageFromCustomer(WebhookReceivedCallbackQuickReplyButtonClick obj, String phone) {
		try {
			Customer customer = dbMessageResource.getCustomerObject(phone);

			dbMessageResource.saveInteractiveReplyIntoDatabase(obj, customer);

			websocketService.convertInteractiveFromOutside(obj, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void saveButtonMessageFromCustomer(WebhookReceivedCallbackQuickReplyInitButtonClick obj, String phone) {
		try {
			Customer customer = dbMessageResource.getCustomerObject(phone);

			dbMessageResource.saveButtonReplyIntoDatabase(obj, customer);

			websocketService.convertButtonFromOutside(obj, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateConfirmationResponse(String idWamid, String respYesOrNo) {
		ConfirmationResponse confirmation = new ConfirmationResponse();

		boolean resp = confirmationResponseRepository.existsByIdWamid(idWamid);

		if (resp) {
			
			confirmation = confirmationResponseRepository.findById_Wamid(idWamid);

			confirmation.setResponse(respYesOrNo);

			confirmationResponseRepository.save(confirmation);
		}

	}

	public void saveInteractiveButtonLocation(String obj, String phone) {
		
		try {
			Customer customer = dbMessageResource.getCustomerObject(phone);

			WebhookReceivedCallbackQuickReplyButtonClick interactive = dbMessageResource
					.saveInteractiveReplyIntoDatabase(obj, customer);

			websocketService.convertInteractiveFromOutside(interactive, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCustomer(String phone, int step, String mode, boolean talk) {
		
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		
		try {
			
			Customer customer = customerRepository.findByPhoneNumber(phone);
			customer.setMode(mode);
			customer.setStep(step);
			customer.setTimelimit(timestamp);
			customer.setTalk(talk);
			
			customerRepository.save(customer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateCustomerWantToTalk(String phone) {
						
		try {
			
			Customer customer = customerRepository.findByPhoneNumber(phone);
			customer.setTalk(true);
			
			String name = customer.getName();
			WantsToTalk talk = new WantsToTalk(name, phone);
			
			wantsToTalkRepository.save(talk);			
			customerRepository.save(customer);
			
			websocketService.updateWantsToTalk();
			websocketService.updateWebsocket();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
