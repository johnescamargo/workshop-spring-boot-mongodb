package com.imav.whatsapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.GenericMessageDto;
import com.imav.whatsapp.dto.MessageEnviaDto;
import com.imav.whatsapp.dto.MessageInitDto;
import com.imav.whatsapp.model.ResponseMessageSent;
import com.imav.whatsapp.resource.DBMessageResource;
import com.imav.whatsapp.service.MessageService;
import com.imav.whatsapp.util.MessageUtil;

@RestController
@RequestMapping("/message")
public class MessageRestController {

	private Logger logger = LogManager.getLogger(MessageRestController.class);

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private MessageService messageService;

	@Autowired
	private DBMessageResource messageResource;

	@GetMapping("/messages")
	public ResponseEntity<List<GenericMessageDto>> loadMessages(@RequestParam String id) {
		logger.info("Loading GET /messages");

		List<GenericMessageDto> dtos = new ArrayList<>();

		try {

			dtos = messageResource.loadMessages(id);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@PostMapping("/send-chat")
	public ResponseEntity<String> sendMessageChat(@RequestBody String msg) {
		logger.info("Sending POST /send-chat");
		logger.info(msg);

		// O (zero) is from us | 1 (one) is from customers
		int ownerOfMessage = 0;

		try {
			messageService.sendMessage(msg, ownerOfMessage);
			// Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}

	@PostMapping("/envia")
	public ResponseEntity<Object> sendEnvia(@RequestBody MessageEnviaDto obj) throws Exception {

		logger.info("Sending POST /envia");
		logger.info(obj);
		boolean response = true;

		try {
			
			MessageEnviaDto dto = messageUtil.messageOrganizerEnvia(obj);
			response = messageService.sendMessageEnvia(dto); 
			
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/send")
	public ResponseEntity<Object> sendInitMessage(@RequestBody MessageInitDto obj) {

		boolean response = true;

		// Checking if phone number contains number 55 and 9 in the beginning.
		// This is for mobile phones and national calls
		response = messageUtil.checkPhoneNumber(obj);

		if (response) {
			//String msg = messageUtil.messageOrganizer(obj);
			response = messageService.sendInitMessageImav(obj); // Call method

		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/sendAll")
	public List<ResponseMessageSent> sendAllInitMessages(@RequestBody List<MessageInitDto> obj) {

		List<ResponseMessageSent> list = new ArrayList<>();

		try {

			for (int i = 0; i < obj.size(); i++) {

				ResponseMessageSent resp = new ResponseMessageSent();
				boolean response = false;

				boolean checkPhoneNumber = true;
				checkPhoneNumber = messageUtil.checkPhoneNumber(obj.get(i));

				if (checkPhoneNumber) {
					// String msg = messageUtil.messageOrganizer(obj.get(i));
					response = messageService.sendInitMessageImav(obj.get(i));

					if (response) {
						Thread.sleep(300);
						resp.setId(obj.get(i).getId());
						resp.setSent(true);
						list.add(resp);
					} else {
						resp.setId(obj.get(i).getId());
						resp.setSent(false);
						list.add(resp);
					}
				} else {
					resp.setId(obj.get(i).getId());
					resp.setSent(false);
					list.add(resp);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return list;
	}

}
