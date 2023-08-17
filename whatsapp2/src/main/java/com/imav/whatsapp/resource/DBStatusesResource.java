package com.imav.whatsapp.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.ConfirmationResponse;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.entity.ImavLocationMessage;
import com.imav.whatsapp.entity.ImavMessage;
import com.imav.whatsapp.repository.ConfirmationResponseRepository;
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
	private ConfirmationResponseRepository confirmationRepository;

	public boolean findIdWamid(String phone, String idWamid, int statusCode) {
		
		// Confirmation status
		if(confirmationRepository.existsByIdWamid(idWamid)) {
			findConfirmationIdWamid(phone, idWamid, statusCode);
		}

		if (imavMessageRepository.existsByIdWamid(idWamid)) {
			findImavMessage(phone, idWamid, statusCode);

			return true;
		} else if (imavButtonMessageRepository.existsByIdWamid(idWamid)) {
			findImavButtonMessage(phone, idWamid, statusCode);

			return true;
		} else if (imavLocationMessageRepository.existsByIdWamid(idWamid)) {
			findImavLocationMessage(phone, idWamid, statusCode);

			return true;
		} else {
			return false;
		}
		
	}

	public boolean findImavMessage(String phone, String idWamid, int statusCode) {
		ImavMessage imavMessage = new ImavMessage();

		try {

			imavMessage = imavMessageRepository.findByIdWamid(idWamid);
			int status = imavMessage.getStatus();

			if (status < 3) {
				imavMessage.setStatus(statusCode);
				updateImavMessage(imavMessage);
			}

			if (status == 3 || statusCode == 3) {
				imavMessage.setStatus(3);
				updateImavMessage(imavMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(imavMessage);
			return false;
		}
		return true;
	}

	public boolean findImavButtonMessage(String phone, String idWamid, int statusCode) {
		ImavButtonMessage buttonMessage = new ImavButtonMessage();

		try {
			buttonMessage = imavButtonMessageRepository.findByIdWamid(idWamid);
			int status = buttonMessage.getB_status();

			if (status < 3) {
				buttonMessage.setB_status(statusCode);
				updateImavButtonMessage(buttonMessage);
			}

			if (status == 3 || statusCode == 3) {
				buttonMessage.setB_status(3);
				updateImavButtonMessage(buttonMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(buttonMessage);
			return false;
		}
		return true;
	}

	public boolean findImavLocationMessage(String phone, String idWamid, int statusCode) {
		ImavLocationMessage imavLocationMessage = new ImavLocationMessage();

		try {
			imavLocationMessage = imavLocationMessageRepository.findByIdWamid(idWamid);

			int status = imavLocationMessage.getL_status();

			if (status < 3) {
				imavLocationMessage.setL_status(statusCode);
				updateImavLocationMessage(imavLocationMessage);
			}

			if (status == 3 || statusCode == 3) {
				imavLocationMessage.setL_status(3);
				updateImavLocationMessage(imavLocationMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(imavLocationMessage);
			return false;
		}
		return true;
	}
	
	public boolean findConfirmationIdWamid(String phone, String idWamid, int statusCode) {
		
		ConfirmationResponse confirmationResponse = new ConfirmationResponse();

		try {
			confirmationResponse = confirmationRepository.findByIdWamid(idWamid);

			int status = confirmationResponse.getStatus();

			if (statusCode == 1 && status <= 1) {			
				confirmationResponse.setStatus(statusCode);
				confirmationResponse.setResponse("ENVIADO");
				updateConfirmation(confirmationResponse);
			}
			
			if (statusCode == 2 && status <= 2) {
				confirmationResponse.setStatus(statusCode);
				confirmationResponse.setResponse("RECEBIDO");
				updateConfirmation(confirmationResponse);
			}

			if (statusCode == 3 && status <= 3) {
				confirmationResponse.setStatus(statusCode);
				confirmationResponse.setResponse("VISUALIZADO");
				
				updateConfirmation(confirmationResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(confirmationResponse);
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
	
	public void updateConfirmation(ConfirmationResponse confirmation) {
		try {
			confirmationRepository.save(confirmation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
