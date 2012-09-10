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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.AlertDialog;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

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
	private String goal;
	private String subject;
	private String location;
	private String name;

	private Bitmap image;
	private ProgressDialog mProgressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// end header

		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("그룹생성");

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
		menu.add("생성").setShowAsAction(
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
			Toast.makeText(getApplicationContext(), "그룹 이름 3자 이상",
					Toast.LENGTH_LONG).show();
			return true;
		} else if (group_goal_edit_text.length() < 3) {
			Toast.makeText(getApplicationContext(), "목표 3자 이상!",
					Toast.LENGTH_LONG).show();
			return true;
		} else if (group_location_edit_text.length() < 3) {
			Toast.makeText(getApplicationContext(), "지역 3자 이상!",
					Toast.LENGTH_LONG).show();
			return true;
		} else {
			if (resized!=null) {
				bm = resized;
			}
			name =group_name_edit_text.getText().toString();
			goal = group_goal_edit_text.getText().toString();
			location = group_location_edit_text.getText()
					.toString();
			subject = group_category_edit_text.getText()
					.toString();
			
			new Groupcreate().execute();
			return super.onOptionsItemSelected(item);
		}
	}
	class Groupcreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				ByteArrayBody bab = null;
				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					bab = new ByteArrayBody(data, "group_image.jpg");
				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "groups.json?auth_token=" + auth_token);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("group[goal]",
						new StringBody(goal, Charset.forName("UTF-8")));
				reqEntity.addPart("group[subject]", new StringBody(subject,
						Charset.forName("UTF-8")));
				reqEntity.addPart("group[place]", new StringBody(location,
						Charset.forName("UTF-8")));
				reqEntity.addPart("group[name]",
						new StringBody(name, Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("group[pictures_attributes][0][image]",
							bab);
				}
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
		@Override
		protected void onPostExecute(Void result) {
			finish();
			Intent in = new Intent(getApplicationContext(),
					GroupIndexActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			removeDialog(0);
			super.onPostExecute(result);
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
	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return null;
	}

}
