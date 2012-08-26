package me.croute.calendarexample.activity;

import java.util.ArrayList;

import com.activity.GroupShowActivity;
import com.activity.R;
import com.fragment.StepFragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

public class calendardialog extends Activity{
	


	    @Override
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.post_show_comment_list);
	         
	        // 이전 액티비티로부터 넘어온 데이터를 꺼낸다.
	        String date = getIntent().getStringExtra("date");
	        String month = getIntent().getStringExtra("month");
	        String year = getIntent().getStringExtra("year");

	      //  int color = getIntent().getIntExtra("color", Color.WHITE);
	         
	       alert(date, month, year);
	        
	      
	    }
	        
	    
	     
	    /**
	     * 자체적으로 만든 알럿다이얼로그 생성 메소드
	     * 
	     * @param title 타이틀
	     * @param message 메시지
	     */
	    private void alert(final String date, final String month, final String year)
	    {
	        // 체인형으로 메소드를 사용한다.
	        new AlertDialog.Builder(this)
	            // 색상을 타이틀에 세팅한다.
	            .setTitle("일정")
	            // 설명을 메시지 부분에 세팅한다.
	            .setMessage(year+"년 "+month+"월 "+ date + "일")
	            // 취소를 못하도록 막는다.
	            .setCancelable(false)
	            // 확인 버튼을 만든다.
	       
	            .setPositiveButton("모임", new DialogInterface.OnClickListener()
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

	                    // 확인버튼이 클릭되면 다이얼로그를 종료한다.
	                    dialog.dismiss();
	                	System.out.println("fucking!!!!!!!!");

	                    // 액티비티를 종료한다.
	                    finish();
	                	System.out.println("fucking!!!!!!!!");

	                    startActivity(intent);
	                	System.out.println("fucking!!!!!!!!");

	                }
	            })
	            		
	            		
	            		
	            .show();
	    }
	 
	}

