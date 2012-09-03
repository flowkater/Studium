package com.model;

/**
 * �Ϸ��� ��¥������ �����ϴ� Ŭ����
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
	 * ��¥�� ��ȯ�Ѵ�.
	 * 
	 * @return day ��¥
	 */
	/**
	 * ��¥�� �����Ѵ�.
	 * 
	 * @param day
	 *            ��¥
	 */

	/**
	 * �̹����� ��¥���� ������ ��ȯ�Ѵ�.
	 * 
	 * @return inMonth(true/false)
	 */
	public boolean isInMonth() {
		return inMonth;
	}

	/**
	 * �̹����� ��¥���� ������ �����Ѵ�.
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