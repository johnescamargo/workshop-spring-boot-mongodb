package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.repository.CustomerRepository;
import com.imav.whatsapp.util.CustomerUtil;
import com.imav.whatsapp.util.WebhookUtil;
import com.imav.whatsapp.util.WeekUtil;

@Service
public class WebhookService {

	@Autowired
	private StatusesService statusesService;

	@Autowired
	private WebhookCustomerNormalService customerNormalService;

	@Autowired
	private WebhookCustomerNormalDayOffService customerNormalDayOffService;

	@Autowired
	private WebhookCustomerConfirmationService confirmationService;

	@Autowired
	private WebhookCustomerTalkingService talkingService;

	@Autowired
	private WebhookUtil webhookUtil;

	@Autowired
	private WeekUtil weekUtil;

	@Autowired
	private CustomerUtil customerUtil;

	@Autowired
	private CustomerRepository customerRepository;

	public void checkWebHookType(String type, String obj) {

		// Check if customer exists on DB
		String phone = webhookUtil.getPhoneNumber(obj);
		String name = webhookUtil.getName(obj);
		existsCustomer(phone, name);

		// Update Status regardless of filter
		if (type.equals("status")) {
			statusesService.checkTypeOfStatus(obj);
		} else {
			isDayOff(type, obj, phone, name);
		}
	}

	public void isDayOff(String type, String obj, String phone, String name) {

		// if true, talk
		boolean response = weekUtil.checkIfDayIsOff();

		if (response) {
			customerMode(type, obj, phone, name);
		} else {
			customerModeDayOff(type, obj, phone, name);
		}

	}

	public void customerMode(String type, String obj, String phone, String name) {

		String mode = "";

		try {

			mode = getCustomerMode(phone);

			// Timelimit. if timelimit < 1 hour turn off any response from BOT
			// true means value is less than 1h minutes, so Turn off bot
			boolean limit = customerUtil.checkTimelimit(phone);

			if (!limit) {
				switch (mode) {

				case "normal":
					customerNormalService.setWebhookClass(type, obj, phone, name);

					break;

				case "confirmation":
					confirmationService.setWebhookConfirmation(type, obj, phone, name);
					break;

				case "appointment":
					// Create medical appointment
					break;

				default:

					break;

				}

			} else {
				talkingService.setWebhookTalking(type, obj, phone, name);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void customerModeDayOff(String type, String obj, String phone, String name) {

		String mode = "";

		try {

			mode = getCustomerMode(phone);

			switch (mode) {

			case "normal":
				customerNormalDayOffService.setWebhookNormalDayOff(type, obj, phone, name);

				break;

			case "confirmation":
				customerNormalDayOffService.setWebhookConfirmationDayOff(type, obj, phone, name);
				
				break;

			case "appointment":
				// Create medical appointment
				break;

			default:

				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void existsCustomer(String phone, String name) {

		try {

			boolean resp = customerRepository.existsByPhoneNumber(phone);

			if (!resp) {
				Customer customer = new Customer();
				String timestamp = Long.toString(System.currentTimeMillis() / 1000);

				customer.setMode("normal");
				customer.setName(name);
				customer.setPhoneNumber(phone);
				customer.setTimestamp(timestamp);
				customer.setTimelimit("0");
				customer.setStep(0);
				customer.setTalk(false);

				customerRepository.save(customer);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getCustomerMode(String phone) {

		String mode = "";

		try {

			Customer cust = customerRepository.findByPhoneNumber(phone);
			mode = cust.getMode();

		} catch (Exception e) {
			e.printStackTrace();
			mode = "normal";
		}
		return mode;
	}

}
