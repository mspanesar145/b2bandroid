package com.denchion.b2b;

import java.util.Arrays;

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

@SuppressLint("SetJavaScriptEnabled") public class EditOrder extends Activity  {
	ProgressDialog dialog;
	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	  private WebView wv1;
      String oid;
      String tp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_edit_order);
		
		Bundle bundle = getIntent().getExtras();
		oid = bundle.getString("id");
		tp = bundle.getString("tp");
		Utils.setActionBar(this,"Edit Order ID#"+oid);
		  wv1=(WebView)findViewById(R.id.webView1);
	      wv1.setWebViewClient(new MyBrowser());
	      wv1.getSettings().setLoadsImagesAutomatically(true);
          wv1.getSettings().setJavaScriptEnabled(true);
          wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
          wv1.addJavascriptInterface(new JavaScriptInterface(this), "Android");
          String ul=Utils.edtOrder+Utils.getDefaults("user_id", getApplicationContext())+"/"+Utils.getDefaults("subid", getApplicationContext())+"/"+oid;
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
				dialog = new ProgressDialog(EditOrder.this);
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
			     Intent loginIntent = null;
			 	if(!Utils.getDefaults("subid",getApplicationContext()).equals("0")){
		    		String []chk1=Utils.getDefaults("permission",getApplicationContext()).split("[ ]");
		          if(Arrays.asList(chk1).contains("5"))
		    			loginIntent = new Intent( EditOrder.this, Orders.class);
		    		    else loginIntent = new Intent( EditOrder.this, SubOrders.class);
		    		}else
		    			loginIntent = new Intent( EditOrder.this, Orders.class);
				 loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 loginIntent.putExtra("tb","0");
				 loginIntent.putExtra("ttp","");
				 EditOrder.this.startActivity(loginIntent);
		         finish(); 
		   }

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
