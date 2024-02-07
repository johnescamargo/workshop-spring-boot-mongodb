package com.imav.whatsapp.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WantsToTalk {

	@Id
	@Column(length = 20)
	private String phone;
	private String name;

	public WantsToTalk() {

	}

	public WantsToTalk(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WantsToTalk other = (WantsToTalk) obj;
		return Objects.equals(phone, other.phone);
	}

	@Override
	public String toString() {
		return "WantsToTalk [name=" + name + ", phone=" + phone + "]";
	}

}
