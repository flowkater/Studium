package com.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fragment.FeedFragment;
import com.fragment.InfoFragment;
import com.fragment.SlideMenuFragment;
import com.fragment.StepFragment;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.utils.Global;

public class GroupShowActivity extends SlidingFragmentActivity {
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private TextView titlebar_text;
	private String group_id;
	private String auth_token;
	private String role;
	private String group_name;
	private SharedPreferences mPreferences;
	// private String mResult;
	// private String role;
	// private Bundle info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// info = new Bundle();
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");
		// new GetRole().execute();

		Intent in = getIntent();
		group_id = in.getExtras().getString("group_id");
		role = in.getExtras().getString("role");
		group_name = in.getExtras().getString("group_name");

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		// start slide_menu
		setBehindContentView(R.layout.frame);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.frame, new SlideMenuFragment());
		ft.commit();

		this.setSlidingActionBarEnabled(true);
		getSlidingMenu().setShadowWidth(R.dimen.shadow_width);
		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		getSlidingMenu().setBehindScrollScale(0.25f);
		// end slide_menu

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText(group_name);

		// tab background, text color change
		LayoutInflater inflater = this.getLayoutInflater();
		View cView = inflater.inflate(R.layout.tab_layout, null);
		View cView2 = inflater.inflate(R.layout.tab_layout, null);
		View cView3 = inflater.inflate(R.layout.tab_layout, null);

		TextView tv = (TextView) cView.findViewById(R.id.tabs_text_bg);
		tv.setText("Feed");

		TextView tv2 = (TextView) cView2.findViewById(R.id.tabs_text_bg);
		tv2.setText("Step");

		TextView tv3 = (TextView) cView3.findViewById(R.id.tabs_text_bg);
		tv3.setText("Info");

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		mTabsAdapter.addTab(bar.newTab().setCustomView(tv).setText("Feed"),
				FeedFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setCustomView(tv2).setText("Step"),
				StepFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setCustomView(tv3).setText("Info"),
				InfoFragment.class, null);

		bar.setStackedBackgroundDrawable(getResources().getDrawable(
				R.drawable.tap_3_menu));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (role.equals(Global.founder)) {
			menu.add("write")
					.setIcon(R.drawable.title_btn_geo)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add("setting")
					.setIcon(R.drawable.title_btn_setting)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		} else if (role.equals(Global.member)) {
			menu.add("write")
					.setIcon(R.drawable.title_btn_geo)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		} else {
			menu.add("Join").setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM
							| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		if (item.getTitle().equals("write")) {
			Intent intent = new Intent(this, PostPageActivity.class);
			intent.putExtra("group_id", group_id);
			intent.putExtra("role", role);
			startActivity(intent);
			return true;
		} else if (item.getTitle().equals("setting")) {

		} else if (item.getTitle().equals("Join")) {
			new Membershipscreate().execute();
		}
		return false;
	}

	public static class TabsAdapter extends FragmentStatePagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
			Fragment fr = getItem(position);
		}

		public void onPageScrollStateChanged(int state) {
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}

	private class Membershipscreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "memberships?auth_token=" + auth_token);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("membership[group_id]", new StringBody(
						group_id));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				Log.e("my", "Response : " + s);
			} catch (Exception e) {
				Log.e("my", e.getClass().getName() + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}
