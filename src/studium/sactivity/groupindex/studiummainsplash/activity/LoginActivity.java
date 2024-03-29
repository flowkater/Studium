package studium.sactivity.groupindex.studiummainsplash.activity;

import java.io.*;
import java.util.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.json.*;

import com.actionbarsherlock.app.SherlockActivity;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.model.ParsedLoginDataSet;
import com.utils.Global;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends SherlockActivity {
	private JSONObject jObject;
	private ProgressDialog mProgressDialog;
	String loginmessage = null;
	Thread t;
	private SharedPreferences mPreferences;
	private Button signin;
	private TextView regist;
	EditText mEmailField;
	EditText mPasswordField;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

		setContentView(R.layout.login_page);

		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE); // SharedPreference
																			// Load
		mEmailField = (EditText) findViewById(R.id.login_id_edit_text);
		mPasswordField = (EditText) findViewById(R.id.login_pw_edit_text);
		regist = (TextView) findViewById(R.id.login_join_button);
		regist.setOnClickListener(new RegistClickListener());

		if (getIntent().getStringExtra("email") != null) {

			mEmailField.setText(getIntent().getStringExtra("email"));
			mPasswordField.setText(getIntent().getStringExtra("password"));

		}

		if (!checkLoginInfo()) { // don't exist user
			signin = (Button) findViewById(R.id.login_btn);
			signin.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showDialog(0); // when user click, show dialog
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
			Intent intent = new Intent(this, GroupIndexActivity.class);
			startActivity(intent);
			finish();
		}
	}

	class RegistClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(in);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("로그인 중입니다....");
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
			HashMap<String, String> sessionTokens = signIn(); // Login method
																// call
		} catch (Exception e) {
			removeDialog(0);
			Toast.makeText(getApplicationContext(), "로그인 에러!",
					Toast.LENGTH_LONG).show();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String loginmsg = (String) msg.obj;
			if (loginmsg.equals("SUCCESS")) {
				removeDialog(0);
				Toast.makeText(getApplicationContext(),
						"로그인 성공!", Toast.LENGTH_LONG).show();

				Intent intent = new Intent(LoginActivity.this,
						GroupIndexActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	private HashMap<String, String> signIn() {

		HashMap<String, String> sessionTokens = null;

		mEmailField = (EditText) findViewById(R.id.login_id_edit_text);
		mPasswordField = (EditText) findViewById(R.id.login_pw_edit_text);

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
			Toast.makeText(getApplicationContext(), "로그인 에러!",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "로그인 에러!",
					Toast.LENGTH_LONG).show();
		}
		ParsedLoginDataSet parsedLoginDataSet = new ParsedLoginDataSet(); // ?
		try {

			sessionTokens = parseToken(response);
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
			removeDialog(0);
			Toast.makeText(getApplicationContext(), "Login Error!",
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
		} else {
			sessionTokens.put("error", "Error");
		}
		return sessionTokens;
	}

	// Checking whether the username and password has stored already or not
	private final boolean checkLoginInfo() {
//		boolean username_set = mPreferences.contains("UserName");
//		boolean password_set = mPreferences.contains("PassWord");
		boolean authtoken_set = mPreferences.contains("AuthToken");
		if (authtoken_set) {
			return true;
		}
		return false;
	}

}
