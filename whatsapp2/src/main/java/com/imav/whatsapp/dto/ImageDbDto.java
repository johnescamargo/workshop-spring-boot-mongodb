package com.imav.whatsapp.dto;

import java.util.Arrays;

public class ImageDbDto {

	private byte[] content;
	private String phone;
	private String timestamp;
	private String caption;
	private String mimeType;

	public ImageDbDto() {

	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
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

	@Override
	public String toString() {
		return "ImageDbDto [content=" + Arrays.toString(content) + ", phone=" + phone + ", timestamp=" + timestamp
				+ ", caption=" + caption + ", mimeType=" + mimeType + "]";
	}

}
