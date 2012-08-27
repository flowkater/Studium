package com.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adapter.PostCommentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.model.Comment;

public class PostPageActivity extends SherlockActivity implements OnClickListener {
	private ArrayList<Comment> mArrayList;
	private PostCommentAdapter mAdapter;
	private PullToRefreshListView mListView;
	private LinearLayout headerview;
	private TextView post_body;
	TextView titlebar_text;
	EditText post_et;
	String post_string;
	
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int REQUEST_CODE = 3;
	private Uri mImageCaptureUri;
	private ImageView mPhotoImageView;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_create);
		// start header
		ActionBar bar = getSupportActionBar();
		bar.setLogo(R.drawable.title_btn_setting);
		bar.setCustomView(R.layout.header);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayHomeAsUpEnabled(false);
		// end header
		titlebar_text = (TextView) findViewById(R.id.titlebar_text);
		titlebar_text.setText("Post#Page");

		// post ȭ��
		mButton = (Button) findViewById(R.id.postcreate_img_btn);
		mPhotoImageView = (ImageView) findViewById(R.id.postcreate_img);

		mButton.setOnClickListener(this);



	}
	private void doTakePhotoAction() {
		/*
		 * References http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http
		 * ://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured
		 * -image http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 * 
		 * 
		 */
		

		  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		  // temp file url
		  String url = "DCIM/AT" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		  mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

		 
		  intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		  intent.putExtra("return-data", true);
		  doTakeAlbumAction();
		  
		  
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
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CROP_FROM_CAMERA: {
			//
			final Bundle extras = data.getExtras();

			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				mPhotoImageView.setImageBitmap(photo);
			}

			// 임시 파일 삭제
			File f = new File(mImageCaptureUri.getPath());
			if (f.exists()) {
				f.delete();
			}

			break;
		}

		case PICK_FROM_ALBUM: {

			mImageCaptureUri = data.getData();
			mPhotoImageView.setImageURI(mImageCaptureUri);

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
		menu.add("�Է�")
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock

		Intent intent = new Intent(this, GroupShowActivity.class);
		post_et = (EditText) findViewById(R.id.post_string);
		post_string = post_et.getText().toString();
		Bundle extras = new Bundle();
		extras.putString("post_string", post_string);
		intent.putExtras(extras);
		startActivity(intent);

		return true;
	}

	

}
