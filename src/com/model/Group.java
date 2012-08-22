package com.model;

public class Group {
	private String name;
	private String goal;
	
	public Group() {
		// TODO Auto-generated constructor stub
	}
	
	public Group(String name, String goal){
		this.name = name;
		this.goal = goal;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoal() {
//		if (goal.length()>10) {
//			
//		}
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
}
