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
import com.model.Post;
import com.utils.Global;
import com.utils.ImageDownloader;

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
			holder.posting_time = (TextView) convertView
					.findViewById(R.id.posting_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (post != null) {
			String member_image = post.getMember_image();
			String content_image = post.getContent_image();
			if (member_image != null) {
				ImageDownloader.download(Global.ServerUrl + member_image,
						holder.member_img);
			} else {
				holder.member_img.setImageResource(R.drawable.ic_launcher);
			}
			if (content_image != null) {
				ImageDownloader.download(Global.ServerUrl + content_image,
						holder.feed_content_img);
			} else {
				holder.feed_content_img
						.setImageResource(R.drawable.ic_none);
			}
			holder.post_body.setText(post.getBody());
			holder.member_name.setText(post.getName());
			holder.comment_count.setText(post.getComment_count());
			holder.posting_time.setText(post.getTime());
		}
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
