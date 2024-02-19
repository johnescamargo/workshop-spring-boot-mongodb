package com.imav.whatsapp.model;

import java.util.ArrayList;

public class InitMessageTemplate2 {

	public String messaging_product;
	public String recipient_type;
	public String to;
	public String type;
	@SuppressWarnings("rawtypes")
	public Template template;

	@SuppressWarnings("rawtypes")
	public InitMessageTemplate2() {
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

	@SuppressWarnings("rawtypes")
	public Template getTemplate() {
		return template;
	}

	@SuppressWarnings("rawtypes")
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
		return "InitMessageTemplate2 [messaging_product=" + messaging_product + ", recipient_type=" + recipient_type
				+ ", to=" + to + ", type=" + type + ", template=" + template + "]";
	}

	public static class Template<T> {
		public String name;
		public Language language;
		public ArrayList<T> components = new ArrayList<>();

		public Template() {
			this.name = "init_send_message";
			this.language = new Language();
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

	public static class Body2 {
		public String type;
		public ArrayList<ParameterBody2> parameters = new ArrayList<>();

		public Body2() {
			this.type = "body";
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public ArrayList<ParameterBody2> getParameters() {
			return parameters;
		}

		public void setParameters(ArrayList<ParameterBody2> parameters) {
			this.parameters = parameters;
		}

		public void setParameters(ParameterBody2 parameter) {
			this.parameters.add(parameter);
		}

		@Override
		public String toString() {
			return "Body2 [type=" + type + ", parameters=" + parameters + "]";
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

	public static class ParameterBody2 {
		public String type;
		public String text;

		public ParameterBody2() {
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
			return "ParameterBody2 [type=" + type + ", text=" + text + "]";
		}

	}

}

//Olá {{1}}, espero que esteja bem. ??
//Somos da Clínica de Olhos *IMAV* e estamos entrando em contato 
//para confirmar seu exame e/ou consulta conosco.
//Texto: {{2}}

