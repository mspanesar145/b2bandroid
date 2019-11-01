package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.Arrays;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SubUserDetails extends Activity implements AsyncResponse{

	String id;
	TableLayout t1;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_sub_user_details);
		Utils.setActionBar(this,"User Details");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");

		
		t1 = (TableLayout) findViewById(R.id.main_table_details);
		
		loadDetails();
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void loadDetails()
	{
		Cursor d=DBHelper.getData(getApplicationContext(),Utils.bsu," where user_id='"+id+"'");
    	if(d.moveToFirst())
    	{
    		String p1="";
    		String p2="";
    		String p3="";
    		String p4="";
    		
    		String []chk1=d.getString(7).split("[ ]");
			if(Arrays.asList(chk1).contains("1"))p1="Orders";
			if(Arrays.asList(chk1).contains("2"))p2="Quotation";
			if(Arrays.asList(chk1).contains("3"))p3="Messages";
			if(Arrays.asList(chk1).contains("4"))p4="Payment";
			if(Arrays.asList(chk1).contains("5"))p4="Access All Orders";
			if(Arrays.asList(chk1).contains("6"))p4="Collection Agent";
			
    		String[] heading = {
    				"Name","Username","password","mobile","designation","department","permission","date","time",
    			
    		};
    	
    		String[] values = {
    				d.getString(1),d.getString(2),d.getString(3),
    				d.getString(4),d.getString(5),d.getString(6),
    				p1+" "+p2+" "+p3+" "+p4,Utils.parseDateToddMMyyyy2(d.getString(8)),Utils.ConvertTime(d.getString(9))
    				};
    	
			 TableRow tr_data_hd1 = new TableRow(this);
    		 tr_data_hd1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
    	
    			
    			TextView data3 = new TextView(this);
    			data3.setId(201);
    			data3.setTypeface(null, Typeface.BOLD);
    			data3.setBackgroundResource(R.drawable.cell_shape_head);
    			data3.setText("SUB USER DETAILS");
    			data3.setTextColor(Color.BLACK);
    			data3.setPadding(15,15,15,15);
    		    tr_data_hd1.addView(data3);// add the column to the table row here
    	         
    		 TableRow.LayoutParams params = (TableRow.LayoutParams)data3.getLayoutParams();
    		 params.span = 2; 
    		 data3.setLayoutParams(params);
    	         
    	     t1.addView(tr_data_hd1, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	     
    			for(int j=0;j<heading.length;j++)
     			{
     			 TableRow tr_data = new TableRow(this);
     			tr_data.setId(Integer.parseInt(d.getString(0)));
     		  
     			tr_data.setLayoutParams(new LayoutParams(
     		     LayoutParams.FILL_PARENT,
     		     LayoutParams.WRAP_CONTENT));	
     			
     			
     			TextView data0 = new TextView(this);
     			data0.setId(201);
     			data0.setTypeface(null, Typeface.BOLD);
     			data0.setBackgroundResource(R.drawable.cell_shape);
     			data0.setText(heading[j]);
     			data0.setTextColor(Color.BLACK);
     			data0.setPadding(7, 7, 7, 7);
     		    tr_data.addView(data0);// add the column to the table row here
     	         
     	        TextView data1 = new TextView(this);
     	        data1.setId(211+j);
     	        data1.setBackgroundResource(R.drawable.cell_shape);
     	        data1.setText(values[j]);
     	        data1.setTextColor(Color.BLACK);
     	        data1.setPadding(7, 7, 7, 7);
     	        tr_data.addView(data1);// add the column to the table row here
     	        t1.addView(tr_data, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
     	         
     			}
    			
    			
    	        
    	}
    	
    	
	}

  public void action(View v){
	  
	  Intent loginIntent =null;
	  
	  switch(v.getId())
	  {
	  case R.id.btnedt:
	    loginIntent = new Intent(this, EditSub.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("id",id);
	        this.startActivity(loginIntent);
      break;
      
	  case R.id.btndel:
		  del();
		  break;
	  
	  case R.id.btnAct:
	   loginIntent = new Intent(this, SubUserActivity.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("id",id);
	        this.startActivity(loginIntent);
	        
	  break;
	        
	  }
}

  
  public void del()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to Delete this User?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
		    		  httpGetAsyncTask = new HttpGetAsyncTask(SubUserDetails.this,3);
				       httpGetAsyncTask.delegate=SubUserDetails.this;
		    		 httpGetAsyncTask.execute(Utils.DelSubUser+id); 
		    		 Toast.makeText(getApplicationContext(), "Deleting User!", Toast.LENGTH_SHORT).show();
		    	 }else 
		    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
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



	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		 Intent loginIntent = new Intent(this, AddSubUser.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(loginIntent);
	        finish();
	}

	
}
