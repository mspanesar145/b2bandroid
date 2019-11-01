package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewPayment extends Activity implements AsyncResponse{
	
	HttpGetAsyncTask httpGetAsyncTask;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	Spinner sp_1;
	Spinner sp_2;
	EditText sp_3;
	EditText sp_4;
	String id;
	String tp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_new_payment);
		Utils.setActionBar(this,"Send Payment");
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		tp = bundle.getString("tp");
		
		sp_1=(Spinner)findViewById(R.id.sp_1);
		sp_2=(Spinner)findViewById(R.id.sp_2);
		sp_3=(EditText)findViewById(R.id.sp_3);
		sp_4=(EditText)findViewById(R.id.sp_4);
		
	
		
		ArrayAdapter<String> adpt1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		adpt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sp_1.setAdapter(adpt1);
		
		Cursor m=DBHelper.getData(getApplicationContext(),Utils.usr,"");
    	if(m.moveToFirst())
    	{
    	Cursor n=DBHelper.getData(getApplicationContext(),Utils.partners," where partner_id in ("+m.getString(16)+")");
    			while(n.moveToNext())
    			{
    		adpt1.add(n.getString(1));
    	        }
    	}
    	adpt1.setNotifyOnChange(true);
    	
    	if(tp.equals("2"))
		{
			final Cursor d=DBHelper.getData(getApplicationContext(),Utils.py," where payment_id='"+id+"'");
	 		if(d.moveToFirst())
	 		{
	 			sp_1.setSelection(adpt1.getPosition(DBHelper.getNameByID(getApplicationContext(),d.getString(3))));
	 			if(d.getString(6).equals("Cheque"))sp_2.setSelection(1); else sp_2.setSelection(0);
	 			sp_3.setText(d.getString(5));
	 			sp_4.setText(d.getString(7));
	 		}
		}
	}
	
	public void action(View v) throws UnsupportedEncodingException
	{
		try {
			data.put("sender", Utils.getDefaults("user_id", getApplicationContext()));
			data.put("subid", Utils.getDefaults("subid", getApplicationContext()));
			data.put("receiver",DBHelper.getIDByName(getApplicationContext(),sp_1.getSelectedItem().toString()));
			data.put("remarks", sp_4.getText().toString());
			data.put("ptype", sp_2.getSelectedItem().toString());
			data.put("amount", sp_3.getText().toString());
			data.put("pid", id);
			data.put("type", tp);
			
		 httpGetAsyncTask = new HttpGetAsyncTask(NewPayment.this,3); 
  	     httpGetAsyncTask.delegate = NewPayment.this;
  	     httpGetAsyncTask.execute(Utils.strpyurl+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));
		}
		
		 catch (JSONException e) { e.printStackTrace();}
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

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		jump();
	}
	
	   public void jump() {

		   Intent loginIntent = new Intent(NewPayment.this, SendPayment.class);
		   loginIntent.putExtra("tb","0");
			 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(loginIntent);
	        finish();
			 
	   }
}
