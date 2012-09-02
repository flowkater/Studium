package com.adapter;

import java.io.File;
import java.util.ArrayList;

import com.activity.R;
import com.adapter.CheckTodoListAdapter.ViewHolder;
import com.model.ThumbImageInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	static final int VISIBLE = 0x00000000;
	static final int INVISIBLE = 0x00000004;
	private Context mContext;
	private int mCellLayout;
	private LayoutInflater mLiInflater;
	private ArrayList<ThumbImageInfo> mThumbImageInfoList;

	public ImageAdapter(Context c, int cellLayout,
			ArrayList<ThumbImageInfo> thumbImageInfoList) {
		mContext = c;
		mCellLayout = cellLayout;
		mThumbImageInfoList = thumbImageInfoList;

		mLiInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 캐쉬 초기화 : 캐쉬의 최대 보관 크기 30개
	}

	public int getCount() {
		return mThumbImageInfoList.size();
	}

	public Object getItem(int position) {
		return mThumbImageInfoList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {
		

		if (convertView == null) {
		
			convertView = mLiInflater.inflate(mCellLayout, null);
			ImageViewHolder holder = new ImageViewHolder();

			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.chkImage = (CheckBox) convertView
					.findViewById(R.id.chkImage);
			holder.ivImage.setImageResource(R.drawable.member_byung);


			convertView.setTag(holder);
//			String st = "R.drawable."+ mThumbImageInfoList.get(position).getId();
			
		}


		else {
			ImageViewHolder holder = (ImageViewHolder) convertView.getTag();
			holder = (ImageViewHolder) convertView.getTag();

		}


		return convertView;
	}
	public class ImageViewHolder
	{
	  ImageView ivImage;
	  CheckBox chkImage;
	  String username;
	}
}


