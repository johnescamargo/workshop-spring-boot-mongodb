package com.imav.whatsapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.dto.NotaFiscalDto;
import com.imav.whatsapp.entity.NotaFiscal;
import com.imav.whatsapp.resource.NotaFiscalResource;

@RestController
@RequestMapping("/nf")
public class NotaFiscalController {

	@Autowired
	private NotaFiscalResource fiscalResource;
	
	// Save new NF
	@PostMapping("/save")
	public ResponseEntity<String> saveNotaFiscal(@RequestBody String obj) {

		try {
			fiscalResource.saveNotaFiscal(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Working...", HttpStatus.OK);
	}
	
	// Save new NF
	@PostMapping("/update")
	public ResponseEntity<String> updateNotaFiscal(@RequestBody String obj) {

		try {
			fiscalResource.updateNotaFiscal(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Working...", HttpStatus.OK);
	}
	
	// Update
	@PostMapping("/updatenf")
	public ResponseEntity<String> updateNF(@RequestBody String obj) {

		try {
			fiscalResource.updateNF(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Working...", HttpStatus.OK);
	}

	// Get all by NF not done
	@GetMapping("/all")
	public ResponseEntity<List<NotaFiscalDto>> getNfNotDone() {

		List<NotaFiscalDto> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findAllByNfNotDoneDto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}

	@GetMapping("/livesearch")
	public ResponseEntity<List<NotaFiscalDto>> findLiveSearch(@RequestParam String input) {

		List<NotaFiscalDto> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findLiveSearch(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}

	@GetMapping("/getcustomer")
	public ResponseEntity<Optional<NotaFiscal>> findById(@RequestParam Long id) {

		Optional<NotaFiscal> nf = java.util.Optional.empty();

		try {
			nf = fiscalResource.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nf, HttpStatus.OK);
	}

	@GetMapping("/getbydate")
	public ResponseEntity<List<NotaFiscalDto>> getByDate(@RequestParam String date) {

		List<NotaFiscalDto> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findAllByTimestampCreatedDto(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}
	
	@GetMapping("/getallbydate")
	public ResponseEntity<List<NotaFiscal>> getAllByDate(@RequestParam String date) {

		List<NotaFiscal> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findAllByTimestampCreated(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}
	
	@GetMapping("/getbymonth")
	public ResponseEntity<List<NotaFiscal>> getByMonth(@RequestParam String date) {

		List<NotaFiscal> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findAllByMonth(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}
	
	@GetMapping("/getbymonthandmedico")
	public ResponseEntity<List<NotaFiscal>> getByMonthAndMedico(@RequestBody String obj) {

		List<NotaFiscal> nfs = new ArrayList<>();

		try {
			nfs = fiscalResource.findAllByMonthAndMedico(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(nfs, HttpStatus.OK);
	}

}
