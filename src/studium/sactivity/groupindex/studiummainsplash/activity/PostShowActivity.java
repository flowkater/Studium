package studium.sactivity.groupindex.studiummainsplash.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.studium.adapter.PostCommentAdapter;
import com.studium.model.Comment;
import com.studium.model.Comments;
import com.utils.Global;
import com.utils.ImageDownloader;
import com.utils.NetHelper;

public class PostShowActivity extends SherlockActivity {
	private ArrayList<Comment> mArrayList;
	private PostCommentAdapter mAdapter;
	private PullToRefreshListView mListView;
	private int mPrevTotalItemCount = 0;
	private Integer mCurrentPage = 1;
	private LinearLayout headerview;
	private ImageView member_img;
	private ImageView content_img;
	private TextView member_name;
	private TextView create_time;
	private TextView post_body;
	private TextView titlebar_text;
	private String mResult;
	private String post_id; // post_id
	private String group_id; // group_id;
	private String role;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_show_comment_list);
		Intent in = getIntent();
		post_id = in.getExtras().getString("post_id");
		group_id = in.getExtras().getString("group_id");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header
		
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("�ۺ���");

		mArrayList = new ArrayList<Comment>();

		mListView = (PullToRefreshListView) findViewById(R.id.post_show_comment_list);

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				mCurrentPage = 1;
				mPrevTotalItemCount = 0;
			}
		});

		mAdapter = new PostCommentAdapter(this, R.layout.post_show_comment_row,
				mArrayList);

		LayoutInflater inflater = getLayoutInflater();

		headerview = (LinearLayout) inflater.inflate(R.layout.post_show_header,
				null);

		member_img = (ImageView) headerview.findViewById(R.id.member_img);
		member_name = (TextView) headerview.findViewById(R.id.member_name);
		create_time = (TextView) headerview.findViewById(R.id.create_time);
		post_body = (TextView) headerview.findViewById(R.id.post_body);
		content_img = (ImageView) headerview.findViewById(R.id.post_content_img);

		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnScrollListener(
				new EndlessScrollListener());

	}

	private class GetCommentList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl + "groups/"
					+ group_id + "/posts/" + post_id + ".json?page=" + mCurrentPage.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONObject post = new JSONObject(mResult);
				member_name.setText(post.getString("name"));
				create_time.setText(post.getString("created_at"));
				post_body.setText(post.getString("body"));
				String image = post.getString("image");
				if (image.equals("")) {
					member_img.setImageResource(R.drawable.photo_frm);
				} else {
					ImageDownloader.download(Global.ServerUrl + image,
							member_img);
				}
				String content_image = post.getString("content_image");
				if (content_image.equals("")) {
					content_img.setImageResource(R.drawable.photo_frm);
				}else{
					ImageDownloader.download(Global.ServerUrl + content_image,
							content_img);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			Comments comments = gson.fromJson(mResult, Comments.class);
			for (Comment comment : comments.getComments()) {
				mArrayList.add(comment);
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
				new GetCommentList().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("���")
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		if (item.getTitle().equals("���")) {
			Intent intent = new Intent(this, CommentPageActivity.class);
			intent.putExtra("post_id", post_id);
			intent.putExtra("group_id", group_id);
			startActivity(intent);
			return true;
		}
		return false;
	}
}
