package com.adapter;

import java.util.ArrayList;

import com.activity.R;
import com.model.Partymessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
			holder.group_message_body = (TextView) convertView
					.findViewById(R.id.group_message_body);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.group_message_body.setText(partymessage.getBody());
		return convertView;
	}

	class ViewHolder {
		TextView group_message_body;
	}
}
