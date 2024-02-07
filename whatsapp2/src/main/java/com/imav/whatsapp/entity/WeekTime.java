package com.imav.whatsapp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WeekTime implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int day;
	private String name;
	private String dayInit;
	private String dayEnd;
	private boolean dayOff;
	
	public WeekTime() {

	}

	public WeekTime(int day, String dayInit, String dayEnd, boolean dayOff, String name) {
		this.day = day;
		this.dayInit = dayInit;
		this.dayEnd = dayEnd;
		this.dayOff = dayOff;
		this.name = name;
	}


	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDayInit() {
		return dayInit;
	}

	public void setDayInit(String dayInit) {
		this.dayInit = dayInit;
	}

	public String getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
	}

	public boolean isDayOff() {
		return dayOff;
	}

	public void setDayOff(boolean dayOff) {
		this.dayOff = dayOff;
	}

	@Override
	public int hashCode() {
		return Objects.hash(day);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeekTime other = (WeekTime) obj;
		return day == other.day;
	}

	@Override
	public String toString() {
		return "WeekTime [day=" + day + ", name=" + name + ", dayInit=" + dayInit + ", dayEnd=" + dayEnd
				+ ", dayOff=" + dayOff + "]";
	}

}
