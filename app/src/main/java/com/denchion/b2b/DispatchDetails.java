package com.denchion.b2b;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class DispatchDetails extends Activity{
	
	EditText sp_1;
	EditText sp_2;
	EditText sp_3;
	String id;
	ImageView imgView;
	String imgPath, fileName,imgPath2;
	JSONObject data = new JSONObject();
	Uri imageUri;
	ProgressDialog dialog2;
	RequestParams params = new RequestParams();
	JSONObject jsonObj;
	Bitmap bitmap;
	String encodedString2;
	TextView Imgpath;
	
	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	private static final int PICK_Camera_IMAGE = 111;
	private static final int PICK_IMAGE = 222;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_dispatch_details);
		DBHelper.loaddb(getApplicationContext());
		Utils.setActionBar(this,"Order Dispatch");
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("oid");
		sp_1=(EditText)findViewById(R.id.tr_1);
		sp_3=(EditText)findViewById(R.id.tr_3);
		sp_2=(EditText)findViewById(R.id.tr_2);
		Imgpath = (TextView) findViewById(R.id.imgpath);
		imgView = (ImageView) findViewById(R.id.imgPreview);

	}
	
		public void action(View v) throws UnsupportedEncodingException
		{
			if(sp_1.getText().toString().equals(""))sp_1.setError("Please Enter Transport Name");
			else if(sp_2.getText().toString().equals(""))sp_2.setError("Please Enter Tracking No.");
			else
			{
				String imgsp="";
				imgsp=Imgpath.getText().toString();
				encodeImagetoString(imgsp);
			}
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



		
		
		
		public void opt(View v)
		{

		 
		final  AlertDialog alert = new AlertDialog.Builder(this).create();
		 LayoutInflater inflater = getLayoutInflater();
		 //inflate view for alertdialog since we are using multiple views inside a viewgroup (root = Layout top-level) (linear, relative, framelayout etc..)
		 View view = inflater.inflate(R.layout.prompts6b, (ViewGroup) findViewById(R.id.layout_root_new2)); 

		 Button button1 = (Button) view.findViewById(R.id.btn1);
		 Button button2 = (Button) view.findViewById(R.id.btn2);

		
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
		 

		 alert.setView(view);
		 alert.show();
		}
		
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    Uri selectedImageUri = null;
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
		        }

		        if(selectedImageUri != null){
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
		                    //encodeImagetoString(filePath);
		                    Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
		                    imgView.setVisibility(View.VISIBLE);
		                    
		                    imgView.setImageBitmap(myBitmap);
		               
		                    
		               
		                    Imgpath.setText(filePath);
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
						  dialog2 = new ProgressDialog(DispatchDetails.this);
					        dialog2.setMessage("Loading...");
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
						if(!Imgpath.getText().toString().equals("")){
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
						}
						return "";
					}

					@Override
					protected void onPostExecute(String msg) {
						
						// Put converted Image string into Async Http Post param
						params.put("image", encodedString2);
						params.put("sender", Utils.getDefaults("user_id",getApplicationContext()));
						params.put("subid", Utils.getDefaults("subid", getApplicationContext()));
						params.put("order_id",id);
						params.put("courier", sp_1.getText().toString());
						params.put("courier_tracking_no", sp_2.getText().toString());
						params.put("remarks", sp_3.getText().toString());
					
						 makeHTTPCall();
						// Trigger Image upload
						//triggerImageUpload();
					}
				}.execute(null, null, null);
			}
			
			public void makeHTTPCall() {
				
				AsyncHttpClient client = new AsyncHttpClient();
				// Don't forget to change the IP address to your LAN address. Port no as well.
				client.post(Utils.dispatch, params, new AsyncHttpResponseHandler() {


					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						// Hide Progress Dialog
						if (dialog2.isShowing())dialog2.dismiss();
						Toast.makeText(getApplicationContext(), "Order Successfully Dispatched!", Toast.LENGTH_LONG).show();
						// TODO Auto-generated method stub
						Intent loginIntent=null;
						if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
							String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
							if(Arrays.asList(chk1).contains("5"))
								loginIntent = new Intent(DispatchDetails.this, Orders.class);
							else loginIntent = new Intent(DispatchDetails.this, SubOrders.class);
						}else
							loginIntent = new Intent(DispatchDetails.this, Orders.class);
						loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						loginIntent.putExtra("tb","0");
						loginIntent.putExtra("ttp","");
						DispatchDetails.this.startActivity(loginIntent);
						finish();

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
			
			
		
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.main,menu);
			
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
