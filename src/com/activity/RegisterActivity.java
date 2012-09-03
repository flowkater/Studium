package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.utils.Global;

public class RegisterActivity extends SherlockActivity implements
		OnClickListener {
	private EditText mNameEditText;
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private EditText mPhoneEditText;
	private ImageView mMember_img;
	private Button mRegistButton;
	private String name;
	private String email;
	private String password;
	private String phone;

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private Uri mImageCaptureUri;

	private Bitmap bm;
	private Bitmap resized;

	Bitmap image;
	AlertDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().hide();
		setContentView(R.layout.regist_page);

		mNameEditText = (EditText) findViewById(R.id.member_name_edit_text);
		mNameEditText.setHint("다른 사람에게 보여줄 이름이에요!");
		mEmailEditText = (EditText) findViewById(R.id.member_id_edit_text);
		mEmailEditText.setHint("로그인할 때 필요한 ID에요! 꼭 Email형식으로!");
		mPasswordEditText = (EditText) findViewById(R.id.member_password_edit_text);
		mPasswordEditText.setHint("비밀번호는 6자이상!");
		mPhoneEditText = (EditText) findViewById(R.id.member_phone_num_edit_text);
		mRegistButton = (Button) findViewById(R.id.regist_btn);
		mMember_img = (ImageView) findViewById(R.id.member_img);

		mMember_img.setOnClickListener(this);

		mRegistButton.setOnClickListener(new RegisterClickListener());

		super.onCreate(savedInstanceState);
	}

	class RegisterClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			name = mNameEditText.getText().toString();
			email = mEmailEditText.getText().toString();
			password = mPasswordEditText.getText().toString();

			if (email.indexOf("@") == -1 || email.indexOf(".") == -1 || email.indexOf("@")>email.indexOf("."))
				Toast.makeText(getApplicationContext(), "ID 형식은 꼭 Email 형식으로!",
						Toast.LENGTH_LONG).show();
			else if (password.length() < 6)
				Toast.makeText(getApplicationContext(), "비밀번호는 6자 이상!",
						Toast.LENGTH_LONG).show();

			else {

				new Usercreate().execute();

				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				Bundle extras = new Bundle();

				extras.putString("email", email);
				extras.putString("password", password);
				intent.putExtras(extras);

				startActivity(intent);
				finish();
			}
		}
	}

	class Usercreate extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				// ByteArrayOutputStream bos = new ByteArrayOutputStream();
				// bm.compress(CompressFormat.JPEG, 75, bos);
				// byte[] data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl + "users");
				// HttpPost("http://192.168.0.12:3000/posts.json");
				// ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				// reqEntity.addPart("post[post_image]", bab);
				// reqEntity.addPart("post[pictures_attributes][image]", bab);
				// reqEntity.addPart("post[pictures_attributes][0][image]",
				// bab);
				// reqEntity.addPart("post[pictures_attributes][1][image]",
				// bab);
				// reqEntity.addPart("post[pictures_attributes][2][image]",
				// bab);
				reqEntity.addPart("user[email]", new StringBody(email));
				reqEntity.addPart("user[password]", new StringBody(password));
				reqEntity.addPart("user[password_confirmation]",
						new StringBody(password));
				reqEntity.addPart("user[name]", new StringBody(name));
				// reqEntity.addPart("user[gender]", new StringBody("male"));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				// response.getStatusLine().toString();
				System.out.println("fsdafasdfasdfasdfsdaf     "
						+ response.getStatusLine().toString());
				String sResponse;
				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				Log.e("my", "Response : " + s);
			} catch (Exception e) {
				Log.e("my", e.getClass().getName() + e.getMessage());
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

					// recycle
					// image.recycle();
					// image = null;
					// ((BitmapDrawable)mMember_img.getDrawable()).getBitmap().recycle();

				}
				break;

			}

			case PICK_FROM_CAMERA: {
				System.out.println("Fffffffffff");

				// mMember_img.setImageURI(mImageCaptureUri);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("write").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
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
