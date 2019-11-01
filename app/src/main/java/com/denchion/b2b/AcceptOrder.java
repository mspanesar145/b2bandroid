package com.denchion.b2b;

import java.util.Arrays;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
public class AcceptOrder extends Activity {

	String id;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	AsyncResponse delegate = null;
	HttpGetAsyncTask httpGetAsyncTask;
	ProgressDialog dialog;
	 private WebView wv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_accept_order);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("oid");
		DBHelper.loaddb(getApplicationContext());
		Utils.setActionBar(this,"Accept Order");
		 wv1=(WebView)findViewById(R.id.webView1);
	     wv1.setWebViewClient(new MyBrowser());
	     wv1.getSettings().setLoadsImagesAutomatically(true);
         wv1.getSettings().setJavaScriptEnabled(true);
         wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
         wv1.addJavascriptInterface(new JavaScriptInterface(this), "Android");
         String ul=Utils.webaccept+id+"/"+Utils.getDefaults("user_id", getApplicationContext())+"/"+Utils.getDefaults("subid", getApplicationContext());
         if(Utils.isConnectingToInternet(getApplicationContext()))
         {wv1.loadUrl(ul);
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
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
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
			   dialog = new ProgressDialog(AcceptOrder.this);
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
			   Intent loginIntent=null;
				if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
		    		String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
		          if(Arrays.asList(chk1).contains("5"))
		    			loginIntent = new Intent( AcceptOrder.this, Orders.class);
		    		    else loginIntent = new Intent( AcceptOrder.this, SubOrders.class);
		    		}else
		    			loginIntent = new Intent( AcceptOrder.this, Orders.class);
				 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 loginIntent.putExtra("tb","0");
				 loginIntent.putExtra("ttp","");
				 AcceptOrder.this.startActivity(loginIntent);
		       finish();
			  
				 
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
