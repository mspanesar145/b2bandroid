package com.denchion.b2b;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
public class GBSplash extends Activity {
	SQLiteDatabase db;

	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.gb_splash);
        DBHelper.loaddb(getApplicationContext());
        DBHelper.createTbls(getApplicationContext());
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
            	
            	if(DBHelper.chkLogin(getApplicationContext())==2)
            		{
            		//DBHelper.storecat(getApplicationContext());
            		File directory = new File(Environment.getExternalStorageDirectory() + "/.b2b/tmp/");
            		File directory2 = new File(Environment.getExternalStorageDirectory() + "/.b2b/dp/");
            		File directory3 = new File(Environment.getExternalStorageDirectory() + "/b2b/files/");
            		if (!directory.exists()) {
            		    directory.mkdir();
            		}
            		if (!directory2.exists()) {
            		    directory2.mkdir();
            		}
            		if (!directory3.exists()) {
            		    directory3.mkdir();
            		}
            	
            		Appdata();
            		}
            	else Login();
            }
            }, SPLASH_TIME_OUT);
    }


    
    
  public void Login()
    {
    	  Intent loginIntent = new Intent(this, B2BLogin.class);
          loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          this.startActivity(loginIntent);
          finish();
    }
    
    public void Appdata()
    {
    	  Intent loginIntent = new Intent(this, B2BDashboard.class);
          loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          this.startActivity(loginIntent);
          finish();
    }
   
    
    @Override
	public void onBackPressed() {
	    finish();
	}
    
    
}