package com.studium.model;

public class CheckString {
	private String string;
	private boolean check;
	
	public CheckString()
	{
	
	}

	public CheckString (String string, boolean check)
	{
		this.string = string;
		this.check = check;
	}
	
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	

}
