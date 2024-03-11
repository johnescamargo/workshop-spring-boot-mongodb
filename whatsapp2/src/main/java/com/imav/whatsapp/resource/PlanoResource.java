package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.Plano;
import com.imav.whatsapp.repository.PlanoRepository;

@Service
public class PlanoResource {
	
	@Autowired
	private PlanoRepository planoRepository;

	public List<Plano> livesearch(String id, String input) {
		
		List<Plano> planos = new ArrayList<>();
		
		try {
			
			planos = planoRepository.liveSearch(id, input);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return planos;
	}

}
