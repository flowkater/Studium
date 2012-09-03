package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class PostPageActivity extends SherlockActivity implements
		OnClickListener {
	private EditText post_body;
	private TextView titlebar_text;
	private EditText post_et;
	private String post_string;

	private SharedPreferences mPreferences;

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int REQUEST_CODE = 3;
	private Uri mImageCaptureUri;
	private ImageView mPhotoImageView;
	private Button mButton;
	private String group_id;
	private String auth_token;

	private Bitmap bm;

	Bitmap[] image = new Bitmap[6];
	AlertDialog mDialog;
	int current_position = 0;
	int sampled = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		Intent in = getIntent();
		group_id = in.getExtras().getString("group_id");

		setContentView(R.layout.post_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Post#Page");

		// post
		post_body = (EditText) findViewById(R.id.post_string);

		mButton = (Button) findViewById(R.id.postcreate_img_btn);
		mPhotoImageView = (ImageView) findViewById(R.id.postcreate_img);

		mButton.setOnClickListener(this);
	}

	// Post Create Server Module
	private class Postcreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				
//				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					ByteArrayBody bab = new ByteArrayBody(data, "post_image.jpg");
//				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "groups/" + group_id + "/posts?auth_token="
						+ auth_token);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("post[body]", new StringBody(post_string));
				reqEntity.addPart("post[posttype]", new StringBody("1"));
				if (bab !=null) {
					reqEntity.addPart("post[pictures_attributes][0][image]", bab);
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
				Toast.makeText(getApplicationContext(), "Error!",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	private void doTakePhotoAction() {
		/*
		 * References http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http
		 * ://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-
		 * captured -image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// temp file url
		// String url = "DCIM/AT" + String.valueOf(System.currentTimeMillis()) +
		// ".jpg";
		// mImageCaptureUri = Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory(), url));
		//
		//
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// mImageCaptureUri);
		// intent.putExtra("return-data", true);
		// doTakeAlbumAction();
		//

		startActivityForResult(intent, PICK_FROM_CAMERA);
		// setDismiss(mDialog);

	}

	private AlertDialog createDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.image_crop_row, null);
		Button camera = (Button) innerView.findViewById(R.id.btn_camera_crop);
		Button gellary = (Button) innerView.findViewById(R.id.btn_gellary_crop);
		camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doTakePhotoAction();
				// setDismiss(mDialog);
			}

		});
		gellary.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doTakeAlbumAction();
				// setDismiss(mDialog);
			}

		});
		AlertDialog.Builder ab = new AlertDialog.Builder(PostPageActivity.this);
		ab.setTitle("Camera");
		ab.setView(innerView);
		return ab.create();
	}

	private void setDismiss(AlertDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
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
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CROP_FROM_CAMERA: {
			image[current_position] = (Bitmap) data.getExtras().get("data");
			break;
		}

		case PICK_FROM_ALBUM: {
			if (data.getData() != null) {
				Log.i("TEST", "data.getData()!=null");

				Cursor c = getContentResolver()
						.query(Uri.parse(data.getDataString()), null, null,
								null, null);
				c.moveToNext();
				String image_url = c.getString(c
						.getColumnIndex(MediaStore.MediaColumns.DATA));
				image[current_position] = BitmapFactory.decodeFile(image_url);
			}
			mPhotoImageView.setImageBitmap(image[current_position]);
			break;

		}

		case PICK_FROM_CAMERA: {
			System.out.println("Fffffffffff");

			mPhotoImageView.setImageURI(mImageCaptureUri);
			// Intent intent = new Intent("com.android.camera.action.CROP");
			// intent.setDataAndType(mImageCaptureUri, "image/*");
			//
			// intent.putExtra("outputX", 320);
			// intent.putExtra("outputY", 480);
			// intent.putExtra("aspectX", 1);
			// intent.putExtra("aspectY", 1.5);
			// intent.putExtra("scale", true);
			// intent.putExtra("return-data", true);
			// startActivityForResult(intent, CROP_FROM_CAMERA);

			image[current_position] = (Bitmap) data.getExtras().get("data");

			mPhotoImageView.setImageBitmap(image[current_position]);

			break;
		}
		}
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
		mPhotoImageView.setImageURI(mImageCaptureUri);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("write").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		post_string = post_body.getText().toString();
		if (image[current_position] != null) {
			bm = image[current_position];
		}
		new Postcreate().execute();

		finish();
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("doTake", "onSaveInstanceState");

		int[] imgFlag = new int[image.length];
		// Flag initialize
		for (int i = 0; i < imgFlag.length; i++) {
			imgFlag[i] = 0;
		}

		for (int i = 0; i < image.length; i++) {
			if (image[i] != null) {
				imgFlag[i] = 1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image[i].compress(Bitmap.CompressFormat.JPEG, 80, baos);
				outState.putByteArray("Bitmap" + i, baos.toByteArray());
			}
			outState.putInt("img" + i, imgFlag[i]);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("doTake", "onRestoreInstanceState");

		int[] imgFlag = new int[image.length];
		for (int i = 0; i < image.length; i++) {
			imgFlag[i] = savedInstanceState.getInt("img" + i);
			if (imgFlag[i] == 1) {
				byte[] b = savedInstanceState.getByteArray("Bitmap" + i);

				image[i] = BitmapFactory.decodeByteArray(b, 0, b.length);

			}
		}

		mPhotoImageView.setImageBitmap(image[current_position]);
		super.onRestoreInstanceState(savedInstanceState);
	}

}
