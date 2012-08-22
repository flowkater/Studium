package com.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.R;
import com.adapter.GroupMemberAdapter;
import com.google.gson.Gson;
import com.model.Group;
import com.model.Groups;
import com.model.User;
import com.utils.Global;
import com.utils.NetHelper;

public class InfoFragment extends SherlockFragment {
	private View mView;
	private GroupMemberAdapter mAdapter;
	private ArrayList<User> mArrayList;
	private ListView mListView;
	private String mResult;
	private RelativeLayout headerview;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.group_show_info_list, container,
				false);
		mArrayList = new ArrayList<User>();
		
		for(int i=0; i<5;i++)
			mArrayList.add(new User("스티브 잡스"));
		
		mListView = (ListView) mView.findViewById(R.id.member_list);
		mAdapter = new GroupMemberAdapter(mView.getContext(),
				R.layout.group_show_info_list_row, mArrayList);
		
		headerview = (RelativeLayout)inflater.inflate(R.layout.group_show_info_header, null);
		mListView.addHeaderView(headerview);
		
		//setAdapter must be located below addheaderview.
		mListView.setAdapter(mAdapter);
		
		return mView;
	}
	
	private class GetMemberList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
//			mResult = NetHelper
//					.DownloadHtml(Global.ServerUrl + "groups.json");
//			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
//			Groups groups = gson.fromJson(mResult, Groups.class);
//			for (Group group : groups.getGroups()) {
//				mArrayList.add(group);
//			}
//			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

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
