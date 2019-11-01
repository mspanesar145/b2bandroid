package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

public class AddSubUser extends Activity implements AsyncResponse{

	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	List<HashMap<String, String>> users = new ArrayList<HashMap<String,String>>();
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_add_sub_user);
		
		listView = (ListView) findViewById(R.id.listView1);
 		
		 Utils.setActionBar(this,"Sub Users");
 		 DBHelper.loaddb(getApplicationContext());
 		 
        httpGetAsyncTask = new HttpGetAsyncTask(AddSubUser.this,3);	
    		 httpGetAsyncTask.delegate=this;
    		 if(Utils.isConnectingToInternet(getApplicationContext()))httpGetAsyncTask.execute(Utils.getSubs+Utils.getDefaults("user_id",getApplicationContext()));
             else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
        getSubUsers();
	}

	
	 public void getSubUsers()
	 {
		 users.clear();
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.bsu," order by user_id DESC");
 	
     		while(c.moveToNext())
     		{
		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(1).toUpperCase(Locale.getDefault()));
		    hm.put("designation","Designation:"+c.getString(5));
		    hm.put("date","Department:"+c.getString(6));
		    hm.put("content_id",c.getString(0));
            users.add(hm);
             }
     		
     		 String[] from = { "uname","designation","date","content_id"};
             int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id};
             SimpleAdapter adapter = new SimpleAdapter(this,users,R.layout.list_layout3, from, to); 

             listView.setAdapter(null);
             adapter.notifyDataSetChanged();
             listView.setAdapter(adapter);
             adapter.notifyDataSetChanged();
            
             // Click event for single list row
             
             listView.setOnItemClickListener(new OnItemClickListener(){

                 @Override
                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                	 
                		ListView lv = (ListView) arg0;
                        TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                        String gid = fishtextview.getText().toString();
                        Intent intent = new Intent(AddSubUser.this, SubUserDetails.class);
                        intent.putExtra("id", gid);
                        startActivity(intent);  
                	   
                 }
                 });
 
     	
 		}
	 
	
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.subuser, menu);
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.nSub:
	             Intent homeIntent = new Intent(this, AddSub.class);
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
				JSONArray Data = jsonObj.getJSONArray("subs");		
				 JSONArray Data2 = jsonObj.getJSONArray("activites");								 
					DBHelper.storeActivities(getApplicationContext(),Data2);
				DBHelper.storeSubs(getApplicationContext(),Data);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			getSubUsers();
		}
}
