package studium.sactivity.groupindex.studiummainsplash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import studium.sactivity.groupindex.studiummainsplash.activity.R;

public class RegisterActivity extends SherlockActivity {
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private TextView titlebar_text;
	private String email;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.regist_page);
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
		titlebar_text.setText("회원가입");

		mEmailEditText = (EditText) findViewById(R.id.member_email_edit_text);
		mPasswordEditText = (EditText) findViewById(R.id.member_password_edit_text);
		mPasswordEditText.setHint("비밀번호는 6자이상!");

		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("다음")
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
		if (item.getTitle().equals("다음")) {
			email = mEmailEditText.getText().toString();
			password = mPasswordEditText.getText().toString();

			if (email.indexOf("@") == -1 || email.indexOf(".") == -1
					|| email.indexOf("@") > email.indexOf("."))
				Toast.makeText(getApplicationContext(), "ID 형식은 꼭 Email 형식으로!",
						Toast.LENGTH_LONG).show();
			else if (password.length() < 6)
				Toast.makeText(getApplicationContext(), "비밀번호는 6자 이상!",
						Toast.LENGTH_LONG).show();

			else {
				Intent intent = new Intent(RegisterActivity.this,
						Register2Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle extras = new Bundle();

				extras.putString("email", email);
				extras.putString("password", password);
				intent.putExtras(extras);

				startActivity(intent);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
