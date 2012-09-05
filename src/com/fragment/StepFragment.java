package com.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import me.croute.calendarexample.activity.calendardialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.R;
import com.adapter.CalendarAdapter;
import com.model.DayInfo;
import com.model.Party;
import com.model.Post;

public class StepFragment extends SherlockFragment implements
		OnItemClickListener, OnClickListener {

	public Party party = new Party("우현-" + "병철-" + "재우", "2300",
			"광화문 올레스퀘어 2층", "1100", "서버구축-우현이 패기-똥싸기", "0", "2012-9-1");
	private String userParty[] = new String[10];
	private Boolean useruser = false;
	private String partystring[] = { party.getMember_name(),
			party.getPartying_time(), party.getLocation(), party.getTime(),
			party.getTodolist(), party.getComment_count(), party.getDate() };

	private int WhenDoyougoToStudy[] = new int[8];
	public static int PARTY_MEMBER = 0;
	public static int PARTYING_TIME = 1;
	public static int LOCATION = 2;
	public static int TIME = 3;
	public static int TODOLIST = 4;
	public static int COMMENT_COUNT = 5;
	public static int DATE = 6;

	private View mView;
	public static int SUNDAY = 1;
	public static int MONDAY = 2;
	public static int TUESDAY = 3;
	public static int WEDNSESDAY = 4;
	public static int THURSDAY = 5;
	public static int FRIDAY = 6;
	public static int SATURDAY = 7;

	private TextView mTvCalendarTitle;
	private GridView mGvCalendar;

	private ArrayList<DayInfo> mDayList = new ArrayList<DayInfo>();
	private CalendarAdapter mCalendarAdapter;

	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;

	int update_color;
	DayInfo update;
	int isUpdate;

	String[][] temp;

	ListView mPostListView;
	ArrayList<Post> mPostList = new ArrayList<Post>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.step_calender, container, false);
		WhenDoyougoToStudy[MONDAY] = 1;
		WhenDoyougoToStudy[WEDNSESDAY] = 1;
		WhenDoyougoToStudy[FRIDAY] = 1;
		

		Button bLastMonth = (Button) mView
				.findViewById(R.id.gv_calendar_activity_b_last);
		Button bNextMonth = (Button) mView
				.findViewById(R.id.gv_calendar_activity_b_next);

		mTvCalendarTitle = (TextView) mView
				.findViewById(R.id.gv_calendar_activity_tv_title);
		mGvCalendar = (GridView) mView
				.findViewById(R.id.gv_calendar_activity_gv_calendar);

		bLastMonth.setOnClickListener(this);
		bNextMonth.setOnClickListener(this);
		mGvCalendar.setOnItemClickListener(this);

		if (getActivity().getIntent().getStringExtra("color") != null) {
			update_color = Integer.parseInt(getActivity().getIntent()
					.getStringExtra("color"));
			isUpdate = 1;
		}

		// callender

		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

		// mPostList.add(object);

		getCalendar(mThisMonthCalendar);
		return mView;
	}

	private void getCalendar(Calendar calendar) {
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		boolean a = mDayList.isEmpty();
		if (mDayList.isEmpty() != true)
			mDayList.clear();

		int[] start_calender = new int[3];

		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);

		if (dayOfMonth == SUNDAY) {
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth - 1) - 1;

		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "��"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "��");

		DayInfo day;

		Log.e("DayOfMOnth", dayOfMonth + "");
		System.out.println("last create");

		int index = 0;

		start_calender[0] = mThisMonthCalendar.get(Calendar.YEAR);
		start_calender[1] = mThisMonthCalendar.get(Calendar.MONTH);
		start_calender[2] = lastMonthStartDay;

		int lastmonthdays = 0;

		for (int i = 0; i < dayOfMonth - 1; i++) {

			lastmonthdays++;
			int date = lastMonthStartDay + i;

			day = new DayInfo();
			day.setDate(Integer.toString(date));

			int showday = i;

			if (i > 6) {
				showday = (i % 7);
			}
			day.setDay(showday);

			day.setInMonth(false);
			day.setMonth(mThisMonthCalendar.get(Calendar.MONTH) + "");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR) + "");

			if (isUpdate == 1
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("date")) == Integer.parseInt(day
							.getDate())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("month")) == Integer.parseInt(day
							.getMonth())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("year")) == Integer.parseInt(day
							.getYear())) {
				day.setBg_color(update_color);
				System.out.println("last create update");

			} else
				day.setBg_color(Color.WHITE);

			mDayList.add(day);
		}
		System.out.println("this create");

		/* user party parsing */
		userParty[0] = party.getDate();
		String delims = "-";
		String[] userPartydates = userParty[0].split(delims);

		for (int i = 1; i <= thisMonthLastDay; i++) {

			day = new DayInfo();
			day.setDate(Integer.toString(i));
			int showday = i;

			if (i > 6) {
				showday = (i % 7);
			}
			day.setDay(showday);

			day.setInMonth(true);
			day.setMonth((mThisMonthCalendar.get(Calendar.MONTH) + 1) + "");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR) + "");

			int WhatisthedayToday = (day.getDay() + lastmonthdays % 7) % 7;

			if (WhenDoyougoToStudy[WhatisthedayToday] == 1) {
				day.setParty(true);
			}

			// user party check
			if (Integer.parseInt(userPartydates[2]) == Integer.parseInt(day
					.getDate())
					&& Integer.parseInt(userPartydates[1]) == Integer
							.parseInt(day.getMonth())
					&& Integer.parseInt(userPartydates[0]) == Integer
							.parseInt(day.getYear())) {
				day.setParty(true);
				useruser = true;

			}
			if (isUpdate == 1
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("date")) == Integer.parseInt(day
							.getDate())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("month")) == Integer.parseInt(day
							.getMonth())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("year")) == Integer.parseInt(day
							.getYear())) {
				day.setBg_color(update_color);
				System.out.println("this create update");

			} else
				day.setBg_color(Color.WHITE);

			mDayList.add(day);
		}
		System.out.println("next create");

		for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
			day = new DayInfo();
			day.setDate(Integer.toString(i));

			int showday = i;

			if (i > 6) {
				showday = (i % 7);
			}
			day.setDay(showday);
			day.setInMonth(false);
			day.setMonth((mThisMonthCalendar.get(Calendar.MONTH) + 2) + "");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR) + "");

			if (isUpdate == 1
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("date")) == Integer.parseInt(day
							.getDate())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("month")) == Integer.parseInt(day
							.getMonth())
					&& Integer.parseInt(getActivity().getIntent()
							.getStringExtra("year")) == Integer.parseInt(day
							.getYear())) {
				day.setBg_color(update_color);
				System.out.println("next create update");

			} else
				day.setBg_color(Color.WHITE);

			mDayList.add(day);
		}

		initCalendarAdapter();

	}

	private Calendar getLastMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, -1);
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "Y"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "M");
		return calendar;
	}

	/**
	 * 
	 * @param calendar
	 * @return NextMonthCalendar
	 */
	private Calendar getNextMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, +1);
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "Y"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "M");
		return calendar;
	}

	DayInfo daycl;

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long arg3) {

		daycl = mDayList.get(position);

		Bundle extras = new Bundle();
		extras.putString("date", daycl.getDate());
		extras.putString("month", daycl.getMonth());
		extras.putString("year", daycl.getYear());
		extras.putBoolean("isParty", daycl.isParty());
		if (daycl.isParty() == true && useruser == true)
			extras.putStringArray("party", partystring);

		Intent intent = new Intent(getActivity(), calendardialog.class);
		intent.putExtras(extras);

		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		if (R.id.gv_calendar_activity_b_last == v.getId()) {
			mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
		}
		if (R.id.gv_calendar_activity_b_next == v.getId()) {
			mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
		}
	}

	private void initCalendarAdapter() {
		mCalendarAdapter = new CalendarAdapter(getActivity(), R.layout.day,
				mDayList);
		mGvCalendar.setAdapter(mCalendarAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
