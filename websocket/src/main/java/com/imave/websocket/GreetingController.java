package com.imave.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}
	
	@MessageMapping("/message")
	@SendTo("/topic/greetings")
	public Greeting messsage(HelloMessage2 message) throws Exception {
		Thread.sleep(1000); // simulated delay
		System.out.println(message.getMessage());
		return new Greeting(HtmlUtils.htmlEscape(message.getName()) + ": " + message.getMessage());
	}

}