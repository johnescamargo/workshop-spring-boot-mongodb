package com.imav.whatsapp.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagedb")
public class ImageRestController {

	//private Logger logger = LogManager.getLogger(ImageRestController.class);
	//@Autowired
	//private DBImageResource imageResource;


	@GetMapping(value = "/image")
	public @ResponseBody byte[] getImage(@RequestParam String url) throws IOException {
		InputStream in = getClass().getResourceAsStream(url);
		return IOUtils.toByteArray(in);
	}

}