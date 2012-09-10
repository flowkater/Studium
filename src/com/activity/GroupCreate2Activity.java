package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.color;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;
import com.utils.SunUtil;

public class GroupCreate2Activity extends SherlockActivity implements
		OnClickListener {

	private ProgressDialog mProgressDialog;
	private TextView titlebar_text;
	private String name;
	private String goal;
	private String location;
	private String subject;
	private String auth_token;
	private Bitmap bm;

	private Button[] week_btn = new Button[8];
	private int[] week_int = new int[8];
	
	
	DatePicker endDP;
	DatePicker startDP;
	private LinearLayout header;
	private LinearLayout linear;
	private ArrayList<EditText> todolist_ed = new ArrayList<EditText>();
	
	// location, date
		private String startdate;
		private String enddate;
		
		private ArrayList<String> todolists = new ArrayList<String>();


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent in = getIntent();
		name = in.getStringExtra("group_name");
		goal = in.getStringExtra("group_goal");
		location = in.getStringExtra("group_location");
		subject = in.getStringExtra("group_category");
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
		titlebar_text.setText("그룹생성");

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		header = (LinearLayout) inflater.inflate(R.layout.group_create_plan,
				null);
		
		startDP = (DatePicker)header.findViewById(R.id.plan_start_date_picker);
		endDP= (DatePicker)header.findViewById(R.id.plan_end_date_picker);

		week_btn[1] = (Button) header.findViewById(R.id.sun_btn);
		week_btn[2] = (Button) header.findViewById(R.id.mon_btn);
		week_btn[3] = (Button) header.findViewById(R.id.tue_btn);
		week_btn[4] = (Button) header.findViewById(R.id.wed_btn);
		week_btn[5] = (Button) header.findViewById(R.id.thu_btn);
		week_btn[6] = (Button) header.findViewById(R.id.fri_btn);
		week_btn[7] = (Button) header.findViewById(R.id.sat_btn);

		week_btn[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[1] == 0) {
					week_int[1] = 1;
					week_btn[1].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[1] = 0;
					week_btn[1].setBackgroundColor(Color.GRAY);

				}
			}
		});
		week_btn[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[2] == 0) {
					week_int[2] = 1;
					week_btn[2].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[2] = 0;
					week_btn[2].setBackgroundColor(Color.GRAY);
				}

			}
		});
		week_btn[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[3] == 0) {
					week_int[3] = 1;
					week_btn[3].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[3] = 0;
					week_btn[3].setBackgroundColor(Color.GRAY);
				}
			}
		});
		week_btn[4].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[4] == 0) {
					week_int[4] = 1;
					week_btn[4].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[4] = 0;
					week_btn[4].setBackgroundColor(Color.GRAY);
				}
			}
		});
		week_btn[5].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[5] == 0) {
					week_int[5] = 1;
					week_btn[5].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[5] = 0;
					week_btn[5].setBackgroundColor(Color.GRAY);
				}
			}
		});
		week_btn[6].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[6] == 0) {
					week_int[6] = 1;
					week_btn[6].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[6] = 0;
					week_btn[6].setBackgroundColor(Color.GRAY);
				}
			}
		});
		week_btn[7].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (week_int[7] == 0) {
					week_int[7] = 1;
					week_btn[7].setBackgroundColor(Color.LTGRAY);
				} else {
					week_int[7] = 0;
					week_btn[7].setBackgroundColor(Color.GRAY);
				}
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

		Button add_todolist = (Button) header.findViewById(R.id.todolist_plus_btn);
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
			}
		});

		setContentView(scroll);
	}

	

	class Groupcreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				ByteArrayBody bab = null;
				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					bab = new ByteArrayBody(data, "group_image.jpg");
				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "groups.json?auth_token=" + auth_token);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("group[goal]",
						new StringBody(goal, Charset.forName("UTF-8")));
				reqEntity.addPart("group[subject]", new StringBody(subject,
						Charset.forName("UTF-8")));
				reqEntity.addPart("group[place]", new StringBody(location,
						Charset.forName("UTF-8")));
				reqEntity.addPart("group[name]",
						new StringBody(name, Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("group[pictures_attributes][0][image]",
							bab);
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			finish();
			Intent in = new Intent(getApplicationContext(),
					GroupIndexActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			removeDialog(0);
			super.onPostExecute(result);
		}
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
		if (item.getTitle().equals("write")) {
			showDialog(0);
			//sever send data
			startdate = startDP.getYear()+ "-"+ startDP.getMonth()+"-"+startDP.getDayOfMonth();
			enddate = endDP.getYear()+ "-"+ endDP.getMonth()+"-"+endDP.getDayOfMonth();
			for (EditText ed : todolist_ed) {
				todolists.add(ed.getText().toString());
			}
			
			new Groupcreate().execute();
			return true;
		}
		return false;
	}

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
