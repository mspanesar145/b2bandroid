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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class B2BRegister extends Activity implements AsyncResponse{
	
	    JSONObject data = new JSONObject();
		JSONObject jsonObj;
		EditText reg_edit1;
		EditText reg_edit2;
		EditText reg_edit2a;
		EditText reg_edit3;
		EditText reg_edit3a;
		EditText reg_edit4;
		EditText reg_edit4a;
		EditText reg_edit5;
		EditText reg_edit6;
		EditText reg_edit7;
		EditText reg_edit8;
		EditText reg_edit9;
		Spinner reg_edit10;
		Spinner reg_edit10a;
		HttpGetAsyncTask httpGetAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.b2b_register);
		//Utils.setActionBar(this,"Sign Up");
		DBHelper.loaddb(getApplicationContext());
		reg_edit1=(EditText)findViewById(R.id.reg_edit1);
		reg_edit2=(EditText)findViewById(R.id.reg_edit2);
		reg_edit2a=(EditText)findViewById(R.id.reg_edit2a);
		reg_edit3=(EditText)findViewById(R.id.reg_edit3);
		reg_edit3a=(EditText)findViewById(R.id.reg_edit3a);
		reg_edit4=(EditText)findViewById(R.id.reg_edit4);
		reg_edit4a=(EditText)findViewById(R.id.reg_edit4a);
		reg_edit5=(EditText)findViewById(R.id.reg_edit5);
		reg_edit6=(EditText)findViewById(R.id.reg_edit6);
		reg_edit7=(EditText)findViewById(R.id.reg_edit7);
		reg_edit8=(EditText)findViewById(R.id.reg_edit8);
		reg_edit9=(EditText)findViewById(R.id.reg_edit9);
		reg_edit10=(Spinner)findViewById(R.id.reg_edit10);
		reg_edit10a=(Spinner)findViewById(R.id.reg_edit10a);
		
		ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Utils.country);
		spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		reg_edit10a.setAdapter(spinnerArrayAdapter3);
		spinnerArrayAdapter3.notifyDataSetChanged();
		reg_edit10a.setSelection(spinnerArrayAdapter3.getPosition("India"));
		final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, Utils.states);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		reg_edit10.setAdapter(spinnerArrayAdapter);
		spinnerArrayAdapter.notifyDataSetChanged();
		
		final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, Utils.states2);
		spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		
		reg_edit10a.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	String text = reg_edit10a.getSelectedItem().toString();
		    	if(!text.equals("India"))
		    	{
		    		reg_edit10.setAdapter(spinnerArrayAdapter2);
		    		spinnerArrayAdapter2.notifyDataSetChanged();
		    	}
		    	else
		    	{
		    		reg_edit10.setAdapter(spinnerArrayAdapter);
		    		spinnerArrayAdapter.notifyDataSetChanged();
		    	}
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		reg_edit2.addTextChangedListener(new TextWatcher() {

		    public void afterTextChanged(Editable s) {

		    	String result = s.toString().replaceAll(" ", "");
		        if (!s.toString().equals(result)) {
		        	reg_edit2.setText(result);
		        	reg_edit2.setSelection(result.length());
		             // alert the user
		        }
		                  }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		    public void onTextChanged(CharSequence s, int start, int before, int count) {}
		        });
	}
	
	
	public void action(View v) throws UnsupportedEncodingException, InterruptedException, ExecutionException
	{
		switch(v.getId())
		{
		
		case R.id.btnReg:
			
	        if(reg_edit1.getText().toString().equals("")){reg_edit1.setEnabled(true);reg_edit1.requestFocus();reg_edit1.setError("Oops! Full Name Missing");}
	          else if(reg_edit2.getText().toString().equals("")){reg_edit2.setEnabled(true);reg_edit1.requestFocus();reg_edit2.setError("Oops! Username Missing");}
	          else if(reg_edit2a.getText().toString().equals("")){reg_edit2a.setEnabled(true);reg_edit2a.requestFocus();reg_edit2a.setError("Oops! Company Name Missing");}
	          else if(reg_edit3.getText().toString().equals("")){reg_edit3.setEnabled(true);reg_edit3.requestFocus();reg_edit3.setError("Oops! Email ID Missing");}
	          else if(Utils.isValidEmail(reg_edit3.getText().toString())==false){reg_edit3.setEnabled(true);reg_edit3.requestFocus();reg_edit3.setError("Oops! Email ID Invalid");}
	          else if(reg_edit4.getText().toString().equals("")){reg_edit4.setEnabled(true);reg_edit4.requestFocus();reg_edit4.setError("Oops! Mobile No. Missing");}
	          else if(reg_edit4.getText().toString().length() < 10){reg_edit4.setEnabled(true);reg_edit4.requestFocus();reg_edit4.setError("Oops! Invalid Mobile Number");}
	          else if(reg_edit7.getText().toString().equals("")){reg_edit7.setEnabled(true);reg_edit7.requestFocus();reg_edit7.setError("Oops! Password Missing");}
	          else if(reg_edit8.getText().toString().equals("")){reg_edit8.setEnabled(true);reg_edit8.requestFocus();reg_edit8.setError("Oops! Please confirm password");}
	          else if(!reg_edit8.getText().toString().equals(reg_edit7.getText().toString())){reg_edit8.setEnabled(true);reg_edit8.requestFocus();reg_edit8.setError("Oops! Password not matched");}
	          else
	          {
                int code =Utils.rand();
				try {
					data.put("full_name", reg_edit1.getText().toString());
					data.put("username",reg_edit2.getText().toString());
					data.put("company",reg_edit2a.getText().toString());
					data.put("email", reg_edit3.getText().toString());
					data.put("sec_email", reg_edit3a.getText().toString());
					data.put("password", reg_edit7.getText().toString());
					data.put("mobile", reg_edit4.getText().toString());
					data.put("sec_mobile", reg_edit4a.getText().toString());
					data.put("address", reg_edit5.getText().toString());
					data.put("reference",reg_edit6.getText().toString());
					data.put("city",reg_edit9.getText().toString());
					data.put("state",reg_edit10.getSelectedItem().toString());
					data.put("country",reg_edit10a.getSelectedItem().toString());
					data.put("code",code+"");
					httpGetAsyncTask = new HttpGetAsyncTask(B2BRegister.this,3); 
			  	    httpGetAsyncTask.delegate = this;
				    String senddata=Utils.UrlRegister+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8");
				
				    if(Utils.isConnectingToInternet(getApplicationContext()))
					  httpGetAsyncTask.execute(senddata);
				    else
			          Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
				}
		        catch (JSONException e) { e.printStackTrace();}
	        	  }
	        break;
	         }
		}
	


	public void jump(int type)
	{
		Intent loginIntent=null;
		switch(type)
		{
		case 1: loginIntent = new Intent(this, B2BLogin.class);break;
		case 2:
			loginIntent = new Intent(this, Verify.class);
			loginIntent.putExtra("uname",reg_edit2.getText().toString());
			break;
		}
		 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         this.startActivity(loginIntent);
         finish();
	}

	@Override
	public void processFinish(String output) {
	// TODO Auto-generated method stub
		if(output.equals("1"))
			{
			reg_edit2.setEnabled(true);reg_edit2.requestFocus();reg_edit2.setError("Oops! Username Already Exists!");
			}
		else if(output.equals("2"))
			{
			reg_edit3.setEnabled(true);reg_edit3.requestFocus();reg_edit3.setError("Oops! Email Already Exists");
			}
		else {
			Utils.setDefaults("code", output, getApplicationContext());
		     jump(2);
		 }
		
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
	                finish();
	                break;
	     }
	     return (super.onOptionsItemSelected(menuItem));
	 }

	}
 

