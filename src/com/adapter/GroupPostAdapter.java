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
import com.adapter.GroupMemberAdapter.ViewHolder;
import com.model.Post;
import com.model.User;

public class GroupPostAdapter extends ArrayAdapter<Post> {
	private Context mContext;
	private int mResource;
	private ArrayList<Post> mList;
	private LayoutInflater mInflater;

	public GroupPostAdapter(Context Context, int mResource,
			ArrayList<Post> mList) {
		super(Context, mResource, mList);
		this.mContext = Context;
		this.mResource = mResource;
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Post post = mList.get(position);
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();

			holder.member_img = (ImageView) convertView
					.findViewById(R.id.member_img);
			holder.member_name = (TextView) convertView
					.findViewById(R.id.member_name);
			holder.post_body = (TextView) convertView
					.findViewById(R.id.post_body);
			holder.feed_content_img = (ImageView) convertView
					.findViewById(R.id.feed_content_img);
			holder.comment_count = (TextView) convertView
					.findViewById(R.id.comment_count);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.member_img.setImageBitmap(post.getMember_img());
		if (post.getContent_img() != null) {
			holder.feed_content_img.setImageBitmap(post.getContent_img());
		}
		holder.member_name.setText(post.getMember_name());
		holder.post_body.setText(post.getBody());
		holder.comment_count.setText(post.getComment_count());
		
//		holder.posting_time.setText(text);

		return convertView;
	}

	class ViewHolder {
		ImageView member_img;
		TextView member_name;
		TextView posting_time;
		TextView post_body;
		ImageView feed_content_img;
		TextView comment_count;
	}
}
