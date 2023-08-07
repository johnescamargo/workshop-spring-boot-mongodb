package com.imav.whatsapp.controller;

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

import com.imav.whatsapp.service.WebhookService;
import com.imav.whatsapp.service.WhatsappTokens;
import com.imav.whatsapp.util.WebhookUtil;

@RestController
@RequestMapping("/api")
public class WebhookRestController {

	private Logger logger = LogManager.getLogger(WebhookRestController.class);

	@Autowired
	private WebhookService webhookService;

	@Autowired
	private WebhookUtil webhookUtil;

	@Autowired
	private WhatsappTokens tokens;

	@GetMapping("/webhook")
	public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
			@RequestParam("hub.challenge") String challenge, @RequestParam("hub.verify_token") String token) {

		logger.info("Receiving GET webhook /");

		if (mode.equals("subscribe") && token.equals(tokens.getVerificationToken())) {
			return new ResponseEntity<>(challenge, HttpStatus.OK);
		} else {
			logger.info("Verification token or mode mismatch");
			return new ResponseEntity<>("Verification token or mode mismatch", HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/webhook")
	public ResponseEntity<String> postWebhook(@RequestBody String obj) {

		logger.info("Receiving POST webhook /");
		logger.info(obj);
		//System.out.println(obj);

		String resp = "";

		try {

			resp = webhookUtil.findTypeOfWebhookMessage(obj);

			if (resp.equals("not found")) {
				return new ResponseEntity<>("Type not found", HttpStatus.NOT_FOUND);
			}

			webhookService.setWebhookClass(resp, obj);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("postWebhook");
			logger.info(obj);
			return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}