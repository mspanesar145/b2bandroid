package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class Reminders extends Activity implements AsyncResponse {
	
	EditText e_sub_1;
	EditText e_sub_2;
	EditText dtime;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	 private int year;
	    private int month;
	    private int day;
	    String curTime;
	    static final int DATE_PICKER_ID = 1111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_reminders);
		Utils.setActionBar(this,"Reminders");
		DBHelper.loaddb(getApplicationContext());
		
		e_sub_1=(EditText) findViewById(R.id.e_sub_1);
		e_sub_2=(EditText) findViewById(R.id.e_sub_2);
		dtime=(EditText) findViewById(R.id.dtime);
		
		Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
		
		curTime=hour+":"+minute;
		
		//dtime.setText(curTime);
		
		final Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
	    
	   // String tm=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
	    e_sub_1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			    	 showDialog(DATE_PICKER_ID); 
			    }else {
			       
			    }
			   }
			});
	    
	    dtime.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
	            // TODO Auto-generated method stub
	            Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(Reminders.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                	dtime.setText(String.format("%02d:%02d", selectedHour, selectedMinute)  );
	                }

	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Time");
	            mTimePicker.show();
			    }else {
				       
			    }
			   }
			});
	}
	
	
	
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_PICKER_ID:
	             
	            // open datepicker dialog. 
	            // set date picker for current date 
	            // add pickerListener listner to date picker
	            return new DatePickerDialog(this, pickerListener, year, month,day);
	        }
	        return null;
	    }
	 
	    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
	 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
	             
	            year  = selectedYear;
	            month = selectedMonth;
	            day   = selectedDay;
	 
	            String tm=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
	            // Show selected date 
	           // e_sub_1.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
	            e_sub_1.setText(tm);
	           }
	        };
	        
	
	public void SetReminder(View v)
	{
		 try {
				data.put("user_id",Utils.getDefaults("user_id",getApplicationContext()));
				data.put("subid",Utils.getDefaults("subid",getApplicationContext()));
				data.put("note",e_sub_2.getText().toString());
				data.put("date",e_sub_1.getText().toString());
				data.put("time",dtime.getText().toString());
	  httpGetAsyncTask = new HttpGetAsyncTask(Reminders.this,3);		
	  httpGetAsyncTask.delegate = this;
	  if(Utils.isConnectingToInternet(getApplicationContext()))
	  httpGetAsyncTask.execute(Utils.storeReminder+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	  else
     	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	
		
		  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
		Intent smIntent2 = new Intent(this, ViewReminders.class);
		smIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(smIntent2);
		
	}
}
