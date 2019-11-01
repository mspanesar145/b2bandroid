package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams") public class ProductDetails extends Activity  implements AsyncResponse {

	TableLayout t1;
	String id;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	ImageView imgs;
	String imgss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_product_info);
		Utils.setActionBar(this,"Product Details");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		t1 = (TableLayout) findViewById(R.id.main_table_details);
		imgs = (ImageView) findViewById(R.id.imgPreview3);
		loadInfo();
	}
	
	@SuppressWarnings("deprecation")
	public void loadInfo()
	{
		t1.removeAllViews();
		
		Cursor d=DBHelper.getData(getApplicationContext(),Utils.bp," where product_id='"+id+"'");
    	if(d.moveToFirst())
    	{
    	
    		File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"+d.getString(6));    
    		imgss=d.getString(6);
      	    Picasso.with(getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgs);
		 TableRow tr_data_hd1 = new TableRow(this);
		 tr_data_hd1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
	
			
			TextView data3 = new TextView(this);
			data3.setId(201);
			data3.setTypeface(null, Typeface.BOLD);
			data3.setBackgroundResource(R.drawable.cell_shape_head);
			data3.setText("PRODUCT DETAILS ");
			data3.setTextColor(Color.BLACK);
			data3.setPadding(15,15,15,15);
		    tr_data_hd1.addView(data3);// add the column to the table row here
	         
		 TableRow.LayoutParams params = (TableRow.LayoutParams)data3.getLayoutParams();
		 params.span = 4; 
		 data3.setLayoutParams(params);
	         
	     t1.addView(tr_data_hd1, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	     
	   
	 	String[] heading = {
	 			"Category","Product Name","Mrp","Description"
				};
	
		String[] values = {
				d.getString(7),d.getString(1),d.getString(4),d.getString(5)
				};
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
 	        data1.setId(211);
 	        data1.setBackgroundResource(R.drawable.cell_shape);
 	        data1.setText(values[j]);
 	        data1.setTextColor(Color.BLACK);
 	        data1.setPadding(7, 7, 7, 7);
 	        tr_data.addView(data1);// add the column to the table row here
 	        
 	       TableRow.LayoutParams params4 = (TableRow.LayoutParams)data1.getLayoutParams();
 			 params4.span = 2; 
 			 data1.setLayoutParams(params4);
 			 
 			 
 			TextView data2a = new TextView(this);
	        data2a.setId(j);
	       
	        
	        data2a.setBackgroundResource(R.drawable.cell_shape_head);
	      if(j==0) data2a.setText("");
	      else {data2a.setText("Edit"); data2a.setOnClickListener(clickdata1);}
	      
	        data2a.setTextColor(Color.RED);
	        data2a.setPadding(7, 7, 7, 7);
	        tr_data.addView(data2a);// add the column to the table row here
  	        
  	      

 	        
 	        t1.addView(tr_data, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
 	        
 	        
 	        
			}
 			
		 TableRow tr_data_hd1v = new TableRow(this);
		 tr_data_hd1v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	   
		TextView data3v = new TextView(this);
		data3v.setId(201);
		data3v.setTypeface(null, Typeface.BOLD);
		data3v.setBackgroundResource(R.drawable.cell_shape_head);
		data3v.setText("PRODUCT SPECIFICATION");
		data3v.setTextColor(Color.BLACK);
		data3v.setPadding(15,15,15,15);
	    tr_data_hd1v.addView(data3v);// add the column to the table row here
         
	 TableRow.LayoutParams paramsvs = (TableRow.LayoutParams)data3v.getLayoutParams();
	 paramsvs.span = 4; 
	 data3v.setLayoutParams(paramsvs);
         
     t1.addView(tr_data_hd1v, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		
 	       Cursor c=DBHelper.getData(getApplicationContext(),Utils.bps," where product_id='"+d.getString(0)+"'");
 	    	int j=0;
 	      while(c.moveToNext())
 	    	{
 	    		 TableRow tr_data2 = new TableRow(this);
      			tr_data2.setId(Integer.parseInt(c.getString(0)));
      		  
      			tr_data2.setLayoutParams(new LayoutParams(
      		     LayoutParams.FILL_PARENT,
      		     LayoutParams.WRAP_CONTENT));	
      			
      			
      			TextView data0a = new TextView(this);
      			data0a.setId(201);
      			data0a.setWidth(150);
      			data0a.setTypeface(null, Typeface.BOLD);
      			data0a.setBackgroundResource(R.drawable.cell_shape);
      			data0a.setText(c.getString(3));
      			data0a.setTextColor(Color.BLACK);
      			data0a.setEllipsize(null);
      		    data0a.setSingleLine(false);
      			data0a.setPadding(7, 7, 7, 7);
      		    tr_data2.addView(data0a);// add the column to the table row here
      		    
      		    
      	         
      	        TextView data1a = new TextView(this);
      	        data1a.setId(211+j);
      	      data1a.setWidth(150);
      	        data1a.setBackgroundResource(R.drawable.cell_shape);
      	        data1a.setText(c.getString(4));
      	        data1a.setTextColor(Color.BLACK);
      	        data1a.setPadding(2, 2, 2, 2);
      	        data1a.setEllipsize(null);
      	      data1a.setSingleLine(false);
      	        tr_data2.addView(data1a);// add the column to the table row here
      	        TableRow.LayoutParams params4v = (TableRow.LayoutParams)data1a.getLayoutParams();
  			    params4v.span = 2; 
  			    data1a.setLayoutParams(params4v);
      	        
      	        TextView data2a = new TextView(this);
    	        data2a.setId(Integer.parseInt(c.getString(0)));
    	        data2a.setOnClickListener(clickdata0);
    	        
    	        data2a.setBackgroundResource(R.drawable.cell_shape_head);
    	        data2a.setText("Delete");
    	      
    	        data2a.setTextColor(Color.RED);
    	        data2a.setPadding(7, 7, 7, 7);
    	        tr_data2.addView(data2a);// add the column to the table row here
      	        
      	      
      	        t1.addView(tr_data2, new TableLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));	
 	    	j++;
 	    	}
			
	 
		
    	}
	}
	
	 private OnClickListener clickdata0 = new OnClickListener() {
         public void onClick(View v) {

        	 dialogInterface(v.getId()+"");
         }
     };
     
     private OnClickListener clickdata1 = new OnClickListener() {
         public void onClick(View v) {

      
        	  dialogDetails(id,"",v.getId());
         }
     };
	
	 @SuppressLint("InflateParams") public void dialogDetails() {
			

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			this);
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompts2a, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.UserInput);
		          final EditText userInput2 = (EditText) promptsView
							.findViewById(R.id.UserInput2);
	
	          builderSingle.setTitle("Add Specification");
	          
	   
	        	  builderSingle.setView(promptsView);
	              
	        
	        	 
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	 if(userInput.getText().toString().equals("") || userInput2.getText().toString().equals(""))
	                    			Toast.makeText(getApplicationContext(), "Oops All Field Mandatory! Try Again!", Toast.LENGTH_SHORT).show();
	                    	 else {try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
	                    	  		data.put("product_id", id);
	                    			data.put("name", userInput.getText().toString());
	                    			data.put("value",userInput2.getText().toString());
	     
	                    			 httpGetAsyncTask = new HttpGetAsyncTask(ProductDetails.this,3);
	                    		        httpGetAsyncTask.delegate=ProductDetails.this;
	                    		        if(Utils.isConnectingToInternet(getApplicationContext()))
	                    	  httpGetAsyncTask.execute(Utils.addNewSpec+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			else
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
	 
	 
	 public void dialogDetails(final String pid,final String name,final int jj) {
	    	

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			this);
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.promptspedit, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);
	        
	          String[] heading = {
	  	 			"Category","Product Name","Mrp","Description"
	  				};
	          
	          builderSingle.setTitle("Update "+heading[jj]);
	        	  builderSingle.setView(promptsView);
	              
	        
	        	  userInput.setText(name);
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	  if(userInput.getText().toString().equals(""))
	                    		  Toast.makeText(getApplicationContext(), "Oops! Empty Name Try Again!", Toast.LENGTH_SHORT).show();	  
	                      else{
	                    	  try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
	                    			data.put("des", userInput.getText().toString());
	                    			data.put("pid",pid);
	                    			data.put("type",jj);
	                    			if(Utils.isConnectingToInternet(getApplicationContext())){	
	                    				 httpGetAsyncTask = new HttpGetAsyncTask(ProductDetails.this,1);
		                    		        httpGetAsyncTask.delegate=ProductDetails.this;
	                    	  httpGetAsyncTask.execute(Utils.edtprdt+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			}else
	                    	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                         
	                    	  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
	        					// TODO Auto-generated catch block
	        					e.printStackTrace();
	        				}
	                      }
	                    	 dialog.dismiss();
	                    	  } 
	                      
	                  });

	     
	          builderSingle.show();
	      }
	 
	 
	 @Override
		public void onBackPressed() {
	   super.onBackPressed();
	  Intent ap2 = new Intent(this, Products.class);
			 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(ap2);
	        finish();
		}
		

	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.cat, menu);
			return true;
		}
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	     switch (menuItem.getItemId()) {
	         case R.id.aCat:
	        	 dialogDetails();
	        	 break;
	         case android.R.id.home:
	        	 Intent ap2 = new Intent(this, Products.class);
				 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        this.startActivity(ap2);
		        finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }
	 
	 public void dialogInterface(final String spcid)
		{
			new AlertDialog.Builder(ProductDetails.this)
			.setMessage("Are you sure want to delete?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	 httpGetAsyncTask = new HttpGetAsyncTask(ProductDetails.this,3);
     		        httpGetAsyncTask.delegate=ProductDetails.this;
     		        if(Utils.isConnectingToInternet(getApplicationContext()))
     	  httpGetAsyncTask.execute(Utils.delSpec+spcid+"/"+Utils.getDefaults("user_id", getApplicationContext()));
     			else
     	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			    }})
			 .setNegativeButton(android.R.string.no, null).show();	
		}


	 public void openImg(View v)
	 {
		 /*File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"+imgss);    
		 Intent intent = new Intent();
		 intent.setAction(Intent.ACTION_VIEW);
		 intent.setDataAndType(Uri.parse(imgFile.toString()), "image/*");
		 startActivity(intent);*/
		 
		 File file = new File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"+imgss);
		Uri path = Uri.fromFile(file);
		Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
		pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		pdfOpenintent.setDataAndType(path, "image/*");
		try {
		startActivity(pdfOpenintent);
		}
		catch (ActivityNotFoundException e) {
		
		}
	 }
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
		try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("products");
			JSONArray Data2 = jsonObj.getJSONArray("specs");	
			DBHelper.storeProductsSpecs(getApplicationContext(),Data2);
			DBHelper.storeProducts(getApplicationContext(),Data);
			
			loadInfo();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
