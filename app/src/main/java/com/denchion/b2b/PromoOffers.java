package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class PromoOffers extends Activity implements AsyncResponse{

	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> users2 = new ArrayList<HashMap<String, String>>();
	Spinner sp_1;
	Spinner sp_2;
	SimpleAdapter adapter;
	 SimpleAdapter adapter2;
	 HttpGetAsyncTask httpGetAsyncTask;
		JSONObject data = new JSONObject();
		JSONObject jsonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_promo_offers);
		Utils.setActionBar(this,"Info/Promo Offers");
		DBHelper.loaddb(getApplicationContext());
		sp_1=(Spinner)findViewById(R.id.sp_1);
		sp_2=(Spinner)findViewById(R.id.sp_2);
		
		
		
	
		ArrayAdapter<String> adpt1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		adpt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sp_1.setAdapter(adpt1);
		sp_2.setAdapter(adpt1);
		adpt1.add("All");
		Cursor m=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(m.moveToFirst())
    	{
    	Cursor n=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+m.getString(16)+")");
    			while(n.moveToNext())
    			{
    		adpt1.add(n.getString(1));
    	        }
    	}
    	adpt1.setNotifyOnChange(true);
    	
    	sp_1.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	String text = sp_1.getSelectedItem().toString();
		    	if(!text.equals("All"))
		    	{
		    		String rid=DBHelper.getIDByName(getApplicationContext(),text);
		    		getPys(" and user_id='"+rid+"' ");
		    		
		    	}
		    	else getPys("");
		    
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	sp_2.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	String text = sp_2.getSelectedItem().toString();
		    	if(!text.equals("All"))
		    	{
		    		
		    		String sid=DBHelper.getIDByName(getApplicationContext(),text);
		    		receivedPys(" and partner_id='"+sid+"' ");
		    	}
		    	else receivedPys("");
		    	
		    		
		    	
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		tabHost(0);
		httpGetAsyncTask = new HttpGetAsyncTask(PromoOffers.this,3); 
	     httpGetAsyncTask.delegate = PromoOffers.this;
		 if(Utils.isConnectingToInternet(getApplicationContext())) httpGetAsyncTask.execute(Utils.gtmn+Utils.getDefaults("user_id", getApplicationContext()));
        else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		
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
		 
		 public void getPys(String para)
		 {
			
			final Cursor c=DBHelper.getData(getApplicationContext(),Utils.po," where partner_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' and ntype='23' "+para+" order by noti_id DESC");

	 		if(c.getCount()==0)
	 		{
	 			
	 			users.clear();
	 			if(adapter!=null)adapter.notifyDataSetChanged();
	 		}
	 		else 
	 		{
	 			
	 			users.clear();
	 			while(c.moveToNext())
	     		{
	 				String ptnr = DBHelper.getNameByID(getApplicationContext(),c.getString(1));
	                String nm="";
				    HashMap<String, String> hm = new HashMap<String,String>();
				    String []chk1=c.getString(4).split("[:]");
				    hm.put("uname",chk1[0]);
				    hm.put("partner","Receiver: "+ptnr);
				    hm.put("dt",c.getString(5));
				    hm.put("status",nm);
				    hm.put("content_id",c.getString(0));
	            users.add(hm);
	             }
	     		
	 			String[] from = { "uname","partner","dt","content_id" ,"status"};
	             int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id,R.id.u_status};
	            adapter = new SimpleAdapter(this,users,R.layout.list_layout3,from,to); 
	             ListView mylist = (ListView) findViewById(R.id.paymnts);
	             mylist.setAdapter(null);
	             adapter.notifyDataSetChanged();
	             mylist.setAdapter(adapter);
	             adapter.notifyDataSetChanged();
	            
	             // Click event for single list row
	             
	             mylist.setOnItemClickListener(new OnItemClickListener(){

	                 @Override
	                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                	 ListView lv = (ListView) arg0;
	    	             TextView  fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	    	             String fieldname = fishtextview.getText().toString();
	    	             
	    	        	 Intent intent = new Intent(PromoOffers.this, NotificationDetails.class);
	    				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    				 intent.putExtra("ttp",fieldname+"");
	    				 startActivity(intent);

	                 }
	                 });
	     	
	 		}
		 }

		 
		 public void receivedPys(String para)
		 {
			
			final Cursor c=DBHelper.getData(getApplicationContext(),Utils.po," where user_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' and ntype='23' "+para+" order by noti_id DESC");
	 		if(c.getCount()==0)
	 		{
	 			users2.clear();
	 			if(adapter2!=null)adapter2.notifyDataSetChanged();
	 			
	 		}
	 		else 
	 		{
	 			users2.clear();
	 			while(c.moveToNext())
	     		{
	 			String ptnr = DBHelper.getNameByID(getApplicationContext(),c.getString(2));
                String nm="";
			    HashMap<String, String> hm = new HashMap<String,String>();
			    String []chk1=c.getString(4).split("[:]");
			    hm.put("uname",chk1[0]);
			    hm.put("partner","Sender: "+ptnr);
			    hm.put("dt",c.getString(5));
			
			    hm.put("status",nm);
			    hm.put("content_id",c.getString(0));
	            users2.add(hm);
	             }
	     		
	     		 String[] from = { "uname","partner","dt","content_id" ,"status"};
	             int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id,R.id.u_status};
	             adapter2 = new SimpleAdapter(this, users2,R.layout.list_layout3 , from, to); 
	             ListView mylist = (ListView) findViewById(R.id.paymnts2);
	             mylist.setAdapter(null);
	             adapter2.notifyDataSetChanged();
	             mylist.setAdapter(adapter2);
	             adapter2.notifyDataSetChanged();
	            
	             // Click event for single list row
	             
	             mylist.setOnItemClickListener(new OnItemClickListener(){

	                 @Override
	                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	                	 
	                	 ListView lv = (ListView) arg0;
	    	             TextView  fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
	    	             String fieldname = fishtextview.getText().toString();         
	    	             Intent intent = new Intent(PromoOffers.this, NotificationDetails.class);
	    				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    				 intent.putExtra("ttp",fieldname+"");
	    				 startActivity(intent);

	                 }
                 });
     	
 		}
	 }
		 
		
		 
			@Override
			public void processFinish(String output) {
				// TODO Auto-generated method stub
				try {
					jsonObj = new JSONObject(output);
					JSONArray Data = jsonObj.getJSONArray("mnots");		
					DBHelper.storePromo(getApplicationContext(),Data);
					getPys("");
					receivedPys("");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			 
		
		 
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.payment, menu);
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.npay:
	        	 Intent intent = new Intent(PromoOffers.this, SendOffers.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent);
				 finish();
	        	 break;
	         case android.R.id.home:
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

}
