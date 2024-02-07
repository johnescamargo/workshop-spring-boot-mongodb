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

import com.imav.whatsapp.entity.WantsToTalk;
import com.imav.whatsapp.repository.WantsToTalkRepository;

@RestController
@RequestMapping("/talk")
public class WantsToTalkController {

	private Logger logger = LogManager.getLogger(WantsToTalkController.class);

	@Autowired
	private WantsToTalkRepository talkRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<WantsToTalk>> loadUsers() {
		logger.info("Loading GET /users");
		List<WantsToTalk> dtos = new ArrayList<>();

		try {
			dtos = talkRepository.findAll();
			if (dtos != null) {
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
		}
	}
}
