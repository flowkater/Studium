package com.activity;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adapter.PostCommentAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Comment;
import com.model.Comments;
import com.utils.Global;
import com.utils.ImageDownloader;
import com.utils.NetHelper;

import com.activity.R;
import com.adapter.CheckTodoListAdapter;
import com.adapter.ImageAdapter;
import com.fragment.StepFragment;
import com.model.CheckString;
import com.model.ThumbImageInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MeetingCreateActivity extends SherlockActivity implements
		OnClickListener {
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
	private int hours;
	private int minute;
	private boolean afternoon;

	private boolean party;
	private boolean mMeeting;
	boolean[] backup_attand = new boolean[members];
	boolean[] backup_listnum = new boolean[listnum];

	//layout variables
	LinearLayout header;
	LinearLayout linear;
	EditText meeting_location;
	Button timeSet;
	private ArrayList<EditText> todolist_ed = new ArrayList<EditText>();
    Calendar calendar = Calendar.getInstance();
    TextView timeshow;

	
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
			header = (LinearLayout) inflater.inflate(
					R.layout.insert_meeting_info_page_header, null);
			meeting_location = (EditText) header.findViewById(R.id.meeting_location_edit_text);
			timeshow = (TextView) header.findViewById(R.id.time_show);
			timeSet= (Button)header.findViewById(R.id.time_set_btn);
		
			timeSet.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 new TimePickerDialog(MeetingCreateActivity.this,
							                                     timeSetListenr,
							                                     calendar.get(Calendar.HOUR_OF_DAY),
							                                     calendar.get(Calendar.MINUTE),
							                                     false).show();					
				}
			});
			
			
			//meeting_time.in
			final LayoutInflater lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			linear = (LinearLayout) lf.inflate(
					R.layout.insert_meeting_info_page_low, null);
			EditText et = (EditText) linear
					.findViewById(R.id.insert_todolist_edit_text);
			
			header.addView(linear);
			
			todolist_ed.add(et);


			final ScrollView scroll = (ScrollView) inflater.inflate(
					R.layout.insert_meeting_info_page_scroll, null);

			scroll.addView(header);

			Button add_todolist = (Button) header
					.findViewById(R.id.plus_todolist);
			add_todolist.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout linear = (LinearLayout) lf.inflate(
							R.layout.insert_meeting_info_page_low, null);
					EditText et = (EditText) linear
							.findViewById(R.id.insert_todolist_edit_text);
					todolist_ed.add(et);

					header.addView(linear);
					scroll.removeAllViewsInLayout();
					scroll.addView(header);

					setContentView(scroll);
					System.out.println(todolist_ed.size()+" 이다 우아아아앙아아");


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
		menu.add("write").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String getLocation = meeting_location.getText().toString();
		/* 시간 변수들
		 * year month date hours minute afternoon을 사용하여 서버에 저장하세용
		 *
		 */
		
		
		finish();
		return true;
	}
	private void setMeetingTime(int hour, int minute, boolean afternoon) {
		if(afternoon)
		 timeshow.setText(year+"년 "+ month +"월 "+ date + "일 오후 "+ hour + "시 " + minute +"분");
		else 
			 timeshow.setText(year+"년 "+ month +"월 "+ date + "일 오전 "+ hour + "시 " + minute +"분");
			hours = hour;
			this.minute = minute;
			this.afternoon = afternoon;
		     }

	 TimePickerDialog.OnTimeSetListener timeSetListenr =
	         new TimePickerDialog.OnTimeSetListener() {
	             @Override
	             public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            	 
	            	 boolean afternoon;
	            	 
	            	 if(hourOfDay==12)
	            		 {
	            		 	afternoon=true;
	            		 }
	            		 else if(hourOfDay>12)
	            		 {
		            		 	hourOfDay= hourOfDay-12;
		            		 	afternoon=true;
	            		 }
	            		 else afternoon=false;
	             
	            	 setMeetingTime(hourOfDay,minute,afternoon);            
	             }
	         };
	         
	         

}
