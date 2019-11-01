package com.denchion.b2b;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity  implements AsyncResponse{

	EditText Email;
	EditText Npwd;
	EditText Rpwd;
	EditText Code;
	Button verify;
	Button codebtn;
	Button  reset;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_forgot_password);
		Utils.setActionBar(this,"Forgot Password");
		DBHelper.loaddb(getApplicationContext());
		Email=(EditText)findViewById(R.id.etEmail);
		Npwd=(EditText)findViewById(R.id.etNpwd);
		Rpwd=(EditText)findViewById(R.id.etRPwd);
		Code=(EditText)findViewById(R.id.etCode);
		codebtn=(Button)findViewById(R.id.btnSendCode);
		verify=(Button)findViewById(R.id.btnVerify);
		reset=(Button)findViewById(R.id.btnReset);
		
	}
	
	public void action(View v)
	{
		HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask(this,3); 
		switch(v.getId())
		{
		case R.id.btnSendCode : 
    	  try {
    		  int code =Utils.rand();
				data.put("email", Email.getText().toString());
				data.put("code", code+"");
				 Utils.setDefaults("vemail", Email.getText().toString()+"", getApplicationContext());
	  httpGetAsyncTask.delegate = this;
	  if(Utils.isConnectingToInternet(getApplicationContext()))
	  httpGetAsyncTask.execute(Utils.forgotPwd+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
	  else
		  Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
	
		
		  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
		
		case R.id.btnVerify : 
			if(Code.getText().toString().equals(""))Code.setError("Please Enter Verification Code!");
			else if(Utils.getDefaults("code", getApplicationContext()).equals(Code.getText().toString()))
			{
				 Code.setVisibility(View.GONE);
				 verify.setVisibility(View.GONE);
				 Npwd.setVisibility(View.VISIBLE);
				 Rpwd.setVisibility(View.VISIBLE);
				 reset.setVisibility(View.VISIBLE);
				
			}else 	Toast.makeText(getApplicationContext(), "Incorrect Verification Code !", Toast.LENGTH_SHORT).show();
				
		break;
			
			
		case R.id.btnReset : 
			if(Npwd.getText().toString().equals(""))Npwd.setError("Please Enter New Password");
			else if(!Npwd.getText().toString().equals(Rpwd.getText().toString()))
				Npwd.setError("Password not Matched");
			else
			{
			  try {
					data.put("email", Utils.getDefaults("vemail", getApplicationContext()));
					data.put("pwd",Npwd.getText().toString());
					
      	  httpGetAsyncTask.delegate = this;
      	  if(Utils.isConnectingToInternet(getApplicationContext()))
      	  httpGetAsyncTask.execute(Utils.resetPwd+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
      	  else
	        	Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		
			
			  } catch (JSONException e) { e.printStackTrace();} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			  break;
			  
		}
	}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		if(output.equals("2"))
		{
			Email.setError("Email ID Not Exists!");
		}
		else if(output.equals("3"))
		{
			Toast.makeText(getApplicationContext(), "Password Reset Successfully ! Please Login", Toast.LENGTH_SHORT).show();
			jump();
		}
		else
		{
			  Toast.makeText(getApplicationContext(), "Verification Code Sent to your Email Address", Toast.LENGTH_SHORT).show();
			 Utils.setDefaults("code", output+"", getApplicationContext());
			 
			
			 Email.setVisibility(View.GONE);
			 codebtn.setVisibility(View.GONE);
			 Code.setVisibility(View.VISIBLE);
			 verify.setVisibility(View.VISIBLE);
		}
	}
	
	public void jump()
	{
	    Intent loginIntent = new Intent(this, B2BLogin.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(loginIntent);
	        finish();
	}
}
