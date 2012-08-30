package com.fragment;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.R;
import com.adapter.GroupMemberAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.User;
import com.utils.Global;
import com.utils.ImageDownloader;
import com.utils.NetHelper;

public class InfoFragment extends SherlockFragment implements
		OnItemClickListener {
	private View mView;
	private GroupMemberAdapter mAdapter;
	private ArrayList<User> mArrayList;
	private PullToRefreshListView mListView;
	private String mResult;
	private RelativeLayout headerview;
	private String id;
	private ImageView group_image;
	private TextView group_name;
	private TextView group_starttime;
	private TextView group_subject;
	private TextView group_goal;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("InfoFragment");
		Intent in = getActivity().getIntent();
		id = in.getExtras().getString("id");
		mView = inflater.inflate(R.layout.group_show_info_list, container,
				false);
		mArrayList = new ArrayList<User>();
		new GetMemberList().execute();
		mListView = (PullToRefreshListView) mView.findViewById(R.id.member_list);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				new GetMemberList().execute();
			}
		});
		mAdapter = new GroupMemberAdapter(mView.getContext(),
				R.layout.group_show_info_list_row, mArrayList);

		headerview = (RelativeLayout) inflater.inflate(
				R.layout.group_show_info_header, null);
		
		group_image = (ImageView)headerview.findViewById(R.id.group_image);
		group_name = (TextView)headerview.findViewById(R.id.group_name);
		group_starttime = (TextView)headerview.findViewById(R.id.group_starttime);
		group_subject = (TextView)headerview.findViewById(R.id.group_subject);
		group_goal = (TextView)headerview.findViewById(R.id.group_goal);
		
		mListView.getRefreshableView().addHeaderView(headerview);

		// setAdapter must be located below addheaderview.
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		return mView;
	}

	private class GetMemberList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/" + id
					+ ".json");
			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONObject group = new JSONObject(mResult);
				String image = group.getString("image");
				if (image !=null) {
					ImageDownloader.download(Global.ServerUrl + image, group_image);
				}else{
					group_image.setImageResource(R.drawable.ic_launcher);
				}
				
				group_name.setText(group.getString("name"));
				group_starttime.setText(group.getString("created_at"));
				group_subject.setText(group.getString("subject"));
				group_goal.setText(group.getString("goal"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Gson gson = new Gson();
			// Groups groups = gson.fromJson(mResult, Groups.class);
			// for (Group group : groups.getGroups()) {
			// mArrayList.add(group);
			// }
			 mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}

	@Override
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
