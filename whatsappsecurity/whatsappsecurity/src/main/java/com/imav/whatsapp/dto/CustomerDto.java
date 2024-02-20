package com.imav.whatsapp.dto;

import java.util.Objects;

public class CustomerDto {

	private String phone_number;

	private String name;

	private int timestamp;

	public CustomerDto(String phone_number, String name, int timestamp) {
		this.phone_number = phone_number;
		this.name = name;
		this.timestamp = timestamp;
	}

	public CustomerDto() {

	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone_number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDto other = (CustomerDto) obj;
		return Objects.equals(phone_number, other.phone_number);
	}

	@Override
	public String toString() {
		return "CustomerDto [phone_number=" + phone_number + ", name=" + name + ", timestamp=" + timestamp + "]";
	}

}
