package com.imav.whatsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.EmailDto;
import com.imav.whatsapp.service.EmailService;

@CrossOrigin(origins = "*")
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/send-email")
	public ResponseEntity<String> sendEmail(@RequestBody EmailDto dto) {

		String resp = "error";
		HttpStatus localResp = HttpStatus.ACCEPTED;

		try {

			System.out.println(dto);
			resp = emailService.sendEmail(dto);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}

		if (resp.equals("Mail Sent Successfully...")) {
			localResp = HttpStatus.ACCEPTED;
		} else {
			localResp = HttpStatus.NOT_ACCEPTABLE;
		}

		return new ResponseEntity<>(resp, localResp);
	}

}
