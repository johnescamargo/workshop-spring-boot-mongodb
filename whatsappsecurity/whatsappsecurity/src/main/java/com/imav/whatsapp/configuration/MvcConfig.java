package com.imav.whatsapp.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class MvcConfig implements WebMvcConfigurer {

	@GetMapping("/")
	public String mainPage() {

		return "login";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@GetMapping("/home")
	public String hello() {

		return "home";
	}
	
	@GetMapping("/chat")
	public String chat() {

		return "chat";
	}
	
	@GetMapping("/register")
	public String register() {

		return "register";
	}


	@GetMapping("/h2-console")
	public String h2Console() {

		return "/h2-console";
	}

}
