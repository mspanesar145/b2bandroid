package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class AddCategory extends Fragment  implements AsyncResponse {

	List<NLevelItem> list;
	Button AddGrp;
	ListView listView;
	 ListView mylist;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	public AddCategory(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater2, ViewGroup container,Bundle savedInstanceState) {
 
        View rootView = inflater2.inflate(R.layout.gb_add_category, container, false);
        AddGrp = (Button) rootView.findViewById(R.id.btncadd);
      
        tabHost(0,rootView);
       
        listView = (ListView) rootView.findViewById(R.id.listView1);
        mylist= (ListView) rootView.findViewById(R.id.specs);
		list = new ArrayList<NLevelItem>();
		
		listView.setFocusable(false); 
		listView.setFocusableInTouchMode(false);
		listView.setLongClickable(true);
		
		 /* if(Utils.isConnectingToInternet(getActivity().getApplicationContext()))	{
			  httpGetAsyncTask = new HttpGetAsyncTask(getActivity(),1);
		  httpGetAsyncTask.delegate=this;
		  httpGetAsyncTask.execute(Utils.getCats+Utils.getDefaults("user_id", getActivity().getApplicationContext()));
		  }else
		  	Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();*/
		
		 AddGrp.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
            	
            	 dialogDetails("0","1","1","");
             }
         });
		
		//Random rng = new Random();
				final LayoutInflater inflater = LayoutInflater.from(getActivity());
				final Cursor c=DBHelper.getData(getActivity().getApplicationContext(),Utils.cattbl,"where parent=0");

		
		while (c.moveToNext()) {
			
			final NLevelItem grandParent = new NLevelItem(new SomeObject(c.getString(2)),new SomeObject(c.getString(0)),new SomeObject(c.getString(4)),null, new NLevelView() {
				
				@Override
				public View getView(NLevelItem item) {
					View view = inflater.inflate(R.layout.list_item,null);
					TextView tv = (TextView) view.findViewById(R.id.textView);
					TextView tv2 = (TextView) view.findViewById(R.id.content_id);
					TextView tv3 = (TextView) view.findViewById(R.id.level);
					//tv.setId(Integer.parseInt(c.getString(0)));
					String name = (String)((SomeObject) item.getWrappedObject()).getName();
					String lid = (String)((SomeObject) item.getWrappedObject2()).getName();
					String lid2 = (String)((SomeObject) item.getWrappedObject3()).getName();
					tv.setText(name);
					tv2.setText(lid);
					tv3.setText(lid2);
					return view;
				}
			});
			list.add(grandParent);
			final Cursor d=DBHelper.getData(getActivity().getApplicationContext(),Utils.cattbl,"where parent="+c.getString(0));
			while (d.moveToNext()) {
				NLevelItem parent = new NLevelItem(new SomeObject(d.getString(2)),new SomeObject(d.getString(0)),new SomeObject(d.getString(4)),grandParent, new NLevelView() {
					
					@Override
					public View getView(NLevelItem item) {
						View view = inflater.inflate(R.layout.list_item, null);
						TextView tv = (TextView) view.findViewById(R.id.textView);
						TextView tv2 = (TextView) view.findViewById(R.id.content_id);
						TextView tv3 = (TextView) view.findViewById(R.id.level);
						tv.setPadding(30, 0, 0, 0);
						String name = (String) ((SomeObject) item.getWrappedObject()).getName();
						String lid = (String)((SomeObject) item.getWrappedObject2()).getName();
						String lid2= (String)((SomeObject) item.getWrappedObject3()).getName();
						tv.setText(name);
						tv2.setText(lid);
						tv3.setText(lid2);
						return view;
					}
				});
		
				list.add(parent);
				final Cursor e=DBHelper.getData(getActivity().getApplicationContext(),Utils.cattbl,"where parent="+d.getString(0));
				while (e.moveToNext()) {
					NLevelItem child = new NLevelItem(new SomeObject(e.getString(2)),new SomeObject(e.getString(0)),new SomeObject(e.getString(4)),parent, new NLevelView() {
						
						@Override
						public View getView(NLevelItem item) {
							View view = inflater.inflate(R.layout.list_item, null);
							TextView tv = (TextView) view.findViewById(R.id.textView);
							TextView tv2 = (TextView) view.findViewById(R.id.content_id);
							TextView tv3 = (TextView) view.findViewById(R.id.level);
							//tv.setId(Integer.parseInt(e.getString(0)));
							tv.setPadding(75, 0, 0, 0);
							String name = (String) ((SomeObject) item.getWrappedObject()).getName();
							String lid = (String)((SomeObject) item.getWrappedObject2()).getName();
							String lid2 = (String)((SomeObject) item.getWrappedObject3()).getName();
							tv.setText(name);
							tv2.setText(lid);
							tv3.setText(lid2);
							return view;
						}
					});
				
					list.add(child);
					final Cursor f=DBHelper.getData(getActivity().getApplicationContext(),Utils.cattbl,"where parent="+e.getString(0));
					while (f.moveToNext()) {
						NLevelItem child2 = new NLevelItem(new SomeObject(f.getString(2)),new SomeObject(f.getString(0)),new SomeObject(f.getString(4)),child, new NLevelView() {
							
							@Override
							public View getView(NLevelItem item) {
								View view = inflater.inflate(R.layout.list_item, null);
								TextView tv = (TextView) view.findViewById(R.id.textView);
								TextView tv2 = (TextView) view.findViewById(R.id.content_id);
								TextView tv3 = (TextView) view.findViewById(R.id.level);
								//tv.setId(Integer.parseInt(f.getString(0)));
								tv.setPadding(105, 0, 0, 0);
								String name = (String) ((SomeObject) item.getWrappedObject()).getName();
								String lid =  (String) ((SomeObject) item.getWrappedObject2()).getName();
								String lid2 =  (String) ((SomeObject) item.getWrappedObject3()).getName();
								tv.setText(name);
								tv2.setText(lid);
								tv3.setText(lid2);
								return view;
							}
						});
					
						list.add(child2);
					}
					
				}
			}
		}
		
		NLevelAdapter adapter = new NLevelAdapter(list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
	
				((NLevelAdapter)listView.getAdapter()).toggle(arg2);
				((NLevelAdapter)listView.getAdapter()).getFilter().filter();
			}
			
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int arg2, long id) {
    			ListView lv = (ListView) arg0;
                TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.textView);
                TextView fishtextview3=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.level);
                String gid = fishtextview.getText().toString();
                String nm = fishtextview2.getText().toString();
                String lvl = fishtextview3.getText().toString();
			  /*TextView tv2 = (TextView) arg1.findViewById(R.id.content_id);*/

				PopOptions(gid,nm,lvl);
				
				 return true;
			}
		});
		
		 getSpecs();
		
		return rootView;

	}
	
	
	 public void getSpecs()
	 {
		users.clear();
		final Cursor c=DBHelper.getData(getActivity().getApplicationContext(),Utils.bps," where type=1 and userid='"+Utils.getDefaults("user_id", getActivity().getApplicationContext())+"'");
 		if(c.getCount()==0)
 		{
          return;
 		}
 		else 
 		{
 		
 			while(c.moveToNext())
     		{
           String t="";
		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(4).toUpperCase(Locale.getDefault()));
		    switch(Integer.parseInt(c.getString(2)))
		    {
		    case 1:t="Product";break;
		    case 2:t="Length";break;
		    case 3:t="Breadth";break;
		    case 4:t="Thickness";break;
		    case 5:t="Weight";break;
		    case 6:t="Variant";break;
		    }
		    hm.put("type",t);
		    hm.put("content_id",c.getString(3));
            users.add(hm);
             }
     		
     		 String[] from = { "uname","type", "content_id" };
             int[] to = {R.id.u_title,R.id.u_type,R.id.content_id};
             SimpleAdapter adapter = new SimpleAdapter(getActivity(), users,R.layout.list_layout2 , from, to); 
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
    	             Intent intent = new Intent(getActivity(), ProductDetails.class);
                     intent.putExtra("id", fieldname);
                     startActivity(intent); 
                  
                 }
                 });
     	
 		}
	 }

    

	class SomeObject {
		public String name;

		public SomeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	
	
	
	private void PopOptions(final String catID,final String name,final String lvl) {
		
	
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());
	    builderSingle.setIcon(R.drawable.ic_launcher);
	    builderSingle.setTitle("Options");
	    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	    		getActivity(),
	            android.R.layout.select_dialog_singlechoice);
	  
	    
	    arrayAdapter.add("Delete");
	    arrayAdapter.add("Edit");
	    arrayAdapter.add("Add Child");
	    
	   // if(lvl.equals("4")){
	    arrayAdapter.add("Add Product");
	    arrayAdapter.add("Add Length");
	    arrayAdapter.add("Add Breadth");
	    arrayAdapter.add("Add Thickness");
	    arrayAdapter.add("Add Weight");
	    arrayAdapter.add("Add Variant");
	 //   }
	   
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
	           		 if(Utils.isConnectingToInternet(getActivity().getApplicationContext())==true){
	                    if(strName.equals("Add Child")){
	                 	int lv=Integer.parseInt(lvl)+1;
	                    	if(lv<=4)dialogDetails(catID,lv+"","1","");
	                    	else Toast.makeText(getActivity().getApplicationContext(), "Maximum Subs Added", Toast.LENGTH_SHORT).show();
	           			 
	           		    }
	                    else if(strName.equals("Add Product"))
	                    {
	                    	dialogDetails(catID,"","3","1");	
	                    }
	                    else if(strName.equals("Add Length"))
	                    {
	                    	dialogDetails(catID,"","3","2");
	                    }
	                    else if(strName.equals("Add Breadth"))
	                    {
	                    	dialogDetails(catID,"","3","3");
	                    }
	                    else if(strName.equals("Add Thickness"))
	                    {
	                    	dialogDetails(catID,"","3","4");
	                    }
	                    else if(strName.equals("Add Weight"))
	                    {
	                    	dialogDetails(catID,"","3","5");
	                    }
	                    else if(strName.equals("Add Variant"))
	                    {
	                    	dialogDetails(catID,"","3","6");
	                    }
	                    	
	                    else if(strName.equals("Delete"))
	                    {	
	                    	del(catID);
	                    }
	                    else if(strName.equals("Edit")){
	                    	dialogDetails(catID,"2","2",name);
	                    }
	                    else  dialog.dismiss();
	                }
	           		else Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	                    
	                }
	            });
	    
	    builderSingle.show();
	}

	public void del(final String catID)
	{
		new AlertDialog.Builder(getActivity())
		.setMessage("Do you really want to Delete This Category?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getActivity().getApplicationContext())){ 

               	  httpGetAsyncTask = new HttpGetAsyncTask(getActivity(),3);
               	  httpGetAsyncTask.delegate=AddCategory.this;
               	 httpGetAsyncTask.execute(Utils.DelCat+Utils.getDefaults("user_id", getActivity().getApplicationContext())+"/"+catID);

		    	 }else 
		    	 Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	
	
public void dialogDetails(final String parent,final String level,final String action,final String name) {
	

      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
      			getActivity());
          builderSingle.setIcon(R.drawable.ic_launcher);
          LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
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
                    		  Toast.makeText(getActivity().getApplicationContext(), "Oops! Empty Name Try Again!", Toast.LENGTH_SHORT).show();	  
                      else{
                    	 	String[] data={
                        			userInput.getText().toString(),parent,level,name
                        	};
                        	try {
								storeCats(data,action);
								
								
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                          dialog.dismiss();
                    	  } 
                      }
                  });

     
          builderSingle.show();
      }

