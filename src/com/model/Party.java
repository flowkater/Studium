package com.model;


public class Party {
	private String member_name;
	private String Partying_time;
	private String location;
	private String time;
	private String todolist;
	private String comment_count;
	private String date;

	public Party() {
		// TODO Auto-generated constructor stub
	}

	
	public Party(String member_name, String Partying_time, String location, String time, String todolist, String comment_count, String date) {
		this.comment_count = comment_count;
		this.location = location;
		this.todolist = todolist;
		this.member_name = member_name;
		this.Partying_time = Partying_time;
		this.time = time;
		this.date=date;

	}

	

	

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getPartying_time() {
		return Partying_time;
	}

	public void setPartying_time(String Partying_time) {
		this.Partying_time = Partying_time;
	}


	public String getComment_count() {
		return "µ¡±Û¼ö " + comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getTodolist() {
		return todolist;
	}


	public void setTodolist(String todolist) {
		this.todolist = todolist;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}

}
