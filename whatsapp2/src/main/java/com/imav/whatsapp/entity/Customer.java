package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Id
	@Column(length = 20)
	private String phoneNumber;

	private String name;

	private String timestamp;

	private int step = 0;

	// This attribute is used to check if out BOT will replay or not
	private String timelimit = "0";

	private String mode = "normal";

	private boolean talk;

	@OneToMany(mappedBy = "customer")
	private List<CustomerMessage> messages = new ArrayList<>();

	public Customer(String phoneNumber, String name, String timestamp) {
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.timestamp = timestamp;
		this.talk = false;
	}

	public Customer() {

	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}

	public List<CustomerMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<CustomerMessage> messages) {
		this.messages = messages;
	}

	public boolean isTalk() {
		return talk;
	}

	public void setTalk(boolean talk) {
		this.talk = talk;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
	public int hashCode() {
		return Objects.hash(phoneNumber, id);
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
		return Objects.equals(phoneNumber, other.phoneNumber) && id == other.id;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", phoneNumber=" + phoneNumber + ", name=" + name + ", timestamp=" + timestamp
				+ ", step=" + step + ", timelimit=" + timelimit + ", mode=" + mode + ", talk=" + talk + ", messages="
				+ messages + "]";
	}

}
