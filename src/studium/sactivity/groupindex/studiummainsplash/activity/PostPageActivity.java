package studium.sactivity.groupindex.studiummainsplash.activity;

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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.utils.Global;

public class PostPageActivity extends SherlockActivity implements
		OnClickListener {
	private ProgressDialog mProgressDialog;
	private EditText post_body;
	private TextView titlebar_text;
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
	private String group_name;
	private String auth_token;

	private String role;

	private Bitmap bm;
	private Bitmap resized;

	private Bitmap image;
	private AlertDialog mDialog;
	int current_position = 0;
	int sampled = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		auth_token = mPreferences.getString("AuthToken", "");

		Intent in = getIntent();
		group_id = in.getExtras().getString("group_id");
		group_name = in.getExtras().getString("group_name");
		role = in.getExtras().getString("role");

		setContentView(R.layout.post_create);
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
		titlebar_text.setText("글 보기");

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
				ByteArrayBody bab = null;
				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					bab = new ByteArrayBody(data, "post_image.jpg");
				}

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Global.ServerUrl
						+ "groups/" + group_id + "/posts.json?auth_token="
						+ auth_token);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("post[body]", new StringBody(post_string,
						Charset.forName("UTF-8")));
				reqEntity.addPart("post[posttype]",
						new StringBody("1", Charset.forName("UTF-8")));
				if (bab != null) {
					reqEntity.addPart("post[pictures_attributes][0][image]",
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
				Toast.makeText(getApplicationContext(), "Error!",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			finish();
			Intent in = new Intent(getApplicationContext(),
					GroupShowActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.putExtra("group_id", group_id);
			in.putExtra("group_name", group_name);
			in.putExtra("role", role);
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
					mPhotoImageView.setImageBitmap(resized);
				}
				break;

			}

			case PICK_FROM_CAMERA: {
				image = (Bitmap) data.getExtras().get("data");
				resized = resizeBitmapImage(image, 900);
				mPhotoImageView.setImageBitmap(resized);
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
		mPhotoImageView.setImageURI(mImageCaptureUri);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("쓰기").setShowAsAction(
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
		if (item.getTitle().equals("쓰기")) {
			showDialog(0);
			post_string = post_body.getText().toString();
			if (image != null) {
				bm = image;
			}
			new Postcreate().execute();
			if (post_body.getText().length() < 1) {
				Toast.makeText(getApplicationContext(), "2자 이상 작성해주세요!",
						Toast.LENGTH_LONG).show();
				return true;
			}
		}
		return false;
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
		mPhotoImageView.setImageBitmap(resized);
		super.onRestoreInstanceState(savedInstanceState);
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
