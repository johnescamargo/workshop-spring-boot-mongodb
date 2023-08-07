package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "imav_button")
public class ImavButton implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String b_id;
	private String b_type;
	private String b_title;

	@ManyToOne
	@JoinColumn(name = "imav_buttonmessage_id", nullable = false)
	private ImavButtonMessage imavButtonMessage;

	public ImavButton() {

	}

	public ImavButton(long id, String b_id, String b_type, String b_title, ImavButtonMessage imavButtonMessage) {
		this.id = id;
		this.b_id = b_id;
		this.b_type = b_type;
		this.b_title = b_title;
		this.imavButtonMessage = imavButtonMessage;
	}

	public long getId() {
		return id;
	}

	public String getB_id() {
		return b_id;
	}

	public void setB_id(String b_id) {
		this.b_id = b_id;
	}

	public String getB_type() {
		return b_type;
	}

	public void setB_type(String b_type) {
		this.b_type = b_type;
	}

	public String getB_title() {
		return b_title;
	}

	public void setB_title(String b_title) {
		this.b_title = b_title;
	}

	public ImavButtonMessage getImavButtonMessage() {
		return imavButtonMessage;
	}

	public void setImavButtonMessage(ImavButtonMessage imavButtonMessage) {
		this.imavButtonMessage = imavButtonMessage;
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
		ImavButton other = (ImavButton) obj;
		return id == other.id;
	}

}