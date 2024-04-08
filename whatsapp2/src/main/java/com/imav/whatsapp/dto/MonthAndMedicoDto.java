package com.imav.whatsapp.dto;

public class MonthAndMedicoDto {

	private String date;
	private String medico;

	public MonthAndMedicoDto() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	@Override
	public String toString() {
		return "MonthAndMedicoDto [date=" + date + ", medico=" + medico + "]";
	}

}
