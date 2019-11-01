package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class SubUserActivity extends Activity{

	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	AsyncResponse delegate = null;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;

	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_activity_sub_user);
		Utils.setActionBar(this,"Sub User Activites");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		
		subactivties();
	}
	
	 public void subactivties()
	 {
		 users.clear();
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.subactivties," where subid='"+id+"' order by activity_id DESC");
 		if(c.getCount()==0)
 		{
 			//Toast.makeText(getApplicationContext(), "Please Wait! Coupons Loading...", Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		else 
 		{
 			
 			
     		while(c.moveToNext())
     		{

		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(3));
		    hm.put("date",Utils.parseDateToddMMyyyy2(c.getString(4))+" "+Utils.ConvertTime(c.getString(5)));
		    hm.put("content_id",c.getString(0));
		    hm.put("type",c.getString(6));
		    hm.put("uid",c.getString(7));
            users.add(hm);
             }
     		
     		 String[] from = {"uname","date", "content_id","type","uid"};
             int[] to = {R.id.u_title,R.id.u_type,R.id.content_id,R.id.type,R.id.uid};
             SimpleAdapter adapter = new SimpleAdapter(this, users,R.layout.list_layout3 , from, to); 
             ListView mylist = (ListView) findViewById(R.id.activity_lists);
             mylist.setAdapter(null);
             adapter.notifyDataSetChanged();
             mylist.setAdapter(adapter);
             adapter.notifyDataSetChanged();
            
             // Click event for single list row
             
             mylist.setOnItemClickListener(new OnItemClickListener(){

                 @Override
                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                  
                	 ListView lv = (ListView) arg0;
    	             TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.type);
    	             TextView fishtextview3=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.uid);
    	             String fieldname2 = fishtextview2.getText().toString();
    	             String fieldname3 = fishtextview3.getText().toString();
    	             Intent intent =null;  
    	             switch(Integer.parseInt(fieldname2))
    	             {
    	     
    	             case 1: 
    	            	 intent = new Intent(SubUserActivity.this, Orders.class);
    	                 intent.putExtra("id", fieldname3);
    	                 intent.putExtra("tp", "0");
    	                 startActivity(intent);  
    	            	 break;
    	             case 2: 
    	             	 intent = new Intent(SubUserActivity.this, SendPayment.class);
    	                 intent.putExtra("tb", "0");
    	                 startActivity(intent);  
    	            	 break;
    	             case 3: 
    	            	 intent = new Intent(SubUserActivity.this, QuoteDetails.class);
    	                 intent.putExtra("id", fieldname3);
    	                 startActivity(intent);  
    	            	 break;
    	             case 4: 
    	             	/* intent = new Intent(SubUserActivity.this, Messages.class);
    	                 startActivity(intent);  */
    	                 break;
    	                 
    	             case 6: 
    	            	 intent = new Intent(SubUserActivity.this, ViewCollection.class);
    	                 startActivity(intent);  
    	            	 break;
    	             }
    	            
                 }
                 });
     	
 		}
	 }

	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case android.R.id.home:
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

	 
}
