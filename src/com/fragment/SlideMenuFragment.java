package com.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.activity.GroupCreateActivity;
import com.activity.LoginActivity;
import com.activity.R;
import com.adapter.MyStudyListAdapter;
import com.google.gson.Gson;
import com.model.Group;
import com.model.Groups;
import com.utils.Global;
import com.utils.ImageDownloader;
import com.utils.NetHelper;

public class SlideMenuFragment extends SherlockListFragment implements
		OnItemClickListener {
	private ArrayList<Group> mArrayList;
	private MyStudyListAdapter mAdapter;
	private ListView mListView;
	private String mResult;
	private int mPrevTotalItemCount = 0;
	private LinearLayout headerview;
	private LinearLayout footerview;
	private TableRow studycreate;
	private RelativeLayout userlogin;
	private SharedPreferences mPreferences;
	private String auth_token;
	private ImageView profile_thumbnail;
	private TextView user_name;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slide_menu, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mArrayList = new ArrayList<Group>();
		mListView = getListView();
		
		mPreferences = getActivity().getSharedPreferences("CurrentUser", getActivity().MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		mAdapter = new MyStudyListAdapter(getActivity(),
				R.layout.slide_menu_mystudy_row, mArrayList);
		
		new GetMyStudyList().execute();
		// header, footer 뷰 설정
		LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		headerview = (LinearLayout) inflater.inflate(
				R.layout.slide_menu_header, null);
		footerview = (LinearLayout) inflater.inflate(
				R.layout.slide_menu_footer, null);
		
		studycreate = (TableRow)footerview.findViewById(R.id.create_study_table);
		studycreate.setOnClickListener(new StudyClickListener());
		
		userlogin = (RelativeLayout)headerview.findViewById(R.id.user_layout);
		userlogin.setOnClickListener(new LoginClickListener());
		
		
		profile_thumbnail = (ImageView)headerview.findViewById(R.id.profile_thumbnail);
		user_name = (TextView)headerview.findViewById(R.id.user_name);
		
		mListView.addHeaderView(headerview);
		mListView.addFooterView(footerview);
		// header, footer end
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	class LoginClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Intent in = new Intent(getActivity(), LoginActivity.class);
			startActivity(in);
		}
	}
	
	class StudyClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getActivity(), GroupCreateActivity.class);
			startActivity(in);
		}
	}

	// My Study 불러와야됨
	private class GetMyStudyList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/currentuser.json?auth_token="+ auth_token);
			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONObject user = new JSONObject(mResult);
				user_name.setText(user.getString("name"));
				String image = user.getString("image");
				if (image != null) {
					ImageDownloader.download(Global.ServerUrl + image, profile_thumbnail);
				}else{
					profile_thumbnail.setImageResource(R.drawable.ic_launcher);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			Groups groups = gson.fromJson(mResult, Groups.class);
			for (Group group : groups.getGroups()) {
				mArrayList.add(group);
			}
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
