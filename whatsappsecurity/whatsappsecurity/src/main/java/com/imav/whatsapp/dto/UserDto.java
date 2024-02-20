package com.imav.whatsapp.dto;

public class UserDto {

	private String username;

	private String email;

	private String password;

	private String matchingPassword;

	private String role;

	public UserDto(String username, String email, String password, String matchingPassword, String role) {

		this.username = username;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
		this.role = role;
	}

	public UserDto() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}