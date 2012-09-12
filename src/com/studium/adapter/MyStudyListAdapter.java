package com.studium.adapter;

import java.util.ArrayList;

import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.model.Group;
import com.utils.Global;
import com.utils.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyStudyListAdapter extends ArrayAdapter<Group> {
	private Context mContext;
	private int mResource;
	private ArrayList<Group> mGroups;
	private LayoutInflater mInflater;
	private Group group;

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
		group = mGroups.get(position);
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.mystudy_img = (ImageView) convertView
					.findViewById(R.id.mystudy_img);
			holder.mystudy_name = (TextView) convertView
					.findViewById(R.id.mystudy_name);
			holder.mystudy_row = (LinearLayout) convertView
					.findViewById(R.id.mystudy_row);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (group!=null) {
			holder.mystudy_name.setText(group.getName());
			String image = group.getImage();
			if (image!=null) {
				ImageDownloader.download(Global.ServerUrl + image, holder.mystudy_img);
			}else{
			}
		}

		return convertView;
	}

	class ViewHolder {
		ImageView mystudy_img;
		TextView mystudy_name;
		LinearLayout mystudy_row;
	}
}
