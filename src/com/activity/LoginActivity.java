package com.activity;

import java.io.*;
import java.util.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.json.*;

import com.actionbarsherlock.app.SherlockActivity;
import com.model.ParsedLoginDataSet;
import com.utils.Global;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends SherlockActivity {
	private final String mainActivityTag = "LogInActivity";
	private JSONObject jObject;
	private ProgressDialog mProgressDialog;
	String loginmessage = null;
	Thread t;
	private SharedPreferences mPreferences;
	private Button signin;
	private TextView regist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		
		setContentView(R.layout.login_page);

		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE); // SharedPreference
																			// Load
		
		regist = (TextView)findViewById(R.id.login_join_button);
		regist.setOnClickListener(new RegistClickListener());

		if (!checkLoginInfo()) { // don't exist user			
			signin = (Button) findViewById(R.id.login_btn);
			signin.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showDialog(0); //when user click, show dialog
					t = new Thread() { // Thread
						public void run() {
							try {
								authenticate();
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					t.start();
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "Login State!",
					Toast.LENGTH_LONG).show();
			/*
			 * Directly opens the Welcome page, if the username and password is
			 * already available in the SharedPreferences
			 */

			// Trying to minimize the number of screens
			// Intent intent = new Intent(this, MainMenuActivity.class);
			// startActivity(intent);
			// finish();
		}
	}
	
	class RegistClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getApplicationContext(),RegisterActivity.class);
			startActivity(in);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) { //Dialog preference
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait while signing in ...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return null;
	}

	private void authenticate() throws ClientProtocolException, IOException {
		try {
			String pin = "";
			HashMap<String, String> sessionTokens = signIn(); // Login method call

		} catch (Exception e) {
			// Intent intent = new Intent(getApplicationContext(),
			// LoginError.class);
			// intent.putExtra("LoginMessage", "Unable to login");
			// startActivity(intent);

			removeDialog(0);
			Toast.makeText(getApplicationContext(), "Authenticate Fail!",
					Toast.LENGTH_LONG).show();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String loginmsg = (String) msg.obj;
			if (loginmsg.equals("SUCCESS")) {
				removeDialog(0);
				// Intent intent = new Intent(getApplicationContext(),
				// WorkoutDetailsListActivity.class);
				// //Intent intent = new Intent(getApplicationContext(),
				// MainMenuActivity.class);
				// startActivity(intent);
				// finish();
				Toast.makeText(getApplicationContext(),
						"Authenticate SUCCESS!", Toast.LENGTH_LONG).show();
			}
		}
	};

	private HashMap<String, String> signIn() {

		HashMap<String, String> sessionTokens = null;

		EditText mEmailField = (EditText) findViewById(R.id.login_id_edit_text);
		EditText mPasswordField = (EditText) findViewById(R.id.login_pw_edit_text);

		String email = mEmailField.getText().toString();
		String password = mPasswordField.getText().toString();

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Global.ServerUrl + "sessions");
		JSONObject holder = new JSONObject();
		JSONObject userObj = new JSONObject();

		try {
			userObj.put("password", password);
			userObj.put("email", email);
			holder.put("user", userObj);
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException js) {
			js.printStackTrace();
		}

		String response = null;
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(post, responseHandler); // ?
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ParsedLoginDataSet parsedLoginDataSet = new ParsedLoginDataSet(); // ?
		// ParsedLoginDataSet parsedLoginDataSet =
		// myLoginHandler.getParsedLoginData();
		try {

			/*
			 * if (response == null) { System.out.println("response is null " +
			 * response); Exception e = new Exception(); throw e; }
			 */
			sessionTokens = parseToken(response);
			// now = Long.valueOf(System.currentTimeMillis());
			// mSignInDbHelper.createSession(mEmailField.getText().toString(),mAuthToken,now);
		} catch (Exception e) {
			e.printStackTrace();
		}
		parsedLoginDataSet.setExtractedString(sessionTokens.get("error"));
		if (parsedLoginDataSet.getExtractedString().equals("Success")) {
			// Store the username and password in SharedPreferences after the
			// successful login
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putString("UserName", email);
			editor.putString("PassWord", password);
			editor.putString("AuthToken", sessionTokens.get("auth_token"));
			editor.commit();
			Message myMessage = new Message();
			myMessage.obj = "SUCCESS";
			handler.sendMessage(myMessage);
		} else {
			// Intent intent = new Intent(getApplicationContext(),
			// LoginError.class);
			// intent.putExtra("LoginMessage", "Invalid Login");
			// startActivity(intent);
			removeDialog(0);
			Toast.makeText(getApplicationContext(), "Login Parse Error!",
					Toast.LENGTH_LONG).show();
		}

		return sessionTokens;

	}

	public HashMap<String, String> parseToken(String jsonResponse)
			throws Exception {
		HashMap<String, String> sessionTokens = new HashMap<String, String>();
		if (jsonResponse != null) {
			jObject = new JSONObject(jsonResponse);
			JSONObject sessionObject = jObject.getJSONObject("session");
			String attributeError = sessionObject.getString("error");
			String attributeToken = sessionObject.getString("auth_token");
			sessionTokens.put("error", attributeError);
			sessionTokens.put("auth_token", attributeToken);

			// String attributeConsumerKey =
			// sessionObject.getString("consumer_key");
			// String attributeConsumerSecret = sessionObject
			// .getString("consumer_secret");
			// sessionTokens.put("consumer_key", attributeConsumerKey);
			// sessionTokens.put("consumer_secret", attributeConsumerSecret);
		} else {
			sessionTokens.put("error", "Error");
		}
		return sessionTokens;
	}

	// Checking whether the username and password has stored already or not
	private final boolean checkLoginInfo() {
		boolean username_set = mPreferences.contains("UserName");
		boolean password_set = mPreferences.contains("PassWord");
		boolean authtoken_set = mPreferences.contains("AuthToken");
		if (username_set || password_set || authtoken_set) {
			return true;
		}
		return false;
	}

}
