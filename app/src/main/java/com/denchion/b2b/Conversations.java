package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

@SuppressLint("NewApi") public class Conversations extends Activity implements AsyncResponse{
	
	  private static final int PICK_Camera_IMAGE = 111;
	private static final int PICK_IMAGE = 222;
	private static final int PICK_PDF = 333;
	private static final String TAG = null;
	Uri selectedImageUri = null;
	private ChatArrayAdapter chatArrayAdapter;
	    private ListView listView;
	    private EditText chatText;
	    private Button buttonSend;
	    JSONObject data = new JSONObject();
		JSONObject jsonObj;
		AsyncResponse delegate = null;
		HttpGetAsyncTask httpGetAsyncTask;
	    private boolean side = false;
	    String id;
	    String imgPath, fileName,imgPath2;
		Bitmap bitmap;
		TextView Imgpath;
		ProgressDialog dialog2;
		ProgressDialog dialog;
		String encodedString2;
		String imgDecodableString;
		Uri imageUri;
		RequestParams params = new RequestParams();
		String ids;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_conversations);
		
		DBHelper.loaddb(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		Utils.setActionBar(this,DBHelper.getNameByID(getApplicationContext(),id));
		buttonSend = (Button) findViewById(R.id.send);
        
        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
		Imgpath = (TextView) findViewById(R.id.imgpath);
		try {
			getMsgs();
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		connvo();
      /*  
     chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
						return sendChatMessage();
					} catch (UnsupportedEncodingException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                return false;
            }
        });
        
        */
		
		
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
					sendChatMessage();
				} catch (UnsupportedEncodingException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

      
       
        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
              
            }
        });
        
        
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
            	connvo();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 2000);
 
	}
	
