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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "imav_buttonmessage")
public class ImavButtonMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int b_status;
	private String b_to;
	private String b_timestamp;
	private String b_type;
	private String idWamid;

	@Column(length = 40000)
	private String b_text;

	@OneToMany
	@JoinColumn(name = "imav_buttonmessage_id") //
	private List<ImavButton> buttons = new ArrayList<>();

	public ImavButtonMessage() {

	}

	public ImavButtonMessage(long id, int b_status, String b_to, String b_timestamp, String b_type, String b_text,
			List<ImavButton> buttons) {
		this.id = id;
		this.b_status = b_status;
		this.b_to = b_to;
		this.b_timestamp = b_timestamp;
		this.b_type = b_type;
		this.b_text = b_text;
		this.buttons = buttons;
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

	public int getB_status() {
		return b_status;
	}

	public void setB_status(int b_status) {
		this.b_status = b_status;
	}

	public String getB_to() {
		return b_to;
	}

	public void setB_to(String b_to) {
		this.b_to = b_to;
	}

	public String getB_timestamp() {
		return b_timestamp;
	}

	public void setB_timestamp(String b_timestamp) {
		this.b_timestamp = b_timestamp;
	}

	public String getB_type() {
		return b_type;
	}

	public void setB_type(String b_type) {
		this.b_type = b_type;
	}

	public String getB_text() {
		return b_text;
	}

	public void setB_text(String b_text) {
		this.b_text = b_text;
	}

	public List<ImavButton> getButtons() {
		return buttons;
	}

	public void setButtons(List<ImavButton> buttons) {
		this.buttons = buttons;
	}

	public void setButton(ImavButton button) {
		this.buttons.add(button);
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
		ImavButtonMessage other = (ImavButtonMessage) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "ImavButtonMessage [id=" + id + ", b_status=" + b_status + ", b_to=" + b_to + ", b_timestamp="
				+ b_timestamp + ", b_type=" + b_type + ", idWamid=" + idWamid + ", b_text=" + b_text + ", buttons="
				+ buttons + "]";
	}

}
