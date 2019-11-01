package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewReminders extends Activity implements AsyncResponse{
	
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	 HttpGetAsyncTask httpGetAsyncTask;
	  List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_view_reminders);
		Utils.setActionBar(this,"Reminders");
		DBHelper.loaddb(getApplicationContext());
	     httpGetAsyncTask = new HttpGetAsyncTask(ViewReminders.this,1);
	        httpGetAsyncTask.delegate=ViewReminders.this;
	        if(Utils.isConnectingToInternet(getApplicationContext()))
	        httpGetAsyncTask.execute(Utils.gtReminder+Utils.getDefaults("user_id", getApplicationContext()));
	        else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	}
	
	
	 public void getReminders()
	 {
		users.clear();
			
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.reminder," order by reminder_id DESC");
		
	while(c.moveToNext())
    		{

		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(2));
		    hm.put("utype",Utils.parseDateToddMMyyyy2(c.getString(3))+" "+Utils.ConvertTime(c.getString(5)));
		    hm.put("content_id",c.getString(0));
           users.add(hm);
            }
    		
    		 String[] from = { "uname", "utype","content_id" };
            int[] to = {R.id.u_title,R.id.u_type,R.id.content_id};
            SimpleAdapter adapter = new SimpleAdapter(this,users,R.layout.list_layout_remind,from,to); 
            ListView mylist = (ListView) findViewById(R.id.reminders);
            mylist.setAdapter(null);
            adapter.notifyDataSetChanged();
            mylist.setAdapter(adapter);
            adapter.notifyDataSetChanged();
           
            // Click event for single list row
            
            mylist.setOnItemClickListener(new OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                 
               	   ListView lv = (ListView) arg0;
                      TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                      String fieldname = fishtextview.getText().toString();
     
                      Delremind(fieldname);
                }
                });
            
           
    	
		
	 }
	 
	 public void Delremind(final String pid)
		{
			new AlertDialog.Builder(this)
			.setMessage("Do you really want to Delete this Reminder?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
			    		  httpGetAsyncTask = new HttpGetAsyncTask(ViewReminders.this,1);
					       httpGetAsyncTask.delegate=ViewReminders.this;
			    		 httpGetAsyncTask.execute(Utils.DelRemind+pid+"/"+Utils.getDefaults("user_id", getApplicationContext())); 
			    		 
			    	 }else 
			    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
		}
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.remind, menu);
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.nRemind:
	             Intent homeIntent = new Intent(this, Reminders.class);
	             startActivity(homeIntent);
	        	 break;
	         case android.R.id.home:
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }


	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("reminder");		
			DBHelper.storeReminders(getApplicationContext(),Data);
			getReminders();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
