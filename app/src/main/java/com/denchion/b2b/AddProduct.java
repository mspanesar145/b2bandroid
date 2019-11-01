package com.denchion.b2b;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class AddProduct extends Activity implements AdvancedWebView.Listener{
	ProgressDialog dialog;
	  private AdvancedWebView wv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_add_product);
		Utils.setActionBar(this,"Add Product");
		DBHelper.loaddb(getApplicationContext());
		 wv1=(AdvancedWebView)findViewById(R.id.webView2);
	      wv1.setWebViewClient(new MyBrowser());
	      wv1.getSettings().setLoadsImagesAutomatically(true);
          wv1.getSettings().setJavaScriptEnabled(true);
          wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
          wv1.addJavascriptInterface(new JavaScriptInterface(this), "Android");

          if(Utils.isConnectingToInternet(getApplicationContext()))
          {
 
          String ul=Utils.webupdt+Utils.getDefaults("user_id", getApplicationContext());
          wv1.loadUrl(ul);
          JavaScriptInterface gb=new JavaScriptInterface(getApplicationContext());
          gb.progress();
         
  			
          } else Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
          
          wv1.setWebViewClient(new WebViewClient() {
      	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
      	    	wv1.loadUrl("file:///android_asset/nointernet.jpg");
      	    	 if (dialog.isShowing())dialog.dismiss();
      	    }
      	});
	}
	
	 private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view,String url) {
	         view.loadUrl(url);
	         return true;
	      }
	  }
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        wv1.onResume();
	    }

	    @Override
	    protected void onPause() {
	    	wv1.onPause();
	        super.onPause();
	    }

	    @Override
	    protected void onDestroy() {
	    	wv1.onDestroy();
	        super.onDestroy();
	    }

	
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	        super.onActivityResult(requestCode, resultCode, intent);
	        wv1.onActivityResult(requestCode, resultCode, intent);
	        // ...
	    }

	    @Override
	    public void onBackPressed() {
	        if (!wv1.onBackPressed()) { return; }
	        // ...
	        super.onBackPressed();
	    }
	    
	
	 public class JavaScriptInterface {
		   Context mContext;

		   /** Instantiate the interface and set the context */
		   
		   JavaScriptInterface(Context c) {
		       mContext = c;
		   }

		   @JavascriptInterface
		   public void progress()
		   {
			   dialog = new ProgressDialog(AddProduct.this);
			    dialog.show();
			    dialog.setCanceledOnTouchOutside(false);
			    dialog.setMessage("Loading...");
		   }
		   
		   @JavascriptInterface
		   public void progressdismiss()
		   {
			   
			   if (dialog.isShowing())dialog.dismiss();
			
			
		   }
          
		   @JavascriptInterface
		   public void ToastMsg(String msg)
		   {
			   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();  
		   }
		   
		   
		   @JavascriptInterface
		   public void jump() {
           dialogInterface();
		   }

		   }
	 
	 @Override
		public void onPageStarted(String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageFinished(String url) {
			// TODO Auto-generated method stub	
		}
		@Override
		public void onPageError(int errorCode, String description, String failingUrl) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onDownloadRequested(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onExternalPageRequest(String url) {
			// TODO Auto-generated method stub
			
		}
		 
		
	public void dialogInterface()
	{
		new AlertDialog.Builder(AddProduct.this)
		.setMessage("Add More product?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("No", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	 Intent ap = new Intent(AddProduct.this, Products.class);
	    		 ap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		 AddProduct.this.startActivity(ap);
		    }})
		 .setNegativeButton("Yes",  new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	  finish();
				 startActivity(getIntent()); 
		    }
		 }).show();
		 
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
