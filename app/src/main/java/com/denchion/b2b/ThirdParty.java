package com.denchion.b2b;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

@SuppressLint("InflateParams") public class ThirdParty extends Activity {


	 private static final String TAG = null;
	private int year;
	    private int month;
	    private int day;
	    String todaydate;
	    Button btnUp1;
	    Button btnUp2;
	    Button btnUp3;
	    Button btnUp4;
	    Button btnUp5;
	    ProgressDialog dialog;
	    Uri selectedImageUri = null;
	    JSONObject data = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gb_third_party);
		DBHelper.loaddb(getApplicationContext());
		Utils.setActionBar(this,"CHOOSE OPTIONS");
		btnUp1 = (Button) findViewById(R.id.btnUp1);
		btnUp2 = (Button) findViewById(R.id.btnUp2);
		btnUp3 = (Button) findViewById(R.id.btnUp3);
		btnUp4 = (Button) findViewById(R.id.btnUp4);
		btnUp5 = (Button) findViewById(R.id.btnUp5);
		final Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
	    
	    todaydate=day+"-"+(month+1)+"-"+year;
	    
	    btnUp1.setOnClickListener(new View.OnClickListener() {

	             @Override
	             public void onClick(View v) {
	            	
	            	 dialogDetails(1);
	             }
	         });
	    btnUp2.setOnClickListener(new View.OnClickListener() {

	             @Override
	             public void onClick(View v) {
	            	
	            	 dialogDetails(2);
	             }
	         });
	      
	    btnUp3.setOnClickListener(new View.OnClickListener() {

	             @Override
	             public void onClick(View v) {
	            	
	            	 dialogDetails(3);
	             }
	         });
	      
	    btnUp4.setOnClickListener(new View.OnClickListener() {

	             @Override
	             public void onClick(View v) {
	            	
	            	 dialogDetails(4);
	             }
	         });
	    
	    btnUp5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
           	
           	 dialogDetails(5);
            }
        });
	}
	

	
	public void openApp(String file2,int tp)
	{
		//File sdCard = Environment.getExternalStorageDirectory();	
		//Uri pdfUri = Uri.parse(sdCard.getAbsolutePath() + "/Download/"+file);  
		
		 File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Download/"+file2);
			Uri path = Uri.fromFile(file);
		String ttl="";
		String pkg="";
		switch(tp)
		{
		case 1:ttl="Upload to Dropbox";pkg="com.dropbox.android"; break;
		case 2:ttl="Upload to Google Drive";pkg="com.google.android.apps.docs"; break;
		case 3:ttl="Upload to Sky Drive";pkg="com.microsoft.skydrive"; break;
		case 4:ttl="Send with Bluetooth";pkg="com.android.bluetooth"; break;
		}
		  boolean installed = appInstalledOrNot(pkg);  
	        if(installed) {
	        	Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setText(ttl)
                            .setType("application/vnd.ms-excel")
                            .setStream(path)
                            .getIntent()
                    .setPackage(pkg);
                   startActivity(shareIntent);        
	        } else {
	        	Toast.makeText(getApplicationContext(), ttl+" APP not installed! Please install first.", Toast.LENGTH_LONG).show();
	        }
	
	}
	
