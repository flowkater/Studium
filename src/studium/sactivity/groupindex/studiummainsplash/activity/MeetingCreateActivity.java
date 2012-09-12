package studium.sactivity.groupindex.studiummainsplash.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.adapter.CheckTodoListAdapter;
import com.studium.adapter.ImageAdapter;
import com.studium.model.CheckString;
import com.studium.model.ThumbImageInfo;
import com.studium.model.Todolist;
import com.utils.Global;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

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

	private CheckTodoListAdapter mAdapter;

	private ImageAdapter mListAdapter;

	private ArrayList<CheckString> todolist_list = new ArrayList<CheckString>();
	private CheckString check;
	private ProgressBar achivebar;
	private ProgressBar attendance_bar;
	int achive_rate;
	int Num_todoList;
	int checked = 0;
	private TextView achive_rate_tv;
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

	// layout variables
	private LinearLayout header;
	private LinearLayout linear;
	private EditText meeting_location;
	private Button timeSet;
	private ArrayList<EditText> todolist_ed = new ArrayList<EditText>();
	private Calendar calendar = Calendar.getInstance();
	private TextView timeshow;

	// group_id, auth_token
	private String group_id;
	private String auth_token;

	// location, date
	private String getLocation;
	private String getDate;
	private String getTime;
	private ArrayList<String> todolists = new ArrayList<String>();

	private ProgressDialog mProgressDialog;

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
		group_id = in.getStringExtra("group_id");
		auth_token = in.getStringExtra("auth_token");

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

		if (mMeeting) {

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			header = (LinearLayout) inflater.inflate(
					R.layout.insert_meeting_info_page_header, null);
			meeting_location = (EditText) header
					.findViewById(R.id.meeting_location_edit_text);
			timeshow = (TextView) header.findViewById(R.id.time_show);
			timeSet = (Button) header.findViewById(R.id.time_set_btn);

			timeSet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new TimePickerDialog(MeetingCreateActivity.this,
							timeSetListenr, calendar.get(Calendar.HOUR_OF_DAY),
							calendar.get(Calendar.MINUTE), false).show();
				}
			});

			// meeting_time.in
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
					System.out.println(todolist_ed.size() + " 이다 우아아아앙아아");
				}
			});
			setContentView(scroll);
		}
	}

	// Party Create Server Module
	private class Partycreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "groups/" + group_id + "/parties.json?auth_token="
						+ auth_token);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("party[place]", new StringBody(getLocation,
						Charset.forName("UTF-8")));
				reqEntity.addPart("party[time]", new StringBody(getTime,
						Charset.forName("UTF-8")));
				reqEntity.addPart("party[date]", new StringBody(getDate,
						Charset.forName("UTF-8")));
				for (int i = 0; i < todolists.size(); i++) {
					reqEntity.addPart("party[todolists_attributes][" + i
							+ "][list]", new StringBody(todolists.get(i),
							Charset.forName("UTF-8")));
				}
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				Log.e("my", "Response : " + s);
			} catch (Exception e) {
				removeDialog(0);
				Log.e("my", e.getClass().getName() + e.getMessage());
				Toast.makeText(getApplicationContext(), "Error!",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			finish();
			removeDialog(0);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("생성").setShowAsAction(
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
		if (item.getTitle().equals("생성")) {
			showDialog(0);
			getLocation = meeting_location.getText().toString();
			getDate = year + "-" + month + "-" + date;
			getTime = hours + "-" + minute;
			for (EditText ed : todolist_ed) {
				todolists.add(ed.getText().toString());
			}

			new Partycreate().execute();
			/*
			 * 시간 변수들 year month date hours minute afternoon을 사용하여 서버에 저장하세용
			 * todolist_ed
			 */
			return true;
		}
		return false;
	}

	private void setMeetingTime(int hour, int minute, boolean afternoon) {
		if (afternoon)
			timeshow.setText(year + "년 " + month + "월 " + date + "일 오후 " + hour
					+ "시 " + minute + "분");
		else
			timeshow.setText(year + "년 " + month + "월 " + date + "일 오전 " + hour
					+ "시 " + minute + "분");
		hours = hour;
		this.minute = minute;
		this.afternoon = afternoon;
	}

	TimePickerDialog.OnTimeSetListener timeSetListenr = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			boolean afternoon;
			if (hourOfDay == 12) {
				afternoon = true;
			} else if (hourOfDay > 12) {
				hourOfDay = hourOfDay - 12;
				afternoon = true;
			} else
				afternoon = false;
			setMeetingTime(hourOfDay, minute, afternoon);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return null;
	}

}
