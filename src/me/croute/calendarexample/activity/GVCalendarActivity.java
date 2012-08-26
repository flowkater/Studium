package me.croute.calendarexample.activity;

import java.util.ArrayList;
import java.util.Calendar;

import com.activity.R;
import com.adapter.CalendarAdapter;
import com.model.DayInfo;
import com.model.Post;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 洹몃━�쒕럭瑜��댁슜���щ젰 �덉젣
 * 
 * @blog http://croute.me
 * @link http://croute.me/335
 * 
 * @author croute
 * @since 2011.03.08
 */
public class GVCalendarActivity extends Activity implements
		OnItemClickListener, OnClickListener {
	public static int SUNDAY = 1;
	public static int MONDAY = 2;
	public static int TUESDAY = 3;
	public static int WEDNSESDAY = 4;
	public static int THURSDAY = 5;
	public static int FRIDAY = 6;
	public static int SATURDAY = 7;

	private TextView mTvCalendarTitle;
	private GridView mGvCalendar;

	private ArrayList<DayInfo> mDayList;
	private CalendarAdapter mCalendarAdapter;

	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;

	int update_color;
	DayInfo update;
	int isUpdate;

	String[][] temp;

	ListView mPostListView;
	ArrayList<Post> mPostList = new ArrayList<Post>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_calender);

		Button bLastMonth = (Button) findViewById(R.id.gv_calendar_activity_b_last);
		Button bNextMonth = (Button) findViewById(R.id.gv_calendar_activity_b_next);

		mTvCalendarTitle = (TextView) findViewById(R.id.gv_calendar_activity_tv_title);
		mGvCalendar = (GridView) findViewById(R.id.gv_calendar_activity_gv_calendar);

		bLastMonth.setOnClickListener(this);
		bNextMonth.setOnClickListener(this);
		mGvCalendar.setOnItemClickListener(this);
		
		//가상의 피드 추가............
//
//		mDayList = new ArrayList<DayInfo>();
//
//		mPostList.add(new Post( "2012-7-1"));
//		mPostList.add(new Post( "2012-7-31"));
//		mPostList.add(new Post( "2012-8-1"));
//		mPostList.add(new Post("2012-8-8"));
//		mPostList.add(new Post( "2012-8-10"));
//		mPostList.add(new Post( "2012-8-12"));
//		mPostList.add(new Post( "2012-8-12"));
//		mPostList.add(new Post( "2012-8-31"));
//		mPostList.add(new Post("2012-9-1"));
//		mPostList.add(new Post("2012-9-3"));
//		mPostList.add(new Post("2012-9-31"));
		
		//시간 정보를 파싱하여 년 월 일로 나눔
