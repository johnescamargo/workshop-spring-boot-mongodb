package com.imav.whatsapp.dto;

import java.util.Objects;

public class InteractiveDto {

	private String id;
	private String type;
	private String title;

	public InteractiveDto(String id, String type, String title) {
		this.id = id;
		this.type = type;
		this.title = title;

	}

	public InteractiveDto() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		InteractiveDto other = (InteractiveDto) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "InteractiveDTO [id=" + id + ", type=" + type + ", title=" + title + "]";
	}

}
