package com.model;

import java.io.Serializable;

public class User implements Serializable{
	private String image;
	private String name;
	private String role;
	private String email;
	private String phone;
	private String gender;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
