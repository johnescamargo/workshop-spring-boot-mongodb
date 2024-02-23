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

	@GetMapping("/send")
	public String send() {

		return "send";
	}
	
	@GetMapping("/chat")
	public String chat() {

		return "chat";
	}

	@GetMapping("/confirmation")
	public String confirmation() {

		return "confirmation";
	}

	@GetMapping("/config")
	public String config() {

		return "config";
	}
	
	@GetMapping("/convenios")
	public String convenios() {

		return "convenios";
	}

	@GetMapping("/register")
	public String register() {

		return "register";
	}
	
	@GetMapping("/particular")
	public String particular() {

		return "particular";
	}
	
	@GetMapping("/notafiscal")
	public String notafiscal() {

		return "notafiscal";
	}

	@GetMapping("/privacy-and-policy")
	public String policy() {

		return "privacy-and-policy";
	}

}
