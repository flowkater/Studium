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

public class GroupListAdapter extends ArrayAdapter<Group>{
	private Context mContext;
	private int mResource;
	private ArrayList<Group> mGroups;
	private LayoutInflater mInflater;
	
	public GroupListAdapter(Context Context, int mResource, ArrayList<Group> mGroups) {
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
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			
			holder.group_image = (ImageView)convertView.findViewById(R.id.group_image);
			holder.group_name = (TextView)convertView.findViewById(R.id.group_name);
			holder.group_goal = (TextView)convertView.findViewById(R.id.group_goal);
			holder.people_count = (TextView)convertView.findViewById(R.id.people_count);
			holder.feed_count = (TextView)convertView.findViewById(R.id.feed_count);
			holder.location = (TextView)convertView.findViewById(R.id.location);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.group_image.setImageBitmap(group.getGroup_img());
		holder.group_name.setText(group.getName());
		holder.group_goal.setText(group.getGoal());
		holder.group_goal.setText(group.getPeople_count());
		holder.group_goal.setText(group.getFeed_count());
		holder.group_goal.setText(group.getLocation());
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView group_image;
		TextView group_name;
		TextView group_goal;
		TextView people_count;
		TextView feed_count;
		TextView location;
	}
}
