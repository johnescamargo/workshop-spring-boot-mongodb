package com.imav.whatsapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.resource.DBCustomerResource;

@RestController
@RequestMapping(value = "/customer")
public class CustomerRestController {

	private Logger logger = LogManager.getLogger(CustomerRestController.class);

	@Autowired
	private DBCustomerResource customerResource;

	@GetMapping(value = "/customer/")
	@ResponseBody
	public ResponseEntity<CustomerDto> getCustomer(@RequestParam String id) {
		logger.info("Get /customer...");
		CustomerDto dto = new CustomerDto();

		try {
			dto = customerResource.getCustomer(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping(value = "/customers")
	public ResponseEntity<String> loadCustomers() {

		try {
			customerResource.loadCustomers();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Not found...", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("OK...", HttpStatus.OK);
	}

	@GetMapping(value = "/livesearch/")
	public ResponseEntity<List<CustomerDto>> liveSearch(@RequestParam String input) {
		logger.info("Get /Livesearch...");
		System.out.println(input);
		List<CustomerDto> dtos = new ArrayList<>();

		try {
			dtos = customerResource.liveSearchCustomers(input);
			if (dtos.isEmpty()) {
				return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

}
