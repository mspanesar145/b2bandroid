package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditProfile extends Activity  implements AsyncResponse{
	
	EditText e_edit1;
	EditText e_edit2;
	EditText e_edit4;
	EditText e_edit5;
	EditText e_edit6;
	EditText e_edit7;
	EditText e_edit8;
	Spinner e_edit9;
	Spinner e_edit10;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	HttpGetAsyncTask httpGetAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_edit_profile);
		Utils.setActionBar(this,"Edit Profile");
		DBHelper.loaddb(getApplicationContext());
		
		e_edit1=(EditText)findViewById(R.id.e_edit1);
		e_edit2=(EditText)findViewById(R.id.e_edit2);
		e_edit4=(EditText)findViewById(R.id.e_edit4);
		e_edit5=(EditText)findViewById(R.id.e_edit5);
		e_edit6=(EditText)findViewById(R.id.e_edit6);
		e_edit7=(EditText)findViewById(R.id.e_edit7);
		e_edit8=(EditText)findViewById(R.id.e_edit8);
		e_edit9=(Spinner)findViewById(R.id.e_edit9);
		e_edit10=(Spinner)findViewById(R.id.e_edit10);
		
		ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Utils.country);
		spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		e_edit10.setAdapter(spinnerArrayAdapter3);
		spinnerArrayAdapter3.notifyDataSetChanged();
		
		e_edit10.setSelection(spinnerArrayAdapter3.getPosition("India"));
		final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, Utils.states);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		e_edit9.setAdapter(spinnerArrayAdapter);
		spinnerArrayAdapter.notifyDataSetChanged();
		
		final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, Utils.states2);
		spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		
		Cursor c=DBHelper.getData(getApplicationContext(),Utils.usr,"");
         if(c.moveToFirst())
         {
        	 e_edit1.setText(c.getString(1));
        	 e_edit2.setText(c.getString(19));
        	 e_edit4.setText(c.getString(17));
        	 e_edit5.setText(c.getString(5));
        	 e_edit6.setText(c.getString(18));
        	 e_edit7.setText(c.getString(6));
        	 e_edit8.setText(c.getString(12));
        	 e_edit9.setSelection(spinnerArrayAdapter.getPosition(c.getString(13)));
        	 e_edit10.setSelection(spinnerArrayAdapter3.getPosition("India"));

         }
		
	}
	
	public void action(View v) throws UnsupportedEncodingException
	{
		if(v.getId()==R.id.btnEdit)
		{
			 httpGetAsyncTask = new HttpGetAsyncTask(EditProfile.this,3);
		        httpGetAsyncTask.delegate=EditProfile.this;
		        try {
					data.put("full_name", e_edit1.getText().toString());
					data.put("company", e_edit2.getText().toString());
					data.put("sec_email", e_edit4.getText().toString());
					data.put("mobile",e_edit5.getText().toString());
					data.put("sec_mobile",e_edit6.getText().toString());
					data.put("address",e_edit7.getText().toString());
					data.put("city",e_edit8.getText().toString());
					data.put("state",e_edit9.getSelectedItem().toString());
					data.put("country",e_edit10.getSelectedItem().toString());
					data.put("user_id",Utils.getDefaults("user_id", getApplicationContext()));

		        
		        if(Utils.isConnectingToInternet(getApplicationContext()))
		        {
		        	
		        	httpGetAsyncTask.execute(Utils.updateProfile+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
		        }
		        else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
		        }
		
        catch (JSONException e) { e.printStackTrace();}
    	 
		}
		
	}
	/*
	 @Override
		public void onBackPressed() {
		 Intent intent = new Intent(this, MyProfile.class);
         startActivity(intent);
         finish();
		}
		*/

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
		  try {
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("user");
	  		 DBHelper.UpdateProfile(getApplicationContext(),Data);
	  		Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
		      } catch (JSONException e) {
			
			     e.printStackTrace();
			
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
