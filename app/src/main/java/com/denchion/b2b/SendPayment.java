package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class SendPayment extends Activity implements AsyncResponse{
	
	
	HttpGetAsyncTask httpGetAsyncTask;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> users2 = new ArrayList<HashMap<String, String>>();
	AlertDialog alertDialog;
	String tab;
	Spinner sp_1;
	Spinner sp_2;
	SimpleAdapter adapter;
	 SimpleAdapter adapter2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_send_payment);
		Utils.setActionBar(this,"Payments");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		tab = bundle.getString("tb");
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
		    		getPys(" and receiver_id='"+rid+"' ");
		    		
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
		    		receivedPys(" and sender_id='"+sid+"' ");
		    	}
		    	else receivedPys("");
		    	
		    		
		    	
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		tabHost(Integer.parseInt(tab));
		
		httpGetAsyncTask = new HttpGetAsyncTask(SendPayment.this,3); 
 	     httpGetAsyncTask.delegate = SendPayment.this;
 		 if(Utils.isConnectingToInternet(getApplicationContext())) httpGetAsyncTask.execute(Utils.gtpyurl+Utils.getDefaults("user_id", getApplicationContext()));
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
			
			final Cursor c=DBHelper.getData(getApplicationContext(),Utils.py," where sender_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' "+para+" order by payment_id DESC");

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
                String nm="";
			    HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname","ID#"+c.getString(0)+" ( To: "+c.getString(12)+" )");
			    hm.put("payment",c.getString(6)+": Rs "+c.getString(5));
			    hm.put("remarks","Remarks: "+c.getString(7));
			    hm.put("dt",Utils.parseDateToddMMyyyy2(c.getString(8))+" "+Utils.ConvertTime(c.getString(9)));
			    if(c.getString(10).equals("1"))nm="Received"; 
			    else if(c.getString(10).equals("2"))nm="Not Received"; 
			    else if(c.getString(10).equals("3"))nm="Cancelled";
			    else nm="Pending";
			    hm.put("status",nm);
			    hm.put("content_id",c.getString(0));
	            users.add(hm);
	             }
	     		
	     		 String[] from = { "uname","payment", "remarks","dt","content_id" ,"status"};
	             int[] to = {R.id.u_title,R.id.u_type,R.id.u_per,R.id.u_dt,R.id.content_id,R.id.u_status};
	            adapter = new SimpleAdapter(this,users,R.layout.list_layout4,from,to); 
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
	    	             TextView  fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
	    	             String fieldname = fishtextview.getText().toString();
	    	             String fieldname2 = fishtextview2.getText().toString();
	    	            if(!fieldname2.equals("Received") && !fieldname2.equals("Cancelled") && !fieldname2.equals("Not Received")) dialogDetails(fieldname);
	                 }
	                 });
	     	
	 		}
		 }

		 
		 public void receivedPys(String para)
		 {
			
			final Cursor c=DBHelper.getData(getApplicationContext(),Utils.py," where receiver_id='"+Utils.getDefaults("user_id", getApplicationContext())+"' and status!=3 "+para+" order by payment_id DESC");
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
                String nm="";
			    HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname","ID#"+c.getString(0)+" ( From: "+c.getString(11)+" )");
			    hm.put("payment",c.getString(6)+": Rs "+c.getString(5));
			    hm.put("remarks","Remarks: "+c.getString(7));
			    hm.put("dt",Utils.parseDateToddMMyyyy2(c.getString(8))+" "+Utils.ConvertTime(c.getString(9)));
			    if(c.getString(10).equals("1"))nm="Received"; 
			    else if(c.getString(10).equals("2"))nm="Not Received"; 
			    else if(c.getString(10).equals("3"))nm="Cancelled";
			    else nm="Pending";
			    hm.put("status",nm);
			    hm.put("content_id",c.getString(0));
	            users2.add(hm);
	             }
	     		
	     		 String[] from = { "uname","payment", "remarks","dt","content_id" ,"status"};
	             int[] to = {R.id.u_title,R.id.u_type,R.id.u_per,R.id.u_dt,R.id.content_id,R.id.u_status};
	             adapter2 = new SimpleAdapter(this, users2,R.layout.list_layout4 , from, to); 
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
	    	             TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
	    	             String fieldname = fishtextview.getText().toString();
	    	             String fieldname2 = fishtextview2.getText().toString();
	                	 if(fieldname2.equals("Pending"))del(fieldname);
	                  
	              
	                 }
                 });
     	
 		}
	 }
		 
		 public void dialogDetails(final String pid) {
		    	

		      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
		      			this);
		          builderSingle.setIcon(R.drawable.ic_launcher);
		          builderSingle.setTitle("Select User");
		          final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
		          		SendPayment.this,
		                  android.R.layout.select_dialog_singlechoice);

		          arrayAdapter.add("Edit Payment");
		          arrayAdapter.add("Cancel Payment");
		        
		          builderSingle.setNegativeButton("cancel",
		                  new DialogInterface.OnClickListener() {

		                      @Override
		                      public void onClick(DialogInterface dialog, int which) {
		                          dialog.dismiss();
		                      }
		                  });
		         
		          builderSingle.setAdapter(arrayAdapter,
		                  new DialogInterface.OnClickListener() {

		                      @Override
		                      public void onClick(DialogInterface dialog, int which) {
		                          final String strName = arrayAdapter.getItem(which);
		                          if(strName.equals("Edit Payment"))
		                          {
		                        	  Intent homeIntent = new Intent(SendPayment.this, NewPayment.class);
		             	             homeIntent.putExtra("id",pid);
		             	             homeIntent.putExtra("tp","2");
		             	             startActivity(homeIntent);  
		                          }
		                          else
		                          {
		                        	  cancell(pid); 
		                          }
		                      }
		          });

		     
		          builderSingle.show();
		      }
		 
		
		 
		 public void cancell(final String pid)
			{
				new AlertDialog.Builder(this)
				.setMessage("Do you really want to Cancel This Payment?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				    public void onClick(DialogInterface dialog, int whichButton) {
				    	 
				    	 
				 		try {
							data.put("sender", Utils.getDefaults("user_id", getApplicationContext()));
							data.put("subid", Utils.getDefaults("subid", getApplicationContext()));
							data.put("pid",pid);
							data.put("tp", "3");
							
							
						 httpGetAsyncTask = new HttpGetAsyncTask(SendPayment.this,3); 
				  	     httpGetAsyncTask.delegate = SendPayment.this;
				  	     httpGetAsyncTask.execute(Utils.confirmreceived+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
						}
						 catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }})
				 .setNegativeButton(android.R.string.no, null).show();
				
			}

		 public void del(final String pid)
			{

			 
			final  AlertDialog alert = new AlertDialog.Builder(this).create();
			 LayoutInflater inflater = getLayoutInflater();
			 //inflate view for alertdialog since we are using multiple views inside a viewgroup (root = Layout top-level) (linear, relative, framelayout etc..)
			 View view = inflater.inflate(R.layout.prompts5, (ViewGroup) findViewById(R.id.layout_root_new)); 

			 Button button1 = (Button) view.findViewById(R.id.btn1);
			 Button button2 = (Button) view.findViewById(R.id.btn2);
			 Button button3 = (Button) view.findViewById(R.id.btn3);
			
			 button1.setOnClickListener(new View.OnClickListener() {

				    public void onClick(View v) {
				    	try {
							data.put("sender", Utils.getDefaults("user_id", getApplicationContext()));
							data.put("subid", Utils.getDefaults("subid", getApplicationContext()));
							data.put("pid",pid);
							data.put("tp", "1");
							
							
						 httpGetAsyncTask = new HttpGetAsyncTask(SendPayment.this,3); 
				  	     httpGetAsyncTask.delegate = SendPayment.this;
				  	     httpGetAsyncTask.execute(Utils.confirmreceived+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
						}
						 catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	alert.dismiss();
				    }
				});
			 button2.setOnClickListener(new View.OnClickListener() {

				    public void onClick(View v) {
				    	try {
							data.put("sender", Utils.getDefaults("user_id", getApplicationContext()));
							data.put("subid", Utils.getDefaults("subid", getApplicationContext()));
							data.put("pid",pid);
							data.put("tp", "2");
							
							
						 httpGetAsyncTask = new HttpGetAsyncTask(SendPayment.this,3); 
				  	     httpGetAsyncTask.delegate = SendPayment.this;
				  	     httpGetAsyncTask.execute(Utils.confirmreceived+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
						}
						 catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	 alert.dismiss();
				    }
				});
			 
			 button3.setOnClickListener(new View.OnClickListener() {

				    public void onClick(View v) {
				    	alert.dismiss();
				    }
				});
			 alert.setView(view);
			 alert.show();
			}
         
		
		 
		@Override
		public void processFinish(String output) {
			// TODO Auto-generated method stub
			try {
				jsonObj = new JSONObject(output);
				JSONArray Data = jsonObj.getJSONArray("py");		
				DBHelper.storePy(getApplicationContext(),Data);
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
	             Intent homeIntent = new Intent(this, NewPayment.class);
	             homeIntent.putExtra("id","0");
	             homeIntent.putExtra("tp","1");
	             startActivity(homeIntent);
	        	 break;
	         case android.R.id.home:
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

}
