package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AddUserToGroup extends Activity implements AsyncResponse{

	String gid;
	ListView p_sent;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	    private int year;
	    private int month;
	    private int day;
	    static final int DATE_PICKER_ID = 1111;
	    DatePickerDialog.OnDateSetListener pickerListener ;
	ArrayList<String> selectedStrings = new ArrayList<String>();
	List<HashMap<String, String>> ulist = new ArrayList<HashMap<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_add_user_to_group);
		Utils.setActionBar(this,"Group Users");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		gid = bundle.getString("id");
		p_sent = (ListView) findViewById(R.id.p_sent);
	    Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
		loadData();
	}
	
	public void loadData()
    {
		ulist.clear();
		
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.grprltbl," where group_id='"+gid+"'");
    	while(c.moveToNext())
    	{
    		
    		
    			Cursor d=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id="+c.getString(3));
    			if(d.moveToFirst())
    			{
    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",d.getString(1).toUpperCase(Locale.getDefault()));
			    hm.put("location",d.getString(8)+", "+d.getString(9));
			    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+d.getString(0)+".jpg");                
			    hm.put("img",imgFile.toString());
			    hm.put("content_id",d.getString(0));
			    ulist.add(hm);  
    		
    			}
    	}
    	
    	 String[] from = { "uname" , "location" , "img" , "content_id" };
         int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
         SimpleAdapter adapter = new SimpleAdapter(this, ulist,R.layout.list_layout, from, to); 
        
         
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
                    String gid = fishtextview.getText().toString();
                    PopOptions(gid);
             }
             }); 
         
    
    }
	
	ArrayList<String> getSelectedString(){
		  return selectedStrings;
		}
	
	
	 private void PopOptions(final String nm) {
			
			
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					this);
		    builderSingle.setIcon(R.drawable.ic_launcher);
		    builderSingle.setTitle("Options");
		    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
		    		this,
		            android.R.layout.select_dialog_singlechoice);
		  
		    arrayAdapter.add("Delete User");
		    arrayAdapter.add("View Profile");

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
		                    if(strName.equals("Delete User"))
		                    {
		                    	  del(nm); 	 	
		                    }
		                    else if(strName.equals("View Profile"))
		                    {
		                    	 Intent intent = new Intent(AddUserToGroup.this, ProfileDetails.class);
		                         intent.putExtra("id", nm);
		                         intent.putExtra("uname","");
		                         intent.putExtra("tp", "1");
		                         startActivity(intent);  
		                         finish();
		                    }
		           		  dialog.dismiss();
		                   
		                }
		            });
		    
		    builderSingle.show();
		}
	 
	 public void del(final String uuid)
		{
			new AlertDialog.Builder(this)
			.setMessage("Do you really want to remove this user ?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	
			    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
			      		  httpGetAsyncTask = new HttpGetAsyncTask(AddUserToGroup.this,1);
			   		       httpGetAsyncTask.delegate=AddUserToGroup.this;
			      		 httpGetAsyncTask.execute(Utils.delUserGroup+gid+"/"+uuid+"/"+Utils.getDefaults("user_id", getApplicationContext())); 
			      		
			      	
			      	 }else 
			      	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			    	
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
		}
	
private void initiatePopupWindow() {
    	
    	
    	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                AddUserToGroup.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select Partner");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        		AddUserToGroup.this,
                android.R.layout.select_dialog_singlechoice);
        
        Cursor m=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(m.moveToFirst())
    	{
    	Cursor n=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+m.getString(16)+")");
    			while(n.moveToNext())
    			{
    				 arrayAdapter.add(n.getString(1));
    		
    	        }
    	}
     
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
                       String idss= DBHelper.getIDByName(getApplicationContext(),strName);
                     //  Toast.makeText(getApplicationContext(), ids, Toast.LENGTH_SHORT).show();
                       httpGetAsyncTask = new HttpGetAsyncTask(AddUserToGroup.this,3);	
         	 		  httpGetAsyncTask.delegate=AddUserToGroup.this;
         	 		 if(Utils.isConnectingToInternet(getApplicationContext())){
         	 			Cursor c=DBHelper.getData(getApplicationContext(),Utils.grprltbl," where group_id='"+gid+"' and user='"+idss+"'");			 
         	 			 if(c.getCount()==0){
         	          httpGetAsyncTask.execute(Utils.StoreUsertogroup+idss+"/"+gid+"/"+Utils.getDefaults("user_id",getApplicationContext()));
         	 			 } else{
         	 				Toast.makeText(getApplicationContext(), "This partner is already added in this group", Toast.LENGTH_SHORT).show();
                    }
         	 		 }  else
         		        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                   }
                });
        builderSingle.show();
    }

	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.addu,menu);
			
			return true;
		}

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.norder:
	        	 initiatePopupWindow();
	        	 break;
	         case android.R.id.home:
	                finish();
	                break;
	    
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }
	 
	 
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_PICKER_ID:
	             
	            // open datepicker dialog. 
	            // set date picker for current date 
	            // add pickerListener listner to date picker
	            return new DatePickerDialog(this, pickerListener, year, month,day);
	        }
	        return null;
	    }
	 

	

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
		if(!output.equals("1")){
	try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("grouprel");
			DBHelper.storeGroupsRelations(getApplicationContext(),Data);
			loadData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		}else{ Toast.makeText(getApplicationContext(), "Notifications successfully sent to the selected users", Toast.LENGTH_SHORT).show(); }
	}
	
	
	 
}
