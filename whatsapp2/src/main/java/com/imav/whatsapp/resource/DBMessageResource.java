package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.GenericMessageDto;
import com.imav.whatsapp.dto.GenericMessageDto.Button;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.CustomerMessage;
import com.imav.whatsapp.entity.ImageDb;
import com.imav.whatsapp.entity.ImavButton;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavLocationMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.repository.CustomerMessageRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.ImageDbRepository;
import com.imav.whatsapp.repository.ImavButtonMessageRepository;
import com.imav.whatsapp.repository.ImavButtonRepository;
import com.imav.whatsapp.repository.ImavLocationMessageRepository;
import com.imav.whatsapp.repository.ImavMessageRepository;
import com.imav.whatsapp.webhookModel.WebhookLocation;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyInitButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class DBMessageResource {

	private Logger logger = LogManager.getLogger(DBMessageResource.class);

	private static Gson GSON = new Gson();

	@Autowired
	private CustomerMessageRepository messageRepo;

	@Autowired
	private ImavMessageRepository imavMessageRepo;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ImavLocationMessageRepository locationRepository;

	@Autowired
	private ImavButtonMessageRepository imavButtonMessageRepository;

	@Autowired
	private ImavButtonRepository buttonRepo;

	@Autowired
	private ImageDbRepository imageRepository;

	public List<GenericMessageDto> loadMessages(String phone) {
		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

			dtos.addAll(convertCustomerMessage(messageRepo.findAllByPhone_from(phone)));

			dtos.addAll(convertImavMessage(imavMessageRepo.findAllByPhone_to(phone)));

			dtos.addAll(convertImavButtonMessage(imavButtonMessageRepository.findAllByPhone_to(phone)));

			dtos.addAll(convertImavLocationMessage(locationRepository.findAllByPhone_to(phone)));

			dtos.addAll(convertImageDb(imageRepository.findAllByPhone(phone)));

			Collections.sort(dtos, Comparator.comparing(GenericMessageDto::getTimestamp));

		} catch (Exception e) {
			logger.info("Error - loadMessages...");
			e.printStackTrace();
			return null;
		}
		return dtos;
	}

	public ImavMessage saveImavMessageIntoDatabase(MessageModel message, String id) {

		ImavMessage messagedb = new ImavMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String phoneNumber = message.getTo();

		messagedb.setStatus(1);
		messagedb.setText(message.getText().getBody());
		messagedb.setTimestamp(timestamp);
		messagedb.setType("text");
		messagedb.setTo(message.getTo());
		messagedb.setIdWamid(id);

		imavMessageRepo.save(messagedb);

		updateCustomersTimestamp(phoneNumber, timestamp);


		return messagedb;
	}
	
	public ImavMessage saveImavFrontEndMessageIntoDatabase(MessageModel message, String id) {

		ImavMessage messagedb = new ImavMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String phoneNumber = message.getTo();

		messagedb.setStatus(1);
		messagedb.setText(message.getText().getBody());
		messagedb.setTimestamp(timestamp);
		messagedb.setType("text");
		messagedb.setTo(message.getTo());
		messagedb.setIdWamid(id);

		imavMessageRepo.save(messagedb);
		updateCustomersTimestampAndTimelimit(phoneNumber, timestamp);

		return messagedb;
	}

	public void saveImavMessageURlIntoDatabase(MessageWithURL url, String idWamid) {
		ImavMessage messagedb = new ImavMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String phone = url.getTo();

		messagedb.setStatus(1);
		messagedb.setText(url.getText().getBody());
		messagedb.setTimestamp(timestamp);
		messagedb.setType("text");
		messagedb.setTo(phone);
		messagedb.setIdWamid(idWamid);

		imavMessageRepo.save(messagedb);
		updateCustomersTimestamp(phone, timestamp);

	}

	public void saveLocationIntoDatabase(WebhookLocation webhookLocation, String phone, String idWamid) {
		ImavLocationMessage location = new ImavLocationMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		location.setL_address(webhookLocation.getLocation().getAddress());
		location.setL_latitude(webhookLocation.getLocation().getLatitude());
		location.setL_longitude(webhookLocation.getLocation().getLongitude());
		location.setL_name(webhookLocation.getLocation().getName());
		location.setL_status(1);
		location.setL_to(phone);
		location.setL_type(webhookLocation.getType());
		location.setTimestamp(timestamp);
		location.setIdWamid(idWamid);

		saveImavLocationMessage(location);
		updateCustomersTimestamp(phone, timestamp);
	}

	public void saveMessageIntoDatabase(WebhookReceivedTextMessage messageReceived, Customer customer) {
		CustomerMessage message = new CustomerMessage();
		// String timestamp =
		// messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		String phone = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

		message.setCustomer(customer);
		message.setM_name(customer.getName());
		message.setM_phone_from(phone);
		message.setM_status(1);// **** TODO check how to set ENUMS
		message.setM_text(messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
				.getText().getBody());
		message.setM_timestamp(timestamp);
		message.setM_type(
				messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		message.setIdWamid(
				messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		saveMessage(message);
		logger.info("Saving messagge: text....");
		updateCustomersTimestamp(phone, "0");

	}
	
	public void saveMessageModelIntoDatabase(MessageModel messageModel, Customer customer, String idWamid) {
		CustomerMessage message = new CustomerMessage();
		// String timestamp =
		// messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		String phone = messageModel.getTo();

		message.setCustomer(customer);
		message.setM_name(customer.getName());
		message.setM_phone_from(phone);
		message.setM_status(1);// **** TODO check how to set ENUMS
		message.setM_text(messageModel.getText().getBody());
		message.setM_timestamp(timestamp);
		message.setM_type("text");
		message.setIdWamid(idWamid);

		saveMessage(message);
		logger.info("Saving messagge: text....");
		updateCustomersTimestamp(phone, timestamp);

	}

	public WebhookReceivedCallbackQuickReplyButtonClick saveInteractiveReplyIntoDatabase(
			WebhookReceivedCallbackQuickReplyButtonClick obj, Customer customer) {

		CustomerMessage message = new CustomerMessage();
		// String timestamp =
		// obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		String phone = obj.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

		message.setCustomer(customer);
		message.setM_name(customer.getName());
		message.setM_phone_from(phone);
		message.setM_status(1);// **** TODO check how to set ENUMS
		message.setM_text(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getInteractive()
				.getButton_reply().getTitle());
		message.setM_timestamp(timestamp);
		message.setM_type(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		message.setIdWamid(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		saveMessage(message);
		updateCustomersTimestamp(phone, timestamp);

		return obj;
	}
	
	public WebhookReceivedCallbackQuickReplyInitButtonClick saveButtonReplyIntoDatabase(
			WebhookReceivedCallbackQuickReplyInitButtonClick obj, Customer customer) {

		CustomerMessage message = new CustomerMessage();
		// String timestamp =
		// obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		String phone = obj.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

		message.setCustomer(customer);
		message.setM_name(customer.getName());
		message.setM_phone_from(phone);
		message.setM_status(1);// **** TODO check how to set ENUMS
		message.setM_text(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getButton().getText());
		message.setM_timestamp(timestamp);
		message.setM_type(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		message.setIdWamid(obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		saveMessage(message);
		updateCustomersTimestamp(phone, timestamp);

		return obj;
	}

	public WebhookReceivedCallbackQuickReplyButtonClick saveInteractiveReplyIntoDatabase(String obj,
			Customer customer) {
		WebhookReceivedCallbackQuickReplyButtonClick interactive = new WebhookReceivedCallbackQuickReplyButtonClick();
		interactive = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);
		CustomerMessage message = new CustomerMessage();
		String phone = interactive.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();
		// String timestamp =
		// interactive.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		message.setCustomer(customer);
		message.setM_name(customer.getName());
		message.setM_phone_from(phone);
		message.setM_status(1);// **** TODO check how to set ENUMS
		message.setM_text(interactive.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
				.getInteractive().getButton_reply().getTitle());
		message.setM_timestamp(timestamp);
		message.setM_type(interactive.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getType());
		message.setIdWamid(interactive.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getId());

		saveMessage(message);
		updateCustomersTimestamp(phone, timestamp);

		return interactive;
	}

	public List<GenericMessageDto> convertCustomerMessage(List<CustomerMessage> customerMessages) {

		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

			if (!customerMessages.isEmpty()) {
				for (int i = 0; i < customerMessages.size(); i++) {
					GenericMessageDto dto = new GenericMessageDto();

					dto.setOwnerOfMessage(1);//1 is customer | 0 is IMAV
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}

	public List<GenericMessageDto> convertImavMessage(List<ImavMessage> imavMessages) {

		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}

	public List<GenericMessageDto> convertImageDb(List<ImageDb> images) {

		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

			if (!images.isEmpty()) {
				for (int i = 0; i < images.size(); i++) {
					GenericMessageDto dto = new GenericMessageDto();

					dto.setOwnerOfMessage(1);
					dto.setId(images.get(i).getId());
					dto.setName(images.get(i).getPhone());
					dto.setPhone_from(images.get(i).getPhone());
					dto.setStatus(0);
					dto.setText(images.get(i).getCaption());
					dto.setTimestamp(images.get(i).getTimestamp());
					dto.setType("image");
					dto.setIdWamid(images.get(i).getIdWamid());
					dto.setContent(images.get(i).getContent());
					dto.setMimeType(images.get(i).getMimeType());

					dtos.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}

	public List<GenericMessageDto> convertImavButtonMessage(List<ImavButtonMessage> imavButtonMessages) {

		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}

	public List<GenericMessageDto> convertImavLocationMessage(List<ImavLocationMessage> imavLocationMessages) {
		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}

	public void saveMessage(CustomerMessage message) {
		try {
			messageRepo.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveImavLocationMessage(ImavLocationMessage message) {
		try {
			locationRepository.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveCustomerIntoDB(Customer customer) {
		saveCustomer(customer);

	}

	public void saveCustomer(Customer customer) {
		try {
			customerRepository.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Customer> findAll() {
		try {
			return customerRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerRepository.findAll();
	}

	public Customer getCustomerObject(String phone) {
		Customer customer = new Customer();

		try {
			Optional<Customer> optional = customerRepository.findById(phone);
			customer = optional.get();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	public void updateCustomersTimestamp(String phoneNumber, String timestamp) {
		Customer customer = new Customer();

		try {
			customer = customerRepository.findByPhoneNumber(phoneNumber);
			customer.setTimestamp(timestamp);
			customerRepository.save(customer);
			logger.info(phoneNumber + ": Updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateCustomersTalk(String phoneNumber, String timestamp) {
		Customer customer = new Customer();

		try {
			customer = customerRepository.findByPhoneNumber(phoneNumber);
			customer.setTimestamp(timestamp);
			customer.setTalk(true);
			customerRepository.save(customer);
			logger.info(phoneNumber + ": Updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateCustomersTimestampAndTimelimit(String phoneNumber, String timestamp) {
		Customer customer = new Customer();

		try {
			customer = customerRepository.findByPhoneNumber(phoneNumber);
			customer.setTimestamp(timestamp);
			customer.setTimelimit(timestamp);
			customerRepository.save(customer);
			logger.info(phoneNumber + " phone and timelimit: Updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}