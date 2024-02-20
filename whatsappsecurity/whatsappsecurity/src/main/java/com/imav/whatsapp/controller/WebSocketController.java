package com.imav.whatsapp.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.dto.GenericMessageDto;

@Controller
public class WebSocketController {

	@MessageMapping("/process-message")
	@SendTo("/topic/message")
	public GenericMessageDto processOurMessage(GenericMessageDto message) throws Exception {
		return message;
	}

	@MessageMapping("/outside-message")
	@SendTo("/topic/message")
	public GenericMessageDto processOutsideMessage(GenericMessageDto message) throws Exception {
		return message;
	}

	@MessageMapping("/customers")
	@SendTo("/topic/customers")
	public List<CustomerDto> processCustomers(List<CustomerDto> objs) throws Exception {
		return objs;
	}

	@MessageMapping("/process-messages")
	@SendTo("/topic/messages")
	public List<GenericMessageDto> processOurMessages(List<GenericMessageDto> messages) throws Exception {
		return messages;
	}

}
