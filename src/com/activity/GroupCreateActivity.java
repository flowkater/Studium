package com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.AlertDialog;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GroupCreateActivity extends SherlockActivity implements
		OnClickListener {
	private TextView titlebar_text;
	private EditText group_name_edit_text;
	private EditText group_goal_edit_text;
	private EditText group_location_edit_text;
	private EditText group_category_edit_text;
	private ImageView group_img;
	private SharedPreferences mPreferences;
	private String auth_token;
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int REQUEST_CODE = 3;
	private Bitmap bm;
	private Bitmap resized;

	private Bitmap image;

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

		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Group#Create");

		group_name_edit_text = (EditText) findViewById(R.id.group_name_edit_text);
		group_goal_edit_text = (EditText) findViewById(R.id.group_goal_edit_text);
		group_location_edit_text = (EditText) findViewById(R.id.group_location_edit_text);
		group_category_edit_text = (EditText) findViewById(R.id.group_category_edit_text);
		group_img = (ImageView) findViewById(R.id.group_img);

		group_img.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View v) {
				{
					DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doTakePhotoAction();
						}
					};

					DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doTakeAlbumAction();
						}
					};

					DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					};

					new AlertDialog.Builder(GroupCreateActivity.this)
							.setTitle("select the image")
							.setPositiveButton("take picture", cameraListener)
							.setNeutralButton("album", albumListener)
							.setNegativeButton("cancel", cancelListener).show();
				}
			}
		}

		);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Next").setShowAsAction(
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
		if (group_name_edit_text.length() < 3) {
			Toast.makeText(getApplicationContext(), "스터디이름은 3자리 이상!",
					Toast.LENGTH_LONG).show();
			return true;
		} else if (group_goal_edit_text.length() < 3) {
			Toast.makeText(getApplicationContext(), "스터디목표는 3자리 이상!",
					Toast.LENGTH_LONG).show();
			return true;
		} else if (group_location_edit_text.length() < 3) {
			Toast.makeText(getApplicationContext(), "스터디장소는 3자리 이상!",
					Toast.LENGTH_LONG).show();
			return true;
		} else {

			Intent in = new Intent(getApplication(), GroupCreate2Activity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.putExtra("auth_token", auth_token);
			in.putExtra("group_name", group_name_edit_text.getText().toString());
			in.putExtra("group_goal", group_goal_edit_text.getText().toString());
			in.putExtra("group_location", group_location_edit_text.getText()
					.toString());
			in.putExtra("group_category", group_category_edit_text.getText()
					.toString());

			finish();
			startActivity(in);
			return super.onOptionsItemSelected(item);
		}
	}

	private void doTakePhotoAction() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	/**
	 * to get Image from album
	 */
	private void doTakeAlbumAction() {
		// call album
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP_FROM_CAMERA: {
				image = (Bitmap) data.getExtras().get("data");
				break;
			}

			case PICK_FROM_ALBUM: {
				System.out.println("intent data : " + data.getData());
				if (data.getData() != null) {
					System.out.println("data.getData()!=null");

					Cursor c = getContentResolver().query(
							Uri.parse(data.getDataString()), null, null, null,
							null);
					c.moveToNext();
					// camera error
					String image_url = c.getString(c
							.getColumnIndex(MediaStore.MediaColumns.DATA));
					// camera error
					image = BitmapFactory.decodeFile(image_url);
					resized = resizeBitmapImage(image, 500);
					group_img.setImageBitmap(resized);
				}
				break;

			}

			case PICK_FROM_CAMERA: {
				image = (Bitmap) data.getExtras().get("data");
				resized = resizeBitmapImage(image, 900);
				group_img.setImageBitmap(resized);
				break;
			}
			}
		}
	}

	public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
		int width = source.getWidth();
		int height = source.getHeight();
		int newWidth = width;
		int newHeight = height;
		float rate = 0.0f;

		if (width > height) {
			if (maxResolution < width) {
				rate = maxResolution / (float) width;
				newHeight = (int) (height * rate);
				newWidth = maxResolution;
			}
		} else {
			if (maxResolution < height) {
				rate = maxResolution / (float) height;
				newWidth = (int) (width * rate);
				newHeight = maxResolution;
			}
		}

		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
