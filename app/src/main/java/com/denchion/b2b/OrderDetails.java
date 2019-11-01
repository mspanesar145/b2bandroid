package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams") public class OrderDetails extends Activity implements AsyncResponse {
	TableLayout t1;
	String id;
	String tp;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	LinearLayout l1;
	LinearLayout l2;
	LinearLayout l3;
	LinearLayout l4;
	LinearLayout l5;
	LinearLayout l6;
	LinearLayout l7;
	ImageView imgs;
	ImageView fwd1;
	ImageView fwd2;
	String qref="";
	String da="";
	String rid;
	LinearLayout footerlayout;
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 11121; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_order_details);
		Utils.setActionBar(this,"Order Details");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		tp = bundle.getString("tp");
		
		t1 = (TableLayout) findViewById(R.id.main_table_details);
		
		footerlayout = (LinearLayout) findViewById(R.id.footerlayout);
		
		l1 = (LinearLayout) findViewById(R.id.icolay1);
		l2 = (LinearLayout) findViewById(R.id.icolay2);
		l3 = (LinearLayout) findViewById(R.id.icolay3);
		l4 = (LinearLayout) findViewById(R.id.icolay4);
		l5 = (LinearLayout) findViewById(R.id.icolay5);
		l6 = (LinearLayout) findViewById(R.id.icolay6);
		l7 = (LinearLayout) findViewById(R.id.icolay7);
		
		fwd1 = (ImageView) findViewById(R.id.ico4);
		fwd2 = (ImageView) findViewById(R.id.ico4a);
		
		if(!Utils.getDefaults("subid", getApplicationContext()).equals("0"))
		{
			fwd1.setVisibility(View.GONE);
			fwd2.setVisibility(View.GONE);
		}
		
		imgs = (ImageView) findViewById(R.id.imgPreview2);
		if(!tp.equals(null)){
		switch(Integer.parseInt(tp))
		{
		case 2:
			l1.setVisibility(View.GONE);
			l3.setVisibility(View.VISIBLE);
			break;
		case 3:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.VISIBLE);
			break;
			
		case 4:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.VISIBLE);
			l5.setVisibility(View.GONE);
			l6.setVisibility(View.GONE);
			break;
			
		case 5:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.GONE);
			l5.setVisibility(View.VISIBLE);
			break;
			
		case 6:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.GONE);
			l5.setVisibility(View.GONE);
			l7.setVisibility(View.GONE);
			l6.setVisibility(View.VISIBLE);
			break;
			
		case 7:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.GONE);
			l5.setVisibility(View.GONE);
			l6.setVisibility(View.GONE);
			l7.setVisibility(View.VISIBLE);
			break;
			
		case 8:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.GONE);
			l5.setVisibility(View.GONE);
			l7.setVisibility(View.GONE);
			l6.setVisibility(View.VISIBLE);
			break;
			
		case 9:
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l4.setVisibility(View.GONE);
			l5.setVisibility(View.GONE);
			l7.setVisibility(View.GONE);
			l6.setVisibility(View.GONE);
			break;
			
			}
		}
		
		loadDetails();
	}
	
	@SuppressWarnings("deprecation")
	public void loadDetails()
	{
		float mrpttl=0;
		float pricettl=0;
		Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders," where order_id='"+id+"'");
    	if(d.moveToFirst())
    	{
    		rid=d.getString(3);
    		String[] heading = {
    				"Order ID","Company","buyer","Delivery Type","Schedule Date","Transport Type","Buyer Remarks","Seller Remarks","Order Status",
    				"Order Dispached","Dispatched Date","Dispatched Time","Attachment","Confirm Received","Confirm Date","Confirm Time","Order Placed Date",
    				"Order Placed Time","Courier","Tracking no.","Remarks","Track Date","Track Time","Rating","Remarks",
    				"Quotation Reference"
    		};
    		String rfnm="";
    		if(d.getString(34).equals("0"))rfnm="No Quote Reference";
    		else rfnm=DBHelper.getQuoteNameByID(getApplicationContext(),d.getString(34))+" (Click to view)";
    		String op4="",op5="",nm="";
    		//if(d.getString(9).equals(""))op="pending";else op=d.getString(9);
    		if(d.getString(15).equals("0"))op4="pending";else op4="Dispatched";
    		if(d.getString(18).equals("0"))op5="pending";else op5="Delivered";
    		    if(d.getString(12).equals("0")) nm="Pending";
	    		else if(d.getString(12).equals("1") && d.getString(15).equals("0") && d.getString(18).equals("0")) nm="Seller Accepted";
	    		else if(d.getString(12).equals("5") && d.getString(15).equals("0") && d.getString(18).equals("0"))
	    			{
	    			if(d.getString(39).equals("1"))nm="Direct Order";
	    			else nm="Buyer Accepted";
	    			}
			    else if(d.getString(15).equals("1") && d.getString(18).equals("0")) nm="Dispatched";
			    else if(d.getString(15).equals("1") && d.getString(18).equals("1")) nm="Delivered";
			    else if(d.getString(12).equals("0") && d.getString(33).equals("1")) nm="Ignored";
			    else if(d.getString(12).equals("4")) nm="Cancelled";
    		
    		    imgs.setVisibility(View.VISIBLE);
			File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/feed_"+d.getString(32)+".jpg");
	  	    Picasso.with(getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgs);
	  	  if(!imgFile.exists())imgs.setVisibility(View.GONE);
    	
    		
    		String bnm="";
    		String bid="";
    		
    		if(!Utils.getDefaults("subid", getApplicationContext()).equals("0"))
    		{
    			if(d.getString(35).equals("3") ||  d.getString(35).equals("13") || d.getString(35).equals("23") || d.getString(35).equals("123"))	
    			{
    				footerlayout.setVisibility(View.GONE);	
    			}
    			
    			if(d.getString(35).equals("1") ||  d.getString(35).equals("12") || d.getString(35).equals("13") || d.getString(35).equals("123"))bnm="N/A";
    			else 
    				{
    				if(!Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))bid=d.getString(1);
    	    		else bid=d.getString(3);
    				bnm=DBHelper.getNameByID(getApplicationContext(),bid);
    				}
    		}
    		else
    		{
    			if(!Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))bid=d.getString(1);
	    		else bid=d.getString(3);
    			bnm=d.getString(4);
    		}
    		
    		String datm ="Pending";
    		if(!d.getString(41).equals("null")){datm = "Click to Download";da = d.getString(41);}
    		
    		
    		
    		String[] values = {
    				d.getString(0),DBHelper.getCompanyByID(getApplicationContext(),bid),bnm,d.getString(6),Utils.parseDateToddMMyyyy(d.getString(38)),d.getString(7),d.getString(10),d.getString(11),
    				nm,op4,Utils.parseDateToddMMyyyy(d.getString(16)),d.getString(17),datm,op5,Utils.parseDateToddMMyyyy(d.getString(19)),d.getString(20)
    				,Utils.parseDateToddMMyyyy2(d.getString(21)),d.getString(22),
    				d.getString(24).replace("null", "Pending"),d.getString(25).replace("null", "Pending"),d.getString(23).replace("null", "Pending"),Utils.parseDateToddMMyyyy2(d.getString(26)),d.getString(27),
    				d.getString(28).replace("null", "Pending"),d.getString(29).replace("null", "Pending"),rfnm
    		};
    		qref=d.getString(34);
    	
			 TableRow tr_data_hd1 = new TableRow(this);
    		 tr_data_hd1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
    	
    			
    			TextView data3 = new TextView(this);
    			data3.setId(201);
    			data3.setTypeface(null, Typeface.BOLD);
    			data3.setBackgroundResource(R.drawable.cell_shape_head);
    			data3.setText("ORDER DETAILS");
    			data3.setTextColor(Color.BLACK);
    			data3.setPadding(15,15,15,15);
    		    tr_data_hd1.addView(data3);// add the column to the table row here
    	         
    		 TableRow.LayoutParams params = (TableRow.LayoutParams)data3.getLayoutParams();
    		 params.span = 4; 
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
     	        data1.setId(j);
     	        data1.setOnClickListener(clickdata0);
     	        data1.setBackgroundResource(R.drawable.cell_shape);
     	        data1.setText(values[j]);
     	        data1.setTextColor(Color.BLACK);
     	        data1.setPadding(7, 7, 7, 7);
     	        tr_data.addView(data1);// add the column to the table row here
     	       TableRow.LayoutParams pd1 = (TableRow.LayoutParams)data1.getLayoutParams();
     	      pd1.span = 4; 
      		data1.setLayoutParams(pd1);
     	        t1.addView(tr_data, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
     	         
     			}
    			 TableRow tr_data_hd2 = new TableRow(this);
        		 tr_data_hd2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
        	
        			
        			TextView data32 = new TextView(this);
        			data32.setId(201);
        			data32.setTypeface(null, Typeface.BOLD);
        			data32.setBackgroundResource(R.drawable.cell_shape_head);
        			data32.setText("PRODUCT DETAILS");
        			data32.setTextColor(Color.BLACK);
        			data32.setPadding(15,15,15,15);
        		    tr_data_hd2.addView(data32);// add the column to the table row here
        	         
        		 TableRow.LayoutParams params2 = (TableRow.LayoutParams)data32.getLayoutParams();
        		 params2.span = 5; 
        		 data32.setLayoutParams(params2);
        	     t1.addView(tr_data_hd2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        	     
        	     
        	     
        	     TableRow tr_data2b = new TableRow(this);
	      			tr_data2b.setId(Integer.parseInt(d.getString(0)));
	      		  
	      			tr_data2b.setLayoutParams(new LayoutParams(
	      		     LayoutParams.FILL_PARENT,
	      		     LayoutParams.WRAP_CONTENT));	
	      			
	      			
	      			TextView data01a = new TextView(this);
	      			data01a.setId(401);
	      			data01a.setBackgroundResource(R.drawable.cell_shape);
	      			data01a.setText("Product");
	      			data01a.setTypeface(null, Typeface.BOLD);
	      			data01a.setTextColor(Color.BLACK);
	      			data01a.setPadding(7, 7, 7, 7);
	      		    tr_data2b.addView(data01a);// add the column to the table row here
	      		    
	      	        TextView data11a = new TextView(this);
	      	        data11a.setId(411);
	      	        data11a.setBackgroundResource(R.drawable.cell_shape);
	      	        data11a.setText("Price");
	      	        data11a.setTypeface(null, Typeface.BOLD);
	      	        data11a.setTextColor(Color.BLACK);
	      	        data11a.setPadding(7, 7, 7, 7);
	      	        tr_data2b.addView(data11a);// add the column to the table row here
	      	  
	      	        
	      	       TextView data11b = new TextView(this);
	      	        data11b.setId(441);
	      	        data11b.setBackgroundResource(R.drawable.cell_shape);
	      	        data11b.setText("Mrp.");
	      	        data11b.setTypeface(null, Typeface.BOLD);
	      	        data11b.setTextColor(Color.BLACK);
	      	        data11b.setPadding(7, 7, 7, 7);
	      	        tr_data2b.addView(data11b);// add the column to the table row here
	      	        
	      	      TextView data11c = new TextView(this);
	      	        data11c.setId(431);
	      	        data11c.setBackgroundResource(R.drawable.cell_shape);
	      	        data11c.setText("qty");
	      	        data11c.setTypeface(null, Typeface.BOLD);
	      	        data11c.setTextColor(Color.BLACK);
	      	        data11c.setPadding(7, 7, 7, 7);
	      	      tr_data2b.addView(data11c);// add the column to the table row here

	      	        t1.addView(tr_data2b, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	      	        
	      	        
    			 JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(d.getString(8));
					JSONArray Data= jsonObject.getJSONArray("products");
 

					 for(int j=0;j<Data.length();j++)
			    		{
						 JSONObject jsonObj  = Data.getJSONObject(j);
						 
						 String odprc=DBHelper.getOldPriceByName(getApplicationContext(),jsonObj.getString("product"),d.getString(1),d.getString(3),d.getString(0));	
			      		
			      			if(mrpttl==0)
			      			mrpttl=Float.parseFloat(jsonObj.getString("mrp"))*Integer.parseInt(jsonObj.getString("qty"));
			      			else
			      			mrpttl=(Float.parseFloat(jsonObj.getString("mrp"))*Integer.parseInt(jsonObj.getString("qty")))+mrpttl;
			      			
			      			if(pricettl==0)
			      				pricettl=(Float.parseFloat(jsonObj.getString("price"))*Integer.parseInt(jsonObj.getString("qty")));
				      			else
				      			pricettl=(Float.parseFloat(jsonObj.getString("price"))*Integer.parseInt(jsonObj.getString("qty")))+pricettl;
			      			
			      			String qttyyy="N/A";
			      			
			      			if(!Utils.getDefaults("subid", getApplicationContext()).equals("0"))
			    			{
			    			if( d.getString(35).equals("2") || d.getString(35).equals("12") ||  d.getString(35).equals("23") || d.getString(35).equals("123"))qttyyy="N/A";
			    			else qttyyy=jsonObj.getString("qty");
			 
			    			}
			    		else
			    		{
			    			qttyyy=jsonObj.getString("qty");
			    		}
			      			
			      			
			      			 TableRow tr_data2a = new TableRow(this);
				      			tr_data2a.setId(Integer.parseInt(d.getString(0)));
				      		  
				      			tr_data2a.setLayoutParams(new LayoutParams(
				      		     LayoutParams.FILL_PARENT,
				      		     LayoutParams.WRAP_CONTENT));
				      			
				      			
			      			
			      			TextView data0a = new TextView(this);
			      			data0a.setId(201);
			      			data0a.setBackgroundResource(R.drawable.cell_shape);
			      			data0a.setText(jsonObj.getString("product"));
			      			data0a.setTextColor(Color.BLACK);
			      			data0a.setPadding(7, 7, 7, 7);
			      		    tr_data2a.addView(data0a);// add the column to the table row here
			      		    
			      		    
			      	         
			      	        TextView data1a = new TextView(this);
			      	        data1a.setId(211+j);
			      	        data1a.setBackgroundResource(R.drawable.cell_shape);
			      	        data1a.setText(jsonObj.getString("price"));
			      	        data1a.setTextColor(Color.BLACK);
			      	        data1a.setPadding(7, 7, 7, 7);
			      	        tr_data2a.addView(data1a);// add the column to the table row here
			      	 
			      	        
			      	      TextView data3a = new TextView(this);
			      	        data3a.setId(211+j);
			      	        data3a.setBackgroundResource(R.drawable.cell_shape);
			      	        data3a.setText(jsonObj.getString("mrp"));
			      	        data3a.setTextColor(Color.BLACK);
			      	        data3a.setPadding(7, 7, 7, 7);
			      	        tr_data2a.addView(data3a);// add the column to the table row here
			      	        
			      	      TextView data4a = new TextView(this);
			      	        data4a.setId(211+j);
			      	        data4a.setBackgroundResource(R.drawable.cell_shape);
			      	        data4a.setText(qttyyy);
			      	        data4a.setTextColor(Color.BLACK);
			      	        data4a.setPadding(7, 7, 7, 7);
			      	        
			      	      tr_data2a.addView(data4a);// add the column to the table row here
			      	  
			      	        
			      	        
			      	      t1.addView(tr_data2a, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			      	      
			      	    TableRow tr_data22a = new TableRow(this);
		      			tr_data22a.setId(Integer.parseInt(d.getString(0)));
		      		  
		      			tr_data22a.setLayoutParams(new LayoutParams(
		      		     LayoutParams.FILL_PARENT,
		      		     LayoutParams.WRAP_CONTENT));
			      	      
			      	      TextView data2aa = new TextView(this);
			      	        data2aa.setId(221+j);
			      	        data2aa.setBackgroundResource(R.drawable.cell_shape);
			      	        data2aa.setText("Old Price: "+odprc);
			      	        data2aa.setTextColor(Color.BLACK);
			      	        data2aa.setPadding(7, 7, 7, 7);
			      	        tr_data22a.addView(data2aa);// add the column to the table row here
			      	        
			      	      TableRow.LayoutParams params222a = (TableRow.LayoutParams)data2aa.getLayoutParams();
			        		 params222a.span = 4; 
			        		 data2aa.setLayoutParams(params222a);
			      	        
			      	      t1.addView(tr_data22a, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			      	      
			      	    TableRow tr_data22 = new TableRow(this);
		      			tr_data22.setId(Integer.parseInt(d.getString(0)));
		      		  
		      			tr_data22.setLayoutParams(new LayoutParams(
		      		     LayoutParams.FILL_PARENT,
		      		     LayoutParams.WRAP_CONTENT));
			      	      
			      	      TextView data2a = new TextView(this);
			      	        data2a.setId(221+j);
			      	        data2a.setBackgroundResource(R.drawable.cell_shape);
			      	        data2a.setText(jsonObj.getString("remarks"));
			      	        data2a.setTextColor(Color.BLACK);
			      	        data2a.setPadding(7, 7, 7, 7);
			      	        tr_data22.addView(data2a);// add the column to the table row here
			      	        
			      	      TableRow.LayoutParams params222 = (TableRow.LayoutParams)data2a.getLayoutParams();
			        		 params222.span = 4; 
			        		 data2a.setLayoutParams(params222);
			      	        
			      	      t1.addView(tr_data22, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			      	      
			      	        
					   
			    		}
					 
					 TableRow tr_data_bottom2 = new TableRow(this);
					 tr_data_bottom2.setId(Integer.parseInt(d.getString(0)));
		     		  
					 tr_data_bottom2.setLayoutParams(new LayoutParams(
		     		     LayoutParams.FILL_PARENT,
		     		     LayoutParams.WRAP_CONTENT));	
		     			
		     			
		     			TextView htdata0 = new TextView(this);
		     			htdata0.setId(213213);
		     			htdata0.setTypeface(null, Typeface.BOLD);
		     			htdata0.setBackgroundResource(R.drawable.cell_shape_head);
		     			htdata0.setText("");
		     			htdata0.setTextColor(Color.BLACK);
		     			htdata0.setPadding(7, 7, 7, 7);
		     			tr_data_bottom2.addView(htdata0);// add the column to the table row here
		     	         
		     	        TextView htdata1 = new TextView(this);
		     	        htdata1.setId(232131);
		     	        htdata1.setBackgroundResource(R.drawable.cell_shape_head);
		     	        htdata1.setText("Total Price");
		     	        htdata1.setTextColor(Color.BLACK);
		     	        htdata1.setPadding(7, 7, 7, 7);
		     	       tr_data_bottom2.addView(htdata1);// add the column to the table row here

		      		     TextView htdata2 = new TextView(this);
		     	         htdata2.setId(232131);
		     	         htdata2.setBackgroundResource(R.drawable.cell_shape_head);
		     	         htdata2.setText("Total Mrp");
		     	         htdata2.setTextColor(Color.BLACK);
		     	         htdata2.setPadding(7, 7, 7, 7);
		     	         tr_data_bottom2.addView(htdata2);// add the column to the table row here
		     	         TableRow.LayoutParams params222c = (TableRow.LayoutParams)htdata2.getLayoutParams();
		        		 params222c.span = 3; 
		        		 htdata2.setLayoutParams(params222c);
		     	         t1.addView(tr_data_bottom2, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
					
					
					 TableRow tr_data_bottom = new TableRow(this);
					 tr_data_bottom.setId(Integer.parseInt(d.getString(0)));
		     		  
					 tr_data_bottom.setLayoutParams(new LayoutParams(
		     		     LayoutParams.FILL_PARENT,
		     		     LayoutParams.WRAP_CONTENT));	
		     			
		     			
		     			TextView tdata0 = new TextView(this);
		     			tdata0.setId(213213);
		     			tdata0.setTypeface(null, Typeface.BOLD);
		     			tdata0.setBackgroundResource(R.drawable.cell_shape);
		     			tdata0.setText("Total");
		     			tdata0.setTextColor(Color.BLACK);
		     			tdata0.setPadding(7, 7, 7, 7);
		     			tr_data_bottom.addView(tdata0);// add the column to the table row here
		     	         
		     	        TextView tdata1 = new TextView(this);
		     	        tdata1.setId(232131);
		     	        tdata1.setBackgroundResource(R.drawable.cell_shape);
		     	        tdata1.setText("Rs."+pricettl+"");
		     	        tdata1.setTextColor(Color.BLACK);
		     	        tdata1.setPadding(7, 7, 7, 7);
		     	       tr_data_bottom.addView(tdata1);// add the column to the table row here
		     	     
		      		
		      		 TextView tdata2 = new TextView(this);
		     	        tdata2.setId(232131);
		     	        tdata2.setBackgroundResource(R.drawable.cell_shape);
		     	        tdata2.setText("Rs."+mrpttl+"");
		     	        tdata2.setTextColor(Color.BLACK);
		     	        tdata2.setPadding(7, 7, 7, 7);
		     	       tr_data_bottom.addView(tdata2);// add the column to the table row here
		     	      TableRow.LayoutParams params222c2 = (TableRow.LayoutParams)tdata2.getLayoutParams();
		        		 params222c2.span = 3; 
		        		 tdata2.setLayoutParams(params222c2);
		     	        t1.addView(tr_data_bottom, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    
    	}
    	
    	
	}
	
	private OnClickListener clickdata0 = new OnClickListener() {
        public void onClick(View v) {

      if(v.getId()==25 && !qref.equals("0"))
      {
    	  Intent loginIntent = new Intent(OrderDetails.this,  QuoteDetails.class);
 		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 		loginIntent.putExtra("id", qref);
         OrderDetails.this.startActivity(loginIntent);
        
      }
      else if(v.getId()==12 && !da.equals("Pending"))
      {
    	  new DownloadFileFromURL().execute(Utils.base+"images/attachments/"+da);  
      }
        }
    };
	
	
	public void action(View v)
	{
		switch(v.getId())
		{
		case R.id.ico1:jump("0");break;
		case R.id.ico3:del();break;
		case R.id.ico4: forward();break;
		case R.id.ico1a:jump("0"); break;
		case R.id.ico2a: ignore();break;
		case R.id.ico3a:accept();break;
		case R.id.ico4a: forward();break;
		case R.id.ico1b:jump("0"); break;
		case R.id.ico2b: break;
		case R.id.ico1c:jump("0"); break;
		case R.id.ico2c: dispatch();break;
		case R.id.ico1d: jump("0");break;
		case R.id.ico2d: feedback();break;
		case R.id.ico1e: jump("0");break;
		case R.id.ico1f: jump("0");break;
		case R.id.ico2f: del();break;
		case R.id.ico3f: BuyerAccept();break;

		}
	}
	
	
	
	public void del()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to Cancel This Order?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
		    		  httpGetAsyncTask = new HttpGetAsyncTask(OrderDetails.this,2);
				      httpGetAsyncTask.delegate=OrderDetails.this;
		    		  httpGetAsyncTask.execute(Utils.DNAZ62SHEiOsXNpM+Utils.getDefaults("user_id", getApplicationContext())+"/"+id+"/1/"+Utils.getDefaults("subid", getApplicationContext())); 
		    		 Toast.makeText(getApplicationContext(), "Cancelling!", Toast.LENGTH_SHORT).show();
		    	
		    	 }else 
		    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	 @SuppressLint("InflateParams") public void dialogDetails() {
	    	

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			this);
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompts_order, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);
	          builderSingle.setTitle("Update Now:-");
	          
	   
	        	  builderSingle.setView(promptsView);
	              
	        
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	               
	                    	  try {
	                    	  		data.put("uid", Utils.getDefaults("user_id", getApplicationContext()));
	                    			data.put("des", userInput.getText().toString());
	                    			data.put("oid",id);
	                    			data.put("rid",rid);
	                    			data.put("subid",Utils.getDefaults("subid", getApplicationContext()));
	                    			 httpGetAsyncTask = new HttpGetAsyncTask(OrderDetails.this,3);
	          				       httpGetAsyncTask.delegate=OrderDetails.this;
	                    			if(Utils.isConnectingToInternet(getApplicationContext()))	{	
	                    	        httpGetAsyncTask.execute(Utils.buyerAccept+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			
	                    	  }
	                    			else
	                    	        Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                         
	                    	  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
	        					// TODO Auto-generated catch block
	        					e.printStackTrace();
	        				}
	                      
	                    	 dialog.dismiss();
	                    	  } 
	                      
	                  });

	     
	          builderSingle.show();
	      }
	 
	
	
	public void BuyerAccept()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to Accept This Order?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	dialogDetails();
		
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	public void ignore()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to ignore?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
		    		  httpGetAsyncTask = new HttpGetAsyncTask(OrderDetails.this,2);
				       httpGetAsyncTask.delegate=OrderDetails.this;
		    	 httpGetAsyncTask.execute(Utils.DNAZ62SHEiOsXNpM+Utils.getDefaults("user_id", getApplicationContext())+"/"+id+"/2/"+Utils.getDefaults("subid", getApplicationContext()));
		    	 Toast.makeText(getApplicationContext(), "Processing Request!", Toast.LENGTH_SHORT).show();
		    	
		    	 }else 
			    Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	public void accept()
	{

		 Intent loginIntent = new Intent(this, AcceptOrder.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 loginIntent.putExtra("oid",id);
        this.startActivity(loginIntent);
        finish();
	}
	
	public void confirm()
	{

		 Intent loginIntent = new Intent(this, Feedback.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 loginIntent.putExtra("oid",id);
         this.startActivity(loginIntent);
         finish();
	}
	
	public void dispatch()
	{

		 Intent loginIntent = new Intent(this, DispatchDetails.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 loginIntent.putExtra("oid",id);
         this.startActivity(loginIntent);
         finish();
	}
	
	public void feedback()
	{

		 Intent loginIntent = new Intent(this, Feedback.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 loginIntent.putExtra("oid",id);
         this.startActivity(loginIntent);
         finish();
	}
	
	public void edit()
	{

		 Intent loginIntent = new Intent(this, EditOrder.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 loginIntent.putExtra("oid",id);
        this.startActivity(loginIntent);
        finish();
	}
	
	public void forward()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to Foward?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	fwdOrder();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	public void fwdOrder() {
		

      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
      			this);
          builderSingle.setIcon(R.drawable.ic_launcher);
          LayoutInflater li = LayoutInflater.from(getApplicationContext());
			View promptsView = li.inflate(R.layout.prompts2_fwd, null);
	          final Spinner userInput = (Spinner) promptsView
						.findViewById(R.id.sp_1_inp);
	          final CheckBox userInput2 = (CheckBox) promptsView
						.findViewById(R.id.chk1);
	          final CheckBox userInput3 = (CheckBox) promptsView
						.findViewById(R.id.chk2);
	          final CheckBox userInput4 = (CheckBox) promptsView
						.findViewById(R.id.chk3);
          builderSingle.setTitle("Forward Order");
          
   
        	  builderSingle.setView(promptsView);
        		ArrayAdapter<String> adpt1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        		adpt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		
        		userInput.setAdapter(adpt1);
        	  final Cursor c3=DBHelper.getData(getApplicationContext(),Utils.bsu," where permission not like '%5%' and permission like '%1%'");
          
        	  adpt1.add("Select User");
      		while(c3.moveToNext())
      		{	
      			adpt1.add(c3.getString(1));
      		}

      		adpt1.setNotifyOnChange(true);
         
          builderSingle.setPositiveButton("FORWARD",
                  new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                    	 if(userInput.getSelectedItem().toString().equals("Select User"))
                    			Toast.makeText(getApplicationContext(), "Oops All Field Mandatory! Try Again!", Toast.LENGTH_SHORT).show();
                    	 else {
                    		 String chkstr1="";
                    		 String chkstr2="";
                    		 String chkstr3="";
                    		 String idss= DBHelper.getIDBySubName(getApplicationContext(),userInput.getSelectedItem().toString());
                    		 if(userInput2.isChecked())chkstr1="1";
                    		 if(userInput3.isChecked())chkstr2="2";
                    		 if(userInput4.isChecked())chkstr3="3";
                    		 
                    		 
                    		 try {
                    	  		data.put("parent", Utils.getDefaults("user_id", getApplicationContext()));
                 				data.put("subid",idss);
                 				data.put("parentsubid",Utils.getDefaults("subid", getApplicationContext()));
                 				data.put("order_id",id);
                 				data.put("hide",chkstr1+chkstr2+chkstr3);
                    			if(Utils.isConnectingToInternet(getApplicationContext()))	{	
                    	    		  httpGetAsyncTask = new HttpGetAsyncTask(OrderDetails.this,3);
               				       httpGetAsyncTask.delegate=OrderDetails.this;
                    	  httpGetAsyncTask.execute(Utils.fwdOrdertoSub+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
                    			Toast.makeText(getApplicationContext(), "Order Successfully Forwarded to Subuser", Toast.LENGTH_SHORT).show();
                    		 }else
                    	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                          
                    	  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
                          dialog.dismiss();
                    	 }
                      }
                  });

     
          builderSingle.show();
      }

	

	
	public void jump(String tb)
	{
		Intent loginIntent =null;
	
		if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
    		String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
          if(Arrays.asList(chk1).contains("5"))
    			loginIntent = new Intent(this, Orders.class);
    		    else loginIntent = new Intent(this, SubOrders.class);
    		}else
    			loginIntent = new Intent(this, Orders.class);
		
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("tb",tb);
			 loginIntent.putExtra("ttp","");
	        this.startActivity(loginIntent);
	        finish();
	}
	
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		 jump("0");
	}
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.main,menu);
			
			return true;
		}
	 
	 @Override
		public void onBackPressed() {
	   super.onBackPressed();
	   Intent loginIntent =null;
	   if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
    		String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
          if(Arrays.asList(chk1).contains("5"))
    			loginIntent = new Intent(this, Orders.class);
    		    else loginIntent = new Intent(this, SubOrders.class);
    		}else
    			loginIntent = new Intent(this, Orders.class);
		
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("tb","0");
			 loginIntent.putExtra("ttp","");
	        this.startActivity(loginIntent);
	        finish();
		}
		

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	      
	         case android.R.id.home:
	        	 Intent loginIntent =null;
	        		
	     		if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
	         		String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
	               if(Arrays.asList(chk1).contains("5"))
	         			loginIntent = new Intent(this, Orders.class);
	         		    else loginIntent = new Intent(this, SubOrders.class);
	         		}else
	         			loginIntent = new Intent(this, Orders.class);
	     		
	     			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	     			 loginIntent.putExtra("tb","0");
	     			loginIntent.putExtra("ttp","");
	     	        this.startActivity(loginIntent);
	     	        finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }
		
	 
	 class DownloadFileFromURL extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread
			 * Show Progress Bar Dialog
			 * */
			
			@SuppressWarnings("deprecation")
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(OrderDetails.this);
				pDialog.setMessage("Downloading file. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setMax(100);
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setCancelable(false);
				pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	cancel(true);
				    	pDialog.dismiss();
				    }
				});
				pDialog.show();
				
				showDialog(progress_bar_type);
			}

			/**
			 * Downloading file in background thread
			 * */
			@Override
			protected String doInBackground(String... f_url) {
				int count;
		        try {
		            URL url = new URL(f_url[0]);
		         //  final  String []chk1=f_url[0].split("[/]");
		            URLConnection conection = url.openConnection();
		            conection.connect();
		            // getting file length
		            int lenghtOfFile = conection.getContentLength();

		            // input stream to read file - with 8k buffer
		            InputStream input = new BufferedInputStream(url.openStream(), 8192);
		            
		            // Output stream to write file
		            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/Download/"+da);

		            byte data[] = new byte[1024];

		            long total = 0;

		            while ((count = input.read(data)) != -1) {
		                total += count;

		                publishProgress(""+(int)((total*100)/lenghtOfFile));
		               
		                pDialog.setProgressNumberFormat((Utils.bytes2String(total)) + "/" + (Utils.bytes2String(lenghtOfFile)));
		                // writing data to file
		                output.write(data, 0, count);
		                
		                if (isCancelled()) break;
		            }

		            // flushing output
		            output.flush();
		            
		            // closing streams
		            output.close();
		            input.close();
		            
		        } catch (Exception e) {
		        	Log.e("B2bError: ", e.getMessage());
		        }
		        
		        return "";
			}
			
			/**
			 * Updating progress bar
			 * */
			protected void onProgressUpdate(String... progress) {
				// setting progress percentage
	            pDialog.setProgress(Integer.parseInt(progress[0]));
			}

			/**
			 * After completing background task
			 * Dismiss the progress dialog
			 * **/
			
			@Override
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after the file was downloaded
				pDialog.dismiss();
			opne(da);

			}

		}
	 
	 public void opne(final String flnm)
		{
			String uri=Environment.getExternalStorageDirectory().getPath()+"/Download/"+flnm;
			new AlertDialog.Builder(this)
			.setMessage("File Saved: "+uri+"\n Do you want open this file?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	String m=Environment.getExternalStorageDirectory().getPath()+"/Download/"+flnm;
			    	File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Download/"+flnm);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					
					intent.setDataAndType(Uri.fromFile(file),Utils.getMimeType(m));
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
		}

}
