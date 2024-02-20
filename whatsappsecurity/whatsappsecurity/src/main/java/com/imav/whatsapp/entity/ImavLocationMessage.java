package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "imav_locationmessage")
public class ImavLocationMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int l_status;
	private String l_to;
	private String l_type;
	private String l_name;
	private String l_address;
	private double l_longitude;
	private double l_latitude;
	private String timestamp;
	private String idWamid;

	public ImavLocationMessage() {
	}

	public ImavLocationMessage(String to, String type, String name, String address, double longitude, double latitude,
			int status, String timestamp) {
		this.l_to = to;
		this.l_type = type;
		this.l_name = name;
		this.l_address = address;
		this.l_longitude = longitude;
		this.l_latitude = latitude;
		this.l_status = status;
		this.timestamp = timestamp;
	}

	public String getIdWamid() {
		return idWamid;
	}

	public void setIdWamid(String idWamid) {
		this.idWamid = idWamid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getL_status() {
		return l_status;
	}

	public void setL_status(int l_status) {
		this.l_status = l_status;
	}

	public String getL_to() {
		return l_to;
	}

	public void setL_to(String l_to) {
		this.l_to = l_to;
	}

	public String getL_type() {
		return l_type;
	}

	public void setL_type(String l_type) {
		this.l_type = l_type;
	}

	public String getL_name() {
		return l_name;
	}

	public void setL_name(String l_name) {
		this.l_name = l_name;
	}

	public String getL_address() {
		return l_address;
	}

	public void setL_address(String l_address) {
		this.l_address = l_address;
	}

	public double getL_longitude() {
		return l_longitude;
	}

	public void setL_longitude(double l_longitude) {
		this.l_longitude = l_longitude;
	}

	public double getL_latitude() {
		return l_latitude;
	}

	public void setL_latitude(double l_latitude) {
		this.l_latitude = l_latitude;
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
		ImavLocationMessage other = (ImavLocationMessage) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "ImavLocationMessage [id=" + id + ", l_status=" + l_status + ", l_to=" + l_to + ", l_type=" + l_type
				+ ", l_name=" + l_name + ", l_address=" + l_address + ", l_longitude=" + l_longitude + ", l_latitude="
				+ l_latitude + ", timestamp=" + timestamp + ", idWamid=" + idWamid + "]";
	}

}
