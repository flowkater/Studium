package com.adapter;


import java.util.ArrayList;

import com.activity.R;
import com.model.DayInfo;
import com.model.ThumbImageInfo;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

		// ĳ�� �ʱ�ȭ : ĳ���� �ִ� ���� ũ�� 30��
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

	
	ImageViewHolder holder;
	public View getView(int position, View convertView, ViewGroup parent) {
		ThumbImageInfo thumb = mThumbImageInfoList.get(position);

		if (convertView == null) {
		
			convertView = mLiInflater.inflate(mCellLayout, null);
			holder = new ImageViewHolder();

			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);
			holder.ivImage.setImageResource(R.drawable.member_byung);
			holder.username = (TextView) convertView.findViewById(R.id.username);
			holder.username.setText(thumb.getId());
			holder.chkImage.setChecked(thumb.getCheckedState());
			
			if(position % 3 == 2)
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP(2)));
			}
			else
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP(2)));	
			}


			convertView.setTag(holder);
			
			
//			String st = "R.drawable."+ mThumbImageInfoList.get(position).getId();
			
		}


		else {
			holder = (ImageViewHolder) convertView.getTag();
		

		}


		return convertView;
	}
	public class ImageViewHolder
	{
	  ImageView ivImage;
	  CheckBox chkImage;
	  TextView username;
	}
	
	private int getCellWidthDP()
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = width/3;
		
		return cellWidth;
	}
	
	private int getRestCellWidthDP()
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = width%3;
		
		return cellWidth;
	}
	
	private int getCellHeightDP(int num)
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellHeight = width/num;
		
		return cellHeight;
	}
}


