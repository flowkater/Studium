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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class Register2Activity extends SherlockActivity implements
		OnClickListener {
	private ImageView mMember_img;
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private Uri mImageCaptureUri;
	private Bitmap resized;
	private Bitmap image;
	private AlertDialog mDialog;
	private String email;
	private String password;

	private TextView titlebar_text;
	private EditText member_name_edit_text;
	private EditText member_phone_num_edit_text;

	private String name;
	private String phone;
	private String gender;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.regist_page2);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Register2");
		// end header

		member_name_edit_text = (EditText) findViewById(R.id.member_name_edit_text);
		member_phone_num_edit_text = (EditText) findViewById(R.id.member_phone_num_edit_text);

		Intent in = getIntent();
		email = in.getStringExtra("email");
		password = in.getStringExtra("password");

		super.onCreate(savedInstanceState);
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.radio_btn_man:
			gender = "male";
			break;

		case R.id.radio_btn_woman:
			gender = "female";
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Join").setShowAsAction(
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
		if (item.getTitle().equals("Join")) {
			name = member_name_edit_text.getText().toString();
			phone = member_phone_num_edit_text.getText().toString();
			bm = resized;
			new Usercreate().execute();
			return true;
		}
		return true;
	}

	class Usercreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				ByteArrayBody bab = null;
				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					bab = new ByteArrayBody(data, "user_image.jpg");
				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl + "users");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("user[email]",
						new StringBody(email, Charset.forName("UTF-8")));
				reqEntity.addPart("user[password]", new StringBody(password,
						Charset.forName("UTF-8")));
				reqEntity.addPart("user[password_confirmation]",
						new StringBody(password, Charset.forName("UTF-8")));
				reqEntity.addPart("user[name]",
						new StringBody(name, Charset.forName("UTF-8")));
				reqEntity.addPart("user[gender]", new StringBody(gender,
						Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("user[avatar]", bab);
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
				Log.e("my", e.getClass().getName() + e.getMessage());
			}
			finish();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
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
					String image_url = c.getString(c
							.getColumnIndex(MediaStore.MediaColumns.DATA));
					image = BitmapFactory.decodeFile(image_url);
					resized = resizeBitmapImage(image, 500);
					mMember_img.setImageBitmap(resized);

				}
				break;

			}

			case PICK_FROM_CAMERA: {
				System.out.println("Fffffffffff");

				image = (Bitmap) data.getExtras().get("data");

				resized = resizeBitmapImage(image, 500);

				mMember_img.setImageBitmap(resized);

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

		new AlertDialog.Builder(this).setTitle("select the image")
				.setPositiveButton("take picture", cameraListener)
				.setNeutralButton("album", albumListener)
				.setNegativeButton("cancel", cancelListener).show();
	}

	public void setImage() {
		mMember_img.setImageURI(mImageCaptureUri);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("doTake", "onSaveInstanceState");

		int[] imgFlag = new int[1];
		// Flag initialize
		for (int i = 0; i < imgFlag.length; i++) {
			imgFlag[i] = 0;
		}

		for (int i = 0; i < 1; i++) {
			if (resized != null) {
				imgFlag[i] = 1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				resized.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				outState.putByteArray("Bitmap" + i, baos.toByteArray());
			}
			outState.putInt("img" + i, imgFlag[i]);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("doTake", "onRestoreInstanceState");

		int[] imgFlag = new int[1];
		for (int i = 0; i < 1; i++) {
			imgFlag[i] = savedInstanceState.getInt("img" + i);
			if (imgFlag[i] == 1) {
				byte[] b = savedInstanceState.getByteArray("Bitmap" + i);

				resized = BitmapFactory.decodeByteArray(b, 0, b.length);

			}
		}

		mMember_img.setImageBitmap(resized);
		super.onRestoreInstanceState(savedInstanceState);
	}
}
