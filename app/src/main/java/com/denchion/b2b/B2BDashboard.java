package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams") @SuppressWarnings("deprecation")
public class B2BDashboard extends Activity implements AsyncResponse {
	private DrawerLayout mDrawerLayout; 
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	HttpGetAsyncTask httpGetAsyncTask;
	// nav drawer title
	private CharSequence mDrawerTitle;
	JSONObject data = new JSONObject();
	Uri selectedImageUri = null;
	// used to store app title
	private CharSequence mTitle;
	ProgressDialog dialog;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	public static final int SUMMARY_ACTIVITY = 1;
	private static final String TAG = null;
	Menu menu;
	String Type;
	ImageView imgView;
	TextView sidenm;
	private int year;
	private int month;
	private int day;
	String todaydate;
	boolean canAddItem = false;

	int serverResponseCode = 0;

	String upLoadServerUri = null;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_b2_bdashboard);
		Utils.setActionBar(this,"Menu");
		DBHelper.loaddb(getApplicationContext());
		mTitle = mDrawerTitle = getTitle();
		 httpGetAsyncTask = new HttpGetAsyncTask(this,1);
	        httpGetAsyncTask.delegate=this;
	        if(Utils.isConnectingToInternet(getApplicationContext()))
	        	httpGetAsyncTask.execute(Utils.getUserData+Utils.getDefaults("user_id", getApplicationContext()));
	        else Toast.makeText(getApplicationContext(),"No Network Connection!", Toast.LENGTH_SHORT).show();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		
		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		if(!Utils.getDefaults("subid",getApplicationContext()).equals("0"))
		{
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		if(menu != null){
		     MenuItem item_up = menu.findItem(R.id.notify);
		     item_up.setVisible(false);
		    }
		}

		LayoutInflater mInflater = (LayoutInflater)
				this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout listHeaderView = (LinearLayout) mInflater.inflate(
				R.layout.drawer_header, null);
		mDrawerList.addHeaderView(listHeaderView);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		imgView = (ImageView) findViewById(R.id.imglogo);
		
		// adding nav drawer items to array
		loaddp();
		sidenm=(TextView)findViewById(R.id.Uname);
		sidenm.setText(Utils.getDefaults("uname", getApplicationContext()));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(0, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(1, -1)));
	
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(3, -1)));
	
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(4, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(5, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(6, -1)));
		
		
		
		final Calendar c = Calendar.getInstance();
	    year  = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day   = c.get(Calendar.DAY_OF_MONTH);
	    
	    todaydate=day+"-"+(month+1)+"-"+year;
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		/*getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);*/

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon 
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				loaddp();
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			loaddp();
			displayView(1);
		}
		
		startService(new Intent(this, MsgService.class));
		if(!Utils.getDefaults("subid",getApplicationContext()).equals("0"))
		{
			
			startService(new Intent(this, SubServices.class));
		}
		else
		{
			if(Utils.getDefaults("notify", getApplicationContext()).equals("1"))
			{
			startService(new Intent(this, Services.class));

			}
			startService(new Intent(this, CreditService.class));
		}
	 	
	// load slide menu items
	}

	/**
	 * Slide menu item click listener
	 **/
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	public void checkPermissions()
	{
		//mDrawerList.setVisibility(View.GONE);
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem menuItem) {
	
		if(Utils.getDefaults("subid",getApplicationContext()).equals("0")){
	   if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
	          return true;
	        }
	}
	     switch(menuItem.getItemId())
	     {

	     case R.id.action_pwd:
	    	 Intent loginIntent = new Intent(this, ChangePwd.class);
	          this.startActivity(loginIntent);
	    	 break;
	     case R.id.action_logout:
	    	 NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    	 nMgr.cancelAll();
	    DBHelper.clearData(getApplicationContext(),B2BDashboard.this);
	    jump();
	    	 break;
	     case R.id.notify:    	 
	    	 TurnNotify();
	    	 break;
	    	 
	     }
	     
	     return (super.onOptionsItemSelected(menuItem));
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.categories, menu);
		if(!Utils.getDefaults("subid",getApplicationContext()).equals("0"))
		{
			if(menu != null){
			     MenuItem item_up = menu.findItem(R.id.notify);
			     item_up.setVisible(false);
			    }
		}
	
		return true;
	}
	


