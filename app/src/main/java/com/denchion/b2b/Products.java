package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
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
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Products extends Activity implements AsyncResponse {

	ProgressDialog dialog;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	EditText searchbox;
	 HttpGetAsyncTask httpGetAsyncTask;
	  List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_products);
		Utils.setActionBar(this,"Products");
		DBHelper.loaddb(getApplicationContext());
		searchbox=  (EditText) findViewById(R.id.keywordbox);
		getProducts(" order by product_id DESC");
	       httpGetAsyncTask = new HttpGetAsyncTask(Products.this,1);
	        httpGetAsyncTask.delegate=Products.this;
	        if(Utils.isConnectingToInternet(getApplicationContext()))
	        httpGetAsyncTask.execute(Utils.getSPecs+Utils.getDefaults("user_id", getApplicationContext()));
	        else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	        
	        searchbox.setOnKeyListener(new View.OnKeyListener() {
        	    public boolean onKey(View v, int keyCode, KeyEvent event) {
        	        // If the event is a key-down event on the "enter" button
        	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
        	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
        	        	getProducts(" where product_name like '%"+searchbox.getText().toString()+"%'");
        	          return true;
        	        }
        	        return false;
        	    }

			
        	});
	}
	
	 public void getProducts(String q)
	 {
		users.clear();
			
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.bp,q);
		
 	while(c.moveToNext())
     		{

		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(1).toUpperCase(Locale.getDefault()));
		    hm.put("utype","Price: "+c.getString(4));
		    File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"+c.getString(6));                
		    hm.put("img",imgFile.toString());
		    hm.put("content_id",c.getString(0));
            users.add(hm);
             }
     		
     		 String[] from = { "uname", "utype", "img","content_id" };
             int[] to = {R.id.u_title,R.id.u_type,R.id.listimg,R.id.content_id};
             SimpleAdapter adapter = new SimpleAdapter(this,users,R.layout.newlist2,from,to); 
             ListView mylist = (ListView) findViewById(R.id.products);
             mylist.setAdapter(null);
             adapter.notifyDataSetChanged();
             mylist.setAdapter(adapter);
             adapter.notifyDataSetChanged();
            
             // Click event for single list row
             
             mylist.setOnItemClickListener(new OnItemClickListener(){

                 @Override
                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                  
                	   ListView lv = (ListView) arg0;
                       TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                       String fieldname = fishtextview.getText().toString();
                       Intent intent = new Intent(Products.this, ProductDetails.class);
                       intent.putExtra("id", fieldname);
                       startActivity(intent);  
                      finish();
                 }
                 });
             
             mylist.setOnItemLongClickListener(new OnItemLongClickListener() {
                 public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                         int arg2, long id) {
         			ListView lv = (ListView) arg0;
                     TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                     String gid = fishtextview.getText().toString();
                     del(gid);
     				
     				 return true;
     			}
     		});
     	
 		
	 }
	 
	 public void del(final String catID)
		{
			new AlertDialog.Builder(this)
			.setMessage("Do you really want to Delete This Product?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 

	               	  httpGetAsyncTask = new HttpGetAsyncTask(Products.this,3);
	               	  httpGetAsyncTask.delegate=Products.this;
	               	 httpGetAsyncTask.execute(Utils.DelProduct+Utils.getDefaults("user_id",getApplicationContext())+"/"+catID);

			    	 }else 
			    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
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
	        	 Intent ap = new Intent(this, AddProduct.class);
	    		 ap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		         this.startActivity(ap);
	        	 break;
	         case android.R.id.home:
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }


	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("products");	
			JSONArray Data2 = jsonObj.getJSONArray("specs");	
			DBHelper.storeProducts(getApplicationContext(),Data);
			DBHelper.storeProductsSpecs(getApplicationContext(),Data2);
			getProducts(" order by product_id DESC");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
