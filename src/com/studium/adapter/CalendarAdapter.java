package com.studium.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import studium.sactivity.groupindex.studiummainsplash.activity.R;
import com.studium.model.DayInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * BaseAdapter를 상속받아 구현한 CalendarAdapter
 * 
 * @author croute10
 * @since 2011.03.08
 */
public class CalendarAdapter extends BaseAdapter
{
	private ArrayList<DayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;
	private boolean past;
	
	/**
	 * Adpater 생성자
	 * 
	 * @param context
	 *            컨텍스트
	 * @param textResource
	 *            레이아웃 리소스
	 * @param dayList
	 *            날짜정보가 들어있는 리스트
	 */
	
	public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList)
	{
		this.mContext = context;
		this.mDayList = dayList;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mDayList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mDayList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		DayInfo day = mDayList.get(position);


		DayViewHolde dayViewHolder;

		
		if(convertView == null)
		{
			convertView = mLiInflater.inflate(mResource, null);

			if(position % 7 == 6)
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
			}
			else
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));	
			}
			int temp_fuck = Integer.parseInt(day.getYear()) * 10000
					+ Integer.parseInt(day.getMonth()) * 100
					+ Integer.parseInt(day.getDate());
			Calendar calender = Calendar.getInstance();
			int now_fuck = calender.get(Calendar.YEAR) * 10000
					+ (calender.get(Calendar.MONTH) + 1) * 100
					+ calender.get(Calendar.DATE);

			
			
			dayViewHolder = new DayViewHolde();

			dayViewHolder.llBackground = (RelativeLayout)convertView.findViewById(R.id.day_cell_ll_background);
			dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);
			TextView feed_num =(TextView) convertView.findViewById(R.id.number_of_feed);
			dayViewHolder.llBackground.setBackgroundColor(day.getBg_color());

			if(day.getPost_num().equals("오늘"))
				{
					feed_num.setText("오늘");
					LinearLayout day_index = (LinearLayout)convertView.findViewById(R.id.day_index);
					day_index.setBackgroundColor(Color.GREEN);
				}
			else
				feed_num.setVisibility(View.INVISIBLE);
			

			if (temp_fuck < now_fuck) {
				
				past=true;
			} 
			else 
				past=false;
			
			convertView.setTag(dayViewHolder);
		}
		else
		{
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}

		if(day != null)
		{
			dayViewHolder.tvDay.setText(day.getDate());

			if(day.isInMonth())
			{
				if(day.isParty())
				{
					LinearLayout day_index = (LinearLayout)convertView.findViewById(R.id.day_index);
					day_index.setBackgroundColor(Color.YELLOW);
					
					if(past)
						day_index.setBackgroundColor(Color.RED);

				}
				if(position % 7 == 0)
				{
					dayViewHolder.tvDay.setTextColor(Color.RED);
				}
				else if(position % 7 == 6)
				{
					dayViewHolder.tvDay.setTextColor(Color.BLUE);
				}
				else
				{
					dayViewHolder.tvDay.setTextColor(Color.BLACK);
				}
			}
			else
			{
				dayViewHolder.tvDay.setTextColor(Color.GRAY);
			}

		}

		return convertView;
	}

	public class DayViewHolde
	{
		public RelativeLayout llBackground;
		public TextView tvDay;
		
	}

	private int getCellWidthDP()
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = width/7;
		
		return cellWidth;
	}
	
	private int getRestCellWidthDP()
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = width%7;
		
		return cellWidth;
	}
	
	private int getCellHeightDP()
	{
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellHeight = width/6;
		
		return cellHeight;
	}
	
}
