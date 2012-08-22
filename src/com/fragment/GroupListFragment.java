package com.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.GroupShowActivity;
import com.activity.R;
import com.adapter.GroupListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.model.Group;
import com.model.Groups;
import com.utils.Global;
import com.utils.NetHelper;

public class GroupListFragment extends SherlockFragment implements
		OnItemClickListener {
	private ArrayList<Group> mArrayList;
	private GroupListAdapter mAdapter;
	private View mView;
	private PullToRefreshListView mListView;
	private String mResult;
	int mPrevTotalItemCount = 0;
	private Integer mCurrentPage = 1;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.group_list, container, false);
		mArrayList = new ArrayList<Group>();
		mListView = (PullToRefreshListView) mView.findViewById(R.id.group_list);
		
		for (int i = 0; i < 15; i++)
			mArrayList.add(new Group("그룹 이름 임의생성","그룹 목표 임의 생성"));
		
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				mArrayList.clear();
//				mCurrentPage = 1;
//				mPrevTotalItemCount = 0;
				mListView.onRefreshComplete();
			}
		});
		mAdapter = new GroupListAdapter(mView.getContext(),
				R.layout.group_list_row, mArrayList);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
//		mListView.getRefreshableView().setOnScrollListener(
//				new EndlessScrollListener());
		return mView;
	}

	private class GetGroupList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper
					.DownloadHtml(Global.ServerUrl + "groups.json");
//			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Groups groups = gson.fromJson(mResult, Groups.class);
			for (Group group : groups.getGroups()) {
				mArrayList.add(group);
			}
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}

	public class EndlessScrollListener implements OnScrollListener {

		private int visibleThreshold = 5;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (loading) {
				if (totalItemCount > mPrevTotalItemCount) {
					loading = false;
					mPrevTotalItemCount = totalItemCount;
				}
			}
			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				new GetGroupList().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in = new Intent(getActivity(),GroupShowActivity.class);
		startActivity(in);
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
