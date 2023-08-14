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
			}

			// check if limit is less than 7200 2 hours calc
			long timelimit = Long.parseLong(customer.getTimelimit());
			long timestamp = System.currentTimeMillis() / 1000;

			long result = timestamp - timelimit;

			// if so return true and save message into DB, websocket etc
			// 3600 seg = 1h
			// 600 = 10min
			if (result <= 3600) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("checkTimelimit error");
			return false;
		}

		return false;
	}

}
