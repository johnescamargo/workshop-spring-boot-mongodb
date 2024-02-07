package com.imav.whatsapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.dto.MessageEnviaDto;
import com.imav.whatsapp.dto.MessageInitDto;
import com.imav.whatsapp.entity.ImavButton;
import com.imav.whatsapp.entity.ImavButtonMessage;
import com.imav.whatsapp.model.ErrorResponse;
import com.imav.whatsapp.model.InitMessageTemplate;
import com.imav.whatsapp.model.InitMessageTemplate.Body;
import com.imav.whatsapp.model.InitMessageTemplate.ParameterBody;
import com.imav.whatsapp.model.SuccessfullResponse;

@Service
public class MessageUtil {

	private static Gson GSON = new Gson();

	private Logger logger = LogManager.getLogger(MessageUtil.class);

	public HashMap<String, String> checkResponseMessageSent(String response) {

		HashMap<String, String> hashMap = new HashMap<>();
		String resp = checkTypeOfResponse(response);

		if (resp.equals("success")) {
			SuccessfullResponse messageResp = GSON.fromJson(response, SuccessfullResponse.class);
			String messageFrom = messageResp.getContacts().get(0).getWa_id();
			String idWamid = messageResp.getMessages().get(0).getWa_id();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);

			hashMap.put("from", messageFrom);
			hashMap.put("resp", "success");
			hashMap.put("idWamid", idWamid);
			hashMap.put("timestamp", timestamp);

		} else if (response.equals("error")) {
			ErrorResponse messageResp = GSON.fromJson(response, ErrorResponse.class);
			logger.info("Error message sent: " + messageResp);
			hashMap.put("resp", "error");
		} else {
			logger.info("Unknown message...");
			hashMap.put("resp", "error");
		}
		return hashMap;
	}

	// For Brazilian phone numbers only
	public boolean checkPhoneNumber(MessageInitDto obj) {

		int phoneLength = obj.getPhone().length();
		String phone = String.valueOf(obj.getPhone().charAt(4));
		String phoneDDI = String.valueOf(obj.getPhone().charAt(0));
		phoneDDI += String.valueOf(obj.getPhone().charAt(1));
		
		if (phone.equals("9") && phoneDDI.equals("55") && phoneLength == 13) {
			return true;
		} else {
			return false;
		}
	}

	public String checkTypeOfResponse(String str) {

		String type = "success";
		int field = str.lastIndexOf("\"error\"");
		if (field > 0) {
			type = "error";
		}
		return type;
	}

	public String messageOrganizer(MessageInitDto obj) {

		String message = "";

		for (int i = 0; i < obj.getMessages().size(); i++) {

			if (i == 0) {
				message += obj.getMessages().get(i);
				message += " " + obj.getName() + System.lineSeparator();
			} else {
				message += obj.getMessages().get(i) + System.lineSeparator();

				if (i == 1) {
					message += "Data: " + obj.getDate() + System.lineSeparator();
					message += "Hora: " + obj.getTime() + System.lineSeparator();
					message += "Medico: " + obj.getDoctor() + System.lineSeparator();
				}
			}
		}

		List<String> msg = new ArrayList<>();
		msg.add(message);

		obj.getMessages().clear();
		obj.setMessages(msg);

		return message;
	}

	public String messageOrganizerDB(MessageInitDto dto) {

		String message = "";

		message += "Olá " + dto.getName() + ", espero que esteja bem. 😊" + System.lineSeparator();
		message += "Somos da Clínica de Olhos IMAV e estamos entrando em contato" + System.lineSeparator();
		message += "para confirmar seu exame e/ou consulta conosco." + System.lineSeparator();
		message += "Serviço: " + dto.getService() + System.lineSeparator();
		message += "Data: " + dto.getDate() + System.lineSeparator();
		message += "Hora: " + dto.getTime() + System.lineSeparator();
		message += "Medico: " + dto.getDoctor() + System.lineSeparator();
		message += "" + System.lineSeparator();
		message += setMessages(dto) + System.lineSeparator();
		message += "Podemos confirmar sua presença?" + System.lineSeparator();
		message += "Aguardamos sua confirmação. Obrigado" + System.lineSeparator();

		return message;
	}

	public String messageOrganizerEnvia(MessageEnviaDto obj) {

		String message = "";

		for (int i = 0; i < obj.getMessages().size(); i++) {
			message += obj.getMessages().get(i) + System.lineSeparator();
		}

		List<String> msg = new ArrayList<>();
		msg.add(message);

		obj.getMessages().clear();
		obj.setMessages(msg);

		return message;
	}

	@SuppressWarnings("unchecked")
	public InitMessageTemplate messageInitOrganizer(MessageInitDto dto) {

		InitMessageTemplate initMessageTemplate = new InitMessageTemplate();

		try {

			InitMessageTemplate.Body body = new Body();
			initMessageTemplate.setTo(dto.getPhone());

			// Add 6 ParameterBody to array inside Body and then Body to InitMessageTemplate
			ArrayList<InitMessageTemplate.ParameterBody> params = new ArrayList<>();
			InitMessageTemplate.ParameterBody paramBody1 = new ParameterBody();
			InitMessageTemplate.ParameterBody paramBody2 = new ParameterBody();
			InitMessageTemplate.ParameterBody paramBody3 = new ParameterBody();
			InitMessageTemplate.ParameterBody paramBody4 = new ParameterBody();
			InitMessageTemplate.ParameterBody paramBody5 = new ParameterBody();
			InitMessageTemplate.ParameterBody paramBody6 = new ParameterBody();

			// Olá {{1}}, espero que esteja bem.
			// Somos da Clínica de Olhos *IMAV* e estamos entrando em contato
			// para confirmar seu exame e/ou consulta conosco.
			paramBody1.setText(dto.getName());
			params.add(paramBody1);

			// Serviço: {{2}}
			paramBody2.setText(dto.getService());
			params.add(paramBody2);

			// Data: *{{3}}*
			paramBody3.setText(dto.getDate());
			params.add(paramBody3);

			// Hora: *{{4}}*
			paramBody4.setText(dto.getTime());
			params.add(paramBody4);

			// Medico:{{5}}
			paramBody5.setText(" " + dto.getDoctor());
			params.add(paramBody5);

			// {{6}} Texto aleatório
			String message = setMessages(dto);
			paramBody6.setText(message);
			params.add(paramBody6);
			// Podemos confirmar sua presença?
			// Aguardamos sua confirmação. Obrigado

			body.setParameters(params);

			initMessageTemplate.template.setComponent(body);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return initMessageTemplate;
	}

	public ImavButtonMessage setImavButtonMessage(MessageInitDto dto, String idWamid) {

		// Set button
		ImavButtonMessage buttonMessage = new ImavButtonMessage();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		try {

			buttonMessage.setB_text(messageOrganizerDB(dto));
			buttonMessage.setB_status(1);
			buttonMessage.setB_timestamp(timestamp);
			buttonMessage.setB_to(dto.getPhone());
			buttonMessage.setB_type("button");
			buttonMessage.setIdWamid(idWamid);

			List<ImavButton> buttons = new ArrayList<>();

			ImavButton sim = new ImavButton();
			sim.setB_id("SIM");
			sim.setB_title("SIM");
			sim.setB_type("reply");

			buttons.add(sim);

			ImavButton remarcar = new ImavButton();
			remarcar.setB_id("REMARCAR");
			remarcar.setB_title("REMARCAR");
			remarcar.setB_type("reply");

			buttons.add(remarcar);

			buttonMessage.setButtons(buttons);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return buttonMessage;
	}

	/*
	 * Set messages in one line because template does not accept 
	 * "next line" or more than 4 spaces
	 */
	private String setMessages(MessageInitDto dto) {
		
		String message = "";
		
		for (int i = 0; i < dto.getMessages().size(); i++) {
			message += dto.getMessages().get(i);
		}
		//System.out.println("Messages ********************** ");
		//System.out.println(message);
		return message;
	}

}
