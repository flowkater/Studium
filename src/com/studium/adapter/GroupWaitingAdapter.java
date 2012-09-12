package com.studium.adapter;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.model.User;
import com.utils.Global;
import com.utils.ImageDownloader;

public class GroupWaitingAdapter extends ArrayAdapter<User> {
	private Context mContext;
	private int mResource;
	private ArrayList<User> mList;
	private LayoutInflater mInflater;

	public GroupWaitingAdapter(Context Context, int mResource,
			ArrayList<User> mList) {
		super(Context, mResource, mList);
		this.mContext = Context;
		this.mResource = mResource;
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		User user = mList.get(position);
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.waiting_img = (ImageView)convertView.findViewById(R.id.waiting_img);
			holder.waiting_name = (TextView)convertView.findViewById(R.id.waiting_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (user!=null) {
			String image = user.getImage();
			if (image!=null) {
				ImageDownloader.download(Global.ServerUrl + image, holder.waiting_img);
			}else{
				holder.waiting_img.setBackgroundResource(R.drawable.photo_frm);
			}
			holder.waiting_name.setText(user.getName());
		}
		return convertView;
	}

	class ViewHolder {
		ImageView waiting_img;
		TextView waiting_name;
	}
}
