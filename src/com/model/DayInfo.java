package com.model;

/**
 * 하루의 날짜정보를 저장하는 클래스
 * 
 * @author croute
 * @since 2011.03.08
 */
public class DayInfo {
	private String date;
	private int day;
	private boolean inMonth;
	private String month;
	private String year;
	private int bg_color;
	private int Post_num;
	private boolean party;

	/**
	 * 날짜를 반환한다.
	 * 
	 * @return day 날짜
	 */
	/**
	 * 날짜를 저장한다.
	 * 
	 * @param day
	 *            날짜
	 */

	/**
	 * 이번달의 날짜인지 정보를 반환한다.
	 * 
	 * @return inMonth(true/false)
	 */
	public boolean isInMonth() {
		return inMonth;
	}

	/**
	 * 이번달의 날짜인지 정보를 저장한다.
	 * 
	 * @param inMonth
	 *            (true/false)
	 */
	public void setInMonth(boolean inMonth) {
		this.inMonth = inMonth;
	}
	
	

	public DayInfo() {
		super();
		setPost_num(0);
	}

	public DayInfo(String month) {
		super();
		this.month = month;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getBg_color() {
		return bg_color;
	}

	public void setBg_color(int bg_color) {
		this.bg_color = bg_color;
	}



	public int getPost_num() {
		return Post_num;
	}

	public void setPost_num(int post_num) {
		this.Post_num = post_num;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public boolean isParty() {
		return party;
	}

	public void setParty(boolean party) {
		this.party = party;
	}

}