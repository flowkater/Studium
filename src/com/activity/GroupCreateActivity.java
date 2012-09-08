package com.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GroupCreateActivity extends SherlockActivity{
	private TextView titlebar_text;
	private EditText group_name_edit_text;
	private EditText group_goal_edit_text;
	private EditText group_location_edit_text;
	private EditText group_category_edit_text;
	private ImageView group_img;
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
		// end header
		
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");
		
		titlebar_text = (TextView)findViewById(R.id.titlebar_text);
		titlebar_text.setText("Group#Create");
	
		group_name_edit_text = (EditText)findViewById(R.id.group_name_edit_text);
		group_goal_edit_text = (EditText)findViewById(R.id.group_goal_edit_text);
		group_location_edit_text = (EditText)findViewById(R.id.group_location_edit_text);
		group_category_edit_text = (EditText)findViewById(R.id.group_category_edit_text);
		group_img = (ImageView)findViewById(R.id.group_img);
		
		
		group_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Next")
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
		Intent in = new Intent(getApplication(),GroupCreate2Activity.class);
		in.putExtra("auth_token", auth_token);
		in.putExtra("group_name", group_name_edit_text.getText().toString());
		in.putExtra("group_goal", group_goal_edit_text.getText().toString());
		in.putExtra("group_location", group_location_edit_text.getText().toString());
		in.putExtra("group_category", group_category_edit_text.getText().toString());
		
		finish();
		startActivity(in);
		return super.onOptionsItemSelected(item);
	}
}

