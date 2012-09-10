package com.activity;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.activity.R;
import com.adapter.CheckTodoListAdapter;
import com.adapter.ImageAdapter;
import com.model.CheckString;
import com.model.Party;
import com.model.ThumbImageInfo;
import com.model.Todolist;
import com.model.User;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MeetingShowActivity extends SherlockActivity implements
		OnItemClickListener, OnClickListener {
	public static int PARTY_MEMBER = 0;
	public static int PARTYING_TIME = 1;
	public static int LOCATION = 2;
	public static int TIME = 3;
	public static int TODOLIST = 4;
	public static int COMMENT_COUNT = 5;
	public static int DATE = 6;
	public static String delims = "-";
	int count = 0;
	public static int members = 4;
	public static int listnum = 3;

	CheckTodoListAdapter mAdapter;

	ImageAdapter mListAdapter;

	ArrayList<CheckString> todolist_list = new ArrayList<CheckString>();
	CheckString check;
	ProgressBar achivebar;
	ProgressBar attendance_bar;
	int achive_rate;
	int Num_todoList;
	int checked = 0;
	TextView achive_rate_tv;
	private ArrayList<ThumbImageInfo> mThumbImageInfoList;
	private GridView mGvImageList;
	private String role;
	private String date;
	private String month;
	private String year;
	private boolean party;
	private boolean mMeeting;
	boolean[] backup_attand = new boolean[members];
	boolean[] backup_listnum = new boolean[listnum];
	private ArrayList<Party> mParties = new ArrayList<Party>();
	private int index;
	private TextView titlebar_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 이전 액티비티로부터 넘어온 데이터를 꺼낸다.
		Intent in = getIntent();
		date = in.getStringExtra("date");
		month = in.getStringExtra("month");
		year = in.getStringExtra("year");
		party = in.getBooleanExtra("isParty", false);
		mMeeting = in.getBooleanExtra("making", false);
		role = in.getStringExtra("role");
		index = in.getIntExtra("index", 0);
		mParties = (ArrayList<Party>) in.getSerializableExtra("party");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("모임 만들기");

		if (party) {
			setContentView(R.layout.meeting_info_page);
			ListView meeting = (ListView) findViewById(R.id.to_do_list_list);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View header = (View) inflater.inflate(
					R.layout.meeting_info_page_header, null);
			final RelativeLayout scr = (RelativeLayout) header
					.findViewById(R.id.setting_personal_layout);
			attendance_bar = (ProgressBar) header
					.findViewById(R.id.attend_progress_bar);
			attandance_tv = (TextView) header
					.findViewById(R.id.attend_rate_text);

			final ArrayList<Todolist> array_todo = mParties.get(index)
					.getTodolists();

			CheckString[] check_input = new CheckString[array_todo.size()];

			System.out.println(Num_todoList);

			for (int i = 0; i < array_todo.size(); i++) {
				String string = array_todo.get(i).getList();
				check_input[i] = new CheckString();
				check_input[i].setString(string);
				todolist_list.add(check_input[i]);
			}

			ListView lv = (ListView) findViewById(R.id.to_do_list_list);
			// if (role.equals(Global.founder)) {
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			// }
			// thum array

			mThumbImageInfoList = new ArrayList<ThumbImageInfo>();

			// thumb start
			for (int i = 0; i < mParties.get(index).getUsers().size(); i++) {
				User temp = mParties.get(index).getUsers().get(i);
				ThumbImageInfo temp_thumb = new ThumbImageInfo();
				temp_thumb.setId(temp.getName());
				temp_thumb.setThum_img(temp.getImage());
				mThumbImageInfoList.add(temp_thumb);
			}
			System.out.println(mThumbImageInfoList.get(0).getId());

			// thum end

			mGvImageList = (GridView) header
					.findViewById(R.id.meeting_entry_grid_view);
			mGvImageList.setOnItemClickListener(this);
			mListAdapter = new ImageAdapter(MeetingShowActivity.this,
					R.layout.image_cell, mThumbImageInfoList);

			mGvImageList.setAdapter(mListAdapter);

			mGvImageList.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					scr.requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});

			mAdapter = new CheckTodoListAdapter(this,
					R.layout.meeting_info_list_row, todolist_list);

			// define
			TextView meeting_location_input_text = (TextView) header
					.findViewById(R.id.meeting_location_input_text);
			meeting_location_input_text.setText(mParties.get(index).getPlace());

			TextView meeting_time_input_text = (TextView) header
					.findViewById(R.id.meeting_time_input_text);

			String[] temp = mParties.get(index).getDate().split(delims);
			String[] temp1 = mParties.get(index).getTime().split(delims);
			if (Integer.parseInt(temp1[0]) > 12)
				temp1[0] = " 오후 " + (Integer.parseInt(temp1[0]) - 12);
			else
				temp1[0] = " 오전 " + temp1[0];

			String time_show = temp[0] + "년 " + temp[1] + "월 " + temp[2] + "일"
					+ temp1[0] + "시 " + temp1[1] + "분";

			meeting_time_input_text.setText(time_show);

			achive_rate_tv = (TextView) header
					.findViewById(R.id.achievement_rate_text);

			achivebar = (ProgressBar) header
					.findViewById(R.id.achievement_progress_bar);

			meeting.addHeaderView(header);
			meeting.setAdapter(mAdapter);

			meeting.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long item) {
					if (position == 0) {
						System.out.println("whatido");
					} else {
						System.out.println("what should I do");

						check = todolist_list.get(position - 1);
						CheckBox tv = (CheckBox) arg1
								.findViewById(R.id.todolist_checkbox);
						if (check.isCheck() == true) {
							tv.setChecked(false);
							checked--;
							check.setCheck(false);
							tv.setPaintFlags(Paint.ANTI_ALIAS_FLAG
									| Paint.DEV_KERN_TEXT_FLAG);
							backup_listnum[position - 1] = false;
						} else {
							tv.setChecked(true);
							System.out.println("fuck " + checked);
							checked++;
							System.out.println("fuck " + checked);
							backup_listnum[position - 1] = true;

							check.setCheck(true);
							tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

						}
						System.out.println("fuck " + checked);

						achive_rate = checked * 100 / array_todo.size();
						System.out.println("fuck       " + achive_rate);

						achive_rate_tv.setText("목표 달성률 : " + achive_rate + "%");

						achivebar.setMax(100);
						achivebar.setProgress(achive_rate);
						System.out.println("fuck       " + achive_rate);
					}
				}
			});
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		} else
			alert(date, month, year);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("저장")
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		if (item.getTitle().equals("저장")) {
			finish();
			return true;
		}
		return false;
	}

	/**
	 * 자체적으로 만든 알럿다이얼로그 생성 메소드
	 * 
	 * @param title
	 *            타이틀
	 * @param message
	 *            메시지
	 */
	private void alert(final String date, final String month, final String year) {
		// 체인형으로 메소드를 사용한다.
		new AlertDialog.Builder(this)
		// 색상을 타이틀에 세팅한다.
				.setTitle("일정")
				// 설명을 메시지 부분에 세팅한다.
				.setMessage(year + "년 " + month + "월 " + date + "일")
				// 취소를 못하도록 막는다.
				.setCancelable(false)
				// 확인 버튼을 만든다.

				.setPositiveButton("모임", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Bundle bundle = new Bundle();
						bundle.putString("color", Color.YELLOW + "");
						bundle.putString("date", date);
						bundle.putString("month", month);
						bundle.putString("year", year);
						Intent intent = new Intent(getApplicationContext(),
								GroupShowActivity.class);

						intent.putExtras(bundle);

						// 확인버튼이 클릭되면 다이얼로그를 종료한다.
						dialog.dismiss();
						// 액티비티를 종료한다.
						finish();
						startActivity(intent);
					}
				}).show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}

	ThumbImageInfo thumb;
	int attandance_check = 0;
	TextView attandance_tv;

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long arg3) {
		CheckBox cb = (CheckBox) v.findViewById(R.id.chkImage);
		thumb = mThumbImageInfoList.get(position);
		if (thumb.getCheckedState() == true) {
			thumb.setCheckedState(false);
			backup_attand[position] = false;

			attandance_check--;
			cb.setChecked(false);

		} else {
			thumb.setCheckedState(true);
			backup_attand[position] = true;
			cb.setChecked(true);
			attandance_check++;

		}

		int attendance_rate = attandance_check * 100
				/ mParties.get(index).getUsers().size();

		attendance_bar.setMax(100);
		attendance_bar.setProgress(attendance_rate);

		attandance_tv.setText("출석률 : " + attendance_rate + "%");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("doTake", "onSaveInstanceState");

		outState.putBooleanArray("attand", backup_attand);
		outState.putBooleanArray("list", backup_listnum);

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("doTake", "onRestoreInstanceState");

		boolean[] temp1 = savedInstanceState.getBooleanArray("attand");
		boolean[] temp2 = savedInstanceState.getBooleanArray("list");

		for (int i = 0; i < listnum; i++)
			todolist_list.get(i).setCheck(temp2[i]);
		for (int i = 0; i < members; i++)
			mThumbImageInfoList.get(i).setCheckedState(temp1[i]);

		super.onRestoreInstanceState(savedInstanceState);
	}

}
