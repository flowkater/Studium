package com.model;

import java.io.Serializable;

public class Todolist implements Serializable{
	private String list;

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}
}
