package com.activity;

import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.actionbarsherlock.app.*;

public class GroupCreateActivity extends SherlockActivity{

	private TextView titlebar_text;
	private EditText group_name_edit_text;
	private EditText group_goal_edit_text;
	private EditText group_location_edit_text;
	private ImageView group_img;
	private Button group_create_btn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
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
				// TODO Auto-generated method stub
				//사진 로직을 넣어놓고 url을 리턴받는다!!!!!!!
			}
		});
		
		group_create_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				//다 디비로 넘겨야함 ㅠㅠㅠㅠ
//				group_name_edit_text.getText().toString();
//				group_goal_edit_text.getText().toString();
//				group_location_edit_text.getText().toString();
				//group_img.getText().toString(); //유알엘 받은거를 디비로 넘긴다
				
				//새로 등록한 스터디 그룹으로 넘어간다
				//Intent intent =
			}
		});
		
//		mArrayList = new ArrayList<Group>();
//		
//		for (int i = 0; i < 5; i++) 
//			mArrayList.add(new Group("불사조", "너와나는 성공한다.", "강남구", ""));
		

		
	}
}
