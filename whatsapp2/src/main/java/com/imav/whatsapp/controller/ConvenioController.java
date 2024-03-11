package com.imav.whatsapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.ConvenioNameDto;
import com.imav.whatsapp.entity.Convenio;
import com.imav.whatsapp.resource.ConvenioResource;

@RestController
@RequestMapping("/convenios")
public class ConvenioController {

	@Autowired
	private ConvenioResource convenioResource;

	// TODO create update Convenio

	@GetMapping("/getAll")
	public ResponseEntity<List<ConvenioNameDto>> getAll() {

		List<ConvenioNameDto> dtos = new ArrayList<>();

		try {

			dtos = convenioResource.getAll();

			if (dtos.size() == 0) {
				return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(dtos, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<Boolean> save(@RequestBody String obj) {

		boolean resp = false;

		try {

			resp = convenioResource.saveConvenio(obj);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@GetMapping("/findbyid")
	public ResponseEntity<Optional<Convenio>> findById(@RequestParam Long id) {

		Optional<Convenio> convenio = Optional.ofNullable(new Convenio());

		try {

			convenio = convenioResource.getConvenioById(id);
			

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(convenio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(convenio, HttpStatus.OK);
	}

	@GetMapping("/livesearch")
	public ResponseEntity<List<ConvenioNameDto>> liveSearch(@RequestParam String input) {

		List<ConvenioNameDto> dtos = new ArrayList<>();

		try {

			dtos = convenioResource.livesearch(input);

			if (dtos.size() == 0) {
				return new ResponseEntity<>(dtos, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(dtos, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {

		boolean resp = false;

		try {

			resp = convenioResource.checkDeleteConvenioById(id);
		if (!resp) {
			return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
		}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
