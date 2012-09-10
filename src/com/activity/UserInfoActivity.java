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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class UserInfoActivity extends SherlockActivity {
	private TextView titlebar_text;
	private String email;
	private String name;
	private String phone;
	private String id;
	private String auth_token;
	private TextView emailtv;
	private TextView nameed;
	private TextView phoneed;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_preference);
		mPreferences = getSharedPreferences("CurrentUser",MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");
		
		Intent in = getIntent();
		id = in.getStringExtra("id");
		email = in.getStringExtra("email");
		name = in.getStringExtra("name");
		phone = in.getStringExtra("phone");

		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("내 정보");
		// end header
		
		emailtv = (TextView)findViewById(R.id.user_email_text_view);
		nameed =  (TextView)findViewById(R.id.user_name_edit_text); 
		phoneed = (TextView)findViewById(R.id.user_phone_num_edit_text);
		
		emailtv.setText(email);
		nameed.setText(name);
		phoneed.setText(phone);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("확인")
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		if (item.getTitle().equals("확인")) {
//			name = nameed.getText().toString();
//			phone = phoneed.getText().toString();
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	class Userupdate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "users.json?auth_token=" + auth_token);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				
				reqEntity.addPart("user[name]",
						new StringBody(name, Charset.forName("UTF-8")));
				reqEntity.addPart("user[phone]", new StringBody(phone,
						Charset.forName("UTF-8")));
				
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
			return null;
		}
	}
	*/
}
