package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.dto.ImageDbDto;
import com.imav.whatsapp.entity.ImageDb;
import com.imav.whatsapp.repository.ImageDbRepository;

@Service
public class DBImageResource {

	@Autowired
	private ImageDbRepository imageRepository;

	public List<ImageDbDto> getByPhone(String phone) {

		List<ImageDbDto> dtos = new ArrayList<>();
		ImageDbDto dto = new ImageDbDto();

		try {

			List<ImageDb> imgs = imageRepository.findAllByPhone(phone);

			for (int i = 0; i < imgs.size(); i++) {
				dto.setPhone(imgs.get(i).getPhone());
				dto.setTimestamp(imgs.get(i).getTimestamp());
				dto.setContent(imgs.get(i).getContent());
				dto.setCaption(imgs.get(i).getCaption());
				dto.setMimeType(imgs.get(i).getMimeType());

				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dtos;

	}

}
