package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SendOffers extends Activity implements AsyncResponse{

	String gid;
	ListView p_sent;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	    private int year;
	    private int month;
	    private int day;
	    static final int DATE_PICKER_ID = 1111;
	    DatePickerDialog.OnDateSetListener pickerListener ;
	ArrayList<String> selectedStrings = new ArrayList<String>();
	List<HashMap<String, String>> ulist = new ArrayList<HashMap<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_add_user_to_group);
		Utils.setActionBar(this,"Send Info/Promo Offers");
		DBHelper.loaddb(getApplicationContext());
		p_sent = (ListView) findViewById(R.id.p_sent);
	    Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
		loadData();
	}
	
	public void loadData()
    {
		ulist.clear();
		
		 Cursor m=DBHelper.getData(getApplicationContext(),Utils.usr,"");
	    	if(m.moveToFirst())
	    	{
    		
    			Cursor d=DBHelper.getData(getApplicationContext(),Utils.partners,"  where partner_id in ("+m.getString(16)+")");
    			while(d.moveToNext())
    			{
    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",d.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(8)+", "+d.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+d.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("content_id",d.getString(0));
			    ulist.add(hm);  
    		
    			}
	    	}
    	
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
         SimpleAdapter adapter = new SimpleAdapter(this, ulist,R.layout.list_layout_checkbox , from, to); 
        
         
         p_sent.setAdapter(null);
         adapter.notifyDataSetChanged();
         p_sent.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
       p_sent.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
                TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               final CheckBox chkbx=(CheckBox)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.multis);
               final String pid = fishtextview.getText().toString();
 
               if(chkbx.isChecked() == false)
               {
                   selectedStrings.add(pid);
            	   chkbx.setChecked(true);
            	   chkbx.setEnabled(true);
            	   
               }
               else
               {
                   selectedStrings.remove(pid);
            	   chkbx.setChecked(false);
            	   chkbx.setEnabled(false);
               }
              
         
             
             }
             }); 
         

    }
	
	ArrayList<String> getSelectedString(){
		  return selectedStrings;
		}
	
	
	
	
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.addu2,menu);
			
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case android.R.id.home:
	                finish();
	                break;
	         case R.id.sendd:
	        	 if(selectedStrings.size() != 0){
	        		 dialogDetails(selectedStrings.toString());
	        	 }
	        	 else
	        	 {
	       Toast.makeText(getApplicationContext(),"Please select one user!", Toast.LENGTH_LONG).show();		 
	        	 }
	        	 break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
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
	 

	 @SuppressLint("InflateParams") 
	 public void dialogDetails(final String ids) {

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			this);
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompts2c, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.UserInput);
		          final EditText userInput2 = (EditText) promptsView
							.findViewById(R.id.UserInput2);
		          final EditText userInput3 = (EditText) promptsView
							.findViewById(R.id.UserInput3);
	          builderSingle.setTitle("Send Notification");
	          
	   
		    
		  
	          
	          userInput2.setOnFocusChangeListener(new OnFocusChangeListener() {
	  			@SuppressWarnings("deprecation")
	  			@Override
	  			public void onFocusChange(View v, boolean hasFocus) {
	  			    if(hasFocus){
	  			    	
	  			    	 showDialog(DATE_PICKER_ID); 
	  			    }else {
	  			       
	  			    }
	  			   }
	  			});
	          
	           pickerListener = new DatePickerDialog.OnDateSetListener() {
	        		 
	  	        // when dialog box is closed, below method will be called.
	  	        @Override
	  	        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
	  	             
	  	            year  = selectedYear;
	  	            month = selectedMonth;
	  	            day   = selectedDay;
	  	 
	  	            String tm=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
	  	            // Show selected date 
	  	           // e_sub_1.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
	  	          userInput2.setText(tm);
	  	           }
	  	        };
	  	    

	          builderSingle.setView(promptsView);
	
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	 if(userInput.getText().toString().equals("") || userInput2.getText().toString().equals(""))
	                    			Toast.makeText(getApplicationContext(), "Oops All Field Mandatory! Try Again!", Toast.LENGTH_SHORT).show();
	                    	 else {
	                    		 
	                    		 try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id",getApplicationContext()));
	                    			data.put("subject", userInput.getText().toString().replace("/", "~"));
	                    			data.put("des", userInput3.getText().toString().replace("/", "~"));
	                    			data.put("expiry",userInput2.getText().toString());
	                    			data.put("partners",ids);
	                    			if(Utils.isConnectingToInternet(getApplicationContext())){		
	                    	  httpGetAsyncTask = new HttpGetAsyncTask(SendOffers.this,3);	
	                          httpGetAsyncTask.delegate=SendOffers.this;
	                    	  httpGetAsyncTask.execute(Utils.createMultiNotifications+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			}else
	                    	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                           
	                    	  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
	        					// TODO Auto-generated catch block
	        					e.printStackTrace();
	        				}
	                          dialog.dismiss();
	                    	 }
	                      }
	                  });

	     
	          builderSingle.show();
	      }


	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
	Toast.makeText(getApplicationContext(), "Notifications successfully sent to the selected users", Toast.LENGTH_SHORT).show(); 
	
	 Intent intent = new Intent(SendOffers.this, PromoOffers.class);
	 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 startActivity(intent);
	 finish();
	}
	
	
	 
}
