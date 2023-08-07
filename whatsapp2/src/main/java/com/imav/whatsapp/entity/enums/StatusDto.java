package com.imav.whatsapp.entity.enums;

public enum StatusDto {
	
	NONE(0), SENT(1), DELIVERED(2), READ(3);

	private int code;

	private StatusDto(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static StatusDto valueOf(int code) {

		for (StatusDto value : StatusDto.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid OrderStatus code");
	}


}
