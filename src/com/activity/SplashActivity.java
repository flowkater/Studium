package com.activity;

import android.app.*;
import android.content.*;
import android.os.*;

public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();
			}
		};

		handler.sendEmptyMessageDelayed(0, 3000);
	}
}
