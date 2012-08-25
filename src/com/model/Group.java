package com.model;

import android.graphics.Bitmap;

public class Group {
	private Bitmap group_img;
	private String name;
	private String goal;
	private String people_count;
	private String feed_count;
	private String location;

	public Group() {
	}

	public Group(String name, String goal) {
		this.name = name;
		this.goal = goal;
	}
	
	public Group(Bitmap group_img,String name, String goal, String people_count, String feed_count, String location) {
		this.group_img = group_img;
		this.name = name;
		this.goal = goal;
		this.people_count = people_count;
		this.feed_count = feed_count;
		this.location = location;
	}

	public String getPeople_count() {
		return people_count + "Έν";
	}

	public void setPeople_count(String people_count) {
		this.people_count = people_count;
	}

	public String getFeed_count() {
		return feed_count;
	}

	public void setFeed_count(String feed_count) {
		this.feed_count = feed_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Bitmap getGroup_img() {
		return group_img;
	}

	public void setGroup_img(Bitmap group_img) {
		this.group_img = group_img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoal() {
		// if (goal.length()>10) {
		//
		// }
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

}
