package com.imav.whatsapp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.imav.whatsapp.dto.CustomerDto;
//import com.imav.whatsapp.dto.CustomerDto;
import com.imav.whatsapp.dto.GenericMessageDto;

@Controller
public class WebSocketController {

	@MessageMapping("/process-message")
	@SendTo("/topic/message")
	public GenericMessageDto processImavMessage(GenericMessageDto message) throws Exception {

		return message;
	}

	@MessageMapping("/customer")
	@SendTo("/topic/customer")
	public CustomerDto processCustomers(CustomerDto obj) throws Exception {

		return obj;
	}

	@MessageMapping("/customers")
	@SendTo("/topic/customers")
	public boolean updateFrontEnd(boolean resp) throws Exception {

		return resp;
	}

	@MessageMapping("/process-messages-customers")
	@SendTo("/topic/messages-customers")
	public boolean updateStatuses(boolean resp) throws Exception {

		return resp;
	}

	@MessageMapping("/talk")
	@SendTo("/topic/talks")
	public boolean processCustomers(boolean resp) throws Exception {
		
		return resp;
	}

}
