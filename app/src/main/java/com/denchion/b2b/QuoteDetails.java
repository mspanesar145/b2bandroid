package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuoteDetails extends Activity  implements AsyncResponse {
	TableLayout t1;
	String id;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	 JSONObject jsonObject;
	 JSONObject jsonObj2;
	 JSONObject jsonObj3;
	 String flnm;
	 TextView ortxt;
	 Button btndownload;
	// button to show progress dialog
		Button btnShowProgress;
		
		// Progress Dialog
		private ProgressDialog pDialog;
		ImageView my_image;
		// Progress dialog type (0 - for Horizontal progress bar)
		public static final int progress_bar_type = 111; 
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_quote_details);
		Utils.setActionBar(this,"Quotation Details");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		
		t1 = (TableLayout) findViewById(R.id.main_table_details);
		ortxt = (TextView) findViewById(R.id.ortxt);
		btndownload = (Button) findViewById(R.id.btndownload);
		try {
			loadDetails();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void loadDetails() throws JSONException
	{
		//final Cursor d=DBHelper.getJoinData(getApplicationContext(),"qq.*,pt.full_name",Utils.quote+" qq","  where quotation_id='"+id+"'");
		final Cursor d=DBHelper.getData(getApplicationContext(),Utils.quote," where quotation_id='"+id+"'");
		if(d.moveToFirst())
    	{
    		if(!d.getString(8).equals("null")){
    		
    		flnm=d.getString(8);
    		ortxt.setText(d.getString(8));
    		
    		}
    		else
    		{
    			ortxt.setText("No Attachment Found !");
    			btndownload.setVisibility(View.GONE);
    		}
    		
    		String[] heading = {
    				"Quotation ID","Partner","Product","Type","Date","Time","Expire On"
    				};
    		String nm="";
    		String unm="";
    		String dts="";
    		if(d.getString(12).equals("0000-00-00"))dts="N/A"; else dts=Utils.parseDateToddMMyyyy2(d.getString(12));
    		if(d.getString(9).equals("1"))nm="Sent Quotation";else nm="Asked Quotation";
    		if(d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))unm=DBHelper.getNameByID(getApplicationContext(),d.getString(3));
    		else unm=DBHelper.getNameByID(getApplicationContext(),d.getString(1));;
    		String[] values = {
    				d.getString(0),unm,d.getString(6),nm,Utils.parseDateToddMMyyyy2(d.getString(10)),Utils.ConvertTime(d.getString(11)),dts
    				};
    	
			 TableRow tr_data_hd1 = new TableRow(this);
    		 tr_data_hd1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
    	
    			
    			TextView data3 = new TextView(this);
    			data3.setId(201);
    			data3.setTypeface(null, Typeface.BOLD);
    			data3.setBackgroundResource(R.drawable.cell_shape_head);
    			data3.setText("QUOTATION DETAILS");
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
     	        data1.setId(211+j);
     	        data1.setBackgroundResource(R.drawable.cell_shape);
     	        data1.setText(values[j]);
     	        data1.setTextColor(Color.BLACK);
     	        data1.setPadding(7, 7, 7, 7);
     	        tr_data.addView(data1);// add the column to the table row here
     	        
     	       TableRow.LayoutParams params3 = (TableRow.LayoutParams)data1.getLayoutParams();
      		 params3.span = 3; 
      		data1.setLayoutParams(params3);
     	        t1.addView(tr_data, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
     	         
     			}
    			
    			if(d.getString(9).equals("1")){
    			
    			 TableRow tr_data_hd2 = new TableRow(this);
        		 tr_data_hd2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
    			
    			TextView data4 = new TextView(this);
    			data4.setId(201);
    			data4.setTypeface(null, Typeface.BOLD);
    			data4.setBackgroundResource(R.drawable.cell_shape_head);
    			data4.setText("CONDITIONS");
    			data4.setTextColor(Color.BLACK);
    			data4.setPadding(15,15,15,15);
    		    tr_data_hd2.addView(data4);// add the column to the table row here
    	         
    		 TableRow.LayoutParams params3 = (TableRow.LayoutParams)data4.getLayoutParams();
    		 params3.span = 4; 
    		 data4.setLayoutParams(params3);
    	         
    	     t1.addView(tr_data_hd2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	     
    	     
    	     TableRow tr_data3 = new TableRow(this);
   			tr_data3.setId(Integer.parseInt(d.getString(0)));
   		  
   			tr_data3.setLayoutParams(new LayoutParams(
   		     LayoutParams.FILL_PARENT,
   		     LayoutParams.WRAP_CONTENT));	
   			
   			
   			TextView data10 = new TextView(this);
   			data10.setId(201);
   			data10.setTypeface(null, Typeface.BOLD);
   			data10.setBackgroundResource(R.drawable.cell_shape);
   			data10.setText("FROM RANGE");
   			data10.setTextColor(Color.BLACK);
   			data10.setPadding(7, 7, 7, 7);
   		    tr_data3.addView(data10);// add the column to the table row here
   		    
   		    
   	         
   	        TextView data11 = new TextView(this);
   	        data11.setId(211);
   	        data11.setBackgroundResource(R.drawable.cell_shape);
   	        data11.setTypeface(null, Typeface.BOLD);
   	        data11.setText("TO RANGE");
   	        data11.setTextColor(Color.BLACK);
   	        data11.setPadding(7, 7, 7, 7);
   	        tr_data3.addView(data11);// add the column to the table row here
   	        
   	      TextView data12 = new TextView(this);
   	        data12.setId(211);
   	        data12.setBackgroundResource(R.drawable.cell_shape);
   	        data12.setTypeface(null, Typeface.BOLD);
   	        data12.setText("PRICE");
   	        data12.setTextColor(Color.BLACK);
   	        data12.setPadding(7, 7, 7, 7);
   	        tr_data3.addView(data12);// add the column to the table row here
   	        
   	        
   	     TextView data13 = new TextView(this);
	        data13.setId(2411);
	        data13.setBackgroundResource(R.drawable.cell_shape);
	        data13.setTypeface(null, Typeface.BOLD);
	        data13.setText("DISCOUNT");
	        data13.setTextColor(Color.BLACK);
	        data13.setPadding(7, 7, 7, 7);
	        tr_data3.addView(data13);// add the column to the table row here
   	        t1.addView(tr_data3, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	     
    			
    			  String jsonstr=d.getString(7);
    			  
    			    JSONObject  jsonObject = new JSONObject(jsonstr);
		            JSONArray Data= jsonObject.getJSONArray("qtyfrom");
		            JSONArray Data2= jsonObject.getJSONArray("qtyto");
		           JSONArray Data3= jsonObject.getJSONArray("price");
		           JSONArray Data4= jsonObject.getJSONArray("discount");
		           
	
		       
		            for(int j=0;j<Data.length();j++)
		    		{
		            	JSONObject jsonObj  = Data.getJSONObject(j);
		            	JSONObject jsonObj2 = Data2.getJSONObject(j);
		            	JSONObject jsonObj2a = Data3.getJSONObject(j);
		            	JSONObject jsonObj2b = Data4.getJSONObject(j);
		            	TableRow tr_data2 = new TableRow(this);
		      			tr_data2.setId(Integer.parseInt(d.getString(0)));
		      		  
		      			tr_data2.setLayoutParams(new LayoutParams(
		      		     LayoutParams.FILL_PARENT,
		      		     LayoutParams.WRAP_CONTENT));	
		      			
		      			
		      			TextView data0 = new TextView(this);
		      			data0.setId(201);
		      			data0.setBackgroundResource(R.drawable.cell_shape);
		      			data0.setText(jsonObj.getString("qtyfrom"));
		      			data0.setTextColor(Color.BLACK);
		      			data0.setPadding(7, 7, 7, 7);
		      		    tr_data2.addView(data0);// add the column to the table row here
		      		    

		      	        TextView data1 = new TextView(this);
		      	        data1.setId(211+j);
		      	        data1.setBackgroundResource(R.drawable.cell_shape);
		      	        data1.setText(jsonObj2.getString("qtyto"));
		      	        data1.setTextColor(Color.BLACK);
		      	        data1.setPadding(7, 7, 7, 7);
		      	        tr_data2.addView(data1);// add the column to the table row here
		      	        
		      	        TextView data2 = new TextView(this);
		      	        data2.setId(211+j);
		      	        data2.setBackgroundResource(R.drawable.cell_shape);
		      	        data2.setText(jsonObj2a.getString("price"));
		      	        data2.setTextColor(Color.BLACK);
		      	        data2.setPadding(7, 7, 7, 7);
		      	        tr_data2.addView(data2);// add the column to the table row here
		      	        
		      	      TextView data2a = new TextView(this);
		      	        data2a.setId(211+j);
		      	        data2a.setBackgroundResource(R.drawable.cell_shape);
		      	        data2a.setText(jsonObj2b.getString("discount")+"%");
		      	        data2a.setTextColor(Color.BLACK);
		      	        data2a.setPadding(7, 7, 7, 7);
		      	        tr_data2.addView(data2a);// add the column to the table row here
		      	        
		      	        t1.addView(tr_data2, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				   
				    }
    			}
		            
		       	 TableRow tr_data_hd3 = new TableRow(this);
        		 tr_data_hd3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
    			
    			TextView data4a = new TextView(this);
    			data4a.setId(201);
    			data4a.setTypeface(null, Typeface.BOLD);
    			data4a.setBackgroundResource(R.drawable.cell_shape_head);
    			data4a.setText("QUOTATION DETAILS");
    			data4a.setTextColor(Color.BLACK);
    			data4a.setPadding(15,15,15,15);
    		    tr_data_hd3.addView(data4a);// add the column to the table row here
    	         
    		 TableRow.LayoutParams params3a = (TableRow.LayoutParams)data4a.getLayoutParams();
    		 params3a.span = 4; 
    		 data4a.setLayoutParams(params3a);
    	         
    	     t1.addView(tr_data_hd3, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
    	     
    	     TableRow tr_data3a = new TableRow(this);
    			tr_data3a.setId(Integer.parseInt(d.getString(0)));
    		  
    			tr_data3a.setLayoutParams(new LayoutParams(
    		     LayoutParams.FILL_PARENT,
    		     LayoutParams.WRAP_CONTENT));	
    			
    			
    			TextView data10a = new TextView(this);
    			data10a.setId(201);
    			data10a.setBackgroundResource(R.drawable.cell_shape);
    			data10a.setText(d.getString(5));
    			data10a.setTextColor(Color.BLACK);
    			data10a.setPadding(7, 7, 7, 7);
    		    tr_data3a.addView(data10a);// add the column to the table row here
    		    
    		    TableRow.LayoutParams params3b = (TableRow.LayoutParams)data10a.getLayoutParams();
       		 params3b.span = 4; 
       		data10a.setLayoutParams(params3b);
    	         
    	  
    	        
    	        t1.addView(tr_data3a, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	        
    	        
    	   	 TableRow tr_data_hd4 = new TableRow(this);
    		 tr_data_hd4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
			
			TextView data4b = new TextView(this);
			data4b.setId(201);
			data4b.setTypeface(null, Typeface.BOLD);
			data4b.setBackgroundResource(R.drawable.cell_shape_head);
			data4b.setText("ATTACHMENT");
			data4b.setTextColor(Color.BLACK);
			data4b.setPadding(15,15,15,15);
		    tr_data_hd4.addView(data4b);// add the column to the table row here
	         
		 TableRow.LayoutParams params3c = (TableRow.LayoutParams)data4b.getLayoutParams();
		 params3c.span = 4; 
		 data4b.setLayoutParams(params3c);
	         
	     t1.addView(tr_data_hd4, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
     	     
    	}
    	
    	
	}
	
	public void action(View v)
	{
		new DownloadFileFromURL().execute(Utils.base+"images/attachments/"+flnm);	
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			
			return pDialog;
		default:
			return null;
		}
	}

	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(QuoteDetails.this);
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
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();

	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            
	            // Output stream to write file
	            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/Download/"+flnm);

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
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(progress_bar_type);

			opne();
		}

	}
	
	public void opne()
	{
		String uri=Environment.getExternalStorageDirectory().getPath()+"/Download/"+flnm;
		new AlertDialog.Builder(this)
		.setMessage("File Saved: "+uri+" \n Do you want open this file?")
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
	

	
	public void jump(String tb)
	{
	    Intent loginIntent = new Intent(this, Quotations.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("tb",tb);
	        this.startActivity(loginIntent);
	        finish();
	}
	
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.main,menu);
			
			return true;
		}
	 
	 @Override
		public void onBackPressed() {
	   super.onBackPressed();
	  Intent ap2 = new Intent(this, Quotations.class);
			 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 ap2.putExtra("tb","0");
	        this.startActivity(ap2);
	        finish();
		}
		

	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	      
	         case android.R.id.home:
	     	    Intent loginIntent = new Intent(this, Quotations.class);
				 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 loginIntent.putExtra("tb","0");
		        this.startActivity(loginIntent);
		        finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

	
	
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
	}

}
