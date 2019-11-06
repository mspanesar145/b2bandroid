package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class B2BLogin extends Activity implements AsyncResponse{
	
	EditText etUserName;
	EditText etPass;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	HttpGetAsyncTask httpGetAsyncTask;
	AsyncResponse delegate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_b2_blogin);
		//Utils.setActionBar(getApplicationContext(),"Login");
		DBHelper.loaddb(getApplicationContext());
        DBHelper.createTbls(getApplicationContext());
		etUserName=(EditText)findViewById(R.id.etUserName);
		etPass=(EditText)findViewById(R.id.etPass);
	}
	

	public void action(View v) throws UnsupportedEncodingException, InterruptedException, ExecutionException
	{
		switch(v.getId())
		{
		case R.id.btnSingIn:
			
			  if(etUserName.getText().toString().equals("")){etUserName.setEnabled(true);etUserName.requestFocus();etUserName.setError("Oops! Full Name Missing");}
	          else if(etPass.getText().toString().equals("")){etPass.setEnabled(true);etPass.requestFocus();etPass.setError("Oops! Username Missing");}
	          else
	          {
	        	  int code =Utils.rand();
	        	  try {
						data.put("username", etUserName.getText().toString());
						data.put("password",etPass.getText().toString());
						data.put("code",code+"");
						  Utils.setDefaults("code", code+"", getApplicationContext());
	        	  HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask(this,3); 
	        	  httpGetAsyncTask.delegate = this;
	        	  System.out.println(Utils.UrlLogin+data.toString());
	        	  if(Utils.isConnectingToInternet(getApplicationContext()))
		        	  httpGetAsyncTask.execute(Utils.UrlLogin+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	        	  else
	  	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	        	  
	        	
	        	  } catch (JSONException e) { e.printStackTrace();}
	        	  
	          }
	          
		break;
		case R.id.btnreg:
			jump(1);
		break;
		
		case R.id.btnfpwd:
			jump2();
		break;
		      }
	}
	
	public void jump(int type)
	{
		Intent loginIntent=null;
		switch(type)
		{
		case 1: loginIntent = new Intent(this, B2BRegister.class);break;
		case 2: loginIntent = new Intent(this, B2BDashboard.class);break;
		}
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         this.startActivity(loginIntent);
         finish();
	}
	
	public void jump2()
	{
		Intent loginIntent=null;
        loginIntent = new Intent(this, ForgotPassword.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         this.startActivity(loginIntent);
 
	}
	

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		if(output.equals("1")){
			etPass.setEnabled(true);
  	    	etPass.requestFocus();
  	    	etPass.setError("Oops! Incorrect Username or Password");
		} else if(output.equals("2")) {

			Intent  loginIntent = new Intent(this, Verify.class);
		 	loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 	loginIntent.putExtra("uname",etUserName.getText().toString());
         	this.startActivity(loginIntent);
         	finish();
  	  } else {
  		Utils.doDownload("profile/","user"+Utils.getDefaults("user_id",getApplicationContext())+".jpg");
  		  try {
			jsonObj = new JSONObject(output);
			
	  		DBHelper.StoreLogin(jsonObj,getApplicationContext());
	  		  jump(2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
  		  
  		  
  	  }
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
