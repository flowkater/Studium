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
	         
	        // ���� ��Ƽ��Ƽ�κ��� �Ѿ�� �����͸� ������.
	        String date = getIntent().getStringExtra("date");
	        String month = getIntent().getStringExtra("month");
	        String year = getIntent().getStringExtra("year");

	      //  int color = getIntent().getIntExtra("color", Color.WHITE);
	         
	       alert(date, month, year);
	        
	      
	    }
	        
	    
	     
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
	 
	}

