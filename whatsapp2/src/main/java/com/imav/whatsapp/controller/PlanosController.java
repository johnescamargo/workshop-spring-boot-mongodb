package com.imav.whatsapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.entity.Plano;
import com.imav.whatsapp.resource.PlanoResource;


@RestController
@RequestMapping("/planos")
public class PlanosController {
	
	@Autowired
	private PlanoResource planoResource;
	
	@GetMapping("/livesearch")
	public ResponseEntity<List<Plano>> liveSearch(@RequestParam String id, @RequestParam String input) {
		
		List<Plano> planos = new ArrayList<>();
		
		try {
			
			planos = planoResource.livesearch(id, input);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(planos, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(planos, HttpStatus.OK);
	}

}
