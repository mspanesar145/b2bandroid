package com.denchion.b2b;


import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.util.Arrays;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


 
@SuppressWarnings("deprecation")
public class Services extends Service implements AsyncResponse{
	
	private static String TAG = "B2B";
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
		if(Utils.getDefaults("subid",getApplicationContext()).equals("0"))
			uid=Utils.getDefaults("user_id",getApplicationContext());
		else uid=Utils.getDefaults("subid",getApplicationContext());
		httpGetAsyncTask = new HttpGetAsyncTask(this,2);
        httpGetAsyncTask.delegate = this;
        if(Utils.isConnectingToInternet(getApplicationContext()))
        	httpGetAsyncTask.execute(Utils.gtnt+uid);
	}
	
	public void readall()
	{
		if(Utils.getDefaults("subid",getApplicationContext()).equals("0"))
			uid=Utils.getDefaults("user_id",getApplicationContext());
		else uid=Utils.getDefaults("subid",getApplicationContext());
		
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
	
	public void createNotification(String txt,int tx,int uid,int pid) {
		Intent intent =null;
		switch(tx)
		{
		case 0:
			intent = new Intent(this, B2BDashboard.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		case 1:
			intent = new Intent(this, Partners.class);
		     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb","2");
			break;
		case 2:
			intent = new Intent(this, Partners.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb","1");
		
			break;
		case 3:
			String tbb = "2";
			String []chk1=txt.split("[ ]");
			if (Arrays.asList(chk1).contains("direct")) 
			{
				 tbb = "4";
			}
			
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb",tbb);
			intent.putExtra("ttp","");
		
			break;
		case 4:
			intent = new Intent(this, Messages.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		break;
			 
		case 5:
			intent = new Intent(this, SendPayment.class);
			intent.putExtra("tb","1");
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		break;
		
		case 6:
			intent = new Intent(this, Quotations.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		break;
		
		case 7:
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("tb","0");
			 intent.putExtra("ttp","");
		
		break;
		
		case 8:
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb","0");
			intent.putExtra("ttp","");
		
			break;
			
		case 9:
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb","1");
			intent.putExtra("ttp","");
		
			break;
			
		case 10:
			intent = new Intent(this, Messages.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		break;
			
		case 11:
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb","0");
			intent.putExtra("ttp","");
		
			break;
			
			
		case 15:
			 intent = new Intent(this, GroupNotDetails.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("id",pid+"");
			break;
			
		case 16:
			intent = new Intent(this, B2BDashboard.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		case 18:
			intent = new Intent(this, ViewCollection.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		case 22:
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("ttp",pid+"");
			 intent.putExtra("tb","0");
			break;
			
		case 23:
			 intent = new Intent(this, NotificationDetails.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("ttp",uid+"");
			break;
		
			
			
		}
		    
	        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), uid, intent, uid);
	        File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+pid+".jpg");
	        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.toString());
            Notification noti = new Notification.Builder(this)
            .setContentTitle(txt).setLargeIcon(bitmap)
            .setAutoCancel(true)
            .setContentText("Click here to view").setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pIntent)
            .build();
        
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // hide the notification after its selected
	        noti.flags |= Notification.FLAG_AUTO_CANCEL;
	        noti.defaults |= Notification.DEFAULT_SOUND;
	
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
			JSONArray Data = jsonObj.getJSONArray("nots");	
			
			for(int i=0;i<Data.length();i++)
			{
				JSONObject jsonObject = Data.getJSONObject(i); 
				String txt= jsonObject.getString("txt");
				int nt=Integer.parseInt(jsonObject.getString("ntype"));
				int uid=Integer.parseInt(jsonObject.getString("noti_id"));
				int pid=Integer.parseInt(jsonObject.getString("partner_id"));
				Utils.doDownload("profile/","user"+pid+".jpg");
			createNotification(txt,nt,uid,pid);
			}
			readall();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
 
}