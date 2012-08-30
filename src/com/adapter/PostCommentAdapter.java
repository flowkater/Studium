package com.adapter;

import java.util.ArrayList;

import com.activity.R;
import com.model.Comment;
import com.utils.Global;
import com.utils.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostCommentAdapter extends ArrayAdapter<Comment> {
	private Context mContext;
	private int mResource;
	private ArrayList<Comment> mLists;
	private LayoutInflater mInflater;

	public PostCommentAdapter(Context Context, int mResource,
			ArrayList<Comment> mLists) {
		super(Context, mResource, mLists);
		this.mContext = Context;
		this.mResource = mResource;
		this.mLists = mLists;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Comment comment = mLists.get(position);
		
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			
			holder.member_image = (ImageView)convertView.findViewById(R.id.member_img);
			holder.comment_body = (TextView)convertView.findViewById(R.id.comment_content);
			holder.member_name = (TextView)convertView.findViewById(R.id.member_name);
			holder.comment_time = (TextView)convertView.findViewById(R.id.comment_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (comment !=null) {
			String image = comment.getImage();
			if (image!=null) {
				ImageDownloader.download(Global.ServerUrl+image, holder.member_image);
			}else{
				holder.member_image.setImageResource(R.drawable.ic_launcher);
			}
			holder.comment_body.setText(comment.getBody());
			holder.member_name.setText(comment.getName());
			holder.comment_time.setText(comment.getTime());
		}
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView member_image;
		TextView member_name;
		TextView comment_body;
		TextView comment_time;
	}
}
