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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.PostShowActivity;
import com.activity.R;
import com.adapter.GroupPostAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Group;
import com.model.Groups;
import com.model.Post;
import com.utils.Global;
import com.utils.NetHelper;

public class FeedFragment extends SherlockFragment implements
		OnItemClickListener {
	private View mView;
	private GroupPostAdapter mAdapter;
	private ArrayList<Post> mArrayList;
	private PullToRefreshListView mListView;
	private String mResult;
	int mPrevTotalItemCount = 0;
	private Integer mCurrentPage = 1;
	private RelativeLayout headerview;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.group_show_feed_list, container,
				false);
		mArrayList = new ArrayList<Post>();

		// 임의 생성
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		Bitmap member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_, options);
		Bitmap content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "일하다 힘들면 운동을", "김병철", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_wall, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Post(member,
				"LGD19800원에 종목 추천.. 약 2개월 후 현재 26500+-.....이정도면 추천할만 함!?", "월",
				"5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_picture, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Post(member,
				"간만에 미용실가서\n 머리잘랐당ㅎ\n 이것도 짧지않다는데 엄청짧은거같다ㅠ\n 머리 쓸어넘길때마다 어색어색;;",
				"픽쳐", "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_jobs, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_4, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList
				.add(new Post(
						member,
						"탁구대회는 토너먼트로 진행 됩니다.\n총 11명이 지원자가 있었고, a,b,c 그룹으로 나눠서 각 그룹의 1등을 뽑은 다음에 리그전을 진행해서 1,2,3등을 정할 겁니다!\n\n각자 대진표를 확인하시고\n1번 째 경기는 다음 주 화요일 8월 28일까지 서로 연락해서 결과를 내시고 저에게 알려주...",
						"잡스", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ladygaga, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_5, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "우왕 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ친해보인당", "레이디가가", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ya, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_6, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "Ministry of sound 레전드라고 했던거 취소.", "야", content, "5"));

		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.group_show_feed_list);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mListView.onRefreshComplete();
				// mArrayList.clear();
				// mCurrentPage = 1;
				// mPrevTotalItemCount = 0;
			}
		});
		mAdapter = new GroupPostAdapter(mView.getContext(),
				R.layout.group_show_feed_list_row, mArrayList);

		headerview = (RelativeLayout) inflater.inflate(
				R.layout.group_show_feed_header, null);

		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
		// mListView.getRefreshableView().setOnScrollListener(
		// new EndlessScrollListener());

		return mView;
	}

	private class GetPostList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// mResult = NetHelper
			// .DownloadHtml(Global.ServerUrl + "groups.json");
			// System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Gson gson = new Gson();
			// Groups groups = gson.fromJson(mResult, Groups.class);
			// for (Group group : groups.getGroups()) {
			// mArrayList.add(group);
			// }
			// mListView.onRefreshComplete();
			// mAdapter.notifyDataSetChanged();

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
				new GetPostList().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in = new Intent(getActivity(), PostShowActivity.class);
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
