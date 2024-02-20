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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.MessageInitDTO;
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

	@GetMapping("/messages/")
	@ResponseBody
	public ResponseEntity<Boolean> loadMessages(@RequestParam String id) {
		logger.info("Loading GET /messages");

		try {
			boolean resp = messageResource.loadMessages(id);
			Thread.sleep(100);

			if (resp) {
				return new ResponseEntity<>(true, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/send-chat")
	public ResponseEntity<String> sendMessageChat(@RequestBody String msg) {
		logger.info("Sending POST /send-chat");
		int ownerOfMessage = 0;// O (zero) is from us | 1 (one) is from customers

		try {
			messageService.sendMessage(msg, ownerOfMessage);
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}

	@PostMapping("/send")
	public ResponseEntity<Object> sendMessage(@RequestBody MessageInitDTO obj) throws Exception {

		logger.info("Sending POST /send");

		boolean response = false;

		boolean checkPhoneNumber = false;
		checkPhoneNumber = messageUtil.checkPhoneNumber(obj);

		if (checkPhoneNumber) {
			String msg = messageUtil.messageOrganizer(obj);
			response = messageService.sendMessageInitial(obj, msg); // Call method

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/sendAll")
	public List<ResponseMessageSent> sendAllMessages(@RequestBody List<MessageInitDTO> obj)
			throws InterruptedException {
		logger.info("Sending POST /sendAll");

		List<ResponseMessageSent> list = new ArrayList<>();

		for (int i = 0; i < obj.size(); i++) {

			ResponseMessageSent resp = new ResponseMessageSent();
			boolean response = false;

			boolean checkPhoneNumber = false;
			checkPhoneNumber = messageUtil.checkPhoneNumber(obj.get(i));

			if (checkPhoneNumber) {
				String msg = messageUtil.messageOrganizer(obj.get(i));
				response = messageService.sendMessageInitial(obj.get(i), msg);

				if (response) {
					Thread.sleep(200);
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
		return list;
	}

}
