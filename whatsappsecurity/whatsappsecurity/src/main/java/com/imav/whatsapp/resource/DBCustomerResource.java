package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.service.SendWebSocketService;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class DBCustomerResource {

	private Gson gson = new Gson();

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SendWebSocketService websocket;

	@Autowired
	private DBMessageResource messageResource;

	/**
	 * It is only works for text messages If a customer sends any text, new
	 * customers and the text will be save into the database. If a customer is
	 * already saved into the database, only a message will be saved into the
	 * database.
	 * 
	 * @param json
	 */
	public void checkNewCustomer(String json) {

		WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
		messageReceived = gson.fromJson(json, WebhookReceivedTextMessage.class);

		try {
			String weId = "";
			String name = "";
			String timestamp = "";

			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray();
			JSONArray arr2 = new JSONArray();

			arr = obj.getJSONArray("entry");
			obj = arr.getJSONObject(0);
			arr = obj.getJSONArray("changes");
			obj = arr.getJSONObject(0).getJSONObject("value");
			arr = obj.getJSONArray("contacts");

			arr2 = obj.getJSONArray("messages");

			weId = arr.getJSONObject(0).getString("wa_id");
			obj = arr.getJSONObject(0).getJSONObject("profile");
			name = obj.getString("name");

			timestamp = arr2.getJSONObject(0).getString("timestamp");

			Customer customer = setNewCustomer(weId, name, timestamp);

			boolean exist = customerRepository.existsById(weId);

			if (!exist) {
				saveCustomer(customer);
				List<Customer> customers = findAll();

				websocket.convertCustomerToDto(customers);

				messageResource.saveMessageIntoDatabase(messageReceived, customer);
			} else {
				customerRepository.save(customer);// Update Customer's name and timestamp

				messageResource.saveMessageIntoDatabase(messageReceived, customer);

				List<Customer> customers = findAll();

				websocket.convertCustomerToDto(customers);

				websocket.convertMessageFromOutsideSend(messageReceived, 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Customer setNewCustomer(String weId, String name, String timestamp) {
		Customer customer = new Customer();
		customer.setC_phone_number(weId);
		customer.setC_name(name);
		customer.setC_timestamp(timestamp);
		return customer;
	}

	public List<Customer> findAll() {
		try {
			return customerRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerRepository.findAll();

	}

	public void saveCustomer(Customer customer) {
		try {
			customerRepository.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all customers and send them to the chat box
	 */
	public void loadCustomers() {
		long resp = customerRepository.count();
		if (resp >= 1) {
			List<Customer> response = findAll();
			websocket.convertCustomerToDto(response);
		}
	}

	public CustomerDto getCustomer(String phone) {
		CustomerDto dto = new CustomerDto();
		try {
			Optional<Customer> customer = customerRepository.findById(phone);
			dto.setName(customer.get().getC_name());
			dto.setPhone_number(customer.get().getC_phone_number());
			dto.setTimestamp(Integer.valueOf(customer.get().getC_timestamp()));

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
				customersDto.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customersDto;
	}

}