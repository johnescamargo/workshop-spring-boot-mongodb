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
import org.springframework.web.bind.annotation.RestController;

import com.imav.whatsapp.entity.WeekTime;
import com.imav.whatsapp.repository.WeekTimeRepository;

@RestController
@RequestMapping("/week")
public class WeekTimeController {

	private Logger logger = LogManager.getLogger(WeekTimeController.class);

	@Autowired
	private WeekTimeRepository weekRepository;

	@GetMapping("/all")
	public ResponseEntity<List<WeekTime>> loadWeek() {
		logger.info("Loading GET /week");
		List<WeekTime> objs = new ArrayList<>();//
		try {
			objs = weekRepository.findAll();
			if (objs != null) {
				return new ResponseEntity<>(objs, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(objs, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(objs, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/update")
	public ResponseEntity<Boolean> loadUpdate(@RequestBody List<WeekTime> days) {
		logger.info("Loading POST /week");

		try {
						
			List<WeekTime> a = weekRepository.saveAll(days);
			System.out.println(a);

			return new ResponseEntity<>(true, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}
}
