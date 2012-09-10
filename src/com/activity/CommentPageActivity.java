package com.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class CommentPageActivity extends SherlockActivity {
	private ProgressDialog mProgressDialog;
	private TextView titlebar_text;
	private EditText comment_body;
	private String comment_string;
	private SharedPreferences mPreferences;
	private String auth_token;
	private String post_id;
	private String group_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");
		Intent in = getIntent();
		post_id = in.getExtras().getString("post_id");
		group_id = in.getExtras().getString("group_id");

		setContentView(R.layout.comment_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Comment#Page");

		// comment �ۼ� ȭ��
		comment_body = (EditText) findViewById(R.id.comment_edit_text);

	}

	private class Commentcreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl + "posts/"
						+ post_id + "/comments.json?auth_token=" + auth_token);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("comment[body]", new StringBody(
						comment_string, Charset.forName("UTF-8")));

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
				removeDialog(0);
				Log.e("my", e.getClass().getName() + e.getMessage());
				Toast.makeText(getApplicationContext(), "Error!",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			finish();
			Intent in = new Intent(getApplicationContext(),
					PostShowActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.putExtra("post_id", post_id);
			in.putExtra("group_id", group_id);
			startActivity(in);
			removeDialog(0);
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("�Է�").setShowAsAction(
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
		if (item.getTitle().equals("�Է�")) {
			showDialog(0);
			comment_string = comment_body.getText().toString();
			new Commentcreate().execute();
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return null;
	}

}
