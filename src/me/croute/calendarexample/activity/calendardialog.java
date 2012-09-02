package me.croute.calendarexample.activity;

import java.util.ArrayList;

import com.activity.GroupShowActivity;
import com.activity.R;
import com.adapter.CheckTodoListAdapter;
import com.adapter.ImageAdapter;
import com.fragment.StepFragment;
import com.model.CheckString;
import com.model.ThumbImageInfo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class calendardialog extends Activity implements OnItemClickListener, OnClickListener{
	public static int PARTY_MEMBER = 0;
	public static int PARTYING_TIME = 1;
	public static int LOCATION = 2;
	public static int TIME = 3;
	public static int TODOLIST = 4;
	public static int COMMENT_COUNT = 5;
	public static int DATE = 6;
	public static String delims = "-";
	int count = 0;
	
	CheckTodoListAdapter mAdapter;

	ImageAdapter mListAdapter;
	
	
    ArrayList<CheckString> todolist_list= new ArrayList<CheckString>();
    CheckString check;
	ProgressBar achivebar;
	int achive_rate;
	int Num_todoList;
	int checked=0;
	TextView achive_rate_tv;
	private ArrayList<ThumbImageInfo> mThumbImageInfoList;
	private GridView mGvImageList;
	



		@Override
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	         
	        // ���� ��Ƽ��Ƽ�κ��� �Ѿ�� �����͸� ������.
	        String date = getIntent().getStringExtra("date");
	        String month = getIntent().getStringExtra("month");
	        String year = getIntent().getStringExtra("year");
	        boolean party = getIntent().getBooleanExtra("isParty", false);
	        String partystring [] = getIntent().getStringArrayExtra("party");

		    //  int color = getIntent().getIntExtra("color", Color.WHITE);
	         
	        
	        if(party)
	        {	        	
		        setContentView(R.layout.meeting_info_page);
		        ListView meeting = (ListView) findViewById(R.id.to_do_list_list);
		        
		        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View header = (View) inflater.inflate(R.layout.meeting_info_page_header, null);

		        String[] party_info = getIntent().getStringArrayExtra("party");

		        Num_todoList=party_info[TODOLIST].split(delims).length;
		        
	        	CheckString [] fuck = new CheckString [Num_todoList];
	        	
	        	
	        	System.out.println(Num_todoList);

		        for(int i=0; i<Num_todoList; i++)
		        {

		        	String string = party_info[TODOLIST].split(delims)[i];
		        	fuck[i] = new CheckString();
		        	
		        	fuck[i].setString(string);

		        	todolist_list.add(fuck[i]);
		        }
		        
		        ListView lv = (ListView)findViewById(R.id.to_do_list_list);
	        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		        
	        
	        
	        //thum array
	        
		        mThumbImageInfoList = new ArrayList<ThumbImageInfo>();

		        
		        //thumb start
		        ThumbImageInfo thumbInfo = new ThumbImageInfo();

		        thumbInfo.setId("��ö");
		        thumbInfo.setThum_img("member_byung");
		        thumbInfo.setCheckedState(false);
		        
		        mThumbImageInfoList.add(thumbInfo);
		        
		        ThumbImageInfo thumbInfo1 = new ThumbImageInfo();

		        thumbInfo.setId("�⽺");
		        thumbInfo.setThum_img("member_jobs");
		        thumbInfo.setCheckedState(false);
		        
		        mThumbImageInfoList.add(thumbInfo1);
		        
		        ThumbImageInfo thumbInfo2 = new ThumbImageInfo();

		        thumbInfo.setId("���̵� ����");
		        thumbInfo.setThum_img("member_ladygaga");
		        thumbInfo.setCheckedState(false);
		        
		        mThumbImageInfoList.add(thumbInfo2);
		        
		        ThumbImageInfo thumbInfo3 = new ThumbImageInfo();

		        thumbInfo.setId("��");
		        thumbInfo.setThum_img("member_ya");
		        thumbInfo.setCheckedState(false);
		        
		        mThumbImageInfoList.add(thumbInfo3);
		        
		        //thum end
		        
		        
		        mGvImageList = (GridView) findViewById(R.id.meeting_entry_grid_view);
		        //mGvImageList.setOnItemClickListener(this);
		        mListAdapter = new ImageAdapter (this, R.layout.image_cell, mThumbImageInfoList);

//		        mGvImageList.setAdapter(mListAdapter);
		        

		       
		        mAdapter = new CheckTodoListAdapter(this, R.layout.meeting_info_list_row, todolist_list);
		        
		        
		        //define
				TextView meeting_location_input_text = (TextView)header.findViewById(R.id.meeting_location_input_text);
				meeting_location_input_text.setText(partystring[LOCATION]);
				
				TextView meeting_time_input_text = (TextView)header.findViewById(R.id.meeting_time_input_text);
				meeting_time_input_text.setText(partystring[TIME]);
				
				achive_rate_tv = (TextView)header.findViewById(R.id.achievement_rate_text);
				
				achivebar = (ProgressBar) header.findViewById(R.id.achievement_progress_bar);
				
				
				
				meeting.addHeaderView(header);
		        meeting.setAdapter(mAdapter);
		        
		        meeting.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long item) {
						if(position==0)
						{
							System.out.println("whatido");
						}
						else{
							System.out.println("what should I do");

							check = todolist_list.get(position-1);
							CheckBox tv = (CheckBox) arg1.findViewById(R.id.todolist_checkbox);
							if(check.isCheck()==true)
							{
								tv.setChecked(false);
								checked--;
								check.setCheck(false);
								tv.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
							}
							else
							{
								tv.setChecked(true);
								System.out.println("fuck "+ checked);
								checked++;
								System.out.println("fuck "+ checked);

								check.setCheck(true);
								tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

							}
							System.out.println("fuck "+ checked);

							achive_rate=checked*100/Num_todoList;
							System.out.println("fuck       "+ achive_rate);
							
							achive_rate_tv.setText("��ǥ �޼��� : "+achive_rate+"%");

							achivebar.setMax(100);
							achivebar.setProgress(achive_rate);
							System.out.println("fuck       "+ achive_rate);



						}
					}
				});
		  
		        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	        }	
	        else   alert(date, month, year);

	        
	      
	    }
		
		String todolist_select;
		
		
	        
	    
	     
	    /**
	     * ��ü������ ���� �˷����̾�α� ���� �޼ҵ�
	     * 
	     * @param title Ÿ��Ʋ
	     * @param message �޽���
	     */
	    private void alert(final String date, final String month, final String year)
	    {
	        // ü�������� �޼ҵ带 ����Ѵ�.
	        new AlertDialog.Builder(this)
	            // ������ Ÿ��Ʋ�� �����Ѵ�.
	            .setTitle("����")
	            // ������ �޽��� �κп� �����Ѵ�.
	            .setMessage(year+"�� "+month+"�� "+ date + "��")
	            // ��Ҹ� ���ϵ��� ���´�.
	            .setCancelable(false)
	            // Ȯ�� ��ư�� �����.
	       
	            .setPositiveButton("����", new DialogInterface.OnClickListener()
	            {
	                /* (non-Javadoc)
	                 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	                 */
	                public void onClick(DialogInterface dialog, int which)
	                {
	                	System.out.println("fucking!!!!!!!!");
	                	Bundle bundle = new Bundle();
	                	bundle.putString("color", Color.YELLOW+"");
	                	bundle.putString("date", date);
	                	bundle.putString("month",  month);
	                	bundle.putString("year",year);
	            		Intent intent = new Intent(getApplicationContext(), GroupShowActivity.class);
	                	System.out.println("fucking!!!!!!!!");

	            		intent.putExtras(bundle);
	          	    

	                	System.out.println("fucking!!!!!!!!");

	                    // Ȯ�ι�ư�� Ŭ���Ǹ� ���̾�α׸� �����Ѵ�.
	                    dialog.dismiss();
	                	System.out.println("fucking!!!!!!!!");

	                    // ��Ƽ��Ƽ�� �����Ѵ�.
	                    finish();
	                	System.out.println("fucking!!!!!!!!");

	                    startActivity(intent);
	                	System.out.println("fucking!!!!!!!!");

	                }
	            })
	            		
	            		
	            		
	            .show();
	    }



		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}
	 
	}

