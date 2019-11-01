package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class SubOrders extends Activity implements AsyncResponse{
	
	
	ProgressDialog dialog;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;

	  List<HashMap<String, String>> accepted  = new ArrayList<HashMap<String, String>>();
	  List<HashMap<String, String>> sentorders  = new ArrayList<HashMap<String, String>>();
	  List<HashMap<String, String>> pendingorders  = new ArrayList<HashMap<String, String>>();
	  List<HashMap<String, String>> ignoredorders  = new ArrayList<HashMap<String, String>>();
	  ListView sentOrders;
	  ListView o_pending;
	  ListView o_ignored;
	  ListView accept;
    String tb="1";
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_sub_orders);
		
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		      tb = bundle.getString("tb");
		      Utils.setActionBar(this,"Sub Orders");
		      tabHost(Integer.parseInt(tb));
	          sentOrders =(ListView) findViewById(R.id.sent);
	          o_pending  =(ListView) findViewById(R.id.o_pending);
	          o_ignored  =(ListView) findViewById(R.id.o_ignored);
	          accept     =(ListView) findViewById(R.id.accept);
	         storeOrders();
	 
	      
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
	  
	 
	
	  public void storeOrders()
		 {
			   httpGetAsyncTask = new HttpGetAsyncTask(this,3);
		       httpGetAsyncTask.delegate=this;
			   httpGetAsyncTask.execute(Utils.getSubOrders+Utils.getDefaults("user_id", getApplicationContext())+"/"+Utils.getDefaults("subid", getApplicationContext()));
		 }
	 
	  
	  public void loadIgnoredOrders(String q)
	    {
		  String tsp="1";
		  ignoredorders.clear();
	    	Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders,q);
	    	while(d.moveToNext())
	    	{
	    	
	    		HashMap<String, String> hm = new HashMap<String,String>();
			    hm.put("uname","ORDER ID: "+d.getString(0).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(4));
			    hm.put("date",Utils.parseDateToddMMyyyy2(d.getString(21))+" "+Utils.ConvertTime(d.getString(22)));
			    hm.put("content_id",d.getString(0));
				    if(d.getString(18).equals("1"))tsp="2";
				    hm.put("tp",tsp);
				    ignoredorders.add(hm);  
	
	    			
	    		
	    		
	    	}
	    	
	    	 String[] from = { "uname" , "location" , "date" , "content_id" };
	         int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id};
	         SimpleAdapter adapter = new SimpleAdapter(this, ignoredorders,R.layout.list_layout3 , from, to); 
	        
	         
	         o_ignored.setAdapter(null);
	         adapter.notifyDataSetChanged();
	         o_ignored.setAdapter(adapter);
	         adapter.notifyDataSetChanged();
	        
	         // Click event for single list row
	         
	         o_ignored.setOnItemClickListener(new OnItemClickListener(){

	             @Override
	             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                   // TODO Auto-generated method stub
	             ListView lv = (ListView) arg0;
	             TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	             String fieldname = fishtextview.getText().toString();
	             Intent intent = new Intent(SubOrders.this, OrderDetails.class);
                 intent.putExtra("id", fieldname);
                 intent.putExtra("tp", "3");
                 startActivity(intent);  
	             }
	             });
	    }
	 
	  public void loadSentOrders(String q)
	    {
	    	sentorders.clear();
	    	String tsp="1";
	    	Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders,q);
	   
	    	while(d.moveToNext())
	    	{
	    		String bid="";
	    		String nm="";
	    		if(Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))bid=d.getString(3);
	    		else bid=d.getString(1);
	    		
	    		HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname","ORDER ID: "+d.getString(0).toUpperCase(Locale.getDefault()));
			    hm.put("location",DBHelper.getNameByID(getApplicationContext(),bid));
			    hm.put("date",Utils.parseDateToddMMyyyy2(d.getString(21))+" "+Utils.ConvertTime(d.getString(22)));
			    hm.put("content_id",d.getString(0));
			    hm.put("uid",d.getString(3));
			    if(d.getString(37).equals("1"))nm="Approval Pending"; 
			    else if(d.getString(37).equals("2"))nm="Saved as Draft";
			    else nm="";
			    hm.put("status",nm);
				    if(d.getString(18).equals("1"))tsp="2";
				    hm.put("tp",tsp);
				    sentorders.add(hm);  
	    	}
	    	
	    	 String[] from = { "uname" , "location" , "date" , "content_id" ,"tp","status","uid"};
	         int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id,R.id.type,R.id.u_status,R.id.uid};
	         SimpleAdapter adapter = new SimpleAdapter(this, sentorders,R.layout.list_layout3 , from, to); 
	        
	         
	         sentOrders.setAdapter(null);
	         adapter.notifyDataSetChanged();
	         sentOrders.setAdapter(adapter);
	         adapter.notifyDataSetChanged();
	        
	         // Click event for single list row
	         
	         sentOrders.setOnItemClickListener(new OnItemClickListener(){

	             @Override
	             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                   // TODO Auto-generated method stub
	               ListView lv = (ListView) arg0;
	               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.type);
	               String fieldname = fishtextview.getText().toString();
	               String fieldname2 = fishtextview2.getText().toString();
	             
	               Intent intent = new Intent(SubOrders.this, OrderDetails.class);
                 intent.putExtra("id", fieldname);
                 intent.putExtra("tp", fieldname2);
                 startActivity(intent);  
	             
	              
	             }
	             });
	    }
		
	    
	    public void loadPendingOrders(String q)
	    {
	    	pendingorders.clear();
	    	Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders,q);
	    	while(d.moveToNext())
	    	{
	    		
	
	    			HashMap<String, String> hm = new HashMap<String, String>();
				    hm.put("uname","ORDER ID: "+d.getString(0).toUpperCase(Locale.getDefault()));
				    hm.put("location",d.getString(4));
				    hm.put("date",Utils.parseDateToddMMyyyy2(d.getString(21))+" "+Utils.ConvertTime(d.getString(22)));
				    hm.put("content_id",d.getString(0));
				    pendingorders.add(hm);  
	    		
	    		
	    	}
	    	
	    	 String[] from = { "uname" , "location" , "date" , "content_id" };
	         int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id};
	         SimpleAdapter adapter = new SimpleAdapter(this, pendingorders,R.layout.list_layout3 , from, to); 
	        
	         
	         o_pending.setAdapter(null);
	         adapter.notifyDataSetChanged();
	         o_pending.setAdapter(adapter);
	         adapter.notifyDataSetChanged();
	        
	         // Click event for single list row
	         
	         o_pending.setOnItemClickListener(new OnItemClickListener(){

	             @Override
	             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                   // TODO Auto-generated method stub
	               ListView lv = (ListView) arg0;
	               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	               String fieldname = fishtextview.getText().toString();
	               Intent intent = new Intent(SubOrders.this, OrderDetails.class);
                 intent.putExtra("id", fieldname);
                 intent.putExtra("tp", "3");
                 startActivity(intent);  
	             }
	             });
	    }
		
	    public void loadAcceptedOrders(String q)
	    {
	    	accepted.clear();
	    	Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders,q);
	    	while(d.moveToNext())
	    	{
	    		
	                String nm="";
	    			HashMap<String, String> hm = new HashMap<String, String>();
	    			 hm.put("uname","ORDER ID: "+d.getString(0).toUpperCase(Locale.getDefault()));
				    hm.put("location",d.getString(4));
				    hm.put("uid",d.getString(1));
				    if(d.getString(12).equals("0")) nm="Pending";
		    		else if(d.getString(12).equals("1") && d.getString(15).equals("0") && d.getString(18).equals("0")) nm="Seller Accepted";
		    		else if(d.getString(12).equals("5") && d.getString(15).equals("0") && d.getString(18).equals("0")) nm="Buyer Accepted";
				    else if(d.getString(15).equals("1") && d.getString(18).equals("0")) nm="Dispatched";
				    else if(d.getString(15).equals("1") && d.getString(18).equals("1")) nm="Delivered";
				    else if(d.getString(12).equals("0") && d.getString(33).equals("1")) nm="Ignored";
				    else if(d.getString(12).equals("4")) nm="Cancelled";
				  
				    hm.put("status",nm);
				    hm.put("date",Utils.parseDateToddMMyyyy2(d.getString(21))+" "+Utils.ConvertTime(d.getString(22)));
				    hm.put("content_id",d.getString(0));
				    accepted.add(hm);  
	  
	    	}
	    	
	    	 String[] from = { "uname" , "location" , "date" , "content_id","status" ,"uid"};
	         int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id,R.id.u_status,R.id.uid};
	         SimpleAdapter adapter = new SimpleAdapter(this, accepted,R.layout.list_layout3 , from, to); 
	        
	         
	         accept.setAdapter(null);
	         adapter.notifyDataSetChanged();
	         accept.setAdapter(adapter);
	         adapter.notifyDataSetChanged();
	        
	         // Click event for single list row
	         
	         accept.setOnItemClickListener(new OnItemClickListener(){

	             @Override
	             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                   // TODO Auto-generated method stub
	               ListView lv = (ListView) arg0;
	               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	               TextView uuid=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.uid);
	               TextView stat=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
	               String fieldname = fishtextview.getText().toString();
	               String uid = uuid.getText().toString();
	               String status = stat.getText().toString();
	               Intent intent = new Intent(SubOrders.this, OrderDetails.class);
                 intent.putExtra("id", fieldname);
                 if(uid.equals(Utils.getDefaults("user_id", getApplicationContext())))
                 {
              	  if(status.equals("Delivered") || status.equals("Cancelled"))intent.putExtra("tp", "6"); 
              	  else if(status.equals("Seller Accepted") || status.equals("Partial Accept"))intent.putExtra("tp", "7");
              	  else if(status.equals("Buyer Accepted"))intent.putExtra("tp", "1");
              	  else if(status.equals("Dispatched"))intent.putExtra("tp", "5");
                 }
                 else 
                 {
              	   if(status.equals("Delivered") || status.equals("Cancelled")) intent.putExtra("tp", "6"); 
              	   else if(status.equals("Seller Accepted") || status.equals("Partial Accept"))intent.putExtra("tp", "6");
              	   else if(status.equals("Buyer Accepted"))intent.putExtra("tp", "4");
              	   else if(status.equals("Dispatched"))intent.putExtra("tp", "6");
              	   
                 }
                 startActivity(intent);  
	             }
	             });
	    }
		
		
		 @Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.orders, menu);
				return true;
			}

		 @Override
		 public boolean onOptionsItemSelected(MenuItem menuItem) {
		     switch (menuItem.getItemId()) {
		         case R.id.norder:
		             Intent homeIntent = new Intent(this, NewOrder.class);
		             startActivity(homeIntent);
	
		        	 break;
		         case R.id.sorder:
		             Intent homeIntent2 = new Intent(this, SearchOrder.class);
		             startActivity(homeIntent2);
	
		        	 break;
		         case android.R.id.home:
		                finish();
		                break;
		     }
		     return (super.onOptionsItemSelected(menuItem));
		 }
		 

		
			
		@Override
		public void processFinish(String output) {
			//Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
		try {
				jsonObj = new JSONObject(output);	
				  JSONArray Data = jsonObj.getJSONArray("orders");								 
					DBHelper.storeOrders(getApplicationContext(),Data); 
					 loadSentOrders(" where sender_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' and  accept_status=0 order by order_id DESC");
					loadPendingOrders(" where accept_status=0 and ignore_status=0 and approve_status=0 and receiver_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' order by order_id DESC");
					loadAcceptedOrders(" where  accept_status in (1,2,4,5) order by order_id DESC");
					loadIgnoredOrders(" where accept_status=0 and ignore_status=1 and receiver_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' order by order_id DESC");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
}
