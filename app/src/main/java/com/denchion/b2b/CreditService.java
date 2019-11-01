package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


 
@SuppressWarnings("deprecation")
public class CreditService extends Service implements AsyncResponse{
	
	private static String TAG = "Credit Period";
	private static Timer timer = new Timer(); 
	String cnt="";
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	HttpGetAsyncTask httpGetAsyncTask;

	@Override
	public IBinder onBind(Intent arg0) {
		
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
		DBHelper.loaddb(getApplicationContext());
    }
	
	@Override
	public void onStart(Intent intent, int startId) {

		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		timer.scheduleAtFixedRate(new mainTask(), 0, 60000);


	}
	


	private class mainTask extends TimerTask
    { 
        public void run() 
        {	
        
        	checkCreditLimit();
        }
    }    
	


	    @Override
	    public void onTaskRemoved(Intent rootIntent){
	    Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
	    restartServiceIntent.setPackage(getPackageName());
	    PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
	    AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	    alarmService.set(
	    AlarmManager.ELAPSED_REALTIME,
	    SystemClock.elapsedRealtime() + 1000,
	    restartServicePendingIntent);
	    super.onTaskRemoved(rootIntent);
	 }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG,"First Service Destroyed");
	}

	public void checkCreditLimit()
	{
		Cursor d=DBHelper.getData(getApplicationContext(),Utils.orders," where confirm_received=1 and receiver_id='"+Utils.getDefaults("user_id",getApplicationContext())+"' and credit=0");
    	while(d.moveToNext())
    	{
    		String days=Utils.get_count_of_days(Utils.parseDateToddMMyyyy4(d.getString(19)));
    		int limits=DBHelper.getCreditFromSender(getApplicationContext(),d.getString(1));
    		
    		if(Integer.parseInt(days)==limits && limits!=0)
    		{
    			httpGetAsyncTask = new HttpGetAsyncTask(this,2);
    	        httpGetAsyncTask.delegate = this;
    	        if(Utils.isConnectingToInternet(getApplicationContext()))
    	        	httpGetAsyncTask.execute(Utils.creditlimit+d.getString(1)+"/"+d.getString(3)+"/"+d.getString(0));	
    		}
    	}
	}


	@Override
	public void processFinish(String output) {
		
		DBHelper.updateCreditOrder(output);
		
	}
	
	
 
}