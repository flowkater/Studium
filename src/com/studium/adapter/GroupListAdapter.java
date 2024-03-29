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
//			holder.group_goal.setSelected(true);
			
			
			holder.place = (TextView)convertView.findViewById(R.id.location);
			holder.user_count = (TextView)convertView.findViewById(R.id.people_count);
			holder.post_count = (TextView)convertView.findViewById(R.id.feed_count);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if (group!= null) {
			String image = group.getImage();
			if (image!=null) {
				ImageDownloader.download(Global.ServerUrl+ image, holder.group_image);
			}else{
				holder.group_image.setImageResource(R.drawable.photo_frm);
			}
			
			holder.group_name.setText(group.getName());			 
			String temp = continueSpaceRemove(group.getGoal());
			if(group.getGoal().length()>150)
			{
				 temp = temp.substring(0, 149) + "...";
			}
			
			
			holder.group_goal.setText(temp);
			holder.place.setText(group.getPlace());
			holder.user_count.setText(group.getUser_count());
			holder.post_count.setText(group.getPost_count());
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView group_image;
		TextView group_name;
		TextView group_goal;
		TextView place;
		TextView user_count;
		TextView post_count;
	}
	public static String continueSpaceRemove(String str){
	    String match2 = "\\s{2,}";
	    str = str.replaceAll(match2, " ");
	    return str;
	}
}