public void dialogDetails(final int tp) {
		

      	AlertDialog.Builder builderSingle = new AlertDialog.Builder(
      			this);
          builderSingle.setIcon(R.drawable.ic_launcher);
          LayoutInflater li = LayoutInflater.from(getApplicationContext());
			View promptsView = li.inflate(R.layout.prompts4, null);
	          final CheckBox categories = (CheckBox) promptsView.findViewById(R.id.chk1);
	          final CheckBox groups = (CheckBox) promptsView.findViewById(R.id.chk2);
	          final CheckBox partners = (CheckBox) promptsView.findViewById(R.id.chk3);
	          final CheckBox orders = (CheckBox) promptsView.findViewById(R.id.chk4);
	          final CheckBox subusers = (CheckBox) promptsView.findViewById(R.id.chk5);
	          final CheckBox specifications = (CheckBox) promptsView.findViewById(R.id.chk6);
	          final CheckBox payments = (CheckBox) promptsView.findViewById(R.id.chk7);
	          final CheckBox quotations = (CheckBox) promptsView.findViewById(R.id.chk8);
	          final CheckBox backup = (CheckBox) promptsView.findViewById(R.id.chk9);
		        
	        
          builderSingle.setTitle("SELECT DATA");
          
   
        	  builderSingle.setView(promptsView);
           
          builderSingle.setPositiveButton("UPLOAD NOW",
                  new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                    	
                    	  
							exportToExcel(categories.isChecked(),groups.isChecked(),partners.isChecked(),orders.isChecked(),subusers.isChecked(),specifications.isChecked(),payments.isChecked(),quotations.isChecked(),tp,backup.isChecked());
                    
                 
                      }
                  });

     
          builderSingle.show();
      }
	
