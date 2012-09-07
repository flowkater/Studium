package com.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.activity.R;
import com.adapter.CheckTodoListAdapter;
import com.adapter.ImageAdapter;
import com.fragment.StepFragment;
import com.model.CheckString;
import com.model.ThumbImageInfo;
import com.utils.Global;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MeetingShowActivity extends SherlockActivity implements OnItemClickListener,
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
	private boolean party;
	private boolean mMeeting;
	boolean[] backup_attand = new boolean[members];
	boolean[] backup_listnum = new boolean[listnum];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���� ��Ƽ��Ƽ�κ��� �Ѿ�� �����͸� ������.
		Intent in = getIntent();
		date = in.getStringExtra("date");
		month = in.getStringExtra("month");
		year = in.getStringExtra("year");
		party = in.getBooleanExtra("isParty", false);
		mMeeting = in.getBooleanExtra("making", false);
		String partystring[] = in.getStringArrayExtra("party");
		role = in.getStringExtra("role");

		

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

			String[] party_info = getIntent().getStringArrayExtra("party");

			Num_todoList = party_info[TODOLIST].split(delims).length;

			CheckString[] fuck = new CheckString[Num_todoList];

			System.out.println(Num_todoList);

			for (int i = 0; i < Num_todoList; i++) {
				String string = party_info[TODOLIST].split(delims)[i];
				fuck[i] = new CheckString();
				fuck[i].setString(string);
				todolist_list.add(fuck[i]);
			}

			ListView lv = (ListView) findViewById(R.id.to_do_list_list);
			// if (role.equals(Global.founder)) {
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			// }
			// thum array

			mThumbImageInfoList = new ArrayList<ThumbImageInfo>();

			// thumb start
			ThumbImageInfo thumbInfo = new ThumbImageInfo();

			thumbInfo.setId("��ö");
			thumbInfo.setThum_img("member_byung");
			thumbInfo.setCheckedState(false);

			mThumbImageInfoList.add(thumbInfo);

			ThumbImageInfo thumbInfo1 = new ThumbImageInfo();

			thumbInfo1.setId("�⽺");
			thumbInfo1.setThum_img("member_jobs");
			thumbInfo1.setCheckedState(false);

			mThumbImageInfoList.add(thumbInfo1);

			ThumbImageInfo thumbInfo2 = new ThumbImageInfo();

			thumbInfo2.setId("���̵� ����");
			thumbInfo2.setThum_img("member_ladygaga");
			thumbInfo2.setCheckedState(false);

			mThumbImageInfoList.add(thumbInfo2);

			ThumbImageInfo thumbInfo3 = new ThumbImageInfo();

			thumbInfo3.setId("��");
			thumbInfo3.setThum_img("member_ya");
			thumbInfo3.setCheckedState(false);

			mThumbImageInfoList.add(thumbInfo3);

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
			meeting_location_input_text.setText(partystring[LOCATION]);

			TextView meeting_time_input_text = (TextView) header
					.findViewById(R.id.meeting_time_input_text);
			meeting_time_input_text.setText(partystring[TIME]);

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

						achive_rate = checked * 100 / Num_todoList;
						System.out.println("fuck       " + achive_rate);

						achive_rate_tv.setText("��ǥ �޼��� : " + achive_rate + "%");

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

	/**
	 * ��ü������ ���� �˷����̾�α� ���� �޼ҵ�
	 * 
	 * @param title
	 *            Ÿ��Ʋ
	 * @param message
	 *            �޽���
	 */
	private void alert(final String date, final String month, final String year) {
		// ü�������� �޼ҵ带 ����Ѵ�.
		new AlertDialog.Builder(this)
		// ������ Ÿ��Ʋ�� �����Ѵ�.
				.setTitle("����")
				// ������ �޽��� �κп� �����Ѵ�.
				.setMessage(year + "�� " + month + "�� " + date + "��")
				// ��Ҹ� ���ϵ��� ���´�.
				.setCancelable(false)
				// Ȯ�� ��ư�� �����.

				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.content.DialogInterface.OnClickListener#onClick
					 * (android.content.DialogInterface, int)
					 */
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("fucking!!!!!!!!");
						Bundle bundle = new Bundle();
						bundle.putString("color", Color.YELLOW + "");
						bundle.putString("date", date);
						bundle.putString("month", month);
						bundle.putString("year", year);
						Intent intent = new Intent(getApplicationContext(),
								GroupShowActivity.class);
						System.out.println("fucking!!!!!!!!");

						intent.putExtras(bundle);

						System.out.println("fucking!!!!!!!!");

						// Ȯ�ι�ư�� Ŭ���Ǹ� ���̾�α׸� �����Ѵ�.
						dialog.dismiss();
						System.out.println("fucking!!!!!!!!");

						// ��Ƽ��Ƽ�� �����Ѵ�.
						finish();
						System.out.println("fucking!!!!!!!!");

						startActivity(intent);
						System.out.println("fucking!!!!!!!!");
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

		int attendance_rate = attandance_check * 100 / members;

		attendance_bar.setMax(100);
		attendance_bar.setProgress(attendance_rate);

		attandance_tv.setText("�⼮�� : " + attendance_rate + "%");

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