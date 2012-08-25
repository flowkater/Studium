package com.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private PullToRefreshListView mListView;
	private View mView;
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
		
		// 비트맵 예제 임의 생성
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.group_party_8, options);
		Bitmap resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"영어회화스터디","영어회화 능력향상 ","6","10","미정"));
		orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.member_jobs, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"금융공기업 재무관리","스터디 동안에 최소 재무관리 전 범위를 4회독 이상 목표로 하고 있습니다.","7","4","백기 스터디룸  "));
		orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.group_party_7, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"취업스터디!","취업!!!!!","6","2","경영대 이명박 라운지"));
		orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.group_party_10, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"취업 인적성 스터디","인적성 모의고사 및 영역별 학습 및 인적성 스터디","6","7",""));
		orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.group_party_9, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"종합 PD 스터디","PD 공채","8","9"," 이화여대 ECC "));
		orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.member_ya, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize,"방학 스터디","개강전 워밍업!","3","5","매일 아침 중광"));
		
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
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups.json");
			// System.out.println(mResult);
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
		Intent in = new Intent(getActivity(), GroupShowActivity.class);
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
