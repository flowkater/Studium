package com.activity;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.activity.R;
import com.adapter.CheckTodoListAdapter;
import com.adapter.ImageAdapter;
import com.model.CheckString;
import com.model.ThumbImageInfo;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

public class MeetingCreateActivity extends SherlockActivity implements OnClickListener {
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
	private TextView titlebar_text;

	private boolean party;
	private boolean mMeeting;
	boolean[] backup_attand = new boolean[members];
	boolean[] backup_listnum = new boolean[listnum];

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
		String partystring[] = in.getStringArrayExtra("party");
		role = in.getStringExtra("role");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Post#Show");
		

		if (mMeeting) {

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final LinearLayout header = (LinearLayout) inflater.inflate(
					R.layout.insert_meeting_info_page_header, null);

			final LayoutInflater lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 LinearLayout linear = (LinearLayout) lf.inflate(
					R.layout.insert_meeting_info_page_low, null);
			header.addView(linear);
			
			final ScrollView scroll = (ScrollView) inflater.inflate(
					R.layout.insert_meeting_info_page_scroll, null);
			
			scroll.addView(header);
			
			
			Button add_todolist = (Button) header.findViewById(R.id.plus_todolist);	
			add_todolist.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 LinearLayout linear = (LinearLayout) lf.inflate(
							R.layout.insert_meeting_info_page_low, null);
				

					header.addView(linear);
					scroll.removeAllViewsInLayout();
					scroll.addView(header);

					setContentView(scroll);
					
					
				}
			});
		
			setContentView(scroll);
			

		}

		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("search")
				.setIcon(R.drawable.title_btn_geo)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, CommentPageActivity.class);
		//intent.putExtra("post_id", post_id);
		startActivity(intent);
		return true;
	}

	

}
