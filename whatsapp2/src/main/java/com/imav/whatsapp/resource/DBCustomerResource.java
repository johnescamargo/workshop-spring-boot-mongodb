package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.entity.WantsToTalk;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.WantsToTalkRepository;
import com.imav.whatsapp.service.MessageButtonReplyService;
import com.imav.whatsapp.service.SendWebSocketService;
import com.imav.whatsapp.util.WeekUtil;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class DBCustomerResource {

	private Logger logger = LogManager.getLogger(DBCustomerResource.class);

	private Gson gson = new Gson();

	@Autowired
	private SendWebSocketService websocket;

	@Autowired
	private DBMessageResource messageResource;

	@Autowired
	private MessageButtonReplyService messageButtonReplyService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private WantsToTalkRepository talkRepository;

	@Autowired
	private WeekUtil weekUtil;

	public boolean checkNewCustomer(String json) {

		WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
		messageReceived = gson.fromJson(json, WebhookReceivedTextMessage.class);
		boolean respZero = false;

		try {

			String from = "";
			String name = "";
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();
			// JSONArray arr2 = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("contacts");

			// arr2 = obj.getJSONArray("messages");

			from = arr.getJSONObject(0).getString("wa_id");
			obj = arr.getJSONObject(0).getJSONObject("profile");
			name = obj.getString("name");

			// timestamp = arr2.getJSONObject(0).getString("timestamp");

			boolean exist = customerRepository.existsById(from);

			if (!exist) {
				Customer newCustomer = setNewCustomer(from, name, timestamp);
				saveCustomer(newCustomer);
				messageResource.saveMessageIntoDatabase(messageReceived, newCustomer);
				websocket.showCustomerToWebSocket();
			} else {

				Customer customer = customerRepository.findByPhoneNumber(from);
				respZero = messageResource.saveMessageIntoDatabase(messageReceived, customer);
				boolean response = weekUtil.checkIfDayIsOff();

				if (respZero && response) {
					customer.setC_timelimit(timestamp);
					customer.setTalk(true);
					saveCustomer(customer);

					messageButtonReplyService.messageZeroResponse(from, name);
					WantsToTalk talk = new WantsToTalk(name, from);
					talkRepository.save(talk);
					websocket.updateWantsToTalk();

				} else {
					// saveCustomer(customer);
				}
				websocket.convertMessageFromOutsideSend(messageReceived, 1);
				websocket.showCustomerToWebSocket();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
		return respZero;
	}

	public List<Customer> findAll() {
		try {
			return customerRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("findAll error *****");
			logger.info(e);
		}
		return customerRepository.findAll();

	}

	public void saveCustomer(Customer customer) {
		try {
			customerRepository.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("saveCustomer error *****");
			logger.info(e);
		}
	}

	public Customer setNewCustomer(String weId, String name, String timestamp) {
		Customer customer = new Customer();
		customer.setC_phone_number(weId);
		customer.setC_name(name);
		customer.setC_timestamp(timestamp);
		return customer;
	}

	public List<CustomerDto> loadCustomers() {
		long resp = customerRepository.count();
		List<CustomerDto> dtos = new ArrayList<>();

		if (resp >= 1) {
			List<Customer> response = customerRepository.findAllLimit150();
			dtos = convertCustomerToDto(response);
		} else {
			return null;
		}
		return dtos;
	}

	public List<CustomerDto> convertCustomerToDto(List<Customer> customers) {
		List<CustomerDto> dtos = new ArrayList<>();

		for (int i = 0; i < customers.size(); i++) {
			CustomerDto dto = new CustomerDto();
			dto.setName(customers.get(i).getC_name());
			dto.setPhone_number(customers.get(i).getC_phone_number());
			dto.setTimestamp(Integer.valueOf(customers.get(i).getC_timestamp()));
			dto.setTalk(customers.get(i).isTalk());
			dtos.add(dto);
		}

		Collections.sort(dtos, Comparator.comparing(CustomerDto::getTimestamp));
		Collections.reverse(dtos);

		return dtos;
	}

	public CustomerDto getCustomer(String phone) {
		CustomerDto dto = new CustomerDto();
		try {
			Optional<Customer> customer = customerRepository.findById(phone);
			dto.setName(customer.get().getC_name());
			dto.setPhone_number(customer.get().getC_phone_number());
			dto.setTimestamp(Integer.valueOf(customer.get().getC_timestamp()));
			dto.setTalk(customer.get().isTalk());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public List<CustomerDto> liveSearchCustomers(String input) {

		List<CustomerDto> customersDto = new ArrayList<>();
		List<Customer> customers = new ArrayList<>();

		try {
			customers = customerRepository.liveSearch(input);
			for (int i = 0; i < customers.size(); i++) {
				CustomerDto dto = new CustomerDto();
				dto.setName(customers.get(i).getC_name());
				dto.setPhone_number(customers.get(i).getC_phone_number());
				dto.setTimestamp(Integer.valueOf(customers.get(i).getC_timestamp()));
				dto.setTalk(customers.get(i).isTalk());
				customersDto.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customersDto;
	}

}