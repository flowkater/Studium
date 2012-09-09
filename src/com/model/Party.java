package com.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Party implements Serializable{
	private String place;
	private String time;
	private String date;
	private ArrayList<User> users;
	private ArrayList<Todolist> todolists;
	
	public Party (String place, String time, String date, ArrayList<User> users, ArrayList<Todolist> todolists)
	{
		this.place=place;
		this.time=time;
		this.date=date;
		this.users=users;
		this.todolists=todolists;
	}
	
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	public ArrayList<Todolist> getTodolists() {
		return todolists;
	}
	public void setTodolists(ArrayList<Todolist> todolists) {
		this.todolists = todolists;
	}
}
