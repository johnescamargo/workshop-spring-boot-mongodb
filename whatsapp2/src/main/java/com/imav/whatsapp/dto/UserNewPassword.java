package com.imav.whatsapp.dto;

public class UserNewPassword {

	private String username;

	private String password;

	private String matchingPassword;

	public UserNewPassword(String username, String email, String password, String matchingPassword) {
		this.username = username;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public UserNewPassword() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

}