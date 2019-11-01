package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


 
@SuppressWarnings("deprecation")
public class SubServices extends Service implements AsyncResponse{
	
	private static String TAG = "Sub Service";
	private static Timer timer = new Timer(); 
	String cnt="";
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	HttpGetAsyncTask httpGetAsyncTask;
	String uid;

	@Override
	public IBinder onBind(Intent arg0) {

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
		uid=Utils.getDefaults("subid",getApplicationContext());
		httpGetAsyncTask = new HttpGetAsyncTask(this,2);
        httpGetAsyncTask.delegate = this;
        if(Utils.isConnectingToInternet(getApplicationContext()))
        	httpGetAsyncTask.execute(Utils.gtnt+uid);
	}
	
	public void readall()
	{
		 uid=Utils.getDefaults("subid",getApplicationContext());
		
		httpGetAsyncTask = new HttpGetAsyncTask(this,2);
        httpGetAsyncTask.delegate = this;
        if(Utils.isConnectingToInternet(getApplicationContext()))
        	httpGetAsyncTask.execute(Utils.readnotifications+uid);
	}
	

	private class mainTask extends TimerTask
    { 
        public void run() 
        {
        	loadNots();
        }
    }    
	
	public void createNotification(String txt,int nt) {
		Intent intent =null;
	
			intent = new Intent(this, SubOrders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("tb","1");
		
	    PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(),nt, intent, nt);
        Notification noti = new Notification.Builder(this)
        .setContentTitle(txt)
        .setAutoCancel(true)
        .setContentText("Click here to view").setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(pIntent)
        .build();
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // hide the notification after its selected
	        noti.flags |= Notification.FLAG_AUTO_CANCEL;
	        noti.defaults |= Notification.DEFAULT_SOUND;
	
	        notificationManager.notify(nt,noti);
	    
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
			jsonObj = new JSONObject(output);
			JSONArray Data = jsonObj.getJSONArray("nots");	
			
			for(int i=0;i<Data.length();i++)
			{
				JSONObject jsonObject = Data.getJSONObject(i); 
				String txt= jsonObject.getString("txt");

			createNotification(txt,i);
				
			

			}
			readall();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
 
}