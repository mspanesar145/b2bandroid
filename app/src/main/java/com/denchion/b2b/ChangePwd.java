package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePwd extends Activity implements AsyncResponse{
	
	EditText opwd;
	EditText npwd;
	EditText cpwd;
	JSONObject data = new JSONObject();
 	ProgressDialog dialog;
 	JSONObject jsonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_change_pwd);
		Utils.setActionBar(this,"Change Password");
		DBHelper.loaddb(getApplicationContext());
		opwd=(EditText)findViewById(R.id.pwdedit1);
		npwd=(EditText)findViewById(R.id.pwdedit2);
		cpwd=(EditText)findViewById(R.id.pwdedit3);

	}
	
	
	
	public void changepwd(View v) throws UnsupportedEncodingException, InterruptedException, ExecutionException
	{
	
		try {
			data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
			data.put("pwd", cpwd.getText().toString());
			data.put("oldpwd", opwd.getText().toString());
			data.put("subid", Utils.getDefaults("subid",getApplicationContext()));
			  if(opwd.getText().toString().equals(""))opwd.setError("Oops! Enter Old Password");
				else if(npwd.getText().toString().equals(""))npwd.setError("Oops! Enter New Password");
				else if(cpwd.getText().toString().equals(""))cpwd.setError("Oops! Please Confirm Password");
				else if(!cpwd.getText().toString().equals(npwd.getText().toString()))cpwd.setError("Oops! Password Not Match");
				else
				{
					  HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask(this,3); 
		        	  httpGetAsyncTask.delegate = this;
		        	  if(Utils.isConnectingToInternet(getApplicationContext())) httpGetAsyncTask.execute(Utils.chpwd+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
		        	  else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
				}
			  
		    }  catch (JSONException e) { e.printStackTrace();}

	}
	
	

public void jump()
  {
  	  Intent loginIntent = new Intent(this, B2BDashboard.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(loginIntent);
        finish();
  }

@Override
public void processFinish(String output) {
	// TODO Auto-generated method stub
	if(output.equals("1"))jump();
	else opwd.setError("Error Old Password is Wrong!");
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
