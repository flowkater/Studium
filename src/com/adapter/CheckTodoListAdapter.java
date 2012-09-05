package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.R;
import com.model.CheckString;
import com.model.User;

public class CheckTodoListAdapter extends ArrayAdapter<CheckString> {

	private Context mContext;
	private int mResource;
	private ArrayList<CheckString> mList;
	private LayoutInflater mInflater;

	public CheckTodoListAdapter(Context Context, int mResource, ArrayList<CheckString> mList) {
		super(Context, mResource, mList);
		this.mContext = Context;
		this.mResource = mResource;
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		CheckString user = mList.get(position);
		if (convertView == null) {
			this.mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(mResource, null);
//			System.out.println("kl;asdfjal;sjfkdl;asfjkl;sd"+convertView.isClickable());
//			convertView.setClickable(true);
			holder = new ViewHolder();

			holder.string = (CheckBox) convertView
					.findViewById(R.id.todolist_checkbox);
			
			holder.string.setChecked(((ListView)parent).isItemChecked(position));
			holder.string.setFocusable(false);
			holder.string.setClickable(false);

			convertView.setTag(holder);
			
			if (user.isCheck() == true) {
			
				
				holder.string.setPaintFlags(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);
				
			} else {
				
				//holder.string.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			}
			
			
			} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.string.setText(user.getString());

		return convertView;
	}

	class ViewHolder {
		CheckBox string;
		boolean check;
		
	}
}
