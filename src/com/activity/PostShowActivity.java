package com.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adapter.PostCommentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Comment;

public class PostShowActivity extends SherlockActivity {
	private ArrayList<Comment> mArrayList;
	private PostCommentAdapter mAdapter;
	private PullToRefreshListView mListView;
	private LinearLayout headerview;
	private TextView post_body;
	TextView titlebar_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_show_comment_list);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		titlebar_text = (TextView)findViewById(R.id.titlebar_text);
		titlebar_text.setText("Post#Show");
		

		mArrayList = new ArrayList<Comment>();
		// 임의 생성
		for (int i = 0; i < 5; i++) 
			mArrayList.add(new Comment("이건 댓글이오."));
		

		mListView = (PullToRefreshListView) findViewById(R.id.post_show_comment_list);

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mListView.onRefreshComplete();
				// mArrayList.clear();
				// mCurrentPage = 1;
				// mPrevTotalItemCount = 0;
			}
		});

		mAdapter = new PostCommentAdapter(this, R.layout.post_show_comment_row,
				mArrayList);

		LayoutInflater inflater = getLayoutInflater();

		headerview = (LinearLayout) inflater.inflate(R.layout.post_show_header,
				null);
		
		post_body = (TextView) headerview.findViewById(R.id.post_body);
		post_body.setText("여기엔 나의 잡소리가 들어간다 많은 글을 여기서 쓸거고 이미지도 들어갈거임 근데 아직 안드감");

		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
		// mListView.getRefreshableView().setOnScrollListener(
		// new EndlessScrollListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("search")
				.setIcon(R.drawable.title_btn_geo)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
}