public void tabHost(int tb,View v)
{
	 TabHost tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
	 tabHost.setup();
	 final TabWidget tabWidget = tabHost.getTabWidget();
	 final FrameLayout tabContent = tabHost.getTabContentView();
	 // Get the original tab textviews and remove them from the viewgroup.
	 TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
	 for (int index = 0; index < tabWidget.getTabCount(); index++) {
	 originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
	 }
	 tabWidget.removeAllViews();
	 // Ensure that all tab content childs are not visible at startup.
	 for (int index = 0; index < tabContent.getChildCount(); index++) {
	 tabContent.getChildAt(index).setVisibility(View.GONE);
	 }
	
	 for (int index = 0; index < originalTextViews.length; index++) {
	 final TextView tabWidgetTextView = originalTextViews[index];
	 final View tabContentView = tabContent.getChildAt(index);
	 TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
	 tabSpec.setContent(new TabContentFactory() {
	 @Override
	 public View createTabContent(String tag) {
	 return tabContentView;
	 }
	 });
	 if (tabWidgetTextView.getBackground() == null) {
	 tabSpec.setIndicator(tabWidgetTextView.getText());
	 } else {
	 tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
	 }
	 tabHost.addTab(tabSpec);
	 tabHost.setCurrentTab(tb);
	 }
}


