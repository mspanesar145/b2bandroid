package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class Partners extends Activity implements AsyncResponse{


	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	
	List<HashMap<String, String>> searchlist  = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> mylist      = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> pendinglist = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> sentlist    = new ArrayList<HashMap<String, String>>();
    
	EditText searchbox;
	ListView srchlist;
	ListView p_sent;
	ListView p_pending;
	ListView p_my_list;
	String tb;
	String ttp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_partners);
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		tb = bundle.getString("tb");
		ttp = bundle.getString("ttp");
		Utils.setActionBar(this,"Find Partners");
		
         searchbox=  (EditText) findViewById(R.id.keywordbox);
         srchlist =  (ListView) findViewById(R.id.p_search_list);
         p_sent =    (ListView) findViewById(R.id.p_sent);
         p_pending = (ListView) findViewById(R.id.p_pending);
         p_my_list=  (ListView) findViewById(R.id.p_my_list);
         
         searchbox.setFocusableInTouchMode(true);
         searchbox.requestFocus();
         
         tabHost(Integer.parseInt(tb));
         
         
     	
         searchbox.setOnKeyListener(new View.OnKeyListener() {
        	    public boolean onKey(View v, int keyCode, KeyEvent event) {
        	        // If the event is a key-down event on the "enter" button
        	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
        	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
        	        	if(Utils.isConnectingToInternet(getApplicationContext()))
        	        	loadsearch();
        	        	else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
        	          return true;
        	        }
        	        return false;
        	    }

			
        	});
         
         httpGetAsyncTask = new HttpGetAsyncTask(Partners.this,3);	
		 httpGetAsyncTask.delegate=this;
		 if(Utils.isConnectingToInternet(getApplicationContext()))httpGetAsyncTask.execute(Utils.dlnt+Utils.getDefaults("user_id",getApplicationContext())+"/"+ttp);
         else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		 
		
	}
	
	
 public void loadsearch()
 {
	 searchlist.clear();
	 if(!searchbox.getText().toString().equals(""))
	 {

	
		try {
	  		data.put("user_id", Utils.getDefaults("user_id",getApplicationContext()));
			data.put("query", searchbox.getText().toString());
			
			 httpGetAsyncTask = new HttpGetAsyncTask(Partners.this,3);	
			 httpGetAsyncTask.delegate=this;
	         httpGetAsyncTask.execute(Utils.loadsearch+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
    	 } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	 }
 }
	
	public void tabHost(int tb)
    {
		 TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		 tabHost.setup();
		 final TabWidget tabWidget = tabHost.getTabWidget();
		 final FrameLayout tabContent = tabHost.getTabContentView();
		 // Get the original tab textviews and remove them from the viewgroup.
		 TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
		 for (int index = 0; index < tabWidget.getTabCount(); index++) {
		 originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
		 }
		 tabWidget.removeAllViews();
		 // Ensure that all tab content childs are not visible at startup.
		 for (int index = 0; index < tabContent.getChildCount(); index++) {
		 tabContent.getChildAt(index).setVisibility(View.GONE);
		 }
		
		 for (int index = 0; index < originalTextViews.length; index++) {
		 final TextView tabWidgetTextView = originalTextViews[index];
		 final View tabContentView = tabContent.getChildAt(index);
		 TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
		 tabSpec.setContent(new TabContentFactory() {
		 @Override
		 public View createTabContent(String tag) {
		 return tabContentView;
		 }
		 });
		 if (tabWidgetTextView.getBackground() == null) {
		 tabSpec.setIndicator(tabWidgetTextView.getText());
		 } else {
		 tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
		 }
		 tabHost.addTab(tabSpec);
		 tabHost.setCurrentTab(tb);
		 }
    }
	
	
    public void loadSearchData()
    {
    	searchlist.clear();
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.seasrchusr,"");
    	if(c.getCount()==0)
    	{
    		//Toast.makeText(getApplicationContext(), "No Partner Found!", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		int cnt=0;
    		int cnt2=0;
    	while(c.moveToNext())
    	{
    		String nm="";
    		cnt=DBHelper.getAcceptedUsers(getApplicationContext(),c.getString(0));
    		if(cnt!=0)nm="Already partner";
    		
    		cnt2=DBHelper.getSentUsers(getApplicationContext(),c.getString(0));
    		if(cnt2!=0)nm="Request Sent";
    		
    	        HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",c.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",c.getString(8)+", "+c.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+c.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("status",nm);
			    hm.put("content_id",c.getString(0));
			    searchlist.add(hm);   
    	}
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id","status" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id,R.id.u_status};
         SimpleAdapter adapter = new SimpleAdapter(this, searchlist,R.layout.list_layout , from, to); 
        
         
         srchlist.setAdapter(null);
         adapter.notifyDataSetChanged();
         srchlist.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
         srchlist.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
               String fieldname = fishtextview.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(Partners.this, ProfileDetails.class);
               intent.putExtra("id", fieldname);
               intent.putExtra("uname", fieldname2);
               intent.putExtra("tp", "0");
               startActivity(intent);  
              finish();
             }
             });
    	}
    }
	
    public void loadSentRequests()
    {
    	sentlist.clear();
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(c.moveToFirst())
    	{
    		
    		
    			Cursor d=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+c.getString(14)+")");
    			while(d.moveToNext())
    			{
    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",d.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(8)+", "+d.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+d.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("content_id",d.getString(0));
			    sentlist.add(hm);  
    		
    			}
    	}
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
         SimpleAdapter adapter = new SimpleAdapter(this, sentlist,R.layout.list_layout , from, to); 
        
         
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
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
               String fieldname = fishtextview.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(Partners.this, ProfileDetails.class);
               intent.putExtra("id", fieldname);
               intent.putExtra("uname", fieldname2);
               intent.putExtra("tp", "3");
               startActivity(intent);  
             finish();
             }
             });
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
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
               String fieldname = fishtextview.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(Partners.this, ProfileDetails.class);
               intent.putExtra("id", fieldname);
               intent.putExtra("uname", fieldname2);
               intent.putExtra("tp", "1");
               startActivity(intent);  
               finish();
             }
             });
    }
	
    
    public void loadPendingList()
    {
    	pendinglist.clear();
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(c.moveToFirst())
    	{
    		
    		
    			Cursor d=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+c.getString(15)+")");
    			while(d.moveToNext())
    			{
    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",d.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(8)+", "+d.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+d.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("content_id",d.getString(0));
			    pendinglist.add(hm);  
    		
    			}
    	}
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
         SimpleAdapter adapter = new SimpleAdapter(this, pendinglist,R.layout.list_layout , from, to); 
        
         
         p_pending.setAdapter(null);
         adapter.notifyDataSetChanged();
         p_pending.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
         p_pending.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
               String fieldname = fishtextview.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(Partners.this, ProfileDetails.class);
               intent.putExtra("id", fieldname);
               intent.putExtra("uname", fieldname2);
               intent.putExtra("tp", "2");
               startActivity(intent);
               finish();
             }
             });
    }
	@Override
	public void processFinish(String output) {
		
	//	Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
	try {
			jsonObj = new JSONObject(output);
			
			JSONArray Data3 = jsonObj.getJSONArray("partners");
			JSONArray Data4 = jsonObj.getJSONArray("user");
			DBHelper.UpdateLogin(getApplicationContext(),Data4);
			DBHelper.storePartners(getApplicationContext(),Data3);
			loadSearchData();
			loadSentRequests();
			loadPendingList();
			loadMyList();
			
			JSONArray Data = jsonObj.getJSONArray("search");	
			if(Data.length()!=0)DBHelper.storeSearch(getApplicationContext(),Data);

			loadSearchData();
			loadSentRequests();
			loadPendingList();
			loadMyList();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
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