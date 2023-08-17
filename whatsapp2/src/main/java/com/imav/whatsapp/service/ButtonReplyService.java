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
	private MessageService messageService;

	@Autowired
	private DBMessageResource messageResource;

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

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getInteractive().getButton_reply().getId();

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
				buttonReplyService.messageTalkToUsResponse(phone);

			case "button-cancel":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);

//				String path = ("/json/message_reply_cancelar.json");
//				messageService.sendMessageModelImav(phone, path);

				int stepCancel = 0;
				boolean talkCancel = false;
				updateCustomer(phone, stepCancel, "normal", talkCancel);
				messageService.sendMessageResponse(obj, textReplyUtil.setTextCancel(), "CANCELADO");

				break;

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

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getInteractive().getButton_reply().getId();

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
				buttonReplyService.messageTalkToUsResponseDayOff(phone);

			case "button-cancel":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);

//				String path = ("/json/message_reply_cancelar.json");
//				messageService.sendMessageModelImav(phone, path);

				int stepCancel = 0;
				boolean talkCancel = false;
				updateCustomer(phone, stepCancel, "normal", talkCancel);
				messageService.sendMessageResponse(obj, textReplyUtil.setTextCancel(), "CANCELADO");

				break;

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

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getInteractive().getButton_reply().getId();

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
				buttonReplyService.messageTalkToUsResponse(phone);

				break;

			case "button-cancel":
				saveInteractiveMessageFromCustomer(buttonClicked, phone);

				int stepCancel = 0;
				boolean talkCancel = false;
				updateCustomer(phone, stepCancel, "normal", talkCancel);
				messageService.sendMessageResponse(obj, textReplyUtil.setTextCancel(), "CANCELADO");

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Interactive button failed");
		}
	}

	public void sendResponseToInitButtonClickedStep1(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();


		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			String payloadResp = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getButton().getPayload();
			
			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getContext().getId();

			switch (payloadResp) {
			case "SIM":
				saveInitButtonClick(obj, phone, payloadResp);
				sendButtonResponse(obj, textReplyUtil.setTextYes(), "SIM");

				break;

			case "REMARCAR":// Button REMARCAR
				saveInitButtonClick(obj, phone, payloadResp);
				
				int step2 = 2;
				boolean talk2 = false;
				updateCustomer(phone, step2, "confirmation", talk2);
				messageService.updateConfirmationResponse(id, "REMARCAR");
				
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

	public void sendResponseToInitButtonClicked(String obj, String phone) {

		WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();

		try {

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			String payloadResp = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getButton().getPayload();

			switch (payloadResp) {
			case "SIM":
				sendButtonResponse(obj, textReplyUtil.setTextYes(), "SIM");

				break;

			case "REMARCAR":// Button REMARCAR
				saveInitButtonClick(obj, phone, payloadResp);
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

			String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getButton()
					.getPayload();

			switch (id) {
			case "SIM":
				sendButtonResponse(obj, textReplyUtil.setTextYes(), "SIM");

				break;

			case "REMARCAR":// Button REMARCAR
				convertWebhookButtonInitDayOff(obj, phone, "REMARCAR");
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
			String phone = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
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
				dbMessageResource.saveImavMessageIntoDatabase(messageModel, idWamid);

				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
					String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
							.getContext().getId();

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
			String phone = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getFrom();

			messageModel.setTo(phone);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("idWamid");
				
				dbMessageResource.saveImavFrontEndMessageIntoDatabase(messageModel, idWamid);

				websocketService.convertMessageSend(messageModel, idWamid);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
					String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
							.getContext().getId();

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


	public void sendMessageResponseInit(String phone, String id, String msgBody, String respYesOrNo) {

		HashMap<String, String> hashMap = new HashMap<>();

		try {

			MessageModel messageModel = new MessageModel();
			messageModel.setTo(phone);// Set customer's number
			messageModel.text.setBody(msgBody);

			String jsonMessage = GSON.toJson(messageModel, MessageModel.class);

			String response = messageHttpService.sendMessage(jsonMessage);

			hashMap = messageUtil.checkResponseMessageSent(response);
			String resp = hashMap.get("resp");

			if (resp.equals("success")) {
				String idWamid = hashMap.get("resp");
				Customer customer = customerRepository.findByPhoneNumber(phone);
				messageResource.saveMessageModelIntoDatabase(messageModel, customer, idWamid);

				websocketService.convertMessageSend(messageModel, idWamid);// int = 1 from customer, int = 0 from IMAV

				int step = 0;
				boolean talk = false;
				String timestamp = "0";
				messageService.updateCustomer(phone, step, "normal", timestamp, talk);

				if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {

					updateConfirmationResponse(id, respYesOrNo);

					if (respYesOrNo.equals("SIM")) {
						int step1 = 0;
						boolean talk1 = false;
						updateCustomer(phone, step1, "normal", talk1);
					}

					if (respYesOrNo.equals("REMARCAR")) {
						int step2 = 2;
						boolean talk2 = false;
						updateCustomer(phone, step2, "confirmation", talk2);
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

	public void saveInitButtonClick(String obj, String msgBody, String respYesOrNo) {

		try {

			WebhookReceivedCallbackQuickReplyInitButtonClick buttonClicked = new WebhookReceivedCallbackQuickReplyInitButtonClick();

			buttonClicked = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyInitButtonClick.class);

			// extract phone number from the WebHook payload
			String phone = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getFrom();
			
			// Save customer response from button Init
			saveButtonMessageFromCustomer(buttonClicked, phone);

			if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
				String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
						.getContext().getId();

				updateConfirmationResponse(id, respYesOrNo);

				if (respYesOrNo.equals("SIM")) {
					int step1 = 0;
					boolean talk1 = false;
					updateCustomer(phone, step1, "normal", talk1);
				}

				if (respYesOrNo.equals("REMARCAR")) {
					int step2 = 2;
					boolean talk2 = false;
					updateCustomer(phone, step2, "confirmation", talk2);
				}

			} else {
				System.out.println("Message Not saved");
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
			String phone = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
					.getFrom();

			saveButtonMessageFromCustomer(buttonClicked, phone);

			if (respYesOrNo.equals("SIM") || respYesOrNo.equals("REMARCAR")) {
				String id = buttonClicked.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
						.getContext().getId();

				if (respYesOrNo.equals("REMARCAR")) {
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

			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			Customer customer = customerRepository.findByPhoneNumber(phone);
			customer.setTalk(true);
			customer.setTimelimit(timestamp);

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

	public void updateCustomerWantToTalkDayOff(String phone) {

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
