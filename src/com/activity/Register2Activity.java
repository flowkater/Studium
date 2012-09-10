package com.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
	private String gender = "male";
	private Bitmap bm;
	private ImageView user_img;
	private int code;
	

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.regist_page2);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bitmap));
		bar.setLogo(R.drawable.logoicon);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("기본정보 입력");
		// end header

		member_name_edit_text = (EditText) findViewById(R.id.member_name_edit_text);
		member_phone_num_edit_text = (EditText) findViewById(R.id.member_phone_num_edit_text);

		Intent in = getIntent();
		email = in.getStringExtra("email");
		password = in.getStringExtra("password");

		user_img = (ImageView) findViewById(R.id.member_img);

		user_img.setOnClickListener(new ImageView.OnClickListener() {
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

					new AlertDialog.Builder(Register2Activity.this)
							.setTitle("select the image")
							.setPositiveButton("take picture", cameraListener)
							.setNeutralButton("album", albumListener)
							.setNegativeButton("cancel", cancelListener).show();
				}
			}
		}

		);

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
		menu.add("가입").setShowAsAction(
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
		if (item.getTitle().equals("가입")) {
			if (member_name_edit_text.getText().length() < 2) {
				Toast.makeText(getApplicationContext(), "이름은 2자 이상!",
						Toast.LENGTH_LONG).show();
				return true;
			} else if (member_phone_num_edit_text.getText().length() < 11) {
				Toast.makeText(getApplicationContext(), "전화번호가 올바르지 않습니다!",
						Toast.LENGTH_LONG).show();
				return true;
			} else {
				showDialog(0);
				name = member_name_edit_text.getText().toString();
				phone = member_phone_num_edit_text.getText().toString();
				if (resized != null) {
					bm = resized;
				}
				new Usercreate().execute();

				return true;
			}
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
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "users.json");
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
				reqEntity.addPart("user[phone]", new StringBody(phone));
				reqEntity.addPart("user[gender]", new StringBody("male",
						Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("user[avatar]", bab);
				}

				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);

				if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED) {
					System.out.println("Success");
					code = 1;
				}else{
					System.out.println("Error");
					code = 2;
				}

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
			if (code==1) {
				finish();
				Intent in = new Intent(getApplicationContext(), LoginActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				in.putExtra("email", email);
				in.putExtra("password", password);
				startActivity(in);
				removeDialog(0);
			}else if(code==2){
				removeDialog(0);
				showDialog(1);
			}
			
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
					user_img.setImageBitmap(resized);

				}
				break;

			}

			case PICK_FROM_CAMERA: {
				System.out.println("Fffffffffff");

				image = (Bitmap) data.getExtras().get("data");

				resized = resizeBitmapImage(image, 800);

				user_img.setImageBitmap(resized);

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
		user_img.setImageURI(mImageCaptureUri);
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

		user_img.setImageBitmap(resized);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected Dialog onCreateDialog(int id) { // Dialog preference
		Dialog dialog = null;
		switch (id) {
		case 0: {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("회원가입을 요청 중입니다...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		case 1: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Error");
			builder.setMessage("중복된 메일입니다.");
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog errorAlert = builder.create();
			return errorAlert;
		}
		}
		return null;
	}
}
