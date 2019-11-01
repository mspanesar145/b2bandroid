package com.denchion.b2b;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

@SuppressWarnings("deprecation")
class HttpGetAsyncTask extends AsyncTask<String, Void, String>{
	public ProgressDialog dialog;
	private Context con;
	private int tpe;
	
	public interface AsyncResponse {
		
	    void processFinish(String output);
		
	}	
	
	public AsyncResponse delegate = null;
	
	  
	HttpGetAsyncTask(Context c,int tp){
		
		this.con=c;
		this.tpe=tp;
		
	}
	 @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        if(this.tpe==1){
	      
	        	 Activity activity = (Activity) this.con;
	        	 activity.setProgressBarIndeterminateVisibility(true);
	        }else if(this.tpe==3)
	        {
	        	dialog = new ProgressDialog(this.con);
				//dialog.setCanceledOnTouchOutside(false);
				//dialog.setMessage("Loading...");
				//dialog.show();
	        }
	    }

	  @Override
		protected String doInBackground(String... params) {
		 
		  HttpClient httpClient=null;
			httpClient = new DefaultHttpClient();

			HttpGet	httpGet=null;
			httpGet = new HttpGet(params[0]);
			HttpResponse httpResponse=null;
			String res="";
			try {
				
				
		
				httpResponse = httpClient.execute(httpGet);

				InputStream inputStream = httpResponse.getEntity().getContent();

				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				StringBuilder stringBuilder = new StringBuilder();

				String bufferedStrChunk = null;
                
				while((bufferedStrChunk = bufferedReader.readLine()) != null){
					stringBuilder.append(bufferedStrChunk);
					 if (isCancelled()) break;
				}
              
				res=stringBuilder.toString();
				
				return res;

			} catch (ClientProtocolException cpe) {
				System.out.println("Exception generates caz of httpResponse :" + cpe);
				cpe.printStackTrace();
			} catch (IOException ioe) {
				System.out.println("Second exception generates caz of httpResponse :" + ioe);
				ioe.printStackTrace();
			}
			
			return "";
		}
	  
	
	  
	  @Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			 if(this.tpe==1){	
				 Activity activity = (Activity) this.con;
	        	 activity.setProgressBarIndeterminateVisibility(false);
			
				 
			 }else if(this.tpe==3)
			 {
				 if (dialog.isShowing())dialog.dismiss();
			 }
			delegate.processFinish(result);
		}
		
	
}
