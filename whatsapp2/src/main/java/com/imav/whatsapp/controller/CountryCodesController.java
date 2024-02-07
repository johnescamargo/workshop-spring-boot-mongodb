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
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.entity.CountryCode;
import com.imav.whatsapp.repository.CountryCodeRepository;

@RestController
@RequestMapping("/countries")
public class CountryCodesController {

	@Autowired
	private CountryCodeRepository countryCodeRespository;

	private Logger logger = LogManager.getLogger(CountryCodesController.class);

	@GetMapping("/getcountries")
	public ResponseEntity<List<CountryCode>> getAll() {
		logger.info("Get /list of countries...");
		List<CountryCode> objs = new ArrayList<>();

		try {
			objs = countryCodeRespository.findAll();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(objs, HttpStatus.OK);
	}

}
