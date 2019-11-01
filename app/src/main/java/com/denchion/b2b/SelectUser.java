package com.denchion.b2b;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

public class SelectUser extends Activity {
	
	List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ListView p_my_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_select_user);
		DBHelper.loaddb(getApplicationContext());
		 p_my_list= (ListView) findViewById(R.id.p_my_list);
		 Utils.setActionBar(this,"Select Partner");
		 loadMyList();
	}
	
	public void loadMyList()
    {
    	mylist.clear();
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(c.moveToFirst())
    	{
    		
    	
    			Cursor d=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+c.getString(16)+")");
    			while(d.moveToNext())
    			{
    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",d.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(8)+", "+d.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+d.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("content_id",d.getString(0));
			    mylist.add(hm); 
			   Cursor e=DBHelper.getData(getApplicationContext(),Utils.partners_subs," where parent='"+d.getString(0)+"'");
		    	while(e.moveToNext())
		    	{
		    		HashMap<String, String> hm2 = new HashMap<String, String>();
				    hm2.put("uname",e.getString(2).toUpperCase(Locale.getDefault()));
				    hm2.put("location",d.getString(8)+", "+d.getString(9));              
				    hm2.put("img","");
				    hm2.put("content_id",e.getString(0));
				    mylist.add(hm2); 	
				   
		    	}
			    
    		
    			}
    	}
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
         SimpleAdapter adapter = new SimpleAdapter(this, mylist,R.layout.list_layout , from, to); 
        
         
         p_my_list.setAdapter(null);
         adapter.notifyDataSetChanged();
         p_my_list.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
         p_my_list.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               String fieldname = fishtextview.getText().toString();
               Intent intent = new Intent(SelectUser.this, Conversations.class);
               intent.putExtra("id", fieldname);
               startActivity(intent);  
               finish();
             }
             });
    }
	
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.main,menu);
			
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
