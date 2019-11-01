package com.denchion.b2b;


import java.util.Timer;
import java.util.TimerTask;

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
public class PendingTaskReminder extends Service{
	
	private static String TAG = "Pending Task";
	private static Timer timer = new Timer(); 

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
		timer.scheduleAtFixedRate(new mainTask(), 0, 43200000);
		//Log.d(TAG, "FirstService started");
		//this.stopSelf();

	}
	
	public void loadNots()
	{
		
           String str=DBHelper.getPendingTasks(getApplicationContext());
           if(!str.equals("2"))createNotification(769668,"Pending Tasks",str);

	}
	
	private class mainTask extends TimerTask
    { 
        public void run() 
        {
        	loadNots();
        	
        }
    }    
	
	public void createNotification(int uid,String txt,String str2) {
		  Intent intent = new Intent(this, B2BDashboard.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), uid, intent, uid);
            Notification noti = new Notification.Builder(this)
            .setAutoCancel(true)
            .setContentTitle(txt)
            .setContentText(str2).setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pIntent)
            .build();
        
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // hide the notification after its selected
	        noti.flags |= Notification.FLAG_AUTO_CANCEL;
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


 
}