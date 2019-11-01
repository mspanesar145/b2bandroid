package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("InflateParams") 
public class EditSub extends Activity implements AsyncResponse{

	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	private EditText sub_1;
	private EditText sub_3;
	private EditText sub_4;
	private EditText sub_5;
	private EditText sub_6;
	private CheckBox chk1,chk2,chk3,chk4,chk5,chk6;
	String permission;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
         setContentView(R.layout.gb_edit_sub);
 		 Utils.setActionBar(this,"Sub Users");
 		 DBHelper.loaddb(getApplicationContext());
 		Bundle bundle = getIntent().getExtras();
		 id = bundle.getString("id");
 		 sub_1=(EditText)findViewById(R.id.e_sub_1);
 		 sub_3=(EditText)findViewById(R.id.e_sub_3);
 		 sub_4=(EditText)findViewById(R.id.e_sub_4);
 		 sub_5=(EditText)findViewById(R.id.e_sub_5);
 		 sub_6=(EditText)findViewById(R.id.e_sub_6);
 		 
 		chk1=(CheckBox)findViewById(R.id.chk1);
 		chk2=(CheckBox)findViewById(R.id.chk2);
 		chk3=(CheckBox)findViewById(R.id.chk3);
 		chk4=(CheckBox)findViewById(R.id.chk4);
 		chk5=(CheckBox)findViewById(R.id.chk5);
 		chk6=(CheckBox)findViewById(R.id.chk6);
 		
 		
 		chk1.setOnCheckedChangeListener(new OnCheckedChangeListener(){

 		    @Override
 		    public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {                   
 		      if (isChecked){
 		         chk5.setEnabled(true); // disable checkbox 
 		      }else{
 		    	 chk5.setEnabled(false); // disable checkbox  
 		      }
 		    }

			
 		});
 		
 		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.bsu," where user_id='"+id+"'");
 	 	
 		if(c.moveToFirst())
 		{
 			sub_1.setText(c.getString(1));
 			sub_3.setText(c.getString(3));
 			sub_4.setText(c.getString(4));
 			sub_5.setText(c.getString(5));
 			sub_6.setText(c.getString(6));
 			
 			String []chk=c.getString(7).split("[ ]");
			if(Arrays.asList(chk).contains("1")){chk1.setChecked(true);chk5.setEnabled(true);}
			if(Arrays.asList(chk).contains("2"))chk2.setChecked(true);
			if(Arrays.asList(chk).contains("3"))chk3.setChecked(true);
			if(Arrays.asList(chk).contains("4"))chk4.setChecked(true);
			if(Arrays.asList(chk).contains("5"))chk5.setChecked(true);
			if(Arrays.asList(chk).contains("6"))chk6.setChecked(true);
 		}

	}
	
	
	 public void EditSubUser(View v) throws UnsupportedEncodingException
	 {
		 String chkstr1="";
		 String chkstr2="";
		 String chkstr3="";
		 String chkstr4="";
		 String chkstr5="";
		 String chkstr6="";
		 
		 if(chk1.isChecked())chkstr1="1";
		 if(chk2.isChecked())chkstr2="2";
		 if(chk3.isChecked())chkstr3="3";
		 if(chk4.isChecked())chkstr4="4";
		 if(chk5.isChecked())chkstr5="5";
		 if(chk6.isChecked())chkstr6="6";
	
		 
		 permission=chkstr1+" "+chkstr2+" "+chkstr3+" "+chkstr4+" "+chkstr5+" "+chkstr6;
		 
		 if(sub_1.getText().toString().equals("")){sub_1.setEnabled(true);sub_1.requestFocus();sub_1.setError("Please Enter Full Name !");}
		 else if(sub_3.getText().toString().equals("")){sub_3.setEnabled(true);sub_3.requestFocus();sub_3.setError("Please Enter Password !");}
		 else if(sub_4.getText().toString().equals("")){sub_4.setEnabled(true);sub_4.requestFocus();sub_4.setError("Please Enter Mobile !");}
		 else if(sub_4.getText().toString().length() < 10){sub_4.setEnabled(true);sub_4.requestFocus();sub_4.setError("Oops! Invalid Mobile Number");}
		 else if(sub_5.getText().toString().equals("")){sub_5.setEnabled(true);sub_5.requestFocus();sub_5.setError("Please Enter Designation !");}
		 else if(sub_6.getText().toString().equals("")){sub_6.setEnabled(true);sub_6.requestFocus();sub_6.setError("Please Enter Department !");}
		 else if(!chk1.isChecked() && !chk2.isChecked() && !chk3.isChecked() && !chk4.isChecked() && !chk5.isChecked() && !chk6.isChecked()){Toast.makeText(getApplicationContext(), "Please Select one Permisison", Toast.LENGTH_SHORT).show();}
		 else {
		 try {
			    data.put("subid",id);
				data.put("full_name", sub_1.getText().toString());
				data.put("password", sub_3.getText().toString());
				data.put("mobile", sub_4.getText().toString());
				data.put("designation", sub_5.getText().toString());
				data.put("department",sub_6.getText().toString());
				data.put("permission",permission);
				httpGetAsyncTask = new HttpGetAsyncTask(EditSub.this,3); 
		  	    httpGetAsyncTask.delegate = this;
			    String senddata=Utils.editSubUsers+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8");
			
			    if(Utils.isConnectingToInternet(getApplicationContext()))
				  httpGetAsyncTask.execute(senddata);
			    else
		          Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
			
			}
	        catch (JSONException e) { e.printStackTrace();}
		 }
     	  }
		 
	
	 
	@Override
	public void processFinish(String output) {

	jump();
	
	}
	
	public void jump()
	{
	    Intent loginIntent = new Intent(this, AddSubUser.class);
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(loginIntent);
	        finish();
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
        