private void storeCats(String[] data2,String action) throws UnsupportedEncodingException, InterruptedException, ExecutionException
{
	
  	try {
  		data.put("user_id", Utils.getDefaults("user_id", getActivity().getApplicationContext()));
		data.put("name", data2[0]);
		data.put("parent",data2[1]);
		data.put("level",data2[2]);
		data.put("stp",data2[3]);
		data.put("type",action);
  if(Utils.isConnectingToInternet(getActivity().getApplicationContext()))	{
	  httpGetAsyncTask = new HttpGetAsyncTask(getActivity(),3);
  httpGetAsyncTask.delegate=this;
  httpGetAsyncTask.execute(Utils.StoreCat+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
  }else
  	Toast.makeText(getActivity().getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();

  } catch (JSONException e) { e.printStackTrace();}
}

@Override
public void processFinish(String output) {
	try {
			//
			jsonObj = new JSONObject(output);	
			  JSONArray Data = jsonObj.getJSONArray("categories");	
			  JSONArray Data2 = jsonObj.getJSONArray("specs");	
			 
				DBHelper.storecategory(getActivity().getApplicationContext(),Data);
				DBHelper.storeProducts(getActivity().getApplicationContext(),Data2);

				//((NLevelAdapter)this.listView.getAdapter()).notifyDataSetChanged();
				((B2BDashboard)getActivity()).displayView(5);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//
}
}
