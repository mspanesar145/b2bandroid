package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


 
@SuppressWarnings("deprecation")
public class MsgService extends Service implements AsyncResponse{
	
	private static String TAG = "Messaging Service";
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
		//Log.d(TAG, "FirstService started");
		//this.stopSelf();

	}
	
	public void loadNots()
	{
		httpGetAsyncTask = new HttpGetAsyncTask(this,2);
        httpGetAsyncTask.delegate = this;
        if(Utils.isConnectingToInternet(getApplicationContext()))
        	httpGetAsyncTask.execute(Utils.gtmesg+Utils.getDefaults("user_id",getApplicationContext()));
	}
	

	private class mainTask extends TimerTask
    { 
        public void run() 
        {	
        	loadNots();	
        	
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



	@Override
	public void processFinish(String output) {
		try {
			//
			jsonObj = new JSONObject(output);	
			 JSONArray Data = jsonObj.getJSONArray("messages");	
			 JSONArray Data2 = jsonObj.getJSONArray("reminder");	
		DBHelper.storeMessages(getApplicationContext(),Data);
		DBHelper.storeReminders(getApplicationContext(),Data2);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
 
}