package com.imav.whatsapp.ai;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imav.whatsapp.webhookModel.WebhookReceivedTextMessage;

@Service
public class MessageAI {

	private Gson gson = new Gson();

	public String analyzeTextYesOrNo(String obj) {

		String text = getText(obj);
		String resp = "void";

		int confInt = 0;

		String[] words = text.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll(",", "").toLowerCase();
		}

		for (int i = 0; i < words.length; i++) {
			if (words[i].equals("sim") || words[i].equals("ok") || words[i].equals("confirmado")) {
				confInt = 10;
				i = 200;
				resp = "sim";
				break;
			}

			if (words[i].equals("pode")) {
				confInt++;
			}

			if (words[i].equals("confirmar")) {
				confInt++;
			}

			if (words[i].equals("não") || words[i].equals("nao") || words[i].equals("remarca") || words[i].equals("remarcar")) {
				confInt = -5;
				resp = "remarcar";
				i = 100;
				break;
			}
			
			if (words[i].equals("cancelar") || words[i].equals("cancela")) {
				confInt = 0;
				i = 200;
				resp = "cancelar";
				break;
			}
		}

		if (confInt > 1) {
			resp = "sim";
		}

		return resp;
	}

	public String analyzeTextTalkOrPhone(String obj) {

		String text = getText(obj);
		String resp = "void";

		int confInt = 0;

		String[] words = text.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll(",", "").toLowerCase();
		}

		for (int i = 0; i < words.length; i++) {

			if (words[i].equals("falar")) {
				confInt++;
			}

			if (words[i].equals("atendente")) {
				confInt++;
			}

			if (words[i].equals("recepção")) {
				confInt++;
			}

			if (words[i].equals("whatsapp")) {
				confInt++;
			}

			if (words[i].equals("telefone")) {
				confInt = 0;
				i = 200;
				resp = "telefone";
				break;
			}
			
			if (words[i].equals("cancelar") || words[i].equals("cancela")) {
				confInt = 0;
				i = 200;
				resp = "cancelar";
				break;
			}

		}

		if (confInt > 1) {
			resp = "falar";
		} 
		
		return resp;
	}

	public String getText(String obj) {

		String text = "";

		try {

			WebhookReceivedTextMessage messageReceived = new WebhookReceivedTextMessage();
			messageReceived = gson.fromJson(obj, WebhookReceivedTextMessage.class);

			text = messageReceived.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getText()
					.getBody();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return text.toLowerCase();
	}
}
