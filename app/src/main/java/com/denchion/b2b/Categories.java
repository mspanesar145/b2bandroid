package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") public class Categories extends Activity implements AsyncResponse{
	
	List<HashMap<String, String>> mylist = new ArrayList<HashMap<String,String>>();
	ListView listview;
	String id;
	String nm;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	String lvl;
    int nlvl;
    String dtt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_categories);
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		nm = bundle.getString("nm");
		lvl = bundle.getString("lvl");
		nlvl=Integer.parseInt(lvl);
		if(!id.equals("0")) nlvl=(Integer.parseInt(lvl)+1);
		Utils.setActionBar(this,nm);
		listview = (ListView) findViewById(R.id.cats);
		loadCategories();
	}

    public void loadCategories()
    {
    	mylist.clear();
    	Cursor c=DBHelper.getData(getApplicationContext(),Utils.cattbl," where parent='"+id+"'");
    	while(c.moveToNext())
    	{

    			HashMap<String, String> hm = new HashMap<String, String>();
			    hm.put("uname",c.getString(2).toUpperCase(Locale.getDefault()));
			    hm.put("content_id",c.getString(0));
			    hm.put("lvl",c.getString(4));
			    mylist.add(hm);  

    	}
    	
    	 String[] from = { "uname" , "content_id" , "lvl"};
         int[] to = {R.id.u_title,R.id.content_id,R.id.content_nm};
         SimpleAdapter adapter = new SimpleAdapter(this, mylist,R.layout.newlist, from, to); 
        
         listview.setAdapter(adapter);
         adapter.notifyDataSetChanged();
        
         // Click event for single list row
         
         listview.setOnItemClickListener(new OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                   // TODO Auto-generated method stub
               ListView lv = (ListView) arg0;
          
               TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
               TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
               String fieldname = fishtextview.getText().toString();
               String fieldname2 = fishtextview2.getText().toString();
               Intent intent = new Intent(Categories.this, Categories.class);
               intent.putExtra("id", fieldname);
               intent.putExtra("nm", fieldname2);
               intent.putExtra("lvl", nlvl+"");
               startActivity(intent);  
            //   finish();
             }
             });
         
         listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int arg2, long id) {
    			ListView lv = (ListView) arg0;
                TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
                String fieldname2 = fishtextview2.getText().toString();
                String gid = fishtextview.getText().toString();
                optcat(gid,fieldname2);
             
				 return true;
			}
		});
    }
    
    public void optcat(final String catID,final String catNM) {
    	

      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
      			this);
          builderSingle.setIcon(R.drawable.ic_launcher);
          builderSingle.setTitle("Select Options");
          final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
          		Categories.this,
                  android.R.layout.select_dialog_singlechoice);

          arrayAdapter.add("Edit Category");
          arrayAdapter.add("Delete Category");
        
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
                          if(strName.equals("Edit Category"))
                          {
                        	   dialogDetails("2",catNM,"2",catID);
                          }
                          else
                          {
                        	 int pcount = DBHelper.getproductscounts(getApplicationContext(),catID); 
                        	 int subcount = DBHelper.checkHaveSub(getApplicationContext(),catID);
                        	  if(subcount==0)
                        		  {
                        		  if(pcount==0){
                        		  del(catID); 
                        		  }
                        		  else
                        		  {
                        			  Toast.makeText(getApplicationContext(), "Sorry! This category have Products, please empty first!", Toast.LENGTH_LONG).show();	  
                        		  }
                        		  }
                        	  else Toast.makeText(getApplicationContext(), "Sorry! This category have Sub Categories, please empty first!", Toast.LENGTH_LONG).show();
                          }
                      }
          });

     
          builderSingle.show();
      }
    
    public void del(final String catID)
	{
    	
    	
    	
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to Delete This Category?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 

               	  httpGetAsyncTask = new HttpGetAsyncTask(Categories.this,3);
               	  httpGetAsyncTask.delegate=Categories.this;
               	 httpGetAsyncTask.execute(Utils.DelCat+Utils.getDefaults("user_id",getApplicationContext())+"/"+catID);

		    	 }else 
		    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
    
    public void dialogDetails(final String action,final String name,final String tp,final String gid) {
    	

      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
      			this);
          builderSingle.setIcon(R.drawable.ic_launcher);
          LayoutInflater li = LayoutInflater.from(getApplicationContext());
			View promptsView = li.inflate(R.layout.prompts, null);
	          final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);
          builderSingle.setTitle("Update Now:-");
          
   
        	  builderSingle.setView(promptsView);
              
        
        	  if(action.equals("2"))userInput.setText(name);
         
          builderSingle.setPositiveButton("SUBMIT",
                  new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                    	  if(userInput.getText().toString().equals(""))
                    		  Toast.makeText(getApplicationContext(), "Oops! Empty Name Try Again!", Toast.LENGTH_SHORT).show();	  
                      else{
                
                    		try {
                          		data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
                        		data.put("name", userInput.getText().toString());
                        		data.put("parent",id);
                        		data.put("level",nlvl+"");
                        		data.put("gid",gid);
                        		
                        		data.put("tp",tp);
                          if(Utils.isConnectingToInternet(getApplicationContext()))	{
                        	  httpGetAsyncTask = new HttpGetAsyncTask(Categories.this,1);
                          httpGetAsyncTask.delegate=Categories.this;
                          httpGetAsyncTask.execute(Utils.StoreCat+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
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
        	 dialogDetails("1","","1","");
        	 break;
         case android.R.id.home:
          this.finish();
                break;
     }
     return (super.onOptionsItemSelected(menuItem));
 }

@Override
public void processFinish(String output) {
	try {
		//
		jsonObj = new JSONObject(output);	
		  JSONArray Data = jsonObj.getJSONArray("categories");	
			DBHelper.storecategory(getApplicationContext(),Data);
			 finish();
			 startActivity(getIntent()); 

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

}
