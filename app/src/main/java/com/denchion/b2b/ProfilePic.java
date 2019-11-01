package com.denchion.b2b;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class ProfilePic extends Activity {
	

		 ImageView imgView;
		 ImageView imgView2;
		 String encodedString;
		 String imgDecodableString;
		 String imgPath, fileName,imgPath2;
	    private final int GALLERY_ACTIVITY_CODE=1;
	    private final int RESULT_CROP = 400;
	    Button btn_choose;
		Bitmap bitmap;
		Drawable dp;
		public ProgressDialog dialog;
		InputStream inputStream;
		 RequestParams params = new RequestParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_profile_pic);
		Utils.setActionBar(this,"Edit Profile Pic");
		DBHelper.loaddb(getApplicationContext());
		imgView = (ImageView) findViewById(R.id.profilePic);
        btn_choose=(Button)findViewById(R.id.btncadd);
        
        File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+Utils.getDefaults("user_id", getApplicationContext())+".jpg");
        Picasso.with(getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgView);
        
        btn_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Utils.isConnectingToInternet(getApplicationContext())){
                //Start Activity To Select Image From Gallery   
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
            	}
            	else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                
            }
        });

	}


protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
   
    if (requestCode == GALLERY_ACTIVITY_CODE) {
        if(resultCode == Activity.RESULT_OK){  
          String picturePath = data.getStringExtra("picturePath");  
            //perform Crop on the Image Selected from Gallery
            performCrop(picturePath);
        }
   }

   if (requestCode == RESULT_CROP ) {
        if(resultCode == Activity.RESULT_OK ){  
        	 
        	
            Bundle extras = data.getExtras();
            Bitmap selectedBitmap = extras.getParcelable("data"); 
            
            // Set The Bitmap Data To ImageView
            imgView.setImageBitmap(selectedBitmap);     
     
            imgView.setScaleType(ScaleType.FIT_XY);
           // Uri u=getImageUri(getApplicationContext(), selectedBitmap);
            //imgView.setImageURI(u);
            try {
				saveImg(selectedBitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            data=null;
            
        }
  
    }
}
    



public void saveImg(Bitmap bmp) throws IOException
{
	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	Random r = new Random();
	int i1 = r.nextInt(80 - 65) + 65;

	//you can create a new file name "test.jpg" in sdcard folder.
	File f = new File(Environment.getExternalStorageDirectory()
	                        + File.separator + ".b2b/tmp/"+i1+".jpg");
	f.createNewFile();
	//write the bytes in file
	FileOutputStream fo = new FileOutputStream(f);
	fo.write(bytes.toByteArray());

	// remember close de FileOutput
	fo.close();
	uploadImage(f.toString());
	
}





private void performCrop(String picUri) {
    try {
        //Start Crop Activity

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        File f = new File(picUri);
        Uri contentUri = Uri.fromFile(f);
   
       
        cropIntent.setDataAndType(contentUri, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 400);
        cropIntent.putExtra("outputY", 400);

        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        
       
        startActivityForResult(cropIntent, RESULT_CROP);
      
        
    }
    // respond to users whose devices do not support the crop action
    catch (ActivityNotFoundException anfe) {
        // display an error message
        //String errorMessage = "your device doesn't support the crop action!";
      //  Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        
    }
}   

public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
}
	public void uploadImage(String imgpath) {
	
		params.put("filename", "user"+Utils.getDefaults("user_id", getApplicationContext())+".jpg");
		// When Image is selected from Gallery
		if (imgpath != null && !imgpath.isEmpty()) {
			// Convert image to String using Base64
			encodeImagetoString(imgpath);
		// When Image is not selected from Gallery
			
		} else {
			Toast.makeText(getApplicationContext(),"You must select image from gallery before you try to upload",Toast.LENGTH_LONG).show();
		}
	}

public void encodeImagetoString(final String imgpath) {
	new AsyncTask<Void, Void, String>() {

		protected void onPreExecute() {
		
			    dialog = new ProgressDialog(ProfilePic.this);
			    dialog.show();
			    dialog.setMessage("Updating...");
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
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); 
			byte[] byte_arr = stream.toByteArray();
			// Encode Image to String
			encodedString = Base64.encodeToString(byte_arr, 0);
			return "";
		}

		@Override
		protected void onPostExecute(String msg) {
			
			// Put converted Image string into Async Http Post param
			params.put("image", encodedString);
			 makeHTTPCall();
			// Trigger Image upload
			//triggerImageUpload();
		}
	}.execute(null, null, null);
}

@Override
public void onBackPressed() {
	 Intent loginIntent2 = new Intent(this, B2BDashboard.class);
	 loginIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     this.startActivity(loginIntent2);
     finish();
}



public void makeHTTPCall() {
	
	AsyncHttpClient client = new AsyncHttpClient();
	// Don't forget to change the IP address to your LAN address. Port no as well.
	client.post(Utils.UpdateProfile, params, new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// Hide Progress Dialog
			if (dialog.isShowing())dialog.dismiss();
			Utils.doDownload("profile/","user"+Utils.getDefaults("user_id", getApplicationContext())+".jpg");
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
				Toast.makeText(
						getApplicationContext(),
						"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
								+ statusCode, Toast.LENGTH_LONG)
						.show();
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
