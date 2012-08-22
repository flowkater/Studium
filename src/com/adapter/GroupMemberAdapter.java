package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.R;
import com.model.User;

public class GroupMemberAdapter extends ArrayAdapter<User> {
	private Context mContext;
	private int mResource;
	private ArrayList<User> mList;
	private LayoutInflater mInflater;

	public GroupMemberAdapter(Context Context, int mResource,
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

			holder.member_img = (ImageView) convertView
					.findViewById(R.id.member_img);
			holder.member_name = (TextView) convertView
					.findViewById(R.id.member_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.member_name.setText(user.getName());

		return convertView;
	}

	class ViewHolder {
		ImageView member_img;
		TextView member_name;
	}
}
