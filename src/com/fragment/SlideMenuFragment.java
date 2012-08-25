package com.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.adapter.MyStudyListAdapter;
import com.google.gson.Gson;
import com.model.Group;
import com.model.Groups;
import com.utils.Global;
import com.utils.NetHelper;

public class SlideMenuFragment extends SherlockListFragment implements
		OnItemClickListener {
	private ArrayList<Group> mArrayList;
	private MyStudyListAdapter mAdapter;
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

		// 임의생성
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ladygaga, options);
		Bitmap resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize, "회화스터디", "영어회화 능력향상 ", "6", "10",
				"미정"));
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_jobs, options);
		resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Group(resize, "재무스터디",
				"스터디 동안에 최소 재무관리 전 범위를 4회독 이상 목표로 하고 있습니다.", "7", "4",
				"백기 스터디룸  "));

		mAdapter = new MyStudyListAdapter(getActivity(),
				R.layout.slide_menu_mystudy_row, mArrayList);
		// header, footer 뷰 설정
		LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		headerview = (LinearLayout) inflater.inflate(
				R.layout.slide_menu_header, null);
		footerview = (LinearLayout) inflater.inflate(
				R.layout.slide_menu_footer, null);
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
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups.json");
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
