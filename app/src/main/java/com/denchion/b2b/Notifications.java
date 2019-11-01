package com.denchion.b2b;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class Notifications extends Activity{

	JSONObject data = new JSONObject();
	JSONObject jsonObj;
	List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
	Button AddGrp;
	EditText gname;
	EditText olimit;
	EditText searchbox;
	Spinner sp_1;
	 private int year;
	    private int month;
	    private int day;
	    String todatDate;
	    SimpleAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gb_notifications);
		DBHelper.loaddb(getApplicationContext());
		Utils.setActionBar(this,"Notifications");
		final Calendar cd = Calendar.getInstance();
	    year  = cd.get(Calendar.YEAR);
	    month = cd.get(Calendar.MONTH);
	    day   = cd.get(Calendar.DAY_OF_MONTH);
      todatDate=Utils.parseDateToddMMyyyy3(year+"-"+(month+1)+"-"+day);
      
      sp_1 = (Spinner)findViewById(R.id.sp_1);
      
      getNots(" order by notidttime DESC");
      sp_1.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	String text = sp_1.getSelectedItem().toString();
		    	String cond = " order by notidttime DESC";
		    	if(!text.equals("All"))
		    	{
		    		switch(text)
		    		{
		    		case "Orders": cond=" where ntype in (3,7,8,9,11,22) order by notidttime DESC"; break;
		    		case "Quotations": cond=" where ntype in (6) order by notidttime DESC"; break;
		    		case "Messages": cond=" where ntype in (4,10) order by notidttime DESC"; break;
		    		case "Payments": cond=" where ntype in (5) order by notidttime DESC"; break;
		    		case "Partners": cond=" where ntype in (1,2) order by notidttime DESC"; break;
		    		case "Collection": cond=" where ntype in (18) order by notidttime DESC"; break;
		    		case "Groups": cond=" where ntype in (15) order by notidttime DESC"; break;
		    		case "Info/promo": cond=" where ntype in (23) order by notidttime DESC"; break;
		    		case "Expired": cond=" where ntype=23 and expiry < '"+todatDate+"' order by notidttime DESC"; break;
		    		
		    		
		    		}
		    		
		    		
		    		 getNots(cond);
		    	}
		    	else  getNots(cond);
		    	
		    		
		    	
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
      
		
	}
	

	
	 public void getNots(String q) 
	 {
		 
		 
		final Cursor c=DBHelper.getData(getApplicationContext(),Utils.bn,q);
 		if(c.getCount()==0)
 		{
 			
 			users.clear();
 			if(adapter!=null)adapter.notifyDataSetChanged();
 			return;
 		}
 		else 
 		{
 			
 			users.clear();
     		while(c.moveToNext())
     		{

		    HashMap<String, String> hm = new HashMap<String, String>();
		    hm.put("uname",c.getString(4));
		   // File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+c.getString(0)+".jpg");  
		    
		    String ico = "";
		    
		    switch(Integer.parseInt(c.getString(3)))
		    {
		    case 0: ico = String.valueOf(R.drawable.n_reminders);break;
			case 1: ico = String.valueOf(R.drawable.n_icon_partners); break;
			case 2: ico = String.valueOf(R.drawable.n_icon_partners); break;
			case 3: ico = String.valueOf(R.drawable.n_icon_order); break;
			case 4: ico = String.valueOf(R.drawable.n_icon_message);break;
			case 5: ico = String.valueOf(R.drawable.n_icon_payment); break;
			 case 6: ico = String.valueOf(R.drawable.n_icon_quote); break;
			 case 7: ico = String.valueOf(R.drawable.n_icon_order); break;
			 case 8: ico = String.valueOf(R.drawable.n_icon_order); break;
			 case 9: ico = String.valueOf(R.drawable.n_icon_order); break;
			 case 10: ico = String.valueOf(R.drawable.n_icon_message); break;
			 case 11: ico = String.valueOf(R.drawable.n_icon_order); break;
			 case 15: ico = String.valueOf(R.drawable.n_icon_group); break;
			 case 16: ico = String.valueOf(R.drawable.n_reminders);break;
			case 18: ico = String.valueOf(R.drawable.n_collection); break;
			case 22: ico = String.valueOf(R.drawable.n_icon_order); break;
			case 23: ico = String.valueOf(R.drawable.n_reminders); break;
	
		    }
		    
		String exx= "";
		    if(compareDate(c.getString(9))<0 && !c.getString(9).equals("0000-00-00"))
            {
		    	exx = "Expired";
            }
		    
		    hm.put("img",ico);
		    hm.put("date",c.getString(5));
		    String idd = c.getString(2);
		    if(c.getString(3).equals("23"))idd = c.getString(0);
		    hm.put("tp",c.getString(3));
		    hm.put("status",exx);
		    hm.put("content_id",idd);
            users.add(hm);
             }
     		
     		 String[] from = { "uname","img","content_id","tp","date","status"};
             int[] to = {R.id.u_title,R.id.listimg,R.id.content_id,R.id.u_status,R.id.u_type,R.id.n_status};
             adapter = new SimpleAdapter(this, users,R.layout.list_layout_notifications , from, to); 
             ListView mylist = (ListView) findViewById(R.id.nots_lists);
             mylist.setAdapter(null);
             adapter.notifyDataSetChanged();
             mylist.setAdapter(adapter);
             adapter.notifyDataSetChanged();
            
             // Click event for single list row
             
             mylist.setOnItemClickListener(new OnItemClickListener(){

                 @Override
                 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                  
                	 ListView lv = (ListView) arg0;
    	             TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
    	             
    	             TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
    	             
    	             TextView fishtextview3=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
    	            
    	             String fieldname = fishtextview.getText().toString();
    	             String fieldname2 = fishtextview2.getText().toString();
    	             String fieldname3 = fishtextview3.getText().toString();
    	             
    	           notiGo(Integer.parseInt(fieldname2),fieldname,fieldname3);
    	         
                 }
                 });
             
           
     	
 		}
	 }
	 
	 
	 public int compareDate(String dates) {
		 
		 int rt = 0;
		    try {
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        Date date1 = sdf.parse(dates);
		        Date date2 = sdf.parse(todatDate);

		        rt = date1.compareTo(date2);
		    } catch(Exception e) { }
		    
		    return rt;
		}
	 
	 public void notiGo(int cond,String pid,String txt){
			Intent intent = null;
		switch(cond)
	 	{
	 	case 0:
	 		intent = new Intent(this, B2BDashboard.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 		break;
	 	case 1:
	 		intent = new Intent(this, Partners.class);
	 	     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		intent.putExtra("tb","2");
	 		  this.startActivity(intent);
	 		break;
	 	case 2:
	 		intent = new Intent(this, Partners.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		intent.putExtra("tb","1");
	 		  this.startActivity(intent);
	 		break;
	 	case 3:
			String tbb = "2";
			 String []chk1=txt.split("[ ]");
			if (Arrays.asList(chk1).contains("direct")) 
			{
				 tbb = "4";
			}
			
			intent = new Intent(this, Orders.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("tb",tbb);
			intent.putExtra("ttp","");
	 		  this.startActivity(intent);
	 		break;
	 	case 4:
	 		intent = new Intent(this, Messages.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 	break;
	 		 
	 	case 5:
	 		intent = new Intent(this, SendPayment.class);
	 		intent.putExtra("tb","1");
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 	break;
	 	
	 	case 6:
	 		intent = new Intent(this, Quotations.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 	break;
	 	
	 	case 7:
	 		intent = new Intent(this, Orders.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		 intent.putExtra("tb","0");
	 		 intent.putExtra("ttp","");
	 		  this.startActivity(intent);
	 	break;
	 	
	 	case 8:
	 		intent = new Intent(this, Orders.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		intent.putExtra("tb","0");
	 		intent.putExtra("ttp","");
	 		  this.startActivity(intent);
	 		break;
	 		
	 	case 9:
	 		intent = new Intent(this, Orders.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		intent.putExtra("tb","1");
	 		intent.putExtra("ttp","");
	 		  this.startActivity(intent);
	 	
	 		break;
	 		
	 	case 10:
	 		intent = new Intent(this, Messages.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 	break;
	 		
	 	case 11:
	 		intent = new Intent(this, Orders.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		intent.putExtra("tb","0");
	 		intent.putExtra("ttp","");
	 		  this.startActivity(intent);
	 	
	 		break;
	 		
	 		
	 	case 15:
	 		 intent = new Intent(this, GroupNotDetails.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		 intent.putExtra("id",pid+"");
	 		  this.startActivity(intent);
	 		break;
	 		
	 	case 16:
	 		intent = new Intent(this, B2BDashboard.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 		break;
	 	case 18:
	 		intent = new Intent(this, ViewCollection.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		  this.startActivity(intent);
	 		break;
	 	case 22:
	 		intent = new Intent(this, Orders.class);
	 		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 		 intent.putExtra("ttp",pid+"");
	 		 intent.putExtra("tb","0");
	 		  this.startActivity(intent);
	 		break;
	 		
	 	case 23:
			intent = new Intent(this, NotificationDetails.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("ttp",pid+"");
	 		  this.startActivity(intent);
	 		break;
	 	
	 		
	 		
	 	}
		
		
		}
	 
		  
	}

        
