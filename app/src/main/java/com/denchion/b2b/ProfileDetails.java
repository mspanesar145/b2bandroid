package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams") public class ProfileDetails extends Activity implements AsyncResponse{
	
	 TextView txt1;
	 TextView txt1a;
	 TextView txt3;
	 TextView txt3a;
	 TextView txt4;
	 TextView txt4a;
	 TextView txt5;
	 TextView txt6;
	 TextView txt8;
	 TextView txt9;
	 TextView txt10;
	 ImageView Image;
	 String uid;
	 String uname;
	 String tp;
	 Button btnrequest;
	 Button btncancel;
	 Button reject;
	 Button removep;
	 Button acceptr;
	 Button vproduct;
	 Button vcredit;
	 JSONObject data = new JSONObject();
	 JSONObject jsonObj;
	 AsyncResponse delegate = null;
	 HttpGetAsyncTask httpGetAsyncTask;
	 RelativeLayout udetailrow7a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_profile_details);
		Bundle bundle = getIntent().getExtras();
		uid   = bundle.getString("id");
		uname = bundle.getString("uname");
		tp    = bundle.getString("tp");
		Utils.setActionBar(this,uname);
		DBHelper.loaddb(getApplicationContext());

		
	         txt1=(TextView)findViewById(R.id.udetailtxt1);
	         txt1a=(TextView)findViewById(R.id.udetailtxt1a);
	         txt3=(TextView)findViewById(R.id.udetailtxt3);
	         txt3a=(TextView)findViewById(R.id.udetailtxt3a);
	         txt4=(TextView)findViewById(R.id.udetailtxt4);
	         txt4a=(TextView)findViewById(R.id.udetailtxt4a);
	         txt5=(TextView)findViewById(R.id.udetailtxt5);
	         txt6=(TextView)findViewById(R.id.udetailtxt6);
	         txt8=(TextView)findViewById(R.id.udetailtxt5a);
	         txt9=(TextView)findViewById(R.id.udetailtxt5b);
	         txt10=(TextView)findViewById(R.id.udetailtxt7a);
	         
	         udetailrow7a=(RelativeLayout)findViewById(R.id.udetailrow7a);
	         
	         
	         
	         btnrequest=(Button)findViewById(R.id.btnrequest);
	         btncancel=(Button)findViewById(R.id.btncancel);
	         reject=(Button)findViewById(R.id.reject);
	         removep=(Button)findViewById(R.id.removep);
	         acceptr=(Button)findViewById(R.id.acceptr);
	         vproduct=(Button)findViewById(R.id.btnProducts);
	         vcredit=(Button)findViewById(R.id.btnCredit);
	         Cursor c=null;
	         if(tp.equals("0"))c=DBHelper.getData(getApplicationContext(),Utils.seasrchusr," where user_id='"+uid+"'");
	         else c=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id='"+uid+"'");
	         if(c.moveToFirst())
	         {
	        	ImageView imgView = (ImageView) findViewById(R.id.imglogo);
	 	 	 	File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+c.getString(0)+".jpg");
	 	  	    Picasso.with(getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgView);
	        	 txt1.setText(c.getString(1));
	        	 txt1a.setText(c.getString(14));
	        	 txt3.setText(c.getString(2));
	        	 txt3a.setText(c.getString(12));
	        	 txt4.setText(c.getString(3));
	        	 txt4a.setText(c.getString(13));
	        	 txt5.setText(c.getString(4));
	        	 txt6.setText(c.getString(15));
	        	 txt8.setText(c.getString(8));
	        	 txt9.setText(c.getString(9));
	        	
	        	
	         } 
	        
	         vis(Integer.parseInt(tp));
	         
	         txt10.setText(DBHelper.getCreditFromSender(getApplicationContext(),uid)+" Days");
	}
	
	public void vis(int t)
	{
		 switch(t)
    	 {
    	 case 4: 
    		 acceptr.setVisibility(View.GONE);
    		 btnrequest.setVisibility(View.VISIBLE);
    		 removep.setVisibility(View.GONE);
    		 reject.setVisibility(View.GONE);
    		 vproduct.setVisibility(View.GONE);
    		 vcredit.setVisibility(View.GONE);
    		 btncancel.setVisibility(View.GONE);
    		 break;
    	 case 3: 
    		 acceptr.setVisibility(View.GONE);
    		 btnrequest.setVisibility(View.GONE);
    		 removep.setVisibility(View.GONE);
    		 vproduct.setVisibility(View.GONE);
    		 vcredit.setVisibility(View.GONE);
    		 reject.setVisibility(View.GONE);
    		 btncancel.setVisibility(View.VISIBLE);
    		 break;
    	 case 1: 
    		 acceptr.setVisibility(View.GONE);
    		 btnrequest.setVisibility(View.GONE);
    		 btncancel.setVisibility(View.GONE);
    		 removep.setVisibility(View.VISIBLE);
    		 vproduct.setVisibility(View.VISIBLE);
    		 vcredit.setVisibility(View.VISIBLE);
    		 reject.setVisibility(View.GONE);
    		 udetailrow7a.setVisibility(View.VISIBLE);
    		 break;
    	 case 2: 
    		 btnrequest.setVisibility(View.GONE);
    		 btncancel.setVisibility(View.GONE);
    		 removep.setVisibility(View.GONE);
    		 vproduct.setVisibility(View.GONE);
    		 vcredit.setVisibility(View.GONE);
    		 reject.setVisibility(View.VISIBLE);
    		 acceptr.setVisibility(View.VISIBLE);
    		 break;
    		 
    	 case 0: 
    		 Cursor c=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	    	if(c.moveToFirst())
    	    	{
    		  String []chk1=c.getString(14).split("[,]");
    		  String []chk2=c.getString(15).split("[,]");
    		  String []chk3=c.getString(16).split("[,]");
	        	 if (Arrays.asList(chk1).contains(uid)) 
	        	 {
	        		 vis(3);
	    		 }
	        	 else if (Arrays.asList(chk2).contains(uid)) 
	        	 {
	        		 vis(2);
	    		 }
	        	 else if (Arrays.asList(chk3).contains(uid)) 
	        	 {
	        		 vis(1);
	    		 }
	        	 
    	    	 }
    		 break;
    	 }
	}
	
	public void action2(View v)
	{
		switch(v.getId())
		{
		case R.id.btnProducts:
		  Intent loginIntent = new Intent(this, UserProducts.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 loginIntent.putExtra("id",uid);
			 this.startActivity(loginIntent);
		break;
		case R.id.btnCredit:
			dialogDetails();
		break;
		}
	      
	}
	
	
	 public void dialogDetails() {
			

	      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	      			this);
	          builderSingle.setIcon(R.drawable.ic_launcher);
	          LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompts2b, null);
		          final EditText userInput = (EditText) promptsView
							.findViewById(R.id.UserInput);
		
	
	          builderSingle.setTitle("Set Credit Limit");
	          
	   
	        	  builderSingle.setView(promptsView);
	              
	        
	        	 
	         
	          builderSingle.setPositiveButton("SUBMIT",
	                  new DialogInterface.OnClickListener() {

	                      @Override
	                      public void onClick(DialogInterface dialog, int which) {
	                    	 if(userInput.getText().toString().equals(""))
	                    			Toast.makeText(getApplicationContext(), "Oops Enter Credit Limit! Try Again!", Toast.LENGTH_SHORT).show();
	                    	 else {
	                    		 try {
	                    	  		data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
	                    	  		data.put("partner", uid);
	                    	  		data.put("limit", userInput.getText().toString());
	     
	                    			 httpGetAsyncTask = new HttpGetAsyncTask(ProfileDetails.this,3);
	                    		        httpGetAsyncTask.delegate=ProfileDetails.this;
	                    		        if(Utils.isConnectingToInternet(getApplicationContext()))
	                    	         httpGetAsyncTask.execute(Utils.setCredit+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
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
	
	public void action(final View v)
	{
		String msg="";
		switch(v.getId())
		{
		
		case R.id.btnrequest:
			msg="Are you sure want to sent this Request";
			break;
			
		case R.id.btncancel:
			msg="Are you sure want to Cancel this Request";
			break;
			
		case R.id.reject:
			msg="Are you sure want to Reject this Request";
			break;
			
		case R.id.acceptr:
			msg="Are you sure want to Accept this Request";
			break;
			
		case R.id.removep:
			msg="Are you sure want to Remove this User";
			break;	
			
		
		}
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 if(Utils.isConnectingToInternet(getApplicationContext())){ 
		    			switch(v.getId())
		    			{
		    			
		    			case R.id.btnrequest:
		    				Request("1","");
		    				break;
		    				
		    			case R.id.btncancel:
		    				Request("2","");
		    				break;
		    				
		    			case R.id.reject:
		    				Request("3","");
		    				break;
		    				
		    			case R.id.acceptr:
		    				Request("4","");
		    				break;
		    				
		    			case R.id.removep:
		    				Request("5","");
		    				break;	
		    			}
		    	 }else 
		    	 Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	
	public void Request(String type,String gid)
	{
		try {
	  		 data.put("user_id", Utils.getDefaults("user_id",getApplicationContext()));
			 data.put("partner_id", uid);
			 data.put("type", type);
			 data.put("group", gid);
			
			 httpGetAsyncTask = new HttpGetAsyncTask(ProfileDetails.this,3);	
			 httpGetAsyncTask.delegate=this;
	         httpGetAsyncTask.execute(Utils.SendRequest+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	         switch(Integer.parseInt(type))
	         {
	         case 1: vis(3);break;
	         case 2: vis(4);break;
	         case 3: vis(4);break;
	         case 4: vis(1);break;
	         case 5: vis(4);break;
	         }
    	    }catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}

	/*@Override
	public void onBackPressed() {
		 Intent loginIntent = new Intent(this, Partners.class);
         loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         loginIntent.putExtra("tb",tp);
         this.startActivity(loginIntent);
         finish();
	}*/
	
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			jsonObj = new JSONObject(output);
			JSONArray Data  = jsonObj.getJSONArray("partners");	
			JSONArray Data2  = jsonObj.getJSONArray("user");
			JSONArray Data3  = jsonObj.getJSONArray("credit");
			DBHelper.storePartners(getApplicationContext(),Data);
			DBHelper.UpdateLogin(getApplicationContext(),Data2);
			DBHelper.storeCreditLimit(getApplicationContext(),Data3);
			finish();
			 startActivity(getIntent()); 
		} catch (JSONException e) {
		
			e.printStackTrace();
			
		}	
	}
	
	
	 @Override
		public void onBackPressed() {
	   super.onBackPressed();
	  Intent ap2 = new Intent(this, Partners.class);
			 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 ap2.putExtra("tb","0");
	        this.startActivity(ap2);
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
	        	 Intent loginIntent =null;
	        	 loginIntent = new Intent(this, Partners.class);
				 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 loginIntent.putExtra("tb","0");
		        this.startActivity(loginIntent);
		        finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

	
}
