package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "confirmation_response")
public class ConfirmationResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String idWamid;
	private String doctor;
	private String service;
	private String name;
	private String serviceDate;
	private String hour;
	private String response;
	private String shippingDate;

	@Column(length = 20)
	private String phoneNumber;

	public ConfirmationResponse(String doctor, String service, String name, String serviceDate, String hour,
			String response, String shippingDate, String phoneNumber) {
		this.doctor = doctor;
		this.service = service;
		this.name = name;
		this.serviceDate = serviceDate;
		this.hour = hour;
		this.response = response;
		this.shippingDate = shippingDate;
		this.phoneNumber = phoneNumber;
	}

	public ConfirmationResponse() {

	}

	public long getId() {
		return id;
	}

	public String getIdWamid() {
		return idWamid;
	}

	public void setIdWamid(String idWamid) {
		this.idWamid = idWamid;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(String shippingDate) {
		this.shippingDate = shippingDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idWamid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfirmationResponse other = (ConfirmationResponse) obj;
		return id == other.id && Objects.equals(idWamid, other.idWamid);
	}

	@Override
	public String toString() {
		return "ConfirmationResponse [id=" + id + ", idWamid=" + idWamid + ", doctor=" + doctor + ", service=" + service
				+ ", name=" + name + ", serviceDate=" + serviceDate + ", hour=" + hour + ", response=" + response
				+ ", shippingDate=" + shippingDate + ", phoneNumber=" + phoneNumber + "]";
	}

}
