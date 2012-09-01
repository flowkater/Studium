package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.utils.Global;

public class RegisterActivity extends SherlockActivity {
	private EditText mNameEditText;
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private EditText mPhoneEditText;
	private Button mRegistButton;
	private String name;
	private String email;
	private String password;
	private String phone;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().hide();
		setContentView(R.layout.regist_page);

		mNameEditText = (EditText) findViewById(R.id.member_name_edit_text);
		mEmailEditText = (EditText) findViewById(R.id.member_id_edit_text);
		mPasswordEditText = (EditText) findViewById(R.id.member_password_edit_text);
		mPhoneEditText = (EditText) findViewById(R.id.member_phone_num_edit_text);
		mRegistButton = (Button) findViewById(R.id.regist_btn);

		mRegistButton.setOnClickListener(new RegisterClickListener());

		super.onCreate(savedInstanceState);
	}

	class RegisterClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			new Usercreate().execute();
			name = mNameEditText.getText().toString();
			email = mEmailEditText.getText().toString();
			password = mPasswordEditText.getText().toString();
		}
	}

	class Usercreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				bm.compress(CompressFormat.JPEG, 75, bos);
//				byte[] data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(
						Global.ServerUrl + "users");
				// HttpPost("http://192.168.0.12:3000/posts.json");
//				ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				// reqEntity.addPart("post[post_image]", bab);
				// reqEntity.addPart("post[pictures_attributes][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][0][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][1][image]", bab);
//				reqEntity.addPart("post[pictures_attributes][2][image]", bab);
				reqEntity.addPart("user[email]", new StringBody(email));
				reqEntity.addPart("user[password]", new StringBody(password));
				reqEntity.addPart("user[password_confirmation]", new StringBody(password));
				reqEntity.addPart("user[name]", new StringBody(name));
//				reqEntity.addPart("user[gender]", new StringBody("male"));
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
