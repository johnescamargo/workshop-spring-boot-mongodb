package com.imav.whatsapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.Customer;
import com.imav.whatsapp.repository.CustomerRepository;

@Service
public class CustomerUtil {

	@Autowired
	private CustomerRepository customerRepository;

	public boolean checkTimelimit(String phone) {
		Customer customer = new Customer();

		try {

			// Check if customer exists
			boolean exist = customerRepository.existsById(phone);
			if (exist) {
				// get customer
				customer = customerRepository.findByPhoneNumber(phone);

				// check if limit is less than 7200 2 hours calc
				long timelimit = Long.parseLong(customer.getTimelimit());
				long timestamp = System.currentTimeMillis() / 1000;

				long result = timestamp - timelimit;

				// if so return true and save message into DB, websocket etc
				// 3600 seg = 1h
				// 10800 = 3h
				// 600 = 10min
				if (result <= 10800) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("checkTimelimit error");
			return false;
		}

		return false;
	}

	public void changeToNormal(String phone) {
		Customer customer = new Customer();

		try {

			customer = customerRepository.findByPhoneNumber(phone);

			long timestampCustomer = Long.parseLong(customer.getTimestamp());
			long timestamp = System.currentTimeMillis() / 1000;

			long result = timestamp - timestampCustomer;

			// 3600 seg = 1h
			// 86400 = 24h
			if (result > 86400) {
				customer.setStep(0);
				customer.setMode("normal");
				customer.setTalk(false);

				customerRepository.save(customer);
			}

		} catch (Exception e) {
			e.printStackTrace();
			customer.setStep(0);
			customer.setMode("normal");
			customer.setTalk(false);
			customerRepository.save(customer);
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
	
	public void updateOnlyCustomerWantToTalk(String phone) {

		try {

			Customer customer = customerRepository.findByPhoneNumber(phone);
			customer.setTalk(true);

			customerRepository.save(customer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