public void uploadpdf()
{
	
    Intent intent = new Intent();
    //sets the select file to all types of files
    intent.setType("application/pdf");
    //allows to select data and return it
    intent.setAction(Intent.ACTION_GET_CONTENT);
    //starts new activity to select file and return data
    startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_PDF);

}



	
	
			public void loadImagefromGallery() {
		        
			
				// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
		        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		startActivityForResult(galleryIntent, PICK_IMAGE);

		
		}
		public void captureFromCam()
		{
			 String fileName = "new-photo-name.jpg";
             //create parameters for Intent with filename
             ContentValues values = new ContentValues();
             values.put(MediaStore.Images.Media.TITLE, fileName);
             values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
             //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
             //create new Intent
             Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
             startActivityForResult(intent, PICK_Camera_IMAGE);
		}
		
			
			
		
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        
	        String filePath = null;
	        switch (requestCode) {
	                case PICK_IMAGE:
	                    if (resultCode == Activity.RESULT_OK) {
	                        selectedImageUri = data.getData();
	                    }
	                    break;
	                case PICK_Camera_IMAGE:
	                     if (resultCode == RESULT_OK) {
	                        //use imageUri here to access the image
	                        selectedImageUri = imageUri;
	                    } else if (resultCode == RESULT_CANCELED) {
	                        //Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
	                    } else {
	                        //Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
	                    }
	                     break;
	                case PICK_PDF:
	                	 if (resultCode == RESULT_OK) {
	                		 selectedImageUri = data.getData();
	               
	                
	                		 if(selectedImageUri != null){
	                             dialog = ProgressDialog.show(Conversations.this,"","Uploading File...",true);
	              
	                             new Thread(new Runnable() {
	                                 @Override
	                                 public void run() {
	                                     //creating new thread to handle Http Operations
	                                	 String str=Utils.getPath(getApplicationContext(),selectedImageUri);
	                                     uploadFile1(str);
	                                 }
	                             }).start();
	                           
	                         }else{
	                             Toast.makeText(Conversations.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
	                         }
	                
	                	 }
	                	break;
	            }
	 
	          if(selectedImageUri != null && requestCode!=PICK_PDF){
	                    try {
	                        // OI FILE Manager
	                        String filemanagerstring = selectedImageUri.getPath();
	 
	                        // MEDIA GALLERY
	                        String selectedImagePath = getPath(selectedImageUri);
	 
	                        if (selectedImagePath != null) {
	                            filePath = selectedImagePath;
	                        } else if (filemanagerstring != null) {
	                            filePath = filemanagerstring;
	                        } else {
	                            Toast.makeText(getApplicationContext(), "Unknown path",
	                                    Toast.LENGTH_LONG).show();
	                           // Log.e("Bitmap", "Unknown path");
	                        }
	 
	                        if (filePath != null) {
	                            decodeFile(filePath);
	                        } else {
	                            bitmap = null;
	                        }
	                        encodeImagetoString(filePath);
	                    } catch (Exception e) {
	                        Toast.makeText(getApplicationContext(), "Internal error",
	                                Toast.LENGTH_LONG).show();
	                        //Log.e(e.getClass().getName(), e.getMessage(), e);
	                    }
	            }
	 
	    }
		
		@SuppressWarnings("deprecation")
		public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        if (cursor != null) {
	            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	            int column_index = cursor
	                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            cursor.moveToFirst();
	            return cursor.getString(column_index);
	        } else
	            return null;
	    }
	 
	    public void decodeFile(String filePath) {
	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, o);
	 
	        // The new size we want to scale to
	        final int REQUIRED_SIZE = 1024;
	 
	        // Find the correct scale value. It should be the power of 2.
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
	                break;
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale *= 2;
	        }
	 
	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        bitmap = BitmapFactory.decodeFile(filePath, o2);
	 
	       // imgView.setImageBitmap(bitmap);
	 
	    }
	 
	
		// AsyncTask - To convert Image to String
		public void encodeImagetoString(final String imgpath) {
			new AsyncTask<Void, Void, String>() {

				protected void onPreExecute() {
					  dialog2 = new ProgressDialog(Conversations.this);
				      dialog2.setMessage("Uploading...");
				  	  dialog2.setCancelable(false);
					  dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					    	cancel(true);
					        dialog.dismiss();
					    }
					});
				      dialog2.show();

				};

				@Override
				protected String doInBackground(Void... params) {
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 3;
					bitmap = BitmapFactory.decodeFile(imgpath,
							options);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make upload easy
					bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					 encodedString2 = Base64.encodeToString(byte_arr, 0);
					return "";
				}

				@Override
				protected void onPostExecute(String msg) {
				
					String sid="";
					if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))sid=Utils.getDefaults("user_id", getApplicationContext());
					else sid=Utils.getDefaults("user_id", getApplicationContext());
					
					// Put converted Image string into Async Http Post param
					params.put("image", encodedString2);
					params.put("sender_id",sid);
					params.put("receiver_id",id);
					params.put("subid",Utils.getDefaults("subid", getApplicationContext()));
					
					if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))
					params.put("common_obj",Utils.nconcat(Integer.parseInt(Utils.getDefaults("user_id", getApplicationContext())),Integer.parseInt(id)));
					else
					params.put("common_obj",Utils.nconcat(Integer.parseInt(Utils.getDefaults("subid", getApplicationContext())),Integer.parseInt(id)));	
					
					params.put("message","Attachment~(Click to Download)");
					
					 makeHTTPCall();
					
				}
			}.execute(null, null, null);
		}
		
		public void makeHTTPCall() {

			AsyncHttpClient client = new AsyncHttpClient();
			// Don't forget to change the IP address to your LAN address. Port no as well.
			client.post(Utils.storeMsgAttachment, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// Hide Progress Dialog
					if (dialog2.isShowing()) dialog2.dismiss();
					try {
						//
						jsonObj = new JSONObject(responseBody.toString());
						JSONArray Data = jsonObj.getJSONArray("messages");
						DBHelper.storeMessages(getApplicationContext(), Data);
						connvo();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					// Hide Progress Dialog

					// When Http response code is '404'
					if (statusCode == 404) {
						Toast.makeText(getApplicationContext(),
								"Requested resource not found",
								Toast.LENGTH_LONG).show();
					}
					// When Http response code is '500'
					else if (statusCode == 500) {
						Toast.makeText(getApplicationContext(),
								"Something went wrong at server end",
								Toast.LENGTH_LONG).show();
					}
					// When Http response code other than 404, 500
					else {
						Toast.makeText(getApplicationContext(),
								"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
										+ statusCode, Toast.LENGTH_LONG).show();
					}
				}
			});
		}

	 private boolean sendChatMessage() throws UnsupportedEncodingException, JSONException 
	 {
			data.put("sender_id",Utils.getDefaults("user_id", getApplicationContext()));
			data.put("receiver_id",id);
			data.put("common_obj",Utils.nconcat(Integer.parseInt(Utils.getDefaults("user_id", getApplicationContext())),Integer.parseInt(id)));
			data.put("message",chatText.getText().toString());
			data.put("subid",Utils.getDefaults("subid", getApplicationContext()));
			if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))
			data.put("common_obj",Utils.nconcat(Integer.parseInt(Utils.getDefaults("user_id", getApplicationContext())),Integer.parseInt(id)));
			else
			data.put("common_obj",Utils.nconcat(Integer.parseInt(Utils.getDefaults("subid", getApplicationContext())),Integer.parseInt(id)));	
			
			httpGetAsyncTask = new HttpGetAsyncTask(Conversations.this,1);
		    httpGetAsyncTask.delegate=Conversations.this;
			if(Utils.isConnectingToInternet(getApplicationContext()))
			httpGetAsyncTask.execute(Utils.strmesg+URLEncoder.encode(data.toString().replace(" ","_"), "UTF-8"));   
			chatText.setText("");
			connvo();
			return true;
	}
	 
	private boolean connvo()
	{
		
		 String oob="";
		 if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))
		 oob=Utils.nconcat(Integer.parseInt(Utils.getDefaults("user_id", getApplicationContext())),Integer.parseInt(id));
		 else
		 oob=Utils.nconcat(Integer.parseInt(Utils.getDefaults("subid", getApplicationContext())),Integer.parseInt(id));
		 
		 Cursor d =DBHelper.getData(getApplicationContext(),Utils.msgtbl," where common_obj='"+oob+"'");
		 chatArrayAdapter.reset();
		 String tp="1";
		 String nmm="";
		 ids = "";
		 String dwnflnm="";
		 while(d.moveToNext())
		 {
			
			 if(d.getString(4).equals("Attachment~(Click to Download)"))
				 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".jpg";
				 tp="2";
				 }
			 else if(d.getString(4).equals("AttachmentPDF~(Click to Download)"))
			 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".pdf";
				 tp="2";
			 }
			 else if(d.getString(4).equals("AttachmentWORD~(Click to Download)"))
			 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".doc";
				 tp="2";
			 }
			 else if(d.getString(4).equals("AttachmentWORDX~(Click to Download)"))
			 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".docx";
				 tp="2";
			 }
			 else if(d.getString(4).equals("AttachmentEXCEL~(Click to Download)"))
			 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".xls";
				 tp="2";
			 }
			 else if(d.getString(4).equals("AttachmentEXCELX~(Click to Download)"))
			 {
				 dwnflnm="msgAttachment_"+d.getString(0)+".xlsx";
				 tp="2";
			 }
			
			 
	
			 String []chk1=d.getString(3).split("[_]");	
				if(chk1[0].equals(Utils.getDefaults("user_id", getApplicationContext())))ids=chk1[1];
				else ids=chk1[0];
			 
			 
			 
		    if(d.getString(5).equals("0")){
			 if(d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))
				 {
				 side =false;
				 nmm="";
			 } else 
				 {
				 side=true;
				 if(d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))nmm=d.getString(12)+": "; else nmm=d.getString(11)+": ";
				 }
		    }
		    else
		    {
		    	if(d.getString(1).equals(Utils.getDefaults("subid", getApplicationContext()))){
		    		side =false;
		    		nmm="";
		    	}else{
		    		side=true;	
		    		if(d.getString(1).equals(Utils.getDefaults("user_id", getApplicationContext())))nmm=d.getString(12)+": "; else nmm=d.getString(11)+": ";
		    	}
		    }
			 
			 chatArrayAdapter.add(new ChatMessage(side, nmm+d.getString(4)+" - "+Utils.ConvertTime(d.getString(9)),tp,dwnflnm,Conversations.this));
			 nmm="";
			 
		 }
	
		
		 return true;
	}
	
	
	public void action(final View v)
	{
		
		
		
	}
	
	
	public void opt()
	{

	 
	final  AlertDialog alert = new AlertDialog.Builder(this).create();
	 LayoutInflater inflater = getLayoutInflater();
	 //inflate view for alertdialog since we are using multiple views inside a viewgroup (root = Layout top-level) (linear, relative, framelayout etc..)
	 View view = inflater.inflate(R.layout.prompts6, (ViewGroup) findViewById(R.id.layout_root_new2)); 

	 Button button1 = (Button) view.findViewById(R.id.btn1);
	 Button button2 = (Button) view.findViewById(R.id.btn2);
	 Button button3 = (Button) view.findViewById(R.id.btn3);

	
	 button1.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View v) {
		    	captureFromCam();
		    	alert.dismiss();
		    }
		});
	 button2.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View v) {
		    	loadImagefromGallery();
		    	 alert.dismiss();
		    }
		});
	 
	 button3.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View v) {
		    	uploadpdf();
		    	 alert.dismiss();
		    }
		});
	 

	 alert.setView(view);
	 alert.show();
	}
 


	
	public void getMsgs() throws UnsupportedEncodingException
	{
		String uuid="";
		httpGetAsyncTask = new HttpGetAsyncTask(Conversations.this,1);
	       httpGetAsyncTask.delegate=Conversations.this;
	       try {
			data.put("sender_id",Utils.getDefaults("user_id", getApplicationContext()));
			   if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))uuid=Utils.getDefaults("user_id", getApplicationContext());
		       else uuid=Utils.getDefaults("subid", getApplicationContext());
			data.put("receiver_id",uuid);
			data.put("sender_id",id);
			data.put("common_obj",Utils.nconcat(Integer.parseInt(uuid),Integer.parseInt(id)));

		 if(Utils.isConnectingToInternet(getApplicationContext()))
		  	 httpGetAsyncTask.execute(Utils.getConversationsUsers+URLEncoder.encode(data.toString(), "UTF-8"));  
	       } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.msg,menu);
		
		return true;
	}

 @Override
 public boolean onOptionsItemSelected(MenuItem menuItem) {
     switch (menuItem.getItemId()) {
      
     
     
         case R.id.nAttach:
        	 opt(); 
         break;
     
         case android.R.id.home:
                finish();
                break;
     }
     return (super.onOptionsItemSelected(menuItem));
 }

 

 
 public int uploadFile1(final String selectedFilePath){
	 
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
    	 String sid="";
    	 String cobj="";
    	 if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))sid=Utils.getDefaults("user_id", getApplicationContext());
			else sid=Utils.getDefaults("user_id", getApplicationContext());
    	 
			if(Utils.getDefaults("subid", getApplicationContext()).equals("0"))
				cobj=Utils.nconcat(Integer.parseInt(Utils.getDefaults("user_id", getApplicationContext())),Integer.parseInt(id));
			else
				cobj=Utils.nconcat(Integer.parseInt(Utils.getDefaults("subid", getApplicationContext())),Integer.parseInt(id));
         try{
             FileInputStream fileInputStream = new FileInputStream(selectedFile);
             URL url = new URL(Utils.uploadpdf+sid+"/"+cobj+"/"+id+"/"+Utils.getDefaults("subid", getApplicationContext()));
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
                     Toast.makeText(Conversations.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
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
                 Toast.makeText(Conversations.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
             }
             String serverResponseMessage = connection.getResponseMessage();

             Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

             //response code of 200 indicates the server status OK
             if (serverResponseCode == 200) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                    	 try {
							getMsgs();
							connvo();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
                     Toast.makeText(Conversations.this, "File Not Found", Toast.LENGTH_SHORT).show();
                 }
             });
         } catch (MalformedURLException e) {
             e.printStackTrace();
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Toast.makeText(Conversations.this, "URL Error!", Toast.LENGTH_SHORT).show();
                 }
             });

         } catch (IOException e) {
             e.printStackTrace();
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Toast.makeText(Conversations.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                 }
             });
         }
         dialog.dismiss();
         return serverResponseCode;
     }



 }
 
 @Override
	public void onBackPressed() {
   super.onBackPressed();
   Intent ap2 = new Intent(this, Messages.class);
		 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       this.startActivity(ap2);
	}
	
 
 
 @Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		try {
			//
			jsonObj = new JSONObject(output);	
			 JSONArray Data = jsonObj.getJSONArray("messages");	
		DBHelper.storeMessages(getApplicationContext(),Data);
		connvo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
