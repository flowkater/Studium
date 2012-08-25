package com.model;

import android.graphics.Bitmap;

public class Partymessage {
	private String group_name;
	private String group_goal;
	private String group_location;
	private String body;
	private Bitmap group_img;
	
	// holder.group_intro_img = (ImageView) convertView
	// .findViewById(R.id.group_intro_img);
	public Partymessage() {
		// TODO Auto-generated constructor stub
	}

	public Bitmap getGroup_img() {
		return group_img;
	}

	public void setGroup_img(Bitmap group_img) {
		this.group_img = group_img;
	}

	public Partymessage(Bitmap group_img,String group_name, String group_goal, String group_location, String body) {
		this.group_img = group_img;
		this.group_name = group_name;
		this.group_goal = group_goal;
		this.group_location = group_location;
		this.body = body;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}	

	public String getGroup_goal() {
		return group_goal;
	}

	public void setGroup_goal(String group_goal) {
		this.group_goal = group_goal;
	}

	public String getGroup_location() {
		return group_location;
	}

	public void setGroup_location(String group_location) {
		this.group_location = group_location;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
