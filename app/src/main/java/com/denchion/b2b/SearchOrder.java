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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchOrder extends Activity implements AsyncResponse{
	
	 List<HashMap<String, String>> accepted  = new ArrayList<HashMap<String, String>>();
	 ListView sOrders;
	 EditText searchbox;
		AsyncResponse delegate = null;
		HttpGetAsyncTask httpGetAsyncTask;
		ProgressDialog dialog;
		JSONObject data = new JSONObject();
		JSONObject jsonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_search_order);
		Utils.setActionBar(this,"Search Orders");
	      DBHelper.loaddb(getApplicationContext());
		sOrders =(ListView) findViewById(R.id.sorders);
		 searchbox=  (EditText) findViewById(R.id.keywordbox2);
		 
		 searchbox.setOnKeyListener(new View.OnKeyListener() {
     	    public boolean onKey(View v, int keyCode, KeyEvent event) {
     	        // If the event is a key-down event on the "enter" button
     	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
     	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
     	        	loadOrders(" where cancel in (0,"+Utils.getDefaults("user_id", getApplicationContext())+")  and order_id like '%"+searchbox.getText().toString()+"%'");
     	          return true;
     	        }
     	        return false;
     	    }

			
     	});
	}
	
	public void loadOrders(String q)
    {
    	accepted.clear();
    	String tsp="1";
    	Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders,q);
    	while(d.moveToNext())
    	{
    		
                String nm="";
    			HashMap<String, String> hm = new HashMap<String, String>();
    			 hm.put("uname","ORDER ID: "+d.getString(0).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(4));
			    hm.put("uid",d.getString(1));
			    
			         if(d.getString(12).equals("0") && d.getString(37).equals("0"))
			        	 {
			        	 if(!d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))
			        	 nm="Pending";
			        	 else nm="Sent";
			        	 }
			    else if(d.getString(12).equals("0") && d.getString(37).equals("1"))nm="Approval Pending";
			    else if(d.getString(12).equals("0") && d.getString(37).equals("2"))nm="Saved as Draft";
	    		else if(d.getString(12).equals("1") && d.getString(15).equals("0") && d.getString(18).equals("0")) nm="Seller Accepted";
	    		else if(d.getString(12).equals("5") && d.getString(15).equals("0") && d.getString(18).equals("0")) nm="Buyer Accepted";
			    else if(d.getString(15).equals("1") && d.getString(18).equals("0")) nm="Dispatched";
			    else if(d.getString(15).equals("1") && d.getString(18).equals("1")) nm="Delivered";
			    else if(d.getString(12).equals("0") && d.getString(33).equals("1")) nm="Ignored";
			    else if(d.getString(12).equals("4")) nm="Cancelled";
			  
			    hm.put("status",nm);
			    hm.put("date",Utils.parseDateToddMMyyyy2(d.getString(21))+" "+Utils.ConvertTime(d.getString(22)));
			    hm.put("content_id",d.getString(0));
			    if(d.getString(18).equals("1"))tsp="2";
			    hm.put("tp",tsp);
			    accepted.add(hm);  
  
    	}
    	
    	
    	 String[] from = { "uname" , "location" , "date" , "content_id","status" ,"uid"};
         int[] to = {R.id.u_title,R.id.u_type,R.id.u_dt,R.id.content_id,R.id.u_status,R.id.uid};
         SimpleAdapter adapter = new SimpleAdapter(this, accepted,R.layout.list_layout3 , from, to); 
        
         
         sOrders.setAdapter(null);
         adapter.notifyDataSetChanged();
         sOrders.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
         sOrders.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               TextView uuid=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.uid);
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.type);
               TextView stat=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
               String fieldname = fishtextview.getText().toString();
               String uid = uuid.getText().toString();
               String status = stat.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(SearchOrder.this, OrderDetails.class);
               intent.putExtra("id", fieldname);
               if(uid.equals(Utils.getDefaults("user_id", getApplicationContext())))
               {
            	  if(status.equals("Delivered") || status.equals("Cancelled"))intent.putExtra("tp", "6"); 
             	  else if(status.equals("Seller Accepted") || status.equals("Partial Accept"))intent.putExtra("tp", "7");
             	  else if(status.equals("Buyer Accepted"))intent.putExtra("tp", "1");
             	  else if(status.equals("Dispatched"))intent.putExtra("tp", "5");
             	  else if(status.equals("Approval Pending"))PopOptions(fieldname,fieldname2,uid,"1");
             	  else if(status.equals("Saved as Draft"))PopOptions(fieldname,fieldname2,uid,"2"); 
             	 else if(status.equals("Sent"))intent.putExtra("tp", "1");
             	 else if(status.equals("Pending"))intent.putExtra("tp", "3");
             	 
               }
               else
               {
            	   if(status.equals("Delivered") || status.equals("Cancelled"))intent.putExtra("tp", "6"); 
             	  else if(status.equals("Seller Accepted") || status.equals("Partial Accept"))intent.putExtra("tp", "7");
             	  else if(status.equals("Buyer Accepted"))intent.putExtra("tp", "1");
             	  else if(status.equals("Dispatched"))intent.putExtra("tp", "5");
             	 else if(status.equals("Approval Pending"))PopOptions(fieldname,fieldname2,uid,"1");
            	  else if(status.equals("Saved as Draft"))PopOptions(fieldname,fieldname2,uid,"2"); 
            	 else if(status.equals("Sent"))intent.putExtra("tp", "1");
            	 else if(status.equals("Pending"))intent.putExtra("tp", "3");
               }
              
               startActivity(intent);  
             }
             });
    }
	
	
	private void PopOptions(final String oid,final String tp,final String rid,final String dftp) {
		
		
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				SearchOrder.this);
	    builderSingle.setIcon(R.drawable.ic_launcher);
	    builderSingle.setTitle("Options");
	    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	    		SearchOrder.this,
	            android.R.layout.select_dialog_singlechoice);
	  
	    
	   if(dftp.equals("1")) arrayAdapter.add("Approve Order");
	   else arrayAdapter.add("Send Order");
	    arrayAdapter.add("Edit Order");
	    arrayAdapter.add("View Details");
	    

	   
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
	                    
	                    if(strName.equals("Approve Order") || strName.equals("Send Order"))
	                    {
	                       httpGetAsyncTask = new HttpGetAsyncTask(SearchOrder.this,3);
	         		       httpGetAsyncTask.delegate=SearchOrder.this;
	         			   httpGetAsyncTask.execute(Utils.ApproveOrder+Utils.getDefaults("user_id", getApplicationContext())+"/"+Utils.getDefaults("subid", getApplicationContext())+"/"+rid+"/"+oid);	
	                    }
	                    else if(strName.equals("Edit Order"))
	                    {
	                        Intent intent = new Intent(SearchOrder.this, EditOrder.class);
	                        intent.putExtra("id", oid);
	                        intent.putExtra("tp", tp);
	                        startActivity(intent);  
	                    }
	                    else if(strName.equals("View Details"))
	                    {
	                    	Intent intent = new Intent(SearchOrder.this, OrderDetails.class);
	                        intent.putExtra("id", oid);
	                        intent.putExtra("tp", tp);
	                        startActivity(intent);  
	                    }
	                }
	            });
	    
	    builderSingle.show();
	}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			
			jsonObj = new JSONObject(output);	
			  JSONArray Data = jsonObj.getJSONArray("orders");
			  JSONArray Data2 = jsonObj.getJSONArray("oldprc");
				DBHelper.storeOrders(getApplicationContext(),Data);
				DBHelper.storeoldprc(getApplicationContext(),Data2);
				
				Intent intent = new Intent(SearchOrder.this, Orders.class);
                intent.putExtra("tpp", "");
                intent.putExtra("tb", "1");
                startActivity(intent);  
				
				 			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
