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
@Table(name = "imav_message")
public class ImavMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int m_status;
	private String m_timestamp;
	private String m_type;
	private String m_to;
	private String idWamid;

	@Column(length = 500)
	private String m_text;

	public ImavMessage() {

	}

	public ImavMessage(String timestamp, String type, String to, String text, int status) {
		this.m_timestamp = timestamp;
		this.m_type = type;
		this.m_to = to;
		this.m_text = text;
		this.m_status = status;
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

	public String getTimestamp() {
		return m_timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.m_timestamp = timestamp;
	}

	public String getType() {
		return m_type;
	}

	public void setType(String type) {
		this.m_type = type;
	}

	public String getTo() {
		return m_to;
	}

	public void setTo(String to) {
		this.m_to = to;
	}

	public String getText() {
		return m_text;
	}

	public void setText(String text) {
		this.m_text = text;
	}

	public int getStatus() {
		return m_status;
	}

	public void setStatus(int status) {
		this.m_status = status;
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
		ImavMessage other = (ImavMessage) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "ImavMessage [id=" + id + ", m_status=" + m_status + ", m_timestamp=" + m_timestamp + ", m_type="
				+ m_type + ", m_to=" + m_to + ", idWamid=" + idWamid + ", m_text=" + m_text + "]";
	}

}
