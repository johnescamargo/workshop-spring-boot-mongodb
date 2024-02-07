package com.imav.whatsapp.entity;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class ImageDb {

	@Id
	private String id;

	private String idWamid;

	private String caption = "";

	@Lob
	@Column(length = 20971520)
	private byte[] content;

	private String mimeType;

	private String phone;

	private String timestamp;

	private String type;

	public ImageDb() {

	}

	public ImageDb(String id, String idWamid, String caption, byte[] content, String mimeType, String phone,
			String timestamp, String type) {
		this.id = id;
		this.idWamid = idWamid;
		this.caption = caption;
		this.content = content;
		this.mimeType = mimeType;
		this.phone = phone;
		this.timestamp = timestamp;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdWamid() {
		return idWamid;
	}

	public void setIdWamid(String idWamid) {
		this.idWamid = idWamid;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		ImageDb other = (ImageDb) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ImageDb [id=" + id + ", idWamid=" + idWamid + ", caption=" + caption + ", content="
				+ Arrays.toString(content) + ", mimeType=" + mimeType + ", phone=" + phone + ", timestamp=" + timestamp
				+ ", type=" + type + "]";
	}

}