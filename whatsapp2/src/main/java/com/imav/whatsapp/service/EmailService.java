package com.imav.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.dto.EmailDto;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;
	private final static String EMAIL_GMAIL = "credenciamento@imav.com.br";

	public String sendEmail(EmailDto dto) {

		try {

			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(EMAIL_GMAIL);
			mailMessage.setText(
					"Nome: " + dto.getName() + " | " + "E-mail: " + dto.getEmail() + " | Texto: " + dto.getBody());
			mailMessage.setSubject(dto.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		} catch (Exception e) {
			return "Error while Sending Mail";
		}

	}

}