//		
//
//		String[] Posttime = new String[mPostList.size()];
//		temp = new String[mPostList.size()][3];
//		for (int i = 0; i < mPostList.size(); i++)
//			Posttime[i] = mPostList.get(i).getTime();
//		String delimiter = "-";
//		for (int i = 0; i < Posttime.length; i++)
//			temp[i] = Posttime[i].split(delimiter);
//
//		for (int j = 0; j < mPostList.size(); j++)
//			for (int i = 0; i < 3; i++)
//				System.out.println("Posttime   [" + j + "][" + i + "] ====  "
//						+ temp[j][i]);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		//클릭해서 다시 캘린더로 돌아왔을 때를 감지함 색변화 봄
		if (getIntent().getStringExtra("color") != null) {
			update_color = Integer
					.parseInt(getIntent().getStringExtra("color"));
			isUpdate = 1;
		}
		
		//callender 생성

		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
		//그리는 부분
		getCalendar(mThisMonthCalendar);
	}

	/**
	 * �щ젰���뗮똿�쒕떎.
	 * 
	 * @param calendar
	 *            �щ젰��蹂댁뿬吏�뒗 �대쾲�ъ쓽 Calendar 媛앹껜
	 */
	private void getCalendar(Calendar calendar)
	{
		int lastMonthStartDay;//지난 달 마지막날
		int dayOfMonth;//이번달 날짜수
		int thisMonthLastDay;//이번달 마지막날
		
		mDayList.clear();
		
		
		//달력의 시작일
		
		int [] start_calender = new int [3];
		
		
		
		
		// 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		calendar.add(Calendar.MONTH, -1);
		Log.e("吏�궃��留덉�留됱씪", calendar.get(Calendar.DAY_OF_MONTH)+"");

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);
		Log.e("�대쾲���쒖옉��", calendar.get(Calendar.DAY_OF_MONTH)+"");
		
		if(dayOfMonth == SUNDAY)
		{
			dayOfMonth += 7;
		}
		
		lastMonthStartDay -= (dayOfMonth-1)-1;
		

		// 캘린더 타이틀(년월 표시)을 세팅한다. 
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "��"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "��");

		DayInfo day;
		
		Log.e("DayOfMOnth", dayOfMonth+"");
		System.out.println("last create");
		
		//인덱스를 0부터 해서 끝날때까지 다비교해보고 하나씩 증가시켜서 feed값확인
		int index=0;
		
		//달력 첫날 설정
		start_calender[0]=mThisMonthCalendar.get(Calendar.YEAR);
		start_calender[1]=mThisMonthCalendar.get(calendar.MONTH);
		start_calender[2]=lastMonthStartDay;
		
		System.out.println(Integer.parseInt(temp[index][0])+ "     "+start_calender[0]);
		System.out.println(Integer.parseInt(temp[index][2])+ "     "+start_calender[2]);

		
	
		//index 조절
		while(true)
		{
			
			if(Integer.parseInt(temp[index][0])< start_calender[0])
		
			{
			index++;
			}
		else if(Integer.parseInt(temp[index][0])== start_calender[0])
			{
				if(Integer.parseInt(temp[index][1])< start_calender[1])
					index++;
				else if(Integer.parseInt(temp[index][1])== start_calender[1])
				{
					if(Integer.parseInt(temp[index][2])< start_calender[2])
					index++;
					else break;
				}
			
			}
		}
		
		for(int i=0; i<dayOfMonth-1; i++)
		{
			
			int date = lastMonthStartDay+i;
			
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);
			day.setMonth(mThisMonthCalendar.get(Calendar.MONTH)+"");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR)+"");
			
			System.out.println("get intent date"+ getIntent().getStringExtra("date") + " ======== " + day.getDay());
			System.out.println("get intent month"+ getIntent().getStringExtra("month") + " ======== " + day.getMonth());
			System.out.println("get intent year"+ getIntent().getStringExtra("year") + " ======== " + day.getYear());

			
			if(isUpdate==1 && Integer.parseInt(getIntent().getStringExtra("date"))==Integer.parseInt(day.getDay())
					&& Integer.parseInt(getIntent().getStringExtra("month"))==Integer.parseInt(day.getMonth()) 
					&& Integer.parseInt(getIntent().getStringExtra("year"))==Integer.parseInt(day.getYear()))
			{
				day.setBg_color(update_color);
				System.out.println("last create update");

			}
			else day.setBg_color(Color.WHITE);
			
			
			//무한 반복으로 중복 feed 찾아내 하나의 date마다 하나씩 feed의 배열과 비교해봐
			while(true)
			{
			if(Integer.parseInt(temp[index][0])== mThisMonthCalendar.get(Calendar.YEAR)
					&& Integer.parseInt(temp[index][1])== mThisMonthCalendar.get(Calendar.MONTH)
					&& Integer.parseInt(temp[index][2])== date)
			{
				day.setPost_num(day.getPost_num()+1);
				index++;
			}
			else break;
			
			System.out.println("Ffffffffffffffffff        " +index);

			
			}
			
				
			
			mDayList.add(day);
		}
		System.out.println("this create");

		for(int i=1; i <= thisMonthLastDay; i++)
		{
			
			System.out.println(index+ "temp    = "+temp[index][2]);
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);
			day.setMonth((mThisMonthCalendar.get(Calendar.MONTH)+1)+"");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR)+"");
			System.out.println("get intent date"+ getIntent().getStringExtra("date") + " ======== " + day.getDay());
			System.out.println("get intent month"+ getIntent().getStringExtra("month") + " ======== " + day.getMonth());
			System.out.println("get intent year"+ getIntent().getStringExtra("year") + " ======== " + day.getYear());

			if(isUpdate==1 && Integer.parseInt(getIntent().getStringExtra("date"))==Integer.parseInt(day.getDay())
					&& Integer.parseInt(getIntent().getStringExtra("month"))==Integer.parseInt(day.getMonth()) 
					&& Integer.parseInt(getIntent().getStringExtra("year"))==Integer.parseInt(day.getYear()))
			{
				day.setBg_color(update_color);
				System.out.println("this create update");

			}
			else day.setBg_color(Color.WHITE);
			
			//무한 반복으로 중복 feed 찾아내 하나의 date마다 하나씩 feed의 배열과 비교해봐
			while(true)
			{
			if(Integer.parseInt(temp[index][0])== mThisMonthCalendar.get(Calendar.YEAR)
					&& Integer.parseInt(temp[index][1])== (mThisMonthCalendar.get(Calendar.MONTH)+1)
					&& Integer.parseInt(temp[index][2])== i)
			{
				day.setPost_num(day.getPost_num()+1);
				index++;
			}
			else break;
			
			}
			

			System.out.println("Ffffffffffffffffff        " +index);
			mDayList.add(day);
		}
		System.out.println("next create");

		for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
		{
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			day.setMonth((mThisMonthCalendar.get(Calendar.MONTH)+2)+"");
			day.setYear(mThisMonthCalendar.get(Calendar.YEAR)+"");
			
			System.out.println("get intent date"+ getIntent().getStringExtra("date") + " ======== " + day.getDay());
			System.out.println("get intent month"+ getIntent().getStringExtra("month") + " ======== " + day.getMonth());
			System.out.println("get intent year"+ getIntent().getStringExtra("year") + " ======== " + day.getYear());

			
			if(isUpdate==1 && Integer.parseInt(getIntent().getStringExtra("date"))==Integer.parseInt(day.getDay())
					&& Integer.parseInt(getIntent().getStringExtra("month"))==Integer.parseInt(day.getMonth()) 
					&& Integer.parseInt(getIntent().getStringExtra("year"))==Integer.parseInt(day.getYear()))
			{
				day.setBg_color(update_color);
				System.out.println("next create update");

			}
			else day.setBg_color(Color.WHITE);

			while(true)
			{
			if(Integer.parseInt(temp[index][0])== mThisMonthCalendar.get(Calendar.YEAR)
					&& Integer.parseInt(temp[index][1])== (mThisMonthCalendar.get(Calendar.MONTH)+2)
					&& Integer.parseInt(temp[index][2])== i)
			{
				day.setPost_num(day.getPost_num()+1);
				index++;
			}
			else break;
			
			}
			
			System.out.println("Ffffffffffffffffff        " +index);

			
			mDayList.add(day);
		}
		
		
		initCalendarAdapter();
	}

	/**
	 * 吏�궃�ъ쓽 Calendar 媛앹껜瑜�諛섑솚�⑸땲��
	 * 
	 * @param calendar
	 * @return LastMonthCalendar
	 */
	private Calendar getLastMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, -1);
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "��"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "��");
		return calendar;
	}

	/**
	 * �ㅼ쓬�ъ쓽 Calendar 媛앹껜瑜�諛섑솚�⑸땲��
	 * 
	 * @param calendar
	 * @return NextMonthCalendar
	 */
	private Calendar getNextMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, +1);
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "��"
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "��");
		return calendar;
	}

	DayInfo daycl;

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long arg3) {

		daycl = mDayList.get(position);
		System.out.println(daycl.getDay() + "                fuck");
		System.out.println(daycl.getMonth() + "                you");
		System.out.println(daycl.getYear() + "                LOVE");

		Bundle extras = new Bundle();
		extras.putString("date", daycl.getDay());
		extras.putString("month", daycl.getMonth());
		extras.putString("year", daycl.getYear());
		System.out.println(daycl.getDay() + "                fuck");
		System.out.println(daycl.getMonth() + "                you");
		System.out.println(daycl.getYear() + "                LOVE");

		Intent intent = new Intent(this, calendardialog.class);
		intent.putExtras(extras);

		System.out.println(daycl.getDay() + "                fuck");
		System.out.println(daycl.getMonth() + "                you");
		System.out.println(daycl.getYear() + "                LOVE");

		startActivity(intent);

		System.out.println(daycl.getDay() + "                fuck");
		System.out.println(daycl.getMonth() + "                you");
		System.out.println(daycl.getYear() + "                LOVE");

	}

	@Override
	public void onClick(View v) {
		
		if( R.id.gv_calendar_activity_b_last==v.getId())
			{
			mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
			}
		if(R.id.gv_calendar_activity_b_next==v.getId())
			{
			mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
			}
	}

	private void initCalendarAdapter() {
		mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
		mGvCalendar.setAdapter(mCalendarAdapter);
	}
}