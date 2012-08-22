package com.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.activity.R;
import com.adapter.GroupListAdapter;
import com.google.gson.Gson;
import com.model.Group;
import com.model.Groups;
import com.utils.Global;
import com.utils.NetHelper;

public class SlideMenuFragment extends SherlockListFragment implements OnItemClickListener{
	private ArrayList<Group> mArrayList;
	private GroupListAdapter mAdapter;
	private ListView mListView;
	private String mResult;
	int mPrevTotalItemCount = 0;
	private LinearLayout headerview;
	private LinearLayout footerview;
	
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
		
		for (int i = 0; i < 2; i++) {
			mArrayList.add(new Group("수학100점","토익100점"));
		}
		
		mAdapter = new GroupListAdapter(getActivity(),
				R.layout.group_list_row, mArrayList);
		// header, footer 뷰 설정
		LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		headerview = (LinearLayout)inflater.inflate(R.layout.slide_menu_header, null);
		footerview = (LinearLayout)inflater.inflate(R.layout.slide_menu_footer, null);
		mListView.addHeaderView(headerview);
		mListView.addFooterView(footerview);
		// header, footer end
		
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setSelector(android.R.color.transparent);
	}
	
	// My Study 불러와야됨
	private class GetMyStudyList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper
					.DownloadHtml(Global.ServerUrl + "groups.json");
			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
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
