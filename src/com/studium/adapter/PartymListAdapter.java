package com.studium.adapter;

import java.util.ArrayList;

import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.model.Partymessage;
import com.utils.Global;
import com.utils.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PartymListAdapter extends ArrayAdapter<Partymessage> {
	private Context mContext;
	private int mResource;
	private ArrayList<Partymessage> mList;
	private LayoutInflater mInflater;

	public PartymListAdapter(Context Context, int mResource,
			ArrayList<Partymessage> mList) {
		super(Context, mResource, mList);
		this.mContext = Context;
		this.mResource = mResource;
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Partymessage partymessage = mList.get(position);

		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();

			holder.image = (ImageView) convertView
					.findViewById(R.id.group_intro_img);
			holder.name = (TextView) convertView
					.findViewById(R.id.group_name);
			holder.goal = (TextView) convertView
					.findViewById(R.id.group_goal);
			holder.place = (TextView) convertView
					.findViewById(R.id.group_location);
			holder.body = (TextView) convertView
					.findViewById(R.id.group_message_body);
//			holder.time = (TextView) convertView
//					.findViewById(R.id.group_);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (partymessage != null) {
			String image = partymessage.getImage();
			if (image != null) {
				ImageDownloader.download(Global.ServerUrl+image, holder.image);
			}else {
				holder.image.setImageResource(R.drawable.photo_frm);
			}
			holder.name.setText(partymessage.getName());
			holder.goal.setText(partymessage.getGoal());
			holder.place.setText(partymessage.getPlace());
			holder.body.setText(partymessage.getBody());
		}
		return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView name;
		TextView goal;
		TextView place;
		TextView body;
		TextView time;
	}
}
