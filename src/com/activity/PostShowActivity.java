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
		// ���� ����
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		Bitmap member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"����� �� ������ ���� ������ ������.....","�躴ö"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_wall, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"�� ���� �θ�� �����̿��� �� ���� �մϴ�. �˼��մϴ�. �����Ͽ� �����̶� Deposit �� ���Կ�.","��"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_picture, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"�� ���� �ؾ� ���͵� ����! ��θ�� ȭ����!.","����"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ladygaga, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"���� �̰ź��� ��ģ���� �����ϴ��� �Ф�","���̵𰡰�"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_byung, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"��Ÿ���� �� �ò������ʾƿ�? �츮 ��ҹٲ��!","�躴ö"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_jobs, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"�Ф� �ϴ� �������� ��Ÿ�����ϰ� ������ �ٲٱ���սô�.","�⽺"));
		
		orgImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_ya, options);
		member = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		mArrayList.add(new Comment(member,"����~~ �� ��ģ�̶� �������ؼ� �̹��� �����Կ�~~ �̾ȹ̾�","��"));
		

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
		post_body.setText("���⿣ ���� ��Ҹ��� ���� ���� ���� ���⼭ ���Ű� �̹����� ������ �ٵ� ���� �ȵ尨");

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
