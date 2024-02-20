package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.CustomerMessage;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavLocationMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.model.MessageModel;
import com.imav.whatsapp.model.MessageWithURL;
import com.imav.whatsapp.repository.CustomerMessageRepository;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.ImavButtonMessageRepository;
import com.imav.whatsapp.repository.ImavLocationMessageRepository;
import com.imav.whatsapp.repository.ImavMessageRepository;
import com.imav.whatsapp.service.SendWebSocketService;
import com.imav.whatsapp.webhookModel.WebhookLocation;
import com.imav.whatsapp.webhookModel.WebhookReceivedCallbackQuickReplyButtonClick;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class DBMessageResource {

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
	private SendWebSocketService websocket;

	public boolean loadMessages(String phoneFrom) {

		try {
			List<CustomerMessage> customerMessages = new ArrayList<>();
			customerMessages = messageRepo.findAllByPhone_from(phoneFrom);

			List<ImavMessage> imavMessages = new ArrayList<>();
			imavMessages = imavMessageRepo.findAllByPhone_to(phoneFrom);

			List<ImavButtonMessage> imavButtonMessages = new ArrayList<>();
			imavButtonMessages = imavButtonMessageRepository.findAllByPhone_to(phoneFrom);

			List<ImavLocationMessage> imavLocationMessages = new ArrayList<>();
			imavLocationMessages = locationRepository.findAllByPhone_to(phoneFrom);

			websocket.convertAllMessageToDto(customerMessages, imavMessages, imavButtonMessages, imavLocationMessages);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - loadMessages...");
			return false;
		}
		return true;

	}

	public ImavMessage saveImavMessageIntoDatabase(MessageModel message, String name, String id) {

		ImavMessage messagedb = new ImavMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String phoneNumber = message.getTo();

		boolean exist = customerRepository.existsById(message.getTo());

		if (!exist) {
			Customer customer = new Customer();
			customer.setC_name(name);
			customer.setC_phone_number(message.getTo());
			customer.setC_timestamp(timestamp);
			saveCustomerIntoDB(customer);
		}

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

	public void saveMessageIntoDatabase(WebhookReceivedTextMessage messageReceived, Customer customer) {
		CustomerMessage message = new CustomerMessage();
		String timestamp = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
				.getTimestamp();
		String phone = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

		message.setCustomer(customer);
		message.setM_name(messageReceived.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0)
				.getProfile().getName());
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

	public WebhookReceivedCallbackQuickReplyButtonClick saveInteractiveReplyIntoDatabase(
			WebhookReceivedCallbackQuickReplyButtonClick obj, Customer customer) {

		CustomerMessage message = new CustomerMessage();
		String timestamp = obj.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getTimestamp();
		String phone = obj.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

		message.setCustomer(customer);
		message.setM_name(
				obj.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName());
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

	public WebhookReceivedCallbackQuickReplyButtonClick saveInteractiveReplyIntoDatabase(String obj,
			Customer customer) {
		WebhookReceivedCallbackQuickReplyButtonClick interactive = new WebhookReceivedCallbackQuickReplyButtonClick();
		interactive = GSON.fromJson(obj, WebhookReceivedCallbackQuickReplyButtonClick.class);
		CustomerMessage message = new CustomerMessage();
		String phone = interactive.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();
		String timestamp = interactive.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0)
				.getTimestamp();

		message.setCustomer(customer);
		message.setM_name(interactive.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile()
				.getName());
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
		sendCustomersToWebsocket();
	}

	public void sendCustomersToWebsocket() {
		List<Customer> customers = findAll();
		websocket.convertCustomerToDto(customers);
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
			customer = customerRepository.findAllByPhoneNumber(phoneNumber);
			customer.setC_timestamp(timestamp);
			customerRepository.save(customer);
			System.out.println(phoneNumber + ": Updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}