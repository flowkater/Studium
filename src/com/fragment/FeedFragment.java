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

		// 績税 持失
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		Bitmap member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_, options);
		Bitmap content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "析馬陥 毘級檎 錘疑聖", "沿佐旦", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_wall, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Post(member,
				"LGD19800据拭 曽鯉 蓄探.. 鉦 2鯵杉 板 薄仙 26500+-.....戚舛亀檎 蓄探拝幻 敗!?", "杉",
				"5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_picture, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Post(member,
				"娃幻拭 耕遂叔亜辞\n 袴軒設串雁ぞ\n 戚依亀 捉走省陥澗汽 畳短捉精暗旭陥ば\n 袴軒 承嬢角掩凶原陥 嬢事嬢事;;",
				"波団", "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_jobs, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_4, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList
				.add(new Post(
						member,
						"店姥企噺澗 塘格胡闘稽 遭楳 桔艦陥.\n恥 11誤戚 走据切亜 赤醸壱, a,b,c 益血生稽 蟹寛辞 唖 益血税 1去聖 嗣精 陥製拭 軒益穿聖 遭楳背辞 1,2,3去聖 舛拝 位艦陥!\n\n唖切 企遭妊研 溌昔馬獣壱\n1腰 属 井奄澗 陥製 爽 鉢推析 8杉 28析猿走 辞稽 尻喰背辞 衣引研 鎧獣壱 煽拭惟 硝形爽...",
						"説什", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ladygaga, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_5, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "酔腎 せせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせせ庁背左昔雁", "傾戚巨亜亜", content, "5"));

		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ya, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.post_content_6, options);
		content = Bitmap.createScaledBitmap(orgImage, 100, 100, true);
		mArrayList.add(new Post(member, "Ministry of sound 傾穿球虞壱 梅揮暗 昼社.", "醤", content, "5"));

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
