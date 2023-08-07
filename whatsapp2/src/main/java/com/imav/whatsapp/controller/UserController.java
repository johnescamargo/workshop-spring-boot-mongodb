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

import com.imav.whatsapp.dto.UserDto;
import com.imav.whatsapp.dto.UserNewPassword;
import com.imav.whatsapp.dto.UserSimpleDto;
import com.imav.whatsapp.dto.UsernameAndPasswordDto;
import com.imav.whatsapp.resource.DBUserResource;

@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private DBUserResource userResource;

	@GetMapping("/users")
	public ResponseEntity<List<UserSimpleDto>> loadUsers() {
		logger.info("Loading GET /users");
		List<UserSimpleDto> dtos = new ArrayList<>();// Change for other DTO without PASSWORDS

		try {
			dtos = userResource.loadUsers();
			if (dtos != null) {
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/newpassword")
	public ResponseEntity<String> newPassword(@RequestBody UserNewPassword dto) {
		logger.info("Loading POST /new user - registration");

		try {
			if (dto.getPassword().equals(dto.getMatchingPassword())) {

				boolean resp = userResource.newUsersPassword(dto);
				if (!resp) {
					return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
				}
				
			} else {
				return new ResponseEntity<>("Senhas não coincidem", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Not created", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Ok", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/newpassword-from-admin")
	public ResponseEntity<String> newPasswordConfig(@RequestBody UsernameAndPasswordDto dto) {
		logger.info("Loading POST / newpassword-from-admin ");

		try {
			
			if (dto.getPassword() != null) {

				boolean resp = userResource.newUsersPasswordConfig(dto);
				if (!resp) {
					return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
				}
				
			} else {
				return new ResponseEntity<>("Senhas não coincidem", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Not created", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}

	@PostMapping("/registration")
	public ResponseEntity<String> newUserEntity(@RequestBody UserDto dto) {
		logger.info("Loading POST /new user - registration");

		try {
			if (dto.getPassword().equals(dto.getMatchingPassword())) {

				userResource.saveUser(dto);

				// TODO
				// Response like user already into DB
				// not found
				// Created
				// Something went wrong

				// Users are being created empty

			} else {
				return new ResponseEntity<>("Senhas não coincidem", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Not created", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Ok", HttpStatus.CREATED); // Change for created not 200
	}

	@GetMapping("/delete")
	public ResponseEntity<String> deleteUser(@RequestParam String username) {
		logger.info("Loading DELETE /delete user");

		System.out.println(username);
		try {
			boolean resp = userResource.deleteUser(username);
			if (!resp) {
				return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Excluído", HttpStatus.OK);
	}

}
