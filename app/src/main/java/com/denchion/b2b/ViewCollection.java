package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCollection extends Activity  implements AsyncResponse{
	
	 HttpGetAsyncTask httpGetAsyncTask;
	 JSONObject data = new JSONObject();
	 JSONObject jsonObj;
	 String url="";
	 Menu menu;
	 Spinner sp_1;
	    private int year;
	    private int month;
	    private int day;
	    String curTime;
	    static final int DATE_PICKER_ID = 1111;
	 RelativeLayout rl;

	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_view_collection);
		Utils.setActionBar(this,"Payment Collections");
		DBHelper.loaddb(getApplicationContext());
		sp_1=(Spinner)findViewById(R.id.sp_1);
		rl=(RelativeLayout)findViewById(R.id.drops);
		
		final Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
		
		if(!Utils.getDefaults("subid", getApplicationContext()).equals("0")){rl.setVisibility(View.GONE);}
		
		 httpGetAsyncTask = new HttpGetAsyncTask(ViewCollection.this,3); 
	     httpGetAsyncTask.delegate = ViewCollection.this;
	     if(!Utils.getDefaults("subid", getApplicationContext()).equals("0"))
	    	 {
	    	 url = Utils.gtcoll+"1/"+Utils.getDefaults("subid", getApplicationContext());
	    	 }
	     else {
	    	 url = Utils.gtcoll+"2/"+Utils.getDefaults("user_id", getApplicationContext());
	    	
	     }
	     if(Utils.isConnectingToInternet(getApplicationContext()))httpGetAsyncTask.execute(url);
         else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	    	
	    	
	    	ArrayAdapter<String> adpt1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
			adpt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			sp_1.setAdapter(adpt1);
			
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
	    	adpt1.add("Other");
	    	adpt1.setNotifyOnChange(true);
	    	
	    	
	    	sp_1.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    	String text = sp_1.getSelectedItem().toString();
			    	if(!text.equals("All"))
			    	{
			    		String rid=DBHelper.getIDByName(getApplicationContext(),text);
			    		getPys(" where partner in ("+rid+")");
			    		
			    	}
			    	else if(text.equals("Other"))getPys(" where partner in (0) ");
			    	else getPys("");
			    		
			    
			    }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});

			
	    	
	    	
	}
	

	 @SuppressLint("DefaultLocale") public void getPys(String srch)
	 {
		 users.clear();
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.bpcl+" "+srch," order by id DESC");

		double amt =0;
 			while(c.moveToNext())
     		{
 			String nm = "Other";
 			String sbnm="";
 				if(!c.getString(3).equals("0"))nm=DBHelper.getNameByID(getApplicationContext(),c.getString(3));
 				
		    HashMap<String, String> hm = new HashMap<String, String>();
		    amt += Double.parseDouble(c.getString(4));
		    hm.put("uname","Partner: "+nm+" ID#"+c.getString(0));
		    hm.put("payment","Mode: "+c.getString(8)+"\nAmount: "+c.getString(4));
		    hm.put("remarks","Remarks: "+c.getString(5));
		    hm.put("dt",Utils.parseDateToddMMyyyy2(c.getString(6))+" "+Utils.ConvertTime(c.getString(7)));
		    if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))sbnm=DBHelper.getNameBySubID(getApplicationContext(),c.getString(1));
		    hm.put("status",sbnm);
		    hm.put("content_id",c.getString(0));
            users.add(hm);
            
             }
 			
 		//	txtt.setText("Total: Rs."+amt);
 			String ff = String.format("%.0f", amt);
 			//getActionBar().setTitle("Total: Rs."+ff);
     		 String[] from = { "uname","payment", "remarks","dt","content_id" ,"status"};
             int[] to = {R.id.u_title,R.id.u_type,R.id.u_per,R.id.u_dt,R.id.content_id,R.id.u_status};
             SimpleAdapter adapter = new SimpleAdapter(this,users,R.layout.list_layout4,from,to); 
             ListView mylist = (ListView) findViewById(R.id.collections);
             mylist.setAdapter(null);
             adapter.notifyDataSetChanged();
             mylist.setAdapter(adapter);
             adapter.notifyDataSetChanged();
             
             mylist.setOnItemClickListener(new OnItemClickListener(){

                 @Override
                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                	 ListView lv = (ListView) arg0;
    	             TextView  fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
    	             String fieldname = fishtextview.getText().toString();
    	             if(!Utils.getDefaults("subid", getApplicationContext()).equals("0")){
    	             OptionsPop(fieldname);
    	             }
                 }
                 });
            
            
 		
	 }

	 
	 private void OptionsPop(final String oid) {
	    	
    	 
	    	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                ViewCollection.this);
	        builderSingle.setIcon(R.drawable.ic_launcher);
	        builderSingle.setTitle("Select Options:");
	        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	        		ViewCollection.this,
	                android.R.layout.select_dialog_singlechoice);

	        
	        	arrayAdapter.add("Edit Entry");
	        	arrayAdapter.add("Delete Entry");
	        
	        
	       
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
	                      
	                        if(strName.equals("Edit Entry"))
	                        {
	                        	  
	                       Intent homeIntent = new Intent(ViewCollection.this, CollectionAgent.class);
	           	           homeIntent.putExtra("id", oid);
	           	           homeIntent.putExtra("tp", "2");
	           	             startActivity(homeIntent);	
	                        }
	                        else if(strName.equals("Delete Entry"))
	                        {
	                        	del(oid);
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
			JSONArray Data = jsonObj.getJSONArray("collection");		
			DBHelper.storeCollection(getApplicationContext(),Data);
			getPys("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	
		
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_PICKER_ID:
             
            return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }
	
	@SuppressWarnings("deprecation")
	public void srch(View v)
	{
		
		 showDialog(DATE_PICKER_ID); 
	}
	
	
	 private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
	             
	            year  = selectedYear;
	            month = selectedMonth;
	            day   = selectedDay;
	 
	            String tm=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
	            getPys(" where date in ('"+tm+"')");
	         
	           }
	        };
	        
	
	 public void del(final String catID)
		{
			new AlertDialog.Builder(this)
			.setMessage("Do you really want to Delete This Entry?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 

	               	  httpGetAsyncTask = new HttpGetAsyncTask(ViewCollection.this,3);
	               	  httpGetAsyncTask.delegate=ViewCollection.this;
	               	 httpGetAsyncTask.execute(Utils.DelCollection+Utils.getDefaults("user_id",getApplicationContext())+"/"+Utils.getDefaults("subid",getApplicationContext())+"/"+catID);

			    	 }else 
			    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
		}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		getMenuInflater().inflate(R.menu.payment, menu);
		if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))
   	 {
		 if(menu != null){
		     MenuItem item_up = menu.findItem(R.id.npay);
		     item_up.setVisible(false);
		    }
   	 }
		return true;
	}

 @Override
 public boolean onOptionsItemSelected(MenuItem menuItem) {
     switch (menuItem.getItemId()) {
         case R.id.npay:
             Intent homeIntent = new Intent(this, CollectionAgent.class);
             homeIntent.putExtra("tp", "1");
             homeIntent.putExtra("id", "0");
             startActivity(homeIntent);
        	 break;
         case android.R.id.home:
                finish();
                break;
     }
     return (super.onOptionsItemSelected(menuItem));
 }

	 
}