private void exportToExcel(Boolean chk1,Boolean chk2,Boolean chk3,Boolean chk4,Boolean chk5,Boolean chk6,Boolean chk7,Boolean chk8,int tp,Boolean chk9) {
    	
		if(chk1!=false || chk2!=false || chk3!=false  || chk4!=false || chk5!=false || chk6!=false || chk7!=false || chk8!=false)
		{
			
			Calendar mcurrentTime = Calendar.getInstance();
	        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	        int minute = mcurrentTime.get(Calendar.MINUTE);
			
		
		final String fileName = "b2b_"+todaydate+"_"+String.format("%02d-%02d", hour, minute)+".xls";
		
		//Saving file in external storage
		File sdCard = Environment.getExternalStorageDirectory();	
		final File directory = new File(sdCard.getAbsolutePath() + "/Download/");
			
		//create directory if not exist
		if(!directory.isDirectory()){
			directory.mkdirs();	
		}
			
		//file path
		final File file = new File(directory, fileName);
	
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));		
		WritableWorkbook workbook;
		Cursor c1=DBHelper.getData(getApplicationContext(),Utils.cattbl,"");
		Cursor d1=DBHelper.getData(getApplicationContext(),Utils.grptbl,"");
		Cursor e1=DBHelper.getData(getApplicationContext(),Utils.partners,"");
		Cursor f1=DBHelper.getData(getApplicationContext(),Utils.orders,"");
		Cursor h1=DBHelper.getData(getApplicationContext(),Utils.bsu," ");
		Cursor ii1=DBHelper.getData(getApplicationContext(),Utils.bps," order by product_id ASC");
		Cursor j1=DBHelper.getData(getApplicationContext(),Utils.py,"");
		Cursor k1=DBHelper.getJoinData(getApplicationContext(),"qq.*,pt.full_name",Utils.quote+" qq"," Left Outer Join "+Utils.partners+" pt ON pt.partner_id=qq.reciever_id  order by qq.quotation_id DESC");
		try {
			workbook = Workbook.createWorkbook(file, wbSettings);			

			WritableSheet sheet=null;
			WritableSheet sheet2=null;
			WritableSheet sheet3=null;
			WritableSheet sheet4=null;
			WritableSheet sheet6=null;
			WritableSheet sheet7=null;
			WritableSheet sheet8=null;
			WritableSheet sheet9=null;
			
			if(chk1==true)sheet = workbook.createSheet("Categories",0);
			if(chk2==true)sheet2 = workbook.createSheet("Groups", 1);
			if(chk3==true)sheet3 = workbook.createSheet("Partners", 2);
			if(chk4==true)sheet4 = workbook.createSheet("Orders", 3);
			if(chk5==true)sheet6 = workbook.createSheet("Sub Users", 5);
			if(chk6==true)sheet7 = workbook.createSheet("Specifications", 6);
			if(chk7==true)sheet8 = workbook.createSheet("Payments", 7);
			if(chk8==true)sheet9 = workbook.createSheet("Quotations", 8);
			

			try {
				if(chk1==true){
				//tbl 1
				sheet.addCell(new Label(0, 0,"ID"));
				sheet.addCell(new Label(1, 0,"Name"));
				sheet.addCell(new Label(2, 0,"Parent"));
				}
				
				if(chk2==true){
				//tbl 2
				sheet2.addCell(new Label(0, 0,"GROUP"));
				sheet2.addCell(new Label(1, 0,"LIMIT"));
				}
				
				if(chk3==true){
				//tbl 3
				sheet3.addCell(new Label(0, 0,"NAME"));
				sheet3.addCell(new Label(1, 0,"EMAIL"));
				sheet3.addCell(new Label(2, 0,"MOBILE"));
				sheet3.addCell(new Label(3, 0,"ADDRESS"));
				sheet3.addCell(new Label(4, 0,"CITY"));
				sheet3.addCell(new Label(5, 0,"STATE"));
				sheet3.addCell(new Label(6, 0,"DATE"));
				sheet3.addCell(new Label(7, 0,"TIME"));
				}
				
				if(chk4==true){
				//tbl 4
				
				sheet4.addCell(new Label(0, 0,"RECEIVER"));
				sheet4.addCell(new Label(1, 0,"ORDER ID"));
				sheet4.addCell(new Label(2, 0,"Transport Type"));
				sheet4.addCell(new Label(3, 0,"Product Data"));
				sheet4.addCell(new Label(4, 0,"Sender Remarks"));
				sheet4.addCell(new Label(5, 0,"Receiver Remarks"));
				sheet4.addCell(new Label(6, 0,"Accept Status"));
				sheet4.addCell(new Label(7, 0,"Dispatched_date"));
				sheet4.addCell(new Label(8, 0,"Dispatched_time"));
				sheet4.addCell(new Label(9, 0,"Confirm_date"));
				sheet4.addCell(new Label(10, 0,"Confirm_time"));
				sheet4.addCell(new Label(11, 0,"Order_date"));
				sheet4.addCell(new Label(12, 0,"Order_time"));
				sheet4.addCell(new Label(13, 0,"Transport"));
				sheet4.addCell(new Label(14, 0,"Tracking no."));
				sheet4.addCell(new Label(15, 0,"Feedback"));
				sheet4.addCell(new Label(16, 0,"Remarks"));
				sheet4.addCell(new Label(17, 0,"Ignore Status"));
				sheet4.addCell(new Label(18, 0,"Delivery Type"));
				sheet4.addCell(new Label(19, 0,"Schedule Date"));
				}
				
				if(chk5==true){
				
                //tbl 6
				sheet6.addCell(new Label(0, 0,"USERNAME"));
				sheet6.addCell(new Label(1, 0,"PASSWORD"));
				sheet6.addCell(new Label(2, 0,"PERMISSION"));
				sheet6.addCell(new Label(3, 0,"DATE"));
				sheet6.addCell(new Label(4, 0,"TIME"));
				}
				
				if(chk6==true){
				//tbl 7
				sheet7.addCell(new Label(0, 0,"NAME"));
				sheet7.addCell(new Label(1, 0,"VALUE"));
				sheet7.addCell(new Label(2, 0,"PRODUCT"));
				}
				
				if(chk7==true){
				//tbl 8
				sheet8.addCell(new Label(0, 0,"USERNAME"));
				sheet8.addCell(new Label(1, 0,"PASSWORD"));
				sheet8.addCell(new Label(2, 0,"PERMISSION"));
				sheet8.addCell(new Label(3, 0,"DATE"));
				sheet8.addCell(new Label(4, 0,"TIME"));
				}
				if(chk8==true){
				sheet9.addCell(new Label(0, 0,"RECEIVER"));
				sheet9.addCell(new Label(1, 0,"DETAILS"));
				sheet9.addCell(new Label(2, 0,"PRODUCT"));
				sheet9.addCell(new Label(3, 0,"CONDITIONS"));
				sheet9.addCell(new Label(4, 0,"TYPE"));
				sheet9.addCell(new Label(5, 0,"DATE"));
				sheet9.addCell(new Label(6, 0,"TIME"));
				}
				
				if(chk1==true){
				int cc=1;
             		while(c1.moveToNext())
             		{
             		    sheet.addCell(new Label(0, cc,c1.getString(0)));
             			sheet.addCell(new Label(1, cc,c1.getString(2)));
             			sheet.addCell(new Label(2, cc,c1.getString(3)));
             			cc++;
             		}
             		c1.close();
             		
				}
				
				if(chk2==true){
             		int dd=1;
             		while(d1.moveToNext())
             		{
             		    sheet2.addCell(new Label(0, dd,d1.getString(1)));
             			sheet2.addCell(new Label(1, dd,d1.getString(2)));
             			dd++;
             		}
             		d1.close();
             		
				}
				if(chk3==true){
             		int ee=1;
             		while(e1.moveToNext())
             		{
             		    sheet3.addCell(new Label(0, ee,e1.getString(1)));
             			sheet3.addCell(new Label(1, ee,e1.getString(2)));
             			sheet3.addCell(new Label(2, ee,e1.getString(3)));
             			sheet3.addCell(new Label(3, ee,e1.getString(4)));
             			sheet3.addCell(new Label(4, ee,e1.getString(8)));
             			sheet3.addCell(new Label(5, ee,e1.getString(9)));
             			sheet3.addCell(new Label(6, ee,e1.getString(10)));
             			sheet3.addCell(new Label(7, ee,e1.getString(11)));
             			ee++;
             		}
             		e1.close();
             		
				}
             		
				if(chk4==true){
             		int ff=1;
             		while(f1.moveToNext())
             		{
             			
             			
             		    sheet4.addCell(new Label(0, ff,f1.getString(4)));
             			sheet4.addCell(new Label(1, ff,f1.getString(0)));
             			sheet4.addCell(new Label(2, ff,f1.getString(7)));
             			sheet4.addCell(new Label(3, ff,f1.getString(8)));
             			sheet4.addCell(new Label(4, ff,f1.getString(10)));
             			sheet4.addCell(new Label(5, ff,f1.getString(11)));
             			sheet4.addCell(new Label(6, ff,f1.getString(12)));
             			sheet4.addCell(new Label(7, ff,f1.getString(16)));
             			sheet4.addCell(new Label(8, ff,f1.getString(17)));
             			sheet4.addCell(new Label(9, ff,f1.getString(19)));
             			sheet4.addCell(new Label(10, ff,f1.getString(20)));
             			sheet4.addCell(new Label(11, ff,f1.getString(21)));
             			sheet4.addCell(new Label(12, ff,f1.getString(22)));
             			sheet4.addCell(new Label(13, ff,f1.getString(24)));
             			sheet4.addCell(new Label(14, ff,f1.getString(25)));
             			sheet4.addCell(new Label(15, ff,f1.getString(28)));
             			sheet4.addCell(new Label(16, ff,f1.getString(29)));
             			sheet4.addCell(new Label(17, ff,f1.getString(33)));
             			sheet4.addCell(new Label(18, ff,f1.getString(6)));
             			sheet4.addCell(new Label(19, ff,f1.getString(38)));
             			ff++;
             		}
             		f1.close();
				}
				
				if(chk5==true){
             		int hh=1;
             		while(h1.moveToNext())
             		{
             		    sheet6.addCell(new Label(0, hh,h1.getString(2)));
             			sheet6.addCell(new Label(1, hh,h1.getString(3)));
             			if(h1.getString(3).equals("1"))sheet6.addCell(new Label(2, hh,"ALL ACCESS"));
             			else sheet6.addCell(new Label(2, hh,"LIMITED"));
             			sheet6.addCell(new Label(3, hh,h1.getString(5)));
             			sheet6.addCell(new Label(4, hh,h1.getString(6)));

             			hh++;
             		}
             		h1.close();
             		
				}
				
				if(chk6==true){
             		int ii=1;
             		while(ii1.moveToNext())
             		{
             		    sheet7.addCell(new Label(0, ii,ii1.getString(3)));
             		    sheet7.addCell(new Label(1, ii,ii1.getString(4)));
             			sheet7.addCell(new Label(2, ii,DBHelper.getNAMEByPRODUCTID(getApplicationContext(),ii1.getString(1))));

             			ii++;
             		}
             		ii1.close();
				}
				
				if(chk7==true){
             		int jj=1;
             		while(j1.moveToNext())
             		{
             		    sheet8.addCell(new Label(0, jj,j1.getString(2)));
             			sheet8.addCell(new Label(1, jj,j1.getString(3)));
             			sheet8.addCell(new Label(3, jj,j1.getString(5)));
             			sheet8.addCell(new Label(4, jj,j1.getString(6)));

             			jj++;
             		}
             		j1.close();
             		
				}
				
				if(chk8==true){
             		int kk=1;
             		while(k1.moveToNext())
             		{
             		    sheet9.addCell(new Label(0, kk,k1.getString(12)));
             			sheet9.addCell(new Label(1, kk,k1.getString(5)));
             			sheet9.addCell(new Label(2, kk,k1.getString(6)));
             			sheet9.addCell(new Label(3, kk,k1.getString(7)));
             			String nm="";
             			if(k1.getString(9).equals("1")) nm="Sent";
            		    else nm="Asked";
             			sheet9.addCell(new Label(4, kk,nm));
             			sheet9.addCell(new Label(5, kk,k1.getString(10)));
             			sheet9.addCell(new Label(6, kk,k1.getString(11)));

             			kk++;
             		}
             		k1.close();
				}
				
				
					
				
             		
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}			
			workbook.write();		
			try {
				workbook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			
			if(tp!=5)openApp(fileName,tp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(chk9==true){


			selectedImageUri=Uri.fromFile(new File(directory, fileName));
			

		
			//closing cursor
			
			
	             dialog = ProgressDialog.show(ThirdParty.this,"","Backing up",true);

	             new Thread(new Runnable() {
	                 @Override
	                 public void run() {
	                     //creating new thread to handle Http Operations
	                	 String str=Utils.getPath(getApplicationContext(),selectedImageUri);
	                	 
	                     uploadFile(str);
	                 }
	             }).start();
	        
			}
	
		Toast.makeText(getApplicationContext(), "Backup Successfully Generated! File saves in download Directory!", Toast.LENGTH_SHORT).show();		
		}else {
			Toast.makeText(getApplicationContext(), "Please Select one Option", Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean appInstalledOrNot(String uri) {
	    PackageManager pm = getPackageManager();
	    boolean app_installed;
	    try {
	        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	        app_installed = true;
	    }
	    catch (PackageManager.NameNotFoundException e) {
	        app_installed = false;
	    }
	    return app_installed;
	}
	
	public int uploadFile(final String selectedFilePath){
		 
	     int serverResponseCode = 0;

	     HttpURLConnection connection;
	     DataOutputStream dataOutputStream;
	     String lineEnd = "\r\n";
	     String twoHyphens = "--";
	     String boundary = "*****";


	     int bytesRead,bytesAvailable,bufferSize;
	     byte[] buffer;
	     int maxBufferSize = 1 * 1024 * 1024;
	     File selectedFile = new File(selectedFilePath);


	    /* String[] parts = selectedFilePath.split("/");
	     final String fileName = parts[parts.length-1];*/

	     if (!selectedFile.isFile()){
	         dialog.dismiss();

	         runOnUiThread(new Runnable() {
	             @Override
	             public void run() {
	               //  tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
	             }
	         });
	         return 0;
	     }else{
	

	         try{
	        	 
	        	
	     			data.put("user_id", Utils.getDefaults("user_id", getApplicationContext()));
	     			data.put("email", Utils.getDefaults("email", getApplicationContext()));
	     		
	             FileInputStream fileInputStream = new FileInputStream(selectedFile);
	             URL url = new URL(Utils.sendBackup+URLEncoder.encode(data.toString(), "UTF-8"));
	             connection = (HttpURLConnection) url.openConnection();
	             connection.setInstanceFollowRedirects( false );
	             connection.setDoInput(true);//Allow Inputs
	             connection.setDoOutput(true);//Allow Outputs
	             connection.setUseCaches(false);//Don't use a cached Copy
	             connection.setRequestMethod("POST");
	             connection.setRequestProperty("Connection", "Keep-Alive");
	             connection.setRequestProperty("ENCTYPE", "multipart/form-data");
	             connection.setRequestProperty(
	                     "Content-Type", "multipart/form-data;boundary=" + boundary);
	             connection.setRequestProperty("uploaded_file",selectedFilePath);

	             //creating new dataoutputstream
	             dataOutputStream = new DataOutputStream(connection.getOutputStream());

	             //writing bytes to data outputstream
	             dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
	             dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	                     + selectedFilePath + "\"" + lineEnd);

	             dataOutputStream.writeBytes(lineEnd);

	             //returns no. of bytes present in fileInputStream
	             bytesAvailable = fileInputStream.available();
	             //selecting the buffer size as minimum of available bytes or 1 MB
	             bufferSize = Math.min(bytesAvailable, maxBufferSize);
	             //setting the buffer as byte array of size of bufferSize
	             buffer = new byte[bufferSize];

	             //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
	             bytesRead = fileInputStream.read(buffer, 0, bufferSize);


	             //loop repeats till bytesRead = -1, i.e., no bytes are left to read
	             while (bytesRead > 0) {

	                 try {

	                     //write the bytes read from inputstream
	                     dataOutputStream.write(buffer, 0, bufferSize);
	                 } catch (OutOfMemoryError e) {
	                     Toast.makeText(ThirdParty.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
	                 }
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	             }

	             dataOutputStream.writeBytes(lineEnd);
	             dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	             try{
	                 serverResponseCode = connection.getResponseCode();
	             }catch (OutOfMemoryError e){
	                 Toast.makeText(ThirdParty.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
	             }
	             String serverResponseMessage = connection.getResponseMessage();

	             Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

	             //response code of 200 indicates the server status OK
	             if (serverResponseCode == 200) {
	                 runOnUiThread(new Runnable() {
	                     @Override
	                     public void run() {
	                    	
	                     }
	                 });
	             }

	             //closing the input and output streams
	             fileInputStream.close();
	             dataOutputStream.flush();
	             dataOutputStream.close();

	            

	         } catch (FileNotFoundException e) {
	             e.printStackTrace();
	             runOnUiThread(new Runnable() {
	                 @Override
	                 public void run() {
	                     Toast.makeText(ThirdParty.this, "File Not Found", Toast.LENGTH_SHORT).show();
	                 }
	             });
	         } catch (MalformedURLException e) {
	             e.printStackTrace();
	             runOnUiThread(new Runnable() {
	                 @Override
	                 public void run() {
	                     Toast.makeText(ThirdParty.this, "URL Error!", Toast.LENGTH_SHORT).show();
	                 }
	             });

	         } catch (IOException e) {
	             e.printStackTrace();
	             runOnUiThread(new Runnable() {
	                 @Override
	                 public void run() {
	                     Toast.makeText(ThirdParty.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
	                 }
	             });
	         } catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         dialog.dismiss();
	         return serverResponseCode;
	     }



	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

 @Override
 public boolean onOptionsItemSelected(MenuItem menuItem) {
     switch (menuItem.getItemId()) {

         case android.R.id.home:
                finish();
                break;
     }
     return (super.onOptionsItemSelected(menuItem));
 }

}