public void loaddp()
{
	  Picasso.with(getApplicationContext()).load(R.drawable.noprofile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgView);
	File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+Utils.getDefaults("user_id", getApplicationContext())+".jpg");
    Picasso.with(getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgView);
    
	
    //Loading profile image

}

	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		//getActionBar().setTitle(mTitle);
		loaddp();
	}

	public void TurnNotify()
	{
		String msg="";
		if(Utils.getDefaults("notify", getApplicationContext()).equals("1"))
			msg="Are you sure disable Notifications?";
		else msg="Are you sure enable Notifications?";
			
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	if(Utils.getDefaults("notify", getApplicationContext()).equals("1"))
				{
		    		Utils.setDefaults("notify", "2", getApplicationContext());
		    		stopService(new Intent(B2BDashboard.this, Services.class));
					stopService(new Intent(B2BDashboard.this, ReminderService.class));
					stopService(new Intent(B2BDashboard.this, PendingTaskReminder.class));
					Toast.makeText(getApplicationContext(),"Notifications Successfully Disabled!", Toast.LENGTH_SHORT).show();
				}
		    	else
		    	{
		    		Utils.setDefaults("notify", "1", getApplicationContext());
		    		startService(new Intent(B2BDashboard.this, Services.class));
		    		startService(new Intent(B2BDashboard.this, ReminderService.class));
					startService(new Intent(B2BDashboard.this, PendingTaskReminder.class));
					Toast.makeText(getApplicationContext(),"Notifications Successfully Enabled!", Toast.LENGTH_SHORT).show();
		    	}
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}

	public void jump() {
		Intent loginIntent = new Intent(this, B2BLogin.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(loginIntent);
		finish();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	/*
	 @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
*/

	@Override
	public void processFinish(String output) {
		
		// TODO Auto-generated method stub
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(output);
			DBHelper.StoreUserData(getApplicationContext(),jsonObj);
			if(Utils.getDefaults("subid",getApplicationContext()).equals("0") && Utils.getDefaults("notify", getApplicationContext()).equals("1"))
			{
			startService(new Intent(this, ReminderService.class));
			startService(new Intent(this, PendingTaskReminder.class));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public void Profile(View v)
    {
    	  Intent loginIntent = new Intent(this, ProfilePic.class);
          this.startActivity(loginIntent);  
    }
	
    @Override
	public void onBackPressed() {
    	/*if(getActionBar().getTitle().equals("DASHBOARD"))
    	{
    	new AlertDialog.Builder(this)
		.setMessage("Do you want to Exit !")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	System.exit(0);	
		    }
		})
		 .setNegativeButton(android.R.string.no, null).show();
		
    	}
    	else {

    		if(menu != null){
    		     MenuItem item_up = menu.findItem(R.id.notify);
    		     item_up.setVisible(true);
    		    }
    		displayView(1);
    	}*/
    	loaddp();
	}
	
    public void displayView(int position) {
		//loaddp();
		
		Fragment fragment = null;
		switch (position) {
		case 0:
			setTitle("My Profile");
			fragment = new MyProfile();
			//fragment = new Notifications();
			break;
		case 1:
			setTitle("Dashboard");
			fragment = new Dashboard();
			break;
		case 2:
			Intent sIntent = new Intent(this, Partners.class);
			sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			sIntent.putExtra("tb", "0");
			
	        this.startActivity(sIntent);
			break;
		case 3:
			Intent sIntent2 = new Intent(this, NewOrder.class);
			sIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(sIntent2);
			break;
		case 4:
			Intent smIntent = new Intent(this, NewPayment.class);
			smIntent.putExtra("id","0");
			smIntent.putExtra("tp","1");
			smIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(smIntent);
			break;
		case 5:
			Intent mIntent = new Intent(this, Messages.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(mIntent);
			break;
		case 6:
			 Intent ap = new Intent(this, Products.class);
    		 ap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		 ap.putExtra("tb", "0");
	         this.startActivity(ap);
			break;
			
		case 7:
			Intent ap2 = new Intent(this, ThirdParty.class);
	   		 ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		         this.startActivity(ap2);
			break;
		case 9:
			if(menu != null){
			     MenuItem item_up = menu.findItem(R.id.notify);
			     item_up.setVisible(false);
			    }
			setTitle("Groups");
			fragment = new AddGroup();
	
			break;
	 
		case 8:
			export();
			break;
			
		case 10:
			Intent smIntent2 = new Intent(this,ViewReminders.class);
			smIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(smIntent2);
			break;

		case 11:
			Intent sIntent1 = new Intent(this, AddSubUser.class);
			sIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        this.startActivity(sIntent1);
			break;
			
		
		default:
			break;
			
		
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			
			mDrawerList.setItemChecked(position, true);
			
			mDrawerList.setSelection(position);
			
			if(position!=9)setTitle(navMenuTitles[position]);
			
			mDrawerLayout.closeDrawer(mDrawerList);
			
		} else {
			// error in creating fragment
			Log.e("MainActivity","Error in creating fragment");
		}
		
		
		
	}


	public void actions(int typ) {
		switch (typ) {
			case 1:
				Intent loginIntent = null;
				if (!Utils.getDefaults("subid", getApplicationContext()).equals("0")) {
					String[] chk1 = Utils.getDefaults("permission", getApplicationContext()).split("[ ]");
					if (Arrays.asList(chk1).contains("5"))
						loginIntent = new Intent(this, Orders.class);
					else loginIntent = new Intent(this, SubOrders.class);
				} else
					loginIntent = new Intent(this, Orders.class);

				loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				loginIntent.putExtra("tb", "0");
				loginIntent.putExtra("ttp", "");
				this.startActivity(loginIntent);
				break;
			case 2:

				displayView(7);

				break;
			case 3:
				Intent loginIntent2 = new Intent(this, Partners.class);
				loginIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				loginIntent2.putExtra("tb", "1");
				this.startActivity(loginIntent2);
				break;
			case 4:
				displayView(5);
				break;
			case 5:
				Intent cIntent = new Intent(this, Categories.class);
				cIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				cIntent.putExtra("id", "0");
				cIntent.putExtra("nm", "Categories");
				cIntent.putExtra("lvl", "1");
				this.startActivity(cIntent);
				break;
			case 6:
				Intent smIntent = new Intent(this, SendPayment.class);
				smIntent.putExtra("tb", "0");
				smIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(smIntent);
				break;
			case 7:
				Intent loginIntent3 = new Intent(this, Quotations.class);
				loginIntent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				loginIntent3.putExtra("tb", "0");
				this.startActivity(loginIntent3);
				break;
			case 8:
				displayView(11);
				break;
			case 9:
				displayView(9);
				break;
			case 10:
				Intent ap = new Intent(this, Products.class);
				ap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ap.putExtra("tb", "0");
				this.startActivity(ap);
				break;
			case 11:
				displayView(10);
				break;
			case 12:
				Intent ap2 = new Intent(this, ThirdParty.class);
				ap2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(ap2);
				break;
			case 13:
				Intent ap3 = new Intent(this, ViewCollection.class);
				ap3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(ap3);
				break;

			case 14:
				Intent ap4 = new Intent(this, Notifications.class);
				ap4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(ap4);
				break;

			case 15:
				Intent ap5 = new Intent(this, PromoOffers.class);
				ap5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(ap5);
				break;

		}
	}
    
	public void export()
	{
		new AlertDialog.Builder(this)
		.setMessage("Do you Want to Export Backup this data?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	dialogDetails();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	public void dialogDetails() {
		

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
		        
	        
          builderSingle.setTitle("Backup Data");
          
   
        	  builderSingle.setView(promptsView);
           
          builderSingle.setPositiveButton("BACKUP NOW",
                  new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                    	
                    	  
							exportToExcel(categories.isChecked(),groups.isChecked(),partners.isChecked(),orders.isChecked(),subusers.isChecked(),specifications.isChecked(),payments.isChecked(),quotations.isChecked(),backup.isChecked());
                    
                 
                      }
                  });

     
          builderSingle.show();
      }

    
	private void exportToExcel(Boolean chk1,Boolean chk2,Boolean chk3,Boolean chk4,Boolean chk5,Boolean chk6,Boolean chk7,Boolean chk8,Boolean chk9) {
    	
		if(chk1!=false || chk2!=false || chk3!=false  || chk4!=false || chk5!=false || chk6!=false || chk7!=false || chk8!=false)
		{
		 int code =Utils.rand();
		
		final String fileName = "b2b_"+code+"_"+todaydate+".xls";
		
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
		Cursor h1=DBHelper.getData(getApplicationContext(),Utils.bsu,"");
		Cursor i1=DBHelper.getData(getApplicationContext(),Utils.bps,"");
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
				sheet4.addCell(new Label(1, 0,"QTY"));
				sheet4.addCell(new Label(2, 0,"PRODUCT"));
				sheet4.addCell(new Label(3, 0,"VARIANT"));
				sheet4.addCell(new Label(4, 0,"BREADTH"));
				sheet4.addCell(new Label(5, 0,"THICKNESS"));
				sheet4.addCell(new Label(6, 0,"WEIGHT"));
				sheet4.addCell(new Label(7, 0,"DELIVERY"));
				sheet4.addCell(new Label(8, 0,"TRANSPORT"));
				sheet4.addCell(new Label(9, 0,"PRICE"));
				sheet4.addCell(new Label(10, 0,"MY REMARKS"));
				sheet4.addCell(new Label(11, 0,"REMARKS"));
				sheet4.addCell(new Label(12, 0,"DISPATCH DATE"));
				sheet4.addCell(new Label(13, 0,"DISPATCH TIME"));
				sheet4.addCell(new Label(14, 0,"CONFIRM DATE"));
				sheet4.addCell(new Label(15, 0,"CONFIRM TIME"));
				sheet4.addCell(new Label(16, 0,"ORDER DATE"));
				sheet4.addCell(new Label(17, 0,"ORDER TIME"));
				sheet4.addCell(new Label(18, 0,"CATEGORY"));
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
				sheet7.addCell(new Label(0, 0,"SPECIFICATION"));
				sheet7.addCell(new Label(1, 0,"TYPE"));
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
             		    sheet4.addCell(new Label(0, ff,f1.getString(5)));
             			sheet4.addCell(new Label(1, ff,f1.getString(7)));
             			sheet4.addCell(new Label(2, ff,f1.getString(8)));
             			sheet4.addCell(new Label(3, ff,f1.getString(9)));
             			sheet4.addCell(new Label(4, ff,f1.getString(10)));
             			sheet4.addCell(new Label(5, ff,f1.getString(11)));
             			sheet4.addCell(new Label(6, ff,f1.getString(12)));
             			sheet4.addCell(new Label(7, ff,f1.getString(13)));
             			sheet4.addCell(new Label(8, ff,f1.getString(14)));
             			sheet4.addCell(new Label(9, ff,f1.getString(15)));
             			sheet4.addCell(new Label(10, ff,f1.getString(16)));
             			sheet4.addCell(new Label(11, ff,f1.getString(17)));
             			sheet4.addCell(new Label(12, ff,f1.getString(18)));
             			sheet4.addCell(new Label(13, ff,f1.getString(23)));
             			sheet4.addCell(new Label(14, ff,f1.getString(24)));
             			sheet4.addCell(new Label(15, ff,f1.getString(26)));
             			sheet4.addCell(new Label(16, ff,f1.getString(27)));
             			sheet4.addCell(new Label(17, ff,f1.getString(28)));
             			sheet4.addCell(new Label(18, ff,f1.getString(29)));
             			sheet4.addCell(new Label(19, ff,DBHelper.Cname(getApplicationContext(),f1.getString(1))));
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
             		while(i1.moveToNext())
             		{
             		    sheet7.addCell(new Label(0, ii,i1.getString(4)));
             			sheet7.addCell(new Label(1, ii,producttype(Integer.parseInt(i1.getString(3)))));

             			ii++;
             		}
             		i1.close();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if(chk9==true){


			selectedImageUri=Uri.fromFile(new File(directory, fileName));
			

		
			//closing cursor
			
			
	             dialog = ProgressDialog.show(B2BDashboard.this,"","Backing up",true);

	             new Thread(new Runnable() {
	                 @Override
	                 public void run() {
	                     //creating new thread to handle Http Operations
	                	 String str=Utils.getPath(getApplicationContext(),selectedImageUri);
	                	 
	                     uploadFile(str);
	                 }
	             }).start();
	        
			}
		Toast.makeText(getApplicationContext(), "Sheet Exported! Check in Download Folder", Toast.LENGTH_SHORT).show();		
		}else {
			Toast.makeText(getApplicationContext(), "Please Select one Option", Toast.LENGTH_SHORT).show();
		}
	}
    
	private String producttype(int type)
	{
		String str="";
		switch(type)
		{
			case 1:str="Product Name"; break;	
			case 2:str="Length"; break;	
			case 3:str="Breadth"; break;	
			case 4:str="Thickness"; break;	
			case 5:str="Weight"; break;	
			case 6:str="Variant"; break;
			
		}
		
		return str;
	}
	
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
	                     Toast.makeText(B2BDashboard.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
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
	                 Toast.makeText(B2BDashboard.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
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
	                     Toast.makeText(B2BDashboard.this, "File Not Found", Toast.LENGTH_SHORT).show();
	                 }
	             });
	         } catch (MalformedURLException e) {
	             e.printStackTrace();
	             runOnUiThread(new Runnable() {
	                 @Override
	                 public void run() {
	                     Toast.makeText(B2BDashboard.this, "URL Error!", Toast.LENGTH_SHORT).show();
	                 }
	             });

	         } catch (IOException e) {
	             e.printStackTrace();
	             runOnUiThread(new Runnable() {
	                 @Override
	                 public void run() {
	                     Toast.makeText(B2BDashboard.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
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
	
	
	

}
