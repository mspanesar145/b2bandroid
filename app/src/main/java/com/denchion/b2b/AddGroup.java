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
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class AddGroup extends Fragment  implements AsyncResponse{

	public AddGroup(){}
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	Button AddGrp;
	EditText gname;
	EditText olimit;
	EditText searchbox;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
      final View rootView = inflater.inflate(R.layout.add_group, container, false);
      
         AddGrp = (Button) rootView.findViewById(R.id.btngadd);
         searchbox=  (EditText) rootView.findViewById(R.id.keywordbox);
        
         httpGetAsyncTask = new HttpGetAsyncTask(getActivity(),3);
	     httpGetAsyncTask.delegate=this;
         AddGrp.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
            	
            	 dialogDetails();
             }
         });
         
         searchbox.setOnKeyListener(new View.OnKeyListener() {
     	    public boolean onKey(View v, int keyCode, KeyEvent event) {
     	        // If the event is a key-down event on the "enter" button
     	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
     	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
     	        	getGroups(rootView," where group_name like '%"+searchbox.getText().toString()+"%'");
     	          return true;
     	        }
     	        return false;
     	    }

			
     	});
        getGroups(rootView," order by group_id DESC");

	
        return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(false);
	}
	
	 public void getGroups(View rootView,String q)
	 {
		 users.clear();
		final Cursor c=DBHelper.getData(getActivity().getApplicationContext(),Utils.grptbl,q);
 		if(c.getCount()==0)
 		{
 			//Toast.makeText(getApplicationContext(), "Please Wait! Coupons Loading...", Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		else 
 		{
 			
 			
     		while(c.moveToNext())
     		{

		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(1).toUpperCase(Locale.getDefault()));
		    hm.put("limit",c.getString(2)+" Order Limit");
		    hm.put("desc",c.getString(4));
		    hm.put("ucount",DBHelper.getUserGroupCount(getActivity().getApplicationContext(),c.getString(0))+" Partners");
		    hm.put("content_id",c.getString(0));
            users.add(hm);
             }
     		
     		 String[] from = { "uname","limit", "content_id","desc","ucount" };
             int[] to = {R.id.u_title,R.id.u_type,R.id.content_id,R.id.u_dt,R.id.u_status};
             SimpleAdapter adapter = new SimpleAdapter(getActivity(), users,R.layout.list_layout3a , from, to); 
             ListView mylist = (ListView) rootView.findViewById(R.id.group_lists);
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
    	            
    	             Intent intent = new Intent(getActivity(), AddUserToGroup.class);
                     intent.putExtra("id", fieldname);
                     startActivity(intent);  
                 }
                 });
             
             mylist.setOnItemLongClickListener(new OnItemLongClickListener() {
                 public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                         int arg2, long id) {
         			ListView lv = (ListView) arg0;
                     TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                     TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_dt);
                     String gid = fishtextview.getText().toString();
                     String fieldname2 = fishtextview2.getText().toString();
                     PopOptions(gid,fieldname2);
     				 return true;
     			}
     		});
     	
 		}
	 }
	 
	 private void PopOptions(final String ggid,final String note) {
			
			
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					getActivity());
		    builderSingle.setIcon(R.drawable.ic_launcher);
		    builderSingle.setTitle("Options");
		    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
		    		getActivity(),
		            android.R.layout.select_dialog_singlechoice);
		  
		    arrayAdapter.add("Edit Note");
		    arrayAdapter.add("Delete Group");

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
		                    if(strName.equals("Edit Note"))
		                    {
		                    	dialogDetails(ggid,note);
		                    }
		                    else if(strName.equals("Delete Group"))
		                    {
		                    	del(ggid);
		                    }
		           		  dialog.dismiss();
		                   
		                }
		            });
		    
		    builderSingle.show();
		}

	 public void del(final String ggid)
		{
			new AlertDialog.Builder(getActivity())
			.setMessage("Do you really want to Delete this group ?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			    	delgroup(ggid);
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
			
		}
	 
	 public void delgroup(String ggid)
	 {
	  	 if(Utils.isConnectingToInternet(getActivity().getApplicationContext())){ 
   		  httpGetAsyncTask = new HttpGetAsyncTask(getActivity(),3);
		       httpGetAsyncTask.delegate=AddGroup.this;
   		 httpGetAsyncTask.execute(Utils.delGroup+ggid+"/"+Utils.getDefaults("user_id", getActivity().getApplicationContext())); 
   		
   	
   	 }else 
   	 Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	 }
	 
	 
	 public void dialogDetails(final String gid,final String name) {
	    	

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			getActivity());
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
				View promptsView = li.inflate(R.layout.promptsgedit, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);
	          builderSingle.setTitle("Update Now:-");
	          
	   
	        	  builderSingle.setView(promptsView);
	              
	        
	        	  userInput.setText(name);
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	  if(userInput.getText().toString().equals(""))
	                    		  Toast.makeText(getActivity().getApplicationContext(), "Oops! Empty Name Try Again!", Toast.LENGTH_SHORT).show();	  
	                      else{
	                    	  try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id", getActivity().getApplicationContext()));
	                    			data.put("des", userInput.getText().toString());
	                    			data.put("gid",gid);
	                    			if(Utils.isConnectingToInternet(getActivity().getApplicationContext()))		
	                    	  httpGetAsyncTask.execute(Utils.editGroupNote+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			else
	                    	        	Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                            
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
	 
	 public void dialogDetails() {
			

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			getActivity());
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
				View promptsView = li.inflate(R.layout.prompts2, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.UserInput);
		          final EditText userInput2 = (EditText) promptsView
							.findViewById(R.id.UserInput2);
		          final EditText userInput3 = (EditText) promptsView
							.findViewById(R.id.UserInput3);
	          builderSingle.setTitle("Groups");
	          
	   
	        	  builderSingle.setView(promptsView);
	              
	        
	        	 
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	 if(userInput.getText().toString().equals("") || userInput2.getText().toString().equals(""))
	                    			Toast.makeText(getActivity().getApplicationContext(), "Oops All Field Mandatory! Try Again!", Toast.LENGTH_SHORT).show();
	                    	 else {try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id", getActivity().getApplicationContext()));
	                    			data.put("name", userInput.getText().toString());
	                    			data.put("des", userInput3.getText().toString());
	                    			data.put("olimit",userInput2.getText().toString());
	                    			data.put("type","1");
	                    			if(Utils.isConnectingToInternet(getActivity().getApplicationContext()))		
	                    	  httpGetAsyncTask.execute(Utils.StoreGroup+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	                    			else
	                    	        	Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                              
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
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("groups");	
			JSONArray Data2 = jsonObj.getJSONArray("grouprel");
			DBHelper.storeGroups(getActivity().getApplicationContext(),Data);
			DBHelper.storeGroupsRelations(getActivity().getApplicationContext(),Data2);
			((B2BDashboard)getActivity()).displayView(9);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		  
	}
}
        
