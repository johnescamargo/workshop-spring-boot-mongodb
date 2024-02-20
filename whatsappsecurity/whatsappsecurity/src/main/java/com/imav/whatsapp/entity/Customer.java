package com.imav.whatsapp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	private String c_phone_number;

	private String c_name;

	private String c_timestamp;

	@OneToMany(mappedBy = "customer")
	private List<CustomerMessage> messages = new ArrayList<>();

	public Customer(String phone_number, String name, String timestamp) {
		this.c_phone_number = phone_number;
		this.c_name = name;
		this.c_timestamp = timestamp;
	}

	public Customer() {

	}

	public String getC_phone_number() {
		return c_phone_number;
	}

	public void setC_phone_number(String c_phone_number) {
		this.c_phone_number = c_phone_number;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_timestamp() {
		return c_timestamp;
	}

	public void setC_timestamp(String c_timestamp) {
		this.c_timestamp = c_timestamp;
	}

	public List<CustomerMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<CustomerMessage> messages) {
		this.messages = messages;
	}

	@Override
	public int hashCode() {
		return Objects.hash(c_phone_number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(c_phone_number, other.c_phone_number);
	}

	@Override
	public String toString() {
		return "Customer [c_phone_number=" + c_phone_number + ", c_name=" + c_name + ", c_timestamp=" + c_timestamp
				+ ", messages=" + messages + "]";
	}

}
