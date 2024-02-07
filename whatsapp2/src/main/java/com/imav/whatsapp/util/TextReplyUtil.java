package com.imav.whatsapp.util;

import org.springframework.stereotype.Service;

@Service
public class TextReplyUtil {

	public String setTextYes() {
		String msg = "Obrigado pela confirmação. \r\n" + "\r\n" + "Esperemos lhe ver em breve." + "\r\n"
				+ "Endereço: Avenida Ministro Oswaldo Aranha, 207 - Rudge Ramos - São Bernardo do Campo. " + "\r\n"
				+ "Caso necessite ver o endereço no GPS, envie um 'oi' para interagir com o nosso menu. " + "\r\n"
				+ "Tenha um ótimo dia. 😊";

		return msg;
	}

	public String setButtonNo() {
		String msg = "Por favor, \r\n"
				+ "clique na mensagem abaixo para falar com uma de nossas atendentes e remarcar tua consulta \r\n"
				+ "Ou, ligue para:" + "\r\n" + "(11)4367-1577 ou (11)4570-0202" + "\r\n"
				+ "Muito obrigado. 😊";
		return msg;
	}

	public String setTextTelephone() {
		String msg = "Nossos números de telefone são: " + "\r\n" + "(11)4367-1577 ou (11)4570-0202" + "\r\n" + "Esperamos tua ligação. Muito obrigado 😊.";
		return msg;
	}

	public String setLinkWhatsapp() {
		String msg = "Clinica de olhos IMAV\r\n" + "Clique em mim!\r\n" + "  \r\n" + "http://wa.me/551143671577";
		return msg;
	}
	
	public String setTextCancel() {
		String msg = "Obrigado por nos avisar sobre o cancelamento e estamos cancelando tua consulta. Caso queira remarcar tua consulta e/ou exame um outro dia, entre em contato conosco. Muito obrigado. 😊.";
		return msg;
	}

}
