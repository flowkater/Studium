package com.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import studium.sactivity.groupindex.studiummainsplash.activity.MeetingCreateActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.MeetingShowActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.google.gson.Gson;
import com.studium.adapter.CalendarAdapter;
import com.studium.model.DayInfo;
import com.studium.model.Party;
import com.studium.model.Partys;
import com.studium.model.Post;
import com.utils.Global;
import com.utils.NetHelper;

public class StepFragment extends SherlockFragment implements
		OnItemClickListener, OnClickListener {
	public int index = 0;

	private Boolean useruser = false;
	public String delims = "-";

	// it is setting of meeting day
	private int WhenDoyougoToStudy[] = new int[8];

	public static int PARTY_MEMBER = 0;
	public static int PARTYING_TIME = 1;
	public static int LOCATION = 2;
	public static int TIME = 3;
	public static int TODOLIST = 4;
	public static int COMMENT_COUNT = 5;
	public static int DATE = 6;

	private View mView;
	boolean mMeeting;

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

	private SharedPreferences mPreferences;
	private String auth_token;
	private String group_id;
	private String role;
	private String mResult;

	private ArrayList<Party> mParties = new ArrayList<Party>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new GetPartyList().execute();
		mPreferences = getActivity().getSharedPreferences("CurrentUser",
				getActivity().MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		Intent in = getActivity().getIntent();
		group_id = in.getExtras().getString("group_id");
		role = in.getExtras().getString("role");
		mView = inflater.inflate(R.layout.step_calender, container, false);
		// LayoutInflater lf = (LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// it is setting of meeting day
		// WhenDoyougoToStudy[MONDAY] = 1;
		// WhenDoyougoToStudy[WEDNSESDAY] = 1;
		// WhenDoyougoToStudy[FRIDAY] = 1;

		Button bLastMonth = (Button) mView
				.findViewById(R.id.gv_calendar_activity_b_last);
		Button bNextMonth = (Button) mView
				.findViewById(R.id.gv_calendar_activity_b_next);
		Button create_meeting = (Button) mView.findViewById(R.id.add_meeting);

		if (role.equals(Global.founder))
			create_meeting.setVisibility(View.VISIBLE);
		else
			create_meeting.setVisibility(View.INVISIBLE);

		mTvCalendarTitle = (TextView) mView
				.findViewById(R.id.gv_calendar_activity_tv_title);
		mGvCalendar = (GridView) mView
				.findViewById(R.id.gv_calendar_activity_gv_calendar);

		create_meeting.setOnClickListener(this);
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

		return mView;
	}

	private int date_to_int(String year, String month, String date) {
		return Integer.parseInt(year) * 10000 + Integer.parseInt(month) * 100
				+ Integer.parseInt(date);

	}

	private int date_to_int(String[] temp) {
		return Integer.parseInt(temp[0]) * 10000 + Integer.parseInt(temp[1])
				* 100 + Integer.parseInt(temp[2]);

	}

	private void getCalendar(Calendar calendar, ArrayList<Party> mParties) {
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		this.mParties = mParties;

		// party compre setting
		int[] party_int_date = new int[mParties.size()];
		for (int i = 0; i < party_int_date.length; i++) {

			String[] temp = mParties.get(i).getDate().split(delims);
			party_int_date[i] = date_to_int(temp);
		}

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

		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

		DayInfo day;

		Log.e("DayOfMOnth", dayOfMonth + "");
		System.out.println("last create");

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

			// party date compare
			if (mParties.size() == 0) {

			} else {
				if (party_int_date[index] > date_to_int(day.getYear(),
						day.getMonth(), day.getDate())) {
				} else if (party_int_date[index] == date_to_int(day.getYear(),
						day.getMonth(), day.getDate())) {
					day.setParty(true);
					day.setParty_id(mParties.get(index).getId());
					day.setIndex(index);
					if (party_int_date.length - 1 == index) {

					} else
						index++;
				}
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
				System.out.println("last create update");

			} else
				day.setBg_color(Color.WHITE);

			mDayList.add(day);
		}
		System.out.println("this create");

		/* user party parsing */
		// userParty[0] = party.getDate();

		// String[] userPartydates = userParty[0].split(delims);

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

			// compare
			if (mParties.size() == 0) {

			} else {
				if (party_int_date[index] > date_to_int(day.getYear(),
						day.getMonth(), day.getDate()))
					Toast.makeText(getActivity(), "  " + day.getDate(),
							Toast.LENGTH_SHORT);

				else if (party_int_date[index] == date_to_int(day.getYear(),
						day.getMonth(), day.getDate())) {
					day.setParty(true);
					day.setParty_id(mParties.get(index).getId());
					day.setIndex(index);
					if (party_int_date.length - 1 == index) {

					} else
						index++;
				}
			}
			int WhatisthedayToday = (day.getDay() + lastmonthdays % 7) % 7;

			// if (WhenDoyougoToStudy[WhatisthedayToday] == 1) {
			// day.setParty(true);
			// }

			// user party check
			// if (Integer.parseInt(userPartydates[2]) == Integer.parseInt(day
			// .getDate())
			// && Integer.parseInt(userPartydates[1]) == Integer
			// .parseInt(day.getMonth())
			// && Integer.parseInt(userPartydates[0]) == Integer
			// .parseInt(day.getYear())) {
			// day.setParty(true);
			// useruser = true;
			//
			// }
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

			// compare
			if (mParties.size() == 0) {

			} else {
				if (party_int_date[index] > date_to_int(day.getYear(),
						day.getMonth(), day.getDate()))
					Toast.makeText(getActivity(),
							":" + day.getDate(),
							Toast.LENGTH_SHORT);

				else if (party_int_date[index] == date_to_int(day.getYear(),
						day.getMonth(), day.getDate())) {
					day.setParty(true);
					day.setParty_id(mParties.get(index).getId());
					day.setIndex(index);
					if (party_int_date.length - 1 == index) {

					} else
						index++;
				}
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
		int temp_fuck = Integer.parseInt(daycl.getYear()) * 10000
				+ Integer.parseInt(daycl.getMonth()) * 100
				+ Integer.parseInt(daycl.getDate());
		Calendar calender = Calendar.getInstance();
		int now_fuck = calender.get(Calendar.YEAR) * 10000
				+ (calender.get(Calendar.MONTH) + 1) * 100
				+ calender.get(Calendar.DATE);

		if (temp_fuck < now_fuck) {

		} else {
			Intent intent;
			Bundle extras = new Bundle();
			extras.putString("date", daycl.getDate());
			extras.putString("month", daycl.getMonth());
			extras.putString("year", daycl.getYear());
			extras.putBoolean("isParty", daycl.isParty());
			extras.putBoolean("making", mMeeting);
			extras.putString("group_id", group_id);
			extras.putString("auth_token", auth_token);
			extras.putInt("index", daycl.getIndex());
			extras.putString("party_id", daycl.getParty_id());

			// if (daycl.isParty() == true && useruser == true)
			// extras.putStringArray("party", partystring);

			if (mMeeting) {
				intent = new Intent(getActivity(), MeetingCreateActivity.class);
				intent.putExtras(extras);
				startActivity(intent);

			} else if (daycl.isParty()) {
				intent = new Intent(getActivity(), MeetingShowActivity.class);
				intent.putExtra("party", mParties);
				intent.putExtras(extras);
				startActivity(intent);

			}
		}

	}

	@Override
	public void onClick(View v) {
		if (R.id.gv_calendar_activity_b_last == v.getId()) {
			mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar, mParties);
		}
		if (R.id.gv_calendar_activity_b_next == v.getId()) {
			mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar, mParties);
		}
		if (R.id.add_meeting == v.getId()) {
			Toast.makeText(getActivity().getApplicationContext(),
					"모임하려면 원하는 날짜를 눌러주세요", Toast.LENGTH_SHORT).show();
			mMeeting = true;

		}
	}

	private void initCalendarAdapter() {
		mCalendarAdapter = new CalendarAdapter(getActivity(), R.layout.day,
				mDayList);
		mGvCalendar.setAdapter(mCalendarAdapter);
	}

	private class GetPartyList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/"
					+ group_id + "/parties.json?auth_token=" + auth_token);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Partys partys = gson.fromJson(mResult, Partys.class);
			try {
				for (Party party : partys.getPartys()) {
					System.out.println(party.getPlace());
					System.out.println(party.getUsers().get(0).getName());
					System.out.println(party.getTodolists().get(0).getList());
					mParties.add(party);
				}
				getCalendar(mThisMonthCalendar, mParties);
			} catch(Exception e){
				Toast.makeText(getActivity(), "인터넷 연결상태가 좋지 않습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_LONG).show();
				getActivity().finish();
			}
			
			// UI Thread
			
			super.onPostExecute(result);
		}
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
