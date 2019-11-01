package com.denchion.b2b;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationDetails extends Activity {
	
	TextView txt1;
	 TextView txt2;
	 TextView txt3;
	 TextView txt4;
	 
	 String nid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gb_notification_details);
		Utils.setActionBar(this,"Notification Details");
		DBHelper.loaddb(getApplicationContext());
		
		Bundle bundle = getIntent().getExtras();
		nid = bundle.getString("ttp");
		
		txt1=(TextView)findViewById(R.id.val_1);
        txt2=(TextView)findViewById(R.id.val_2);
        txt3=(TextView)findViewById(R.id.val_3);
        txt4=(TextView)findViewById(R.id.val_4);
        
        Cursor c=DBHelper.getData(getApplicationContext(),Utils.po," where noti_id='"+nid+"'");
        if(c.moveToFirst())
        {
        	String uid = "";
        	if(c.getString(2).equals(Utils.getDefaults("user_id", getApplicationContext())))uid = c.getString(1);
        	else uid = c.getString(2);
        	  String []chk1=c.getString(4).split("[:]");
        	txt1.setText(DBHelper.getNameByID(getApplicationContext(),uid));
        	txt2.setText(chk1[0]);
        	txt3.setText(chk1[1]);
        	txt4.setText(c.getString(9));
        }
	}
}
