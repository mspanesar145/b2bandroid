package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
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

public class Messages extends Activity implements AsyncResponse{

	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	ListView lv;
	
	List<HashMap<String, String>> Msglist  = new ArrayList<HashMap<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_messages);
		DBHelper.loaddb(getApplicationContext());
		Utils.setActionBar(this,"Messages");
		 lv     =(ListView) findViewById(R.id.recent);
		 httpGetAsyncTask = new HttpGetAsyncTask(this,1);
	        httpGetAsyncTask.delegate = this;
	        if(Utils.isConnectingToInternet(getApplicationContext()))
	        	httpGetAsyncTask.execute(Utils.gtmesg+Utils.getDefaults("user_id",getApplicationContext()));
	        else
	        	Toast.makeText(Messages.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();
	
	} 

	
	 public void loadConversations()
	    {
		 Msglist.clear();
		String q=" GROUP BY common_obj";
		/* if(!Utils.getDefaults("subid",getApplicationContext()).equals("0"))
			{
			 q=" where parent!=0 GROUP BY common_obj";
			}*/
		 
		 Cursor d=DBHelper.getData(getApplicationContext(),Utils.msgtbl,q);
	    	
	    	while(d.moveToNext())
	    			{
	    		    String nm="";
	    		    String nmm="";
	    		    String ids="";
	    			String []chk1=d.getString(3).split("[_]");	
	    			if(chk1[0].equals(Utils.getDefaults("user_id", getApplicationContext())))ids=chk1[1];
	    			else ids=chk1[0];
	    			
	    			HashMap<String, String> hm = new HashMap<String, String>();
	    			
	    			if(d.getString(5).equals("0"))nm=DBHelper.getNameByID(getApplicationContext(),ids);
	    			else nm=DBHelper.getNameByID(getApplicationContext(),d.getString(5))+"( Subuser )";
	    			
	    			if(nm.equals(""))nm="(Subuser)";
	    			
	    			if(d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))nmm=d.getString(12); else 
	    				
	    				{
	    				nmm=d.getString(11)+" \n( "+d.getString(10)+" )";
	    				}
	    			
	    			hm.put("uname",nmm.replace("( null )", ""));
				    hm.put("location",Utils.parseDateToddMMyyyy2(d.getString(8))+" -  "+Utils.ConvertTime(d.getString(9)));
				    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+ids+".jpg");                
				    hm.put("img",imgFile.toString());
				    hm.put("content_id",ids);
				    int tmesg=DBHelper.getunreadMesgs(getApplicationContext(),d.getString(1));
				    hm.put("status",tmesg+" unread");
				  
				    Msglist.add(hm);  
	    		
	    			}
	    	
	    	 String[] from = { "uname" , "location" , "img" , "content_id", "status" };
	         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id,R.id.u_status };
	         SimpleAdapter adapter = new SimpleAdapter(this, Msglist,R.layout.list_layout, from, to); 
	        
	         
	         lv.setAdapter(null);
	         adapter.notifyDataSetChanged();
	         lv.setAdapter(adapter);
	         adapter.notifyDataSetChanged();
	        
	         // Click event for single list row
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

	             @Override
	             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                   // TODO Auto-generated method stub
	               ListView lv = (ListView) arg0;
	               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
	               String fieldname = fishtextview.getText().toString();
	               String fieldname2 = fishtextview2.getText().toString();
	               Intent intent = new Intent(Messages.this,Conversations.class);
	               intent.putExtra("id", fieldname);
	               intent.putExtra("uname", fieldname2);
	               startActivity(intent);  
	               finish();
	             }
	             });
	    }
	 
	 
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.nmsg, menu);
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.nmsg:
	             Intent homeIntent = new Intent(this, SelectUser.class);
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
			//
			jsonObj = new JSONObject(output);	
			 JSONArray Data = jsonObj.getJSONArray("messages");	
			 JSONArray Data2 = jsonObj.getJSONArray("reminder");	
		DBHelper.storeMessages(getApplicationContext(),Data);
		DBHelper.storeReminders(getApplicationContext(),Data2);
		
		loadConversations();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
