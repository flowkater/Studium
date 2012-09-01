package com.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.utils.Global;

public class GroupCreateActivity extends SherlockActivity{
	private TextView titlebar_text;
	private EditText group_name_edit_text;
	private EditText group_goal_edit_text;
	private EditText group_location_edit_text;
	private String name;
	private String goal;
	private String location;
	private ImageView group_img;
	private Button group_create_btn;
	private SharedPreferences mPreferences;
	private String auth_token;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");
		
		titlebar_text = (TextView)findViewById(R.id.titlebar_text);
		titlebar_text.setText("Group#Create");
	
		group_name_edit_text = (EditText)findViewById(R.id.group_name_edit_text);
		group_goal_edit_text = (EditText)findViewById(R.id.group_goal_edit_text);
		group_location_edit_text = (EditText)findViewById(R.id.group_location_edit_text);
		group_img = (ImageView)findViewById(R.id.group_img);
		
		group_create_btn = (Button)findViewById(R.id.group_create_btn);
		
		group_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		group_create_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name = group_name_edit_text.getText().toString();
				goal = group_goal_edit_text.getText().toString();
				location = group_location_edit_text.getText().toString();
				new Groupcreate().execute();
			}
		});
	}
	
	class Groupcreate extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			try {
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				bm.compress(CompressFormat.JPEG, 75, bos);
//				byte[] data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(
						Global.ServerUrl+"groups?auth_token=" + auth_token);
				// HttpPost("http://192.168.0.12:3000/posts.json");
//				ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				// reqEntity.addPart("post[post_image]", bab);
				// reqEntity.addPart("post[pictures_attributes][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][0][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][1][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][2][image]", bab);
				
				reqEntity.addPart("group[goal]", new StringBody(goal));
//				reqEntity.addPart("group[subject]", new StringBody("SAT 유학"));
				reqEntity.addPart("group[place]", new StringBody(location));
				reqEntity.addPart("group[name]", new StringBody(name));
//				reqEntity.addPart("group[introduce]", new StringBody("안녕하세요. 우린 미쿰 SAT 준비하는 사람들입니다."));
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
				Log.e("my", e.getClass().getName() + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}

