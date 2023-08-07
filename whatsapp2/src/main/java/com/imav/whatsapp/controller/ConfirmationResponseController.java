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
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;

@RestController
@RequestMapping("/confirmation")
public class ConfirmationResponseController {

	@Autowired
	private ConfirmationResponseRepository confirmationRespository;

	private Logger logger = LogManager.getLogger(ConfirmationResponseController.class);

	@GetMapping("/getdate")
	public ResponseEntity<List<ConfirmationResponse>> getByShippingDate(@RequestParam String shippingDate) {
		logger.info("Get /list of date...");
		List<ConfirmationResponse> objs = new ArrayList<>();

		try {
			objs = confirmationRespository.findByShippingDate(shippingDate);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(objs, HttpStatus.OK);
	}

	@GetMapping("/livesearch")
	public ResponseEntity<List<ConfirmationResponse>> getByPhone(@RequestParam String input) {
		logger.info("Get /livesearch get dates...");

		List<ConfirmationResponse> objs = new ArrayList<>();

		try {
			objs = confirmationRespository.findByPhoneNumberOrName(input);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(objs, HttpStatus.OK);
	}

}
