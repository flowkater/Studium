package com.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.PostShowActivity;
import com.activity.R;
import com.adapter.GroupPostAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Party;
import com.model.Post;
import com.model.Posts;
import com.model.User;
import com.utils.Global;
import com.utils.NetHelper;

public class FeedFragment extends SherlockFragment implements
		OnItemClickListener {
	// party test
	
	
	
	
	public ArrayList<Party> pppp= new ArrayList<Party>();
	

	public ArrayList<String> party_dates = new ArrayList<String>();

	private View mView;
	private GroupPostAdapter mAdapter;
	private ArrayList<Post> mArrayList;
	private PullToRefreshListView mListView;
	private String mResult;
	private int mPrevTotalItemCount = 0;
	private Integer mCurrentPage = 1;
	private RelativeLayout headerview;
	private String id; // id
	private TextView group_name; // headerview group_name
	private TextView group_goal; // headerview group_goal
	private Button party_info;// headerview party_info
	public final String delims = "-";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Intent in = getActivity().getIntent();
		id = in.getExtras().getString("group_id");
		mView = inflater.inflate(R.layout.group_show_feed_list, container,
				false);
		mArrayList = new ArrayList<Post>();

		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.group_show_feed_list);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				mCurrentPage = 1;
				mPrevTotalItemCount = 0;
			}
		});
		mAdapter = new GroupPostAdapter(mView.getContext(),
				R.layout.group_show_feed_list_row, mArrayList);
		headerview = (RelativeLayout) inflater.inflate(
				R.layout.group_show_feed_header, null);

		// headerview get id
		group_name = (TextView) headerview.findViewById(R.id.group_name);
		group_goal = (TextView) headerview.findViewById(R.id.group_goal);
		party_info = (Button) headerview
				.findViewById(R.id.group_meeting_date_btn);
		
		party_dates.add("2012-8-30");
		party_dates.add("2012-9-3");
		party_dates.add("2012-9-5");
		party_dates.add("2012-9-9");

	
	
		int next_index = getNextParty(party_dates);
		
		party_info.setText("ㅅㅂ이다 ㅅㅂ색히야 \n "+next_index);
		
		//Show_Party(next_index);

		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().setOnScrollListener(
				new EndlessScrollListener());

		return mView;
	}

//	private void Show_Party(int next_index) {
//
//		
//		
//		String date = pppp.get(next_index).getDate();
//		String location = pppp.get(next_index).getLocation();
//		
//		String[] temp = date.split(delims);
//		
//		
//		party_info.setText("다음 모임 \n"+temp[1]+"월 " + temp[2]+"일 \n"+ "장소 : "+ location);
//
//		
//	}

	private int getNextParty(ArrayList<String> party_dates2) {
		Calendar calender = Calendar.getInstance();
		int i ;
		boolean IfindTheFUCKINDEX = false;
		for (i = 0; i < party_dates2.size(); i++) {
			
			String find_dates = party_dates2.get(i);

			String[] temp = find_dates.split(delims);
			int [] temp_int = new int [3];
			temp_int[0] = Integer.parseInt(temp[0]);
			temp_int[1] = Integer.parseInt(temp[1]);
			temp_int[2] = Integer.parseInt(temp[2]);
			
			int temp_fuck = temp_int[0]*10000 + temp_int[1]*100 + temp_int[2];
			int now_fuck = calender.get(Calendar.YEAR) * 10000 + (calender.get(Calendar.MONTH)+1) *100 + calender.get(Calendar.DATE);

			
			
			if(temp_fuck < now_fuck)
			{
				
			}
			else
			{
				IfindTheFUCKINDEX=true;
			}
			
			if(IfindTheFUCKINDEX)
				break;
			
		}
		return i;
		
	}

	private class GetPostList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/" + id
					+ "/posts.json?page=" + mCurrentPage.toString());
			// System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Posts posts = gson.fromJson(mResult, Posts.class);
			try {
				JSONObject group = new JSONObject(mResult);
				group_name.setText(group.getString("name"));
				group_goal.setText(group.getString("goal"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Post post : posts.getPosts()) {
				mArrayList.add(post);
			}

			mCurrentPage++;
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
				new GetPostList().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		Post post = mArrayList.get(position - 2);
		Intent in = new Intent(getActivity(), PostShowActivity.class);
		in.putExtra("post_id", post.getId());
		in.putExtra("group_id", post.getGroup_id());
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
