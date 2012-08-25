package com.model;

import android.graphics.Bitmap;

public class Comment {
	private Bitmap member_img;
	private String body;
	private String member_name;
	private String comment_time;

	public Comment() {

	}

	public Comment(Bitmap member_img, String body, String member_name) {
		this.member_img = member_img;
		this.body = body;
		this.member_name = member_name;
	}

	public Bitmap getMember_img() {
		return member_img;
	}

	public void setMember_img(Bitmap member_img) {
		this.member_img = member_img;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public Comment(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
