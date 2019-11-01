package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Verify extends Activity implements AsyncResponse{
EditText vcode;
String uname;
HttpGetAsyncTask httpGetAsyncTask;
JSONObject data = new JSONObject();
AsyncResponse delegate = null;
JSONObject jsonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_verify);
		Utils.setActionBar(this,"Verification") ;
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		uname = bundle.getString("uname");
		vcode=(EditText)findViewById(R.id.verifyedit);
	
	}
	

	public void verify(View v)
	{
		if(Utils.isConnectingToInternet(getApplicationContext())){
			if(vcode.getText().toString().equals(""))vcode.setError("Please Enter Verification Code!");
			else if(Utils.getDefaults("code", getApplicationContext()).equals(vcode.getText().toString()))
			{
				 httpGetAsyncTask = new HttpGetAsyncTask(Verify.this,1); 
		  	     httpGetAsyncTask.delegate = Verify.this;
			     String senddata=Utils.chvrf+uname+"";
			     httpGetAsyncTask.execute(senddata);
			    
			}
			else vcode.setError("Invalid Verification Code!");
		}else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			
		}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "You Verified Please Login!", Toast.LENGTH_SHORT).show();
		/*  try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("user");
	  		  JSONObject jsonObject = Data.getJSONObject(0); 
	  		  DBHelper.StoreLogin(jsonObject,getApplicationContext());
		Intent loginIntent = new Intent(this, B2BDashboard.class);
          loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          this.startActivity(loginIntent);
          finish();
		  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	*/
		
		Intent loginIntent=null;
        loginIntent = new Intent(this, B2BLogin.class);
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         this.startActivity(loginIntent);
         finish();
 
	}
		
	
			
}
