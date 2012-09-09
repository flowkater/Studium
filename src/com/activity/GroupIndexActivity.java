package com.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fragment.GroupListFragment;
import com.fragment.PartyMessagesFragment;
import com.fragment.SlideMenuFragment;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class GroupIndexActivity extends SlidingFragmentActivity  {
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	TextView titlebar_text;
	TextView tabCenter;
	TextView tabText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		// tab background, text color change
		LayoutInflater inflater = this.getLayoutInflater();
		View cView = inflater.inflate(R.layout.tab_layout, null);
		View cView2 = inflater.inflate(R.layout.tab_layout, null);

		TextView tv = (TextView) cView.findViewById(R.id.tabs_text_bg);
		tv.setText("Group Message");

		TextView tv2 = (TextView) cView2.findViewById(R.id.tabs_text_bg);
		tv2.setText("Group List");

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		mTabsAdapter.addTab(bar.newTab().setCustomView(tv).setText("모집메시지"),
				PartyMessagesFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setCustomView(tv2).setText("그룹"),
				GroupListFragment.class, null);
		bar.setStackedBackgroundDrawable(getResources().getDrawable(
				R.drawable.tap_2_menu));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("create")
				.setIcon(R.drawable.create_group_icon)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		if (item.getTitle().equals("create")) {
			
		}
		return super.onOptionsItemSelected(item);
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
}