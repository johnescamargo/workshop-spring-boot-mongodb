package com.johnescamargo.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class SecurityController {

	@GetMapping("/test1")
	@ResponseStatus(code = HttpStatus.OK)
	public String test() {
		return "Hey there 1";
	}

	@GetMapping("/test2")
	public ResponseEntity<String> test2() {
		return ResponseEntity.ok().body("Hey there 2");
	}

}
