package com.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adapter.GroupWaitingAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.model.User;
import com.model.Users;
import com.utils.Global;
import com.utils.NetHelper;

public class GroupManageActivity extends SherlockActivity {
	private String group_id;
	private TextView titlebar_text;
	private ArrayList<User> mArrayList;
	private String mResult;
	private PullToRefreshListView mListView;
	private ArrayAdapter<User> mAdapter;
	private LinearLayout headerview;
	private String auth_token;
	private SharedPreferences mPreferences;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken","");
		
		setContentView(R.layout.group_manage_waiting_list);
		Intent in = getIntent();
		group_id = in.getExtras().getString("group_id");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("스터디 관리");
		// end header
		mArrayList = new ArrayList<User>();
		new GetWaitingList().execute();
		mListView = (PullToRefreshListView) findViewById(R.id.waiting_list);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				new GetWaitingList().execute();
			}
		});
		mAdapter = new GroupWaitingAdapter(getApplicationContext(),
				R.layout.group_manage_waiting_list_low, mArrayList);
		LayoutInflater inflater = this.getLayoutInflater();
		headerview = (LinearLayout) inflater.inflate(
				R.layout.group_manage_header, null);
		//
		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
	}

	private class GetWaitingList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "/groups/"
					+ group_id + "/founder.json?auth_token=" + auth_token);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			Gson gson = new Gson();
			Users users = gson.fromJson(mResult, Users.class);
			for (User user : users.getUsers()) {
				mArrayList.add(user);
			}
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
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
			return true;
		}
		return false;
	}
}
