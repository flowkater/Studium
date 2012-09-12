package studium.sactivity.groupindex.studiummainsplash.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.studium.adapter.GroupWaitingAdapter;
import com.studium.model.User;
import com.studium.model.Users;
import com.utils.Global;
import com.utils.NetHelper;

public class GroupManageActivity extends SherlockActivity implements OnItemClickListener{
	private String group_id;
	private TextView titlebar_text;
	private ArrayList<User> mArrayList;
	private String mResult;
	private PullToRefreshListView mListView;
	private ArrayAdapter<User> mAdapter;
	private LinearLayout headerview;
	private String auth_token;
	private SharedPreferences mPreferences;
	private Button create_partymessage_btn;
	private ProgressDialog mProgressDialog;
	private String memberId;
	private String memberName;
	private String memberPhone;
	private String memberGender;
	private int code;
	private User selectuser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		setContentView(R.layout.group_manage_waiting_list);
		Intent in = getIntent();
		group_id = in.getExtras().getString("group_id");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("스터디 관리");
		// end header
		mArrayList = new ArrayList<User>();
		new GetWaitingList().execute();
		mListView = (PullToRefreshListView) findViewById(R.id.waiting_list);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				new GetWaitingList().execute();
			}
		});
		mAdapter = new GroupWaitingAdapter(getApplicationContext(),
				R.layout.group_manage_waiting_list_low, mArrayList);
		LayoutInflater inflater = this.getLayoutInflater();
		headerview = (LinearLayout) inflater.inflate(
				R.layout.group_manage_header, null);
		create_partymessage_btn = (Button) headerview
				.findViewById(R.id.create_partymessage_btn);
		create_partymessage_btn.setOnClickListener(new PartymClickListener());
		//
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().addHeaderView(headerview);
		mListView.getRefreshableView().setAdapter(mAdapter);
	}

	class PartymClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getApplicationContext(),
					PartymessageCreateActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.putExtra("group_id", group_id);
			startActivity(in);
		}
	}

	private class GetWaitingList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl
					+ "/groups/"+group_id+"/founder.json?auth_token=" + auth_token);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Users users = gson.fromJson(mResult, Users.class);
			try {
				for (User user : users.getUsers()) {
					mArrayList.add(user);
				}
			} catch(Exception e){
				Toast.makeText(getApplicationContext(), "인터넷 연결상태가 좋지 않습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_LONG).show();
			}
			
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		selectuser = mArrayList.get(position-2);
		showDialog(1);
	}
	
	class Membershipsaccept extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl + "/memberships/accept.json?auth_token=" + auth_token);
				
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("membership[user_id]",
						new StringBody(selectuser.getId(), Charset.forName("UTF-8")));
				reqEntity.addPart("membership[group_id]",
						new StringBody(group_id, Charset.forName("UTF-8")));

				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);

				if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
					System.out.println("Success");
					code = 1;
				}else{
					System.out.println("Error");
					code = 2;
				}

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
				removeDialog(0);
				Log.e("my", e.getClass().getName() + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (code==1) {
				Intent in = new Intent(getApplicationContext(),GroupManageActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				in.putExtra("group_id",group_id);
				startActivity(in);
				removeDialog(0);
			}else if(code==2){
				
			}
			
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("확인").setShowAsAction(
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
		if (item.getTitle().equals("확인")) {
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		final Dialog dialog = null;
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		case 1: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("가입승인");
			builder.setMessage("승인하시겠습니까?\n이름: " + selectuser.getName() + "\n성별: " + selectuser.getGender());
			builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					showDialog(0);
					new Membershipsaccept().execute();
				}
				
			});
			builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					dialog.dismiss();
				}
				
			});
			AlertDialog dAlert = builder.create();
			return dAlert;
		}
		}
		return null;
	}
}
