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
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.R;
import com.adapter.PartymListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.model.Partymessage;
import com.model.Partymessages;
import com.utils.Global;
import com.utils.NetHelper;

public class PartyMessagesFragment extends SherlockFragment implements
		OnItemClickListener {
	private PartymListAdapter mAdapter;
	private ArrayList<Partymessage> mArrayList;
	private View mView;
	private PullToRefreshListView mListView;
	int mPrevTotalItemCount = 0;
	private String mResult;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.partymessages_list, container, false);
		mArrayList = new ArrayList<Partymessage>();
		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.party_message_list);
//		new GetPartymList().execute();
		
		// 임의 생성
		for (int i = 0; i < 10; i++) {
			mArrayList.add(new Partymessage("모집메시지 임의생성"));
		}
		
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				mArrayList.clear();
//				new GetPartymList().execute();
				mListView.onRefreshComplete();
			}
		});
		mAdapter = new PartymListAdapter(mView.getContext(),
				R.layout.partymessages_row, mArrayList);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
//		mListView.getRefreshableView().setOnScrollListener(
//				new EndlessScrollListener());
		return mView;
	}

	private class GetPartymList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/partym.json");
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Partymessages partyms = gson.fromJson(mResult, Partymessages.class);
			for (Partymessage partym : partyms.getPartymessages()) {
				if (!partym.getBody().equals("")) {
					mArrayList.add(partym);
				}
			}
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();
			
			super.onPostExecute(result);
		}
	}
	
//	public class EndlessScrollListener implements OnScrollListener {
//
//		private int visibleThreshold = 5;
//		private boolean loading = true;
//
//		public EndlessScrollListener() {
//		}
//
//		public EndlessScrollListener(int visibleThreshold) {
//			this.visibleThreshold = visibleThreshold;
//		}
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,
//				int visibleItemCount, int totalItemCount) {
//
//			if (loading) {
//				if (totalItemCount > mPrevTotalItemCount) {
//					loading = false;
//					mPrevTotalItemCount = totalItemCount;
//				}
//			}
//			if (!loading
//					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//				new GetPartymList().execute();
//				loading = true;
//			}
//		}
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//		}
//	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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
