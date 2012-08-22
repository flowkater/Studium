package com.adapter;

import java.util.ArrayList;

import com.activity.R;
import com.model.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyStudyListAdapter extends ArrayAdapter<Group> {
	private Context mContext;
	private int mResource;
	private ArrayList<Group> mGroups;
	private LayoutInflater mInflater;

	public MyStudyListAdapter(Context Context, int mResource,
			ArrayList<Group> mGroups) {
		super(Context, mResource, mGroups);
		this.mContext = Context;
		this.mResource = mResource;
		this.mGroups = mGroups;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Group group = mGroups.get(position);
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.group_image = (ImageView) convertView
					.findViewById(R.id.mystudy_img);
			holder.group_name = (TextView) convertView
					.findViewById(R.id.mystudy_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.group_name.setText(group.getName());

		return convertView;
	}

	class ViewHolder {
		ImageView group_image;
		TextView group_name;
	}
}
