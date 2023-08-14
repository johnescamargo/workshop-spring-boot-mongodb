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
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.repository.WantsToTalkRepository;
import com.imav.whatsapp.service.SendWebSocketService;
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
	private CustomerRepository customerRepository;
	
	@Autowired
	private WantsToTalkRepository wantsToTalkRepository;;
	
	

	public void saveCustomerMessage(String json) {

		WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
		messageReceived = gson.fromJson(json, WebhookReceivedTextMessage.class);

		try {

			String phone = "";

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("contacts");

			phone = arr.getJSONObject(0).getString("wa_id");
			obj = arr.getJSONObject(0).getJSONObject("profile");

			Customer customer = customerRepository.findByPhoneNumber(phone);
			messageResource.saveMessageIntoDatabase(messageReceived, customer);

			websocket.convertMessageFromCustomer(messageReceived, 1);
			websocket.showCustomerToWebSocket();

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}

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
			logger.info("save customer error *****");
			logger.info(e);
		}
	}
	
	public boolean updateCustomerTalk(String phone) {
		
		boolean resp = false;
		
		try {
			
			Customer customer =	customerRepository.findByPhoneNumber(phone);
			customer.setTalk(false);
			customerRepository.save(customer);
			
			wantsToTalkRepository.deleteByPhone(phone);
			
			websocket.updateWantsToTalk();
			websocket.updateWebsocket();
			
			resp = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Update Customer error *****");
			logger.info(e);
			return resp;
		}
		return resp;
	}

	public Customer setNewCustomer(String weId, String name, String timestamp) {
		Customer customer = new Customer();
		customer.setPhoneNumber(weId);
		customer.setName(name);
		customer.setTimestamp(timestamp);
		customer.setMode("normal");

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
			dto.setName(customers.get(i).getName());
			dto.setPhone_number(customers.get(i).getPhoneNumber());
			dto.setTimestamp(Integer.valueOf(customers.get(i).getTimestamp()));
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
			dto.setName(customer.get().getName());
			dto.setPhone_number(customer.get().getPhoneNumber());
			dto.setTimestamp(Integer.valueOf(customer.get().getTimestamp()));
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
				dto.setName(customers.get(i).getName());
				dto.setPhone_number(customers.get(i).getPhoneNumber());
				dto.setTimestamp(Integer.valueOf(customers.get(i).getTimestamp()));
				dto.setTalk(customers.get(i).isTalk());
				customersDto.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customersDto;
	}

}