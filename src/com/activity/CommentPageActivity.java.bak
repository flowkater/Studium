package com.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adapter.PostCommentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Comment;

public class CommentPageActivity extends SherlockActivity {
	private ArrayList<Comment> mArrayList;
	private PostCommentAdapter mAdapter;
	private PullToRefreshListView mListView;
	private LinearLayout headerview;
	private TextView post_body;
	TextView titlebar_text;
	EditText comment_et;
	String comment_string;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Comment#Page");

		// comment 작성 화면

		comment_et = (EditText) findViewById(R.id.post_string);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("입력")
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock

		Intent intent = new Intent(this, PostShowActivity.class);
		 comment_string = comment_et.getText().toString();
		Bundle extras = new Bundle();
		extras.putString("comment_string", comment_string);
		intent.putExtras(extras);
		startActivity(intent);

		return true;
	}

}
