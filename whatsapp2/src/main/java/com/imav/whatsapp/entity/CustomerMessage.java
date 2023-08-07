package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_message")
public class CustomerMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Integer m_status;

	private String m_name;

	private String m_phone_from;

	@Column(length = 2000)
	private String m_text;

	private String m_type;

	private String m_timestamp;

	private String idWamid;

	@ManyToOne
	@JoinColumn(name = "customer_c_phone_number", nullable = false)
	private Customer customer;

	public CustomerMessage() {

	}

	public CustomerMessage(String name, String phone_from, String text, String type, String timestamp,
			Customer customer) {

		this.m_name = name;
		this.m_phone_from = phone_from;
		this.m_text = text;
		this.m_type = type;
		this.m_timestamp = timestamp;
		this.customer = customer;
	}

	public String getIdWamid() {
		return idWamid;
	}

	public void setIdWamid(String idWamid) {
		this.idWamid = idWamid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getM_status() {
		return m_status;
	}

	public void setM_status(Integer m_status) {
		this.m_status = m_status;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getM_phone_from() {
		return m_phone_from;
	}

	public void setM_phone_from(String m_phone_from) {
		this.m_phone_from = m_phone_from;
	}

	public String getM_text() {
		return m_text;
	}

	public void setM_text(String m_text) {
		this.m_text = m_text;
	}

	public String getM_type() {
		return m_type;
	}

	public void setM_type(String m_type) {
		this.m_type = m_type;
	}

	public String getM_timestamp() {
		return m_timestamp;
	}

	public void setM_timestamp(String m_timestamp) {
		this.m_timestamp = m_timestamp;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerMessage other = (CustomerMessage) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CustomerMessage [id=" + id + ", m_status=" + m_status + ", m_name=" + m_name + ", m_phone_from="
				+ m_phone_from + ", m_text=" + m_text + ", m_type=" + m_type + ", m_timestamp=" + m_timestamp
				+ ", idWamid=" + idWamid + ", customer=" + customer + "]";
	}

}
