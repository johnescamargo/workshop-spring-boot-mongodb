package com.imav.whatsapp.dto;

public class ConvenioNameDto {

	private Long id;
	private String name;

	public ConvenioNameDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ConvenioNameDto [id=" + id + ", name=" + name + "]";
	}

}
