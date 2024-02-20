package com.imav.whatsapp.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavLocationMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.repository.ImavButtonMessageRepository;
import com.imav.whatsapp.repository.ImavLocationMessageRepository;
import com.imav.whatsapp.repository.ImavMessageRepository;

@Service
public class DBStatusesResource {

	@Autowired
	private ImavMessageRepository imavMessageRepository;

	@Autowired
	private ImavButtonMessageRepository imavButtonMessageRepository;

	@Autowired
	private ImavLocationMessageRepository imavLocationMessageRepository;

	@Autowired
	private DBMessageResource dbMessageResource;

	public boolean findIdWamid(String phone, String idWamid, int statusCode) {

		if (findImavMessage(phone, idWamid, statusCode)) {
			findImavMessage(phone, idWamid, statusCode);
			dbMessageResource.loadMessages(phone);
			return true;
		} else if (findImavButtonMessage(phone, idWamid, statusCode)) {
			findImavButtonMessage(phone, idWamid, statusCode);
			dbMessageResource.loadMessages(phone);
			return true;
		} else if (findImavLocationMessage(phone, idWamid, statusCode)) {
			findImavLocationMessage(phone, idWamid, statusCode);
			dbMessageResource.loadMessages(phone);
			return true;
		} else {
			return false;
		}
	}

	public boolean findImavMessage(String phone, String idWamid, int statusCode) {
		ImavMessage imavMessage = new ImavMessage();

		try {
			imavMessage = imavMessageRepository.findByPhoneAndIdWamid(phone, idWamid);
			imavMessage.setStatus(statusCode);
			updateImavMessage(imavMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean findImavButtonMessage(String phone, String idWamid, int statusCode) {
		ImavButtonMessage buttonMessage = new ImavButtonMessage();

		try {
			buttonMessage = imavButtonMessageRepository.findByPhoneAndIdWamid(phone, idWamid);
			buttonMessage.setB_status(statusCode);
			updateImavButtonMessage(buttonMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean findImavLocationMessage(String phone, String idWamid, int statusCode) {
		ImavLocationMessage imavLocationMessage = new ImavLocationMessage();

		try {
			imavLocationMessage = imavLocationMessageRepository.findByPhoneAndIdWamid(phone, idWamid);
			imavLocationMessage.setL_status(statusCode);
			updateImavLocationMessage(imavLocationMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void updateImavMessage(ImavMessage message) {
		try {
			imavMessageRepository.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateImavButtonMessage(ImavButtonMessage message) {
		try {
			imavButtonMessageRepository.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateImavLocationMessage(ImavLocationMessage message) {
		try {
			imavLocationMessageRepository.save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
