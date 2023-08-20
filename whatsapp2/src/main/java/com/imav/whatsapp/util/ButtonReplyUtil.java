package com.imav.whatsapp.util;

import org.springframework.stereotype.Service;

@Service
public class ButtonReplyUtil {

	public String setButtonYes() {
		String msg = "Agradecemos tua confirmação.\r\n" + "\r\n" + "Esperemos lhe ver em breve. \r\n tenha um ótimo dia. 😊";
		return msg;
	}

	public String setButtonNo() {
		String msg = "Por favor, \r\n"
				+ "clique na mensagem abaixo para falar com uma de nossas atendentes e remarcar tua consulta \r\n"
				+ "Ou, ligue para:" + "\r\n" + "1143671577" + "\r\n" 
				+ "Ou, Digite o número 0 que um de nossos atendendes entrará em contato com você:" + "\r\n" + "1143671577" + "\r\n" 
				+ "Muito obrigado. 😊";
		return msg;
	}

	public String setButtonTelephone() {
		String msg = "Nosso número de telefone é o:" + "\r\n" + "1143671577" + "\r\n" + "Muito obrigado.";
		return msg;
	}

	public String setButtonWhatsapp() {
		String msg = "Clinica de olhos IMAV\r\n" + "Clique em mim!\r\n" + "  \r\n" + "http://wa.me/551143671577";
		return msg;
	}

}
