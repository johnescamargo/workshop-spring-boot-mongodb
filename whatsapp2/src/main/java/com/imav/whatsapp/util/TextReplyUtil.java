package com.imav.whatsapp.util;

import org.springframework.stereotype.Service;

@Service
public class TextReplyUtil {

	public String setTextYes() {
		String msg = "Obrigado pela confirmação.\r\n" + "\r\n" + "Esperemos lhe ver em breve." + "\r\n"
				+ "Endereço: Avenida Ministro Oswaldo Aranha, 207 - Rudge Ramos - São Bernardo do Campo" + "\r\n"
				+ "Tenha um ótimo dia. 😊";

		return msg;
	}

	public String setButtonNo() {
		String msg = "Por favor, \r\n"
				+ "clique na mensagem abaixo para falar com uma de nossas atendentes e remarcar tua consulta \r\n"
				+ "Ou, ligue para:" + "\r\n" + "1143671577" + "\r\n"
				+ "Muito obrigado. 😊";
		return msg;
	}

	public String setTextTelephone() {
		String msg = "Nosso número de telefone é o:" + "\r\n" + "1143671577" + "\r\n" + "Esperamos tua ligação. Muito obrigado 😊.";
		return msg;
	}

	public String setLinkWhatsapp() {
		String msg = "Clinica de olhos IMAV\r\n" + "Clique em mim!\r\n" + "  \r\n" + "http://wa.me/551143671577";
		return msg;
	}
	
	public String setTextCancel() {
		String msg = "Obrigado pelo retorno, vamos cancelar tua consulta. Esperamos por você em uma outra oportunidade. Muito obrigado 😊.";
		return msg;
	}

}
