package com.activity;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		Bitmap member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"팀장님 넘 빡새요 ㅎㅎ 숙제좀 많은듯.....","김병철"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_wall, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"저 오늘 부모님 생신이여서 못 갈듯 합니다. 죄송합니다. 수요일에 벌금이랑 Deposit 꼭 낼게요.","월"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_picture, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"이 정도 해야 스터디가 되죠! 모두모두 화이팅!.","픽쳐"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ladygaga, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"지금 이거보고 미친듯이 숙제하는중 ㅠㅠ","레이디가가"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"스타벅스 넘 시끄럽지않아요? 우리 장소바꿔요!","김병철"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_jobs, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"ㅠㅠ 일단 월요일은 스타벅스하고 다음에 바꾸기로합시다.","잡스"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ya, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"오빠~~ 나 남친이랑 놀러가기로해서 이번만 빠질게용~~ 미안미안","야"));
		

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
