package com.denchion.b2b;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
	
	private Context _context;
	private ProgressDialog dialog;
	public static final int progress_bar_type = 0; 
	


	public DownloadFileFromURL(Context c) {
		this._context=c;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//showDialog(progress_bar_type);
	       dialog = new ProgressDialog(this._context);
			dialog.setMessage("Downloading file. Please wait...");
			dialog.setIndeterminate(false);
			dialog.setMax(100);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	cancel(true);
			        dialog.dismiss();
			    }
			});
			dialog.show();
			
	}

	@Override
	protected String doInBackground(final String... f_url) {
		int count;
        try {
            URL url = new URL(Utils.base+"images/attachments/"+f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            
            // Output stream to write file
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/Download/"+f_url[0]);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                publishProgress(""+(int)((total*100)/lenghtOfFile));
               
                dialog.setProgressNumberFormat((Utils.bytes2String(total)) + "/" + (Utils.bytes2String(lenghtOfFile)));
                // writing data to file
                output.write(data, 0, count);
                if (isCancelled()) break;
            }

            // flushing output
            output.flush();
            
            // closing streams
            output.close();
            input.close();
            
        } catch (Exception e) {
        	Log.e("B2bError: ", e.getMessage());
        }
        
        return f_url[0];
	}
	
	
	
	
	protected void onProgressUpdate(String... progress) {
		// setting progress percentage
        dialog.setProgress(Integer.parseInt(progress[0]));
   }

	
	@Override
	protected void onPostExecute(String file_url) {
		// dismiss the dialog after the file was downloaded
		//dismissDialog(progress_bar_type);
		if (dialog.isShowing())dialog.dismiss();
	opne(file_url);
	}
	
	public void opne(final String fil)
	{
		final  Activity activity = (Activity) this._context;
		String uri=Environment.getExternalStorageDirectory().getPath()+"/Download/"+fil;
		new AlertDialog.Builder(this._context)
		.setMessage("File Saved: "+uri+" \n Do you want open this file?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	String m=Environment.getExternalStorageDirectory().getPath()+"/Download/"+fil;
		    	File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Download/"+fil);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				
				intent.setDataAndType(Uri.fromFile(file),Utils.getMimeType(m));
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				activity.startActivity(intent);
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}

}
