package com.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import studium.sactivity.groupindex.studiummainsplash.activity.GroupCreateActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.GroupIndexActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.GroupShowActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.LoginActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.UserInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.google.gson.Gson;
import com.studium.adapter.MyStudyListAdapter;
import com.studium.model.Group;
import com.studium.model.Groups;
import com.utils.Global;
import com.utils.ImageDownloader;
import com.utils.NetHelper;

public class SlideMenuFragment extends SherlockListFragment implements
		OnItemClickListener {
	private ArrayList<Group> mArrayList;
	private MyStudyListAdapter mAdapter;
	private ListView mListView;
	private String mResult;
	private LinearLayout headerview;
	private LinearLayout footerview;
	private TableRow studycreate;
	private TableRow studysearch;
	private TableRow logout;
	private RelativeLayout userlayout;
	private SharedPreferences mPreferences;
	private String auth_token;
	private ImageView profile_thumbnail;
	private TextView user_name;
	private RelativeLayout userdivider;
	private ImageView preferecedivider;
	private String name;
	private String email;
	private String phone;
	private String image;
	private String id;

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

		mPreferences = getActivity().getSharedPreferences("CurrentUser",
				getActivity().MODE_PRIVATE);
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
		
		userlayout = (RelativeLayout) headerview.findViewById(R.id.user_layout);
		userlayout.setOnClickListener(new UserClickListener());
		
		userdivider = (RelativeLayout) headerview.findViewById(R.id.user_divider);
		userdivider.setOnClickListener(new UserDividerClickListener());
		
		preferecedivider = (ImageView)footerview.findViewById(R.id.preference_and_logout);
		preferecedivider.setOnClickListener(new PrefClickListener());

		studycreate = (TableRow) footerview
				.findViewById(R.id.create_study_table);
		studycreate.setOnClickListener(new StudyCreateClickListener());

		studysearch = (TableRow) footerview
				.findViewById(R.id.search_study_table);
		studysearch.setOnClickListener(new StudySearchClickListener());

		logout = (TableRow) footerview.findViewById(R.id.logout_table);
		logout.setOnClickListener(new LogoutClickListener());

		profile_thumbnail = (ImageView) headerview
				.findViewById(R.id.profile_thumbnail);
		user_name = (TextView) headerview.findViewById(R.id.user_name);

		mListView.addHeaderView(headerview);
		mListView.addFooterView(footerview);
		// header, footer end
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	class PrefClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// pref divider
		}
	}
	
	class UserDividerClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// user divider
		}
	}
	
	class UserClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Intent in = new Intent(getActivity(), UserInfoActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.putExtra("id",id);
			in.putExtra("email",email);
			in.putExtra("name", name);
			in.putExtra("phone", phone);
			startActivity(in);
		}
	}

	class StudyCreateClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getActivity(), GroupCreateActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
		}
	}

	class StudySearchClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent in = new Intent(getActivity(), GroupIndexActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
		}
	}

	class PreferenceClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// preference
		}
	}

	class LogoutClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Editor ed = mPreferences.edit();
			ed.remove("UserName");
			ed.remove("PassWord");
			ed.remove("AuthToken");
			ed.commit();
			getActivity().finish();

			Intent in = new Intent(getActivity(), LoginActivity.class);
			startActivity(in);
		}
	}

	// My Study 불러와야됨
	private class GetMyStudyList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl
					+ "groups/currentuser.json?auth_token=" + auth_token);
			System.out.println(mResult);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONObject user = new JSONObject(mResult);
				id = user.getString("id");
				name = user.getString("name");
				user_name.setText(name);
				email = user.getString("email");
				phone = user.getString("phone");
				
				image = user.getString("image");
				if (image.equals("")) {
					profile_thumbnail.setImageResource(R.drawable.photo_frm);
				} else{
					ImageDownloader.download(Global.ServerUrl + image,
							profile_thumbnail);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			Groups groups = gson.fromJson(mResult, Groups.class);
			
			try {
				for (Group group : groups.getGroups()) {
					if (group.getName()!=null) {
						mArrayList.add(group);
					}
				}
			} catch(Exception e){
				Toast.makeText(getActivity(), "인터넷 연결상태가 좋지 않습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_LONG).show();
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Group group = mArrayList.get(position - 1);
		Intent in = new Intent(getActivity(), GroupShowActivity.class);
		in.putExtra("group_id", group.getId());
		in.putExtra("group_name", group.getName());
		in.putExtra("role", group.getRole());
//		Toast.makeText(getActivity(), group.getRole(), Toast.LENGTH_SHORT)
//				.show();
		startActivity(in);
	}
}
