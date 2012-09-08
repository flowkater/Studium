package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class GroupCreate2Activity extends SherlockActivity{
	private ProgressDialog mProgressDialog;
	private TextView titlebar_text;
	private String name;
	private String goal;
	private String location;
	private String subject;
	private String auth_token;
	private Bitmap bm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_create_plan);
		Intent in = getIntent();
		name = in.getStringExtra("group_name");
		goal = in.getStringExtra("group_goal");
		location = in.getStringExtra("group_location");
		subject = in.getStringExtra("group_category");
		auth_token = in.getStringExtra("auth_token");
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header
		
		
		titlebar_text = (TextView)findViewById(R.id.titlebar_text);
		titlebar_text.setText("Group#Create2");
		
		
	}
	
	class Groupcreate extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			try {
				ByteArrayBody bab = null;
				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					bab = new ByteArrayBody(data, "group_image.jpg");
				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(
						Global.ServerUrl+"groups.json?auth_token=" + auth_token);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				
				reqEntity.addPart("group[goal]", new StringBody(goal,Charset.forName("UTF-8")));
				reqEntity.addPart("group[subject]", new StringBody(subject,Charset.forName("UTF-8")));
				reqEntity.addPart("group[place]", new StringBody(location,Charset.forName("UTF-8")));
				reqEntity.addPart("group[name]", new StringBody(name,Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("group[pictures_attributes][0][image]",bab);
				}
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
			}
			removeDialog(0);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("write").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showDialog(0);
		
		new Groupcreate().execute();

		finish();
		return true;
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

