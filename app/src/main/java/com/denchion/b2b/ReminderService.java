package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.util.Calendar;
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
import android.database.Cursor;
import android.database.SQLException;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


 
@SuppressWarnings("deprecation")
public class ReminderService extends Service implements AsyncResponse{
	
	private static String TAG = "Reminder";
	private static Timer timer = new Timer(); 
	 private int year;
	    private int month;
	    private int day;
	String cnt="";
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	HttpGetAsyncTask httpGetAsyncTask;
	String uid;
	String todatDate;
	String curTime;

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
		timer.scheduleAtFixedRate(new mainTask(), 0, 50000);
		//Log.d(TAG, "FirstService started");
		//this.stopSelf();

	}
	
	public void loadNots()
	{
		final Calendar cd = Calendar.getInstance();
	    year  = cd.get(Calendar.YEAR);
	    month = cd.get(Calendar.MONTH);
	    day   = cd.get(Calendar.DAY_OF_MONTH);
	    Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
	    todatDate=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
	    curTime=hour+":"+minute+":00";

		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.reminder," where  date='"+todatDate+"' and time='"+curTime+"'");
		
		while(c.moveToNext())
	    		{
			int uid=Integer.parseInt(c.getString(0));

			createNotification(uid,c.getString(2));
	    		}
		
		readall();
	}
	
	public void readall()
	{
	
		httpGetAsyncTask = new HttpGetAsyncTask(this,2);
        httpGetAsyncTask.delegate = this;
        if(Utils.isConnectingToInternet(getApplicationContext()))
        	httpGetAsyncTask.execute(Utils.readReminder+Utils.getDefaults("user_id",getApplicationContext()));
	}
	

	private class mainTask extends TimerTask
    { 
        public void run() 
        {
        	loadNots();
        	
        }
    }    
	
	public void createNotification(int uid,String txt) {
		  Intent intent = new Intent(this, B2BDashboard.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), uid, intent, uid);
            Notification noti = new Notification.Builder(this)
            .setAutoCancel(true)
            .setContentTitle(txt)
            .setContentText("Click here to view").setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pIntent)
            .build();
        
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // hide the notification after its selected
	        noti.flags |= Notification.FLAG_AUTO_CANCEL;
	        noti.defaults |= Notification.DEFAULT_SOUND;
	        noti.defaults |= Notification.DEFAULT_VIBRATE;
	
	        notificationManager.notify(uid,noti);
	    
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
			 JSONArray Data2 = jsonObj.getJSONArray("reminder");	
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