package com.model;

import android.graphics.Bitmap;

public class Post {
	private Bitmap member_img;
	private String body;
	private String member_name;
	private String posting_time;
	private Bitmap content_img;
	private String comment_count;

	public Post() {
		// TODO Auto-generated constructor stub
	}

	public Post(String body) {
		this.body = body;
	}

	public Post(Bitmap member_img, String body, String member_name,
			Bitmap content_img, String comment_count) {
		this.member_img = member_img;
		this.body = body;
		this.member_name = member_name;
		this.content_img = content_img;
		this.comment_count = comment_count;
	}
	
	public Post(Bitmap member_img, String body, String member_name,
			String comment_count) {
		this.member_img = member_img;
		this.body = body;
		this.member_name = member_name;
		this.comment_count = comment_count;
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

	public String getPosting_time() {
		return posting_time;
	}

	public void setPosting_time(String posting_time) {
		this.posting_time = posting_time;
	}

	public Bitmap getContent_img() {
		return content_img;
	}

	public void setContent_img(Bitmap content_img) {
		this.content_img = content_img;
	}

	public String getComment_count() {
		return "µ¡±Û¼ö " +comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
