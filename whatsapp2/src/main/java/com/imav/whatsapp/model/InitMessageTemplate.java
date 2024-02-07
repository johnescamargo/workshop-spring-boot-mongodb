package com.imav.whatsapp.model;

import java.util.ArrayList;

public class InitMessageTemplate {

	public String messaging_product;
	public String recipient_type;
	public String to;
	public String type;
	public Template template;

	public InitMessageTemplate() {
		this.template = new Template();
		this.messaging_product = "whatsapp";
		this.recipient_type = "individual";
		this.type = "template";
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getMessaging_product() {
		return messaging_product;
	}

	public String getRecipient_type() {
		return recipient_type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "InitMessageTemplate [messaging_product=" + messaging_product + ", recipient_type=" + recipient_type
				+ ", to=" + to + ", type=" + type + ", template=" + template + "]";
	}

	public static class Template<T> {
		public String name;
		public Language language;
		public ArrayList<T> components = new ArrayList<>();

		public Template() {
			this.name = "init_talk";
			this.language = new Language();


			Button bt = new Button();
			bt.index = "0";
			ParameterButton pb = new ParameterButton();
			pb.setPayload("SIM");
			bt.setParameter(pb);
			setComponent((T) bt);

			Button bt2 = new Button();
			bt2.index = "1";
			ParameterButton pb2 = new ParameterButton();
			pb2.setPayload("REMARCAR");
			bt2.setParameter(pb2);
			setComponent((T) bt2);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
		}

		public ArrayList<T> getComponents() {
			return components;
		}

		public void setComponents(ArrayList<T> components) {
			this.components = components;
		}

		public void setComponent(T component) {
			this.components.add(component);
		}

		@Override
		public String toString() {
			return "Template [name=" + name + ", language=" + language + ", components=" + components + "]";
		}

	}

	public static class Body {
		public String type;
		public ArrayList<ParameterBody> parameters = new ArrayList<>();

		public Body() {
			this.type = "body";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public ArrayList<ParameterBody> getParameters() {
			return parameters;
		}

		public void setParameters(ArrayList<ParameterBody> parameters) {
			this.parameters = parameters;
		}

		public void setParameters(ParameterBody parameter) {
			this.parameters.add(parameter);
		}

		@Override
		public String toString() {
			return "Body [type=" + type + ", parameters=" + parameters + "]";
		}

	}

	public static class Button {
		public String type;
		public String sub_type;
		public String index;
		public ArrayList<ParameterButton> parameters = new ArrayList<>();


		public Button() {
			this.type = "button";
			this.sub_type = "quick_reply";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public ArrayList<ParameterButton> getParameters() {
			return parameters;
		}

		public void setParameters(ArrayList<ParameterButton> parameters) {
			this.parameters = parameters;
		}

		public void setParameter(ParameterButton parameter) {
			this.parameters.add(parameter);
		}

		public String getSub_type() {
			return sub_type;
		}

		public void setSub_type(String sub_type) {
			this.sub_type = sub_type;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		@Override
		public String toString() {
			return "Button [type=" + type + ", parameters=" + parameters + ", sub_type=" + sub_type + ", index=" + index
					+ "]";
		}

	}

	public static class Language {
		public String code;

		public Language() {
			this.code = "pt_BR";
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return "Language [code=" + code + "]";
		}

	}

	public static class ParameterBody {
		public String type;
		public String text;

		public ParameterBody() {
			this.type = "text";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "ParameterBody [type=" + type + ", text=" + text + "]";
		}

	}

	public static class ParameterButton {
		public String type;
		public String payload;

		public ParameterButton() {
			this.type = "payload";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getPayload() {
			return payload;
		}

		public void setPayload(String payload) {
			this.payload = payload;
		}

		@Override
		public String toString() {
			return "ParameterButton [type=" + type + ", payload=" + payload + "]";
		}

	}

}

//Olá {{1}}, espero que esteja bem. ??
//Somos da Clínica de Olhos *IMAV* e estamos entrando em contato 
//para confirmar seu exame e/ou consulta conosco.
//Serviço: {{2}}
//Data: *{{3}}*
//Hora: *{{4}}* 
//Medico:{{5}}
//
//{{6}}
//Podemos confirmar sua presença? 
//Aguardamos sua confirmação. Obrigado
