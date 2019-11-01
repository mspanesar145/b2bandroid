package com.denchion.b2b;

import java.io.File;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
	
	static SQLiteDatabase db;
	
	public static void loaddb(Context c)
	{
		 db=c.openOrCreateDatabase("gb_b2b", Context.MODE_PRIVATE, null);
	}
	
	public static void  createTbls(Context c)
	{
		
		
		   String query="CREATE TABLE IF NOT EXISTS "+Utils.usr+" (" +
				/*0*/	"user_id INT," +
				/*1*/	"full_name VARCHAR," +
				/*2*/	"username VARCHAR," +
				/*3*/	"email VARCHAR," +
				/*4*/	"password VARCHAR," +
				/*5*/	"mobile VARCHAR," +
				/*6*/	"address VARCHAR," +
				/*7*/	"reference VARCHAR," +
				/*8*/	"business VARCHAR," +
				/*9*/	"payment_policy VARCHAR," +
				/*10*/	"date VARCHAR," +
				/*11*/	"time VARCHAR," +
				/*12*/	"city VARCHAR," +
				/*13*/	"state VARCHAR," +
				/*14*/	"sent_requests VARCHAR," +
				/*15*/	"pending_requests VARCHAR," +
				/*16*/	"accepted VARCHAR," +
				/*17*/	"sec_email VARCHAR," +
				/*18*/	"sec_mobile VARCHAR," +
				/*19*/	"company_name VARCHAR," +
				/*20*/	"country VARCHAR," +
				"UNIQUE(user_id) ON CONFLICT REPLACE)";
				db.execSQL(query);
				
				String query1a="CREATE TABLE IF NOT EXISTS b2b_search_users (" +
						/*0*/	"user_id INT," +
						/*1*/	"full_name VARCHAR," +
						/*2*/	"email VARCHAR," +
						/*3*/	"mobile VARCHAR," +
						/*4*/	"address VARCHAR," +
						/*5*/	"reference VARCHAR," +
						/*6*/	"business VARCHAR," +
						/*7*/	"payment_policy VARCHAR," +
						/*8*/	"city VARCHAR," +
						/*9*/	"state VARCHAR," +
						/*10*/	"date VARCHAR," +
						/*11*/	"time VARCHAR," +
						/*12*/	"sec_email VARCHAR," +
						/*13*/	"sec_mobile VARCHAR," +
						/*14*/	"company_name VARCHAR," +
						/*15*/	"country VARCHAR," +
						"UNIQUE(user_id) ON CONFLICT REPLACE)";
						db.execSQL(query1a);
				
				String query1="CREATE TABLE IF NOT EXISTS "+Utils.bsu+" (" +
				/*0*/	"user_id INT," +
				/*1*/	"full_name VARCHAR," +
				/*2*/	"username VARCHAR," +
				/*3*/	"password VARCHAR," +
				/*4*/	"mobile VARCHAR," +
				/*5*/	"designation VARCHAR," +
				/*6*/	"department VARCHAR," +
				/*7*/	"permission VARCHAR," +
				/*8*/	"date VARCHAR," +
				/*9*/	"time VARCHAR," +
				"UNIQUE(user_id) ON CONFLICT REPLACE)";
				db.execSQL(query1);
				
				String query2="CREATE TABLE IF NOT EXISTS "+Utils.quote+" (" +
				/*0*/	"quotation_id INT," +
				/*1*/	"sender_id VARCHAR," +
				/*2*/	"sub_sender_id VARCHAR," +
				/*3*/	"reciever_id VARCHAR," +
				/*4*/	"sub_receiver_id VARCHAR," +
				/*5*/	"quotation_text VARCHAR," +
				/*6*/	"productname VARCHAR," +
				/*7*/	"conditions VARCHAR," +
				/*8*/	"path VARCHAR," +
				/*9*/	"type VARCHAR," +
				/*10*/	"date VARCHAR," +
				/*11*/	"time VARCHAR," +
				/*12*/	"expiry VARCHAR," +
				"UNIQUE(quotation_id) ON CONFLICT REPLACE)";
				db.execSQL(query2);    
			
				String query3="CREATE TABLE IF NOT EXISTS "+Utils.py+" (" +
				/*0*/	"payment_id INT," +
				/*1*/	"sender_id VARCHAR," +
				/*2*/	"sub_sender_id VARCHAR," +
				/*3*/	"receiver_id VARCHAR," +
				/*4*/	"sub_receiver_id VARCHAR," +
				/*5*/	"payment VARCHAR," +
				/*6*/	"payment_type VARCHAR," +
				/*7*/	"remarks VARCHAR," +
				/*8*/	"date VARCHAR," +
				/*9*/	"time VARCHAR," +
				/*10*/	"status VARCHAR," +
				/*11*/	"sender_nm VARCHAR," +
				/*12*/	"receiver_nm VARCHAR," +
				"UNIQUE(payment_id) ON CONFLICT REPLACE)";
				db.execSQL(query3); 
				
				
				String query3a="CREATE TABLE IF NOT EXISTS "+Utils.bpcl+" (" +
						/*0*/	"id INT," +
						/*1*/	"uid VARCHAR," +
						/*2*/	"userid VARCHAR," +
						/*3*/	"partner VARCHAR," +
						/*4*/	"amount VARCHAR," +
						/*5*/	"remarks VARCHAR," +
						/*6*/	"date VARCHAR," +
						/*7*/	"time VARCHAR," +
						/*8*/	"mode VARCHAR," +
						"UNIQUE(id) ON CONFLICT REPLACE)";
						db.execSQL(query3a); 
				
				
				String query4="CREATE TABLE IF NOT EXISTS "+Utils.partners+" (" +
				/*0*/	"partner_id INT," +
				/*1*/	"full_name VARCHAR," +
				/*2*/	"email VARCHAR," +
				/*3*/	"mobile VARCHAR," +
				/*4*/	"address VARCHAR," +
				/*5*/	"reference VARCHAR," +
				/*6*/	"business VARCHAR," +
				/*7*/	"payment_policy VARCHAR," +
				/*8*/	"city VARCHAR," +
				/*9*/	"state VARCHAR," +
				/*10*/	"date VARCHAR," +
				/*11*/	"time VARCHAR," +
				/*12*/	"sec_email VARCHAR," +
				/*13*/	"sec_mobile VARCHAR," +
				/*14*/	"company_name VARCHAR," +
				/*15*/	"country VARCHAR," +
				"UNIQUE(partner_id) ON CONFLICT REPLACE)";
				db.execSQL(query4); 
				
				String query4a="CREATE TABLE IF NOT EXISTS "+Utils.partners_subs+" (" +
						/*0*/	"user_id INT," +
						/*1*/	"parent INT," +
						/*2*/	"full_name VARCHAR," +
						"UNIQUE(user_id) ON CONFLICT REPLACE)";
						db.execSQL(query4a);
				
				String query5="CREATE TABLE IF NOT EXISTS "+Utils.to+" (" +
				/*0*/	"tracking_id INT," +
				/*1*/	"order_id VARCHAR," +
				/*2*/	"remarks VARCHAR," +
				/*3*/	"courier VARCHAR," +
				/*4*/	"courier_tracking_no VARCHAR," +
				/*5*/	"status VARCHAR," +
				/*6*/	"date VARCHAR," +
				/*7*/	"time VARCHAR," +
				"UNIQUE(tracking_id) ON CONFLICT REPLACE)";
				db.execSQL(query5); 
				
				String query6="CREATE TABLE IF NOT EXISTS "+Utils.orders+" (" +
						/*0*/	"order_id INT," +
						/*1*/	"sender_id VARCHAR," +
						/*2*/	"sub_sender_id VARCHAR," +
						/*3*/	"receiver_id VARCHAR," +
						/*4*/	"receiver_name VARCHAR," +
						/*5*/	"sub_receiver_id VARCHAR," +
						/*6*/	"delivery_type VARCHAR," +
						/*7*/	"transport_type VARCHAR," +
						/*8*/	"product_data VARCHAR," +
						/*9*/	"order_price VARCHAR," +
						/*10*/	"sender_remarks VARCHAR," +
						/*11*/	"receiver_remarks VARCHAR," +
						/*12*/	"accept_status  VARCHAR," +
						/*13*/	"attachment_id VARCHAR," +
						/*14*/	"order_read_by_receiver VARCHAR," +
						/*15*/	"dispatched VARCHAR," +
						/*16*/	"dispatched_date  VARCHAR," +
						/*17*/	"dispatched_time VARCHAR," +
						/*18*/	"confirm_received VARCHAR," +
						/*19*/	"confirm_date  VARCHAR," +
						/*20*/	"confirm_time VARCHAR," +
						/*21*/	"order_date VARCHAR," +
						/*22*/	"order_time VARCHAR," +
						/*23*/	"tremarks VARCHAR," +
						/*24*/	"courier VARCHAR," +
						/*25*/	"courier_tracking_no VARCHAR," +
						/*26*/	"tdate VARCHAR," +
						/*27*/	"ttime VARCHAR," +
						/*28*/	"feedback VARCHAR," +
						/*29*/	"fremarks VARCHAR," +
						/*30*/	"fdate VARCHAR," +
						/*31*/	"ftime VARCHAR," +
						/*32*/	"fid VARCHAR," +
						/*33*/	"ignore_status VARCHAR," +
						/*34*/	"quote_ref VARCHAR," +
						/*35*/	"hidefields VARCHAR," +
						/*36*/	"credit VARCHAR," +
						/*37*/	"approve_status VARCHAR," +
						/*38*/	"schedule_date VARCHAR," +
						/*39*/	"ordtype VARCHAR," +
						/*40*/	"cancel VARCHAR," +
						/*41*/	"attachment VARCHAR," +
				        "UNIQUE(order_id) ON CONFLICT REPLACE)";
				db.execSQL(query6); 
				
				String query7="CREATE TABLE IF NOT EXISTS "+Utils.grptbl+" (" +
				/*0*/	"group_id INT," +
				/*1*/	"group_name VARCHAR," +
				/*2*/	"order_limit VARCHAR," +
				/*3*/	"user_id VARCHAR," +
				/*4*/	"description VARCHAR," +
				"UNIQUE(group_id) ON CONFLICT REPLACE)";
				db.execSQL(query7); 
				
				String query7b="CREATE TABLE IF NOT EXISTS "+Utils.grptbl+" (" +
						/*0*/	"uni_id INT," +
						/*1*/	"product_name VARCHAR," +
						/*2*/	"price VARCHAR," +
						/*3*/	"order_id VARCHAR," +
						/*4*/	"sender VARCHAR," +
						/*5*/	"receiver VARCHAR," +
						/*6*/	"date VARCHAR," +
						"UNIQUE(uni_id) ON CONFLICT REPLACE)";
						db.execSQL(query7b); 
				
				String query7a="CREATE TABLE IF NOT EXISTS "+Utils.grprltbl+" (" +
						/*0*/	"rid INT," +
						/*1*/	"group_id VARCHAR," +
						/*2*/	"user_id VARCHAR," +
						/*3*/	"user VARCHAR," +
						/*4*/	"obj VARCHAR," +
						"UNIQUE(rid) ON CONFLICT REPLACE)";
						db.execSQL(query7a); 
				
				String query8="CREATE TABLE IF NOT EXISTS "+Utils.msgtbl+" (" +
				/*0*/	"message_id INT," +
				/*1*/	"sender_id VARCHAR," +
				/*2*/	"receiver_id VARCHAR," +
				/*3*/	"common_obj VARCHAR," +
				/*4*/	"message VARCHAR," +
				/*5*/	"parent VARCHAR,"+
				/*6*/	"status VARCHAR," +
				/*7*/	"sent VARCHAR," +
				/*8*/	"date VARCHAR," +
				/*9*/	"time VARCHAR," +
				/*10*/	"parent_nm VARCHAR," +
				/*11*/	"sender_nm VARCHAR," +
				/*12*/	"receiver_nm VARCHAR," +
				/*13*/	"sender_receipt VARCHAR," +
				/*14*/	"receiver_recipt VARCHAR," +
				"UNIQUE(message_id) ON CONFLICT REPLACE)";
				db.execSQL(query8); 
				
				String query9="CREATE TABLE IF NOT EXISTS "+Utils.cattbl+" (" +
				/*0*/	"category_id INT,"+
				/*1*/	"user_id VARCHAR,"+
				/*2*/	"name VARCHAR," +
				/*3*/	"parent VARCHAR,"+
				/*4*/	"level VARCHAR,"+
				"UNIQUE(category_id) ON CONFLICT REPLACE)";
				db.execSQL(query9); 
				
				
				String query10="CREATE TABLE IF NOT EXISTS b2b_attachments (" +
				/*0*/	"attachment_id INT," +
				/*1*/	"path VARCHAR," +
				/*2*/	"type VARCHAR," +
				"UNIQUE(attachment_id) ON CONFLICT REPLACE)";
				db.execSQL(query10); 
				
				String query11="CREATE TABLE IF NOT EXISTS "+Utils.bp+" (" +
						/*0*/	"product_id INT," +
						/*1*/	"product_name VARCHAR," +
						/*2*/	"catid VARCHAR," +
						/*3*/	"user_id VARCHAR," +
						/*4*/	"mrp VARCHAR," +
						/*5*/	"description VARCHAR," +
						/*6*/	"img VARCHAR," +
						/*7*/	"catnm VARCHAR," +
						"UNIQUE(product_id) ON CONFLICT REPLACE)";
						db.execSQL(query11); 
						
						String query11a="CREATE TABLE IF NOT EXISTS "+Utils.bps+" (" +
								/*0*/	"id INT," +
								/*1*/	"product_id VARCHAR," +
								/*2*/	"user_id VARCHAR," +
								/*3*/	"name VARCHAR," +
								/*4*/	"value VARCHAR," +
								"UNIQUE(id) ON CONFLICT REPLACE)";
								db.execSQL(query11a); 
						
						String query12="CREATE TABLE IF NOT EXISTS "+Utils.feedback+" (" +
								/*0*/	"id INT," +
								/*1*/	"orderid VARCHAR," +
								/*2*/	"feedback VARCHAR," +
								/*3*/	"remarks VARCHAR," +
								/*4*/	"attachment_id VARCHAR," +
								/*5*/	"date VARCHAR," +
								/*6*/	"time VARCHAR," +
								"UNIQUE(id) ON CONFLICT REPLACE)";
								db.execSQL(query12); 
								
								String query12a="CREATE TABLE IF NOT EXISTS "+Utils.subactivties+" (" +
										/*0*/	"activity_id INT," +
										/*1*/	"subid VARCHAR," +
										/*2*/	"parent VARCHAR," +
										/*3*/	"activity VARCHAR," +
										/*4*/	"date VARCHAR," +
										/*5*/	"time VARCHAR," +
										/*6*/	"type VARCHAR," +
										/*7*/	"typeid VARCHAR," +
										"UNIQUE(activity_id) ON CONFLICT REPLACE)";
										db.execSQL(query12a); 
										
										String query12b="CREATE TABLE IF NOT EXISTS "+Utils.reminder+" (" +
												/*0*/	"reminder_id INT," +
												/*1*/	"user_id VARCHAR," +
												/*2*/	"note VARCHAR," +
												/*3*/	"date VARCHAR," +
												/*4*/	"readd VARCHAR," +
												/*5*/	"time VARCHAR," +
												"UNIQUE(reminder_id) ON CONFLICT REPLACE)";
												db.execSQL(query12b); 
												
												String query12c="CREATE TABLE IF NOT EXISTS "+Utils.oldprc+" (" +
														/*0*/	"uni_id VARCHAR," +
														/*1*/	"product_name VARCHAR," +
														/*2*/	"price VARCHAR," +
														/*3*/	"order_id VARCHAR," +
														/*4*/	"sender VARCHAR," +
														/*5*/	"receiver VARCHAR," +
														/*6*/	"date VARCHAR," +
														"UNIQUE(uni_id) ON CONFLICT REPLACE)";
														db.execSQL(query12c); 
														
														String query12d="CREATE TABLE IF NOT EXISTS "+Utils.bcl+" (" +
																/*0*/	"clid VARCHAR," +
																/*1*/	"partner_id VARCHAR," +
																/*2*/	"user_id VARCHAR," +
																/*3*/	"limits VARCHAR," +
																"UNIQUE(clid) ON CONFLICT REPLACE)";
																db.execSQL(query12d);
																
																String query13d="CREATE TABLE IF NOT EXISTS "+Utils.bn+" (" +
																		/*0*/	"noti_id VARCHAR," +
																		/*1*/	"user_id VARCHAR," +
																		/*2*/	"partner_id VARCHAR," +
																		/*3*/	"ntype VARCHAR," +
																		/*4*/	"txt VARCHAR," +
																		/*5*/	"notidttime VARCHAR," +
																		/*6*/	"date VARCHAR," +
																		/*7*/	"read_status VARCHAR," +
																		/*8*/	"delstatus VARCHAR," +
																		/*9*/	"expiry VARCHAR," +
																		"UNIQUE(noti_id) ON CONFLICT REPLACE)";
																		db.execSQL(query13d);
																		
																		String query13c="CREATE TABLE IF NOT EXISTS "+Utils.po+" (" +
																				/*0*/	"noti_id VARCHAR," +
																				/*1*/	"user_id VARCHAR," +
																				/*2*/	"partner_id VARCHAR," +
																				/*3*/	"ntype VARCHAR," +
																				/*4*/	"txt VARCHAR," +
																				/*5*/	"notidttime VARCHAR," +
																				/*6*/	"date VARCHAR," +
																				/*7*/	"read_status VARCHAR," +
																				/*8*/	"delstatus VARCHAR," +
																				/*9*/	"expiry VARCHAR," +
																				"UNIQUE(noti_id) ON CONFLICT REPLACE)";
																				db.execSQL(query13c);
		              
				
	}
	
	public static int chkLogin(Context c)
	{
		
		int rt=1;
		Cursor d=null;
		try{
		d=db.rawQuery("SELECT * FROM "+Utils.usr+"", null);
        
         if(d.getCount()!=0 && d.moveToFirst()) rt=2;
		}finally{if(d!=null)d.close();}
		return rt;
	}
	
	
	public static void UpdateProfile(Context c,JSONArray Data) throws JSONException
	{
		JSONObject jsonObject = Data.getJSONObject(0); 
		db.execSQL("INSERT INTO "+Utils.usr+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("username")+"','"+jsonObject.getString("email")+"','"+jsonObject.getString("password")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("reference")+"','"+jsonObject.getString("business")+"','"+jsonObject.getString("payment_policy")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("city")+"','"+jsonObject.getString("state")+"','"+jsonObject.getString("sent_requests")+"','"+jsonObject.getString("pending_requests")+"','"+jsonObject.getString("accepted")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");

	}
	
	public static void StoreMyDetails(Context c,JSONArray Data)
	{
		 JSONObject jsonObject;
		try {
			db.execSQL("Delete FROM "+Utils.usr);
			jsonObject = Data.getJSONObject(0);
			
			db.execSQL("INSERT INTO "+Utils.usr+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("username")+"','"+jsonObject.getString("email")+"','"+jsonObject.getString("password")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("reference")+"','"+jsonObject.getString("business")+"','"+jsonObject.getString("payment_policy")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("city")+"','"+jsonObject.getString("state")+"','"+jsonObject.getString("sent_requests")+"','"+jsonObject.getString("pending_requests")+"','"+jsonObject.getString("accepted")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	public static void StoreLogin(JSONObject jsonObj,Context c)
	{
		try {
				
				JSONArray Data = jsonObj.getJSONArray("user");
				 JSONObject jsonObject = Data.getJSONObject(0); 
				
				if(jsonObject.getString("parent").equals("0")){
					Utils.setDefaults("user_id", jsonObject.getString("user_id"), c);
					Utils.setDefaults("subid", "0", c);
					Utils.setDefaults("credit", jsonObject.getString("reference"), c);
					db.execSQL("INSERT INTO "+Utils.usr+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("username")+"','"+jsonObject.getString("email")+"','"+jsonObject.getString("password")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("reference")+"','"+jsonObject.getString("business")+"','"+jsonObject.getString("payment_policy")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("city")+"','"+jsonObject.getString("state")+"','"+jsonObject.getString("sent_requests")+"','"+jsonObject.getString("pending_requests")+"','"+jsonObject.getString("accepted")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");
					Utils.setDefaults("email", jsonObject.getString("email"), c);
				}
				else 
				{
					 JSONArray Data2 = jsonObj.getJSONArray("parent");
					 JSONObject jsonObject2 = Data2.getJSONObject(0); 
	
					 db.execSQL("INSERT INTO "+Utils.usr+" VALUES('"+jsonObject2.getString("user_id")+"','"+jsonObject2.getString("full_name")+"','"+jsonObject2.getString("username")+"','"+jsonObject2.getString("email")+"','"+jsonObject2.getString("password")+"','"+jsonObject2.getString("mobile")+"','"+jsonObject2.getString("address")+"','"+jsonObject2.getString("reference")+"','"+jsonObject2.getString("business")+"','"+jsonObject2.getString("payment_policy")+"','"+jsonObject2.getString("date")+"','"+jsonObject2.getString("time")+"','"+jsonObject2.getString("city")+"','"+jsonObject2.getString("state")+"','"+jsonObject2.getString("sent_requests")+"','"+jsonObject2.getString("pending_requests")+"','"+jsonObject2.getString("accepted")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");
					 Utils.setDefaults("user_id", jsonObject.getString("parent"), c);
					 Utils.setDefaults("subid", jsonObject.getString("user_id"),  c);
					 Utils.setDefaults("credit", jsonObject.getString("reference"), c);
					 Utils.setDefaults("email", jsonObject.getString("email"), c);
					 
				}
				Utils.setDefaults("uname", jsonObject.getString("full_name"), c);
				
				Utils.setDefaults("permission", jsonObject.getString("permission"), c);
			Utils.setDefaults("notify", "1", c);
			
			}
			
			
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}

	public static Cursor getData(Context c,String tbl,String param)
	{
		Cursor d=db.rawQuery("SELECT * FROM "+tbl+" "+param, null);
		
		return d;
	}
	
	public static int LevelMaxx(Context c)
	{
		Cursor d=null;
	    int mx=0;
		try{
		
		d=db.rawQuery("SELECT max(level) as mx FROM "+Utils.cattbl+" ", null);
		if(d.moveToFirst())mx=Integer.parseInt(d.getString(0));
		}finally{if(d!=null)d.close();}
		return mx;
	}
	
	public static Cursor getJoinData(Context c,String slt,String tbl,String param)
	{
		Cursor d=null;
	
		d=db.rawQuery("SELECT "+slt+" FROM "+tbl+" "+param, null);
	   
	   return d;
	}
	
	
	public static void getRow(String tbl,String param)
	{

		db.rawQuery("DELETE FROM "+tbl+" "+param, null);

	}
	
	public static void storeReminders(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.reminder+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.reminder+" VALUES('"+jsonObject.getString("reminder_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("note")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("readd")+"','"+jsonObject.getString("time")+"')");
			
		}
		
	}
	
	public static void storeNotifications(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.bn+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.bn+" VALUES('"+jsonObject.getString("noti_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("partner_id")+"','"+jsonObject.getString("ntype")+"','"+jsonObject.getString("txt")+"','"+jsonObject.getString("notidttime")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("read_status")+"','"+jsonObject.getString("delstatus")+"','"+jsonObject.getString("expiry")+"')");
			
		}
		
	}
	
	public static void storePromo(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.po+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.po+" VALUES('"+jsonObject.getString("noti_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("partner_id")+"','"+jsonObject.getString("ntype")+"','"+jsonObject.getString("txt")+"','"+jsonObject.getString("notidttime")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("read_status")+"','"+jsonObject.getString("delstatus")+"','"+jsonObject.getString("expiry")+"')");
			
		}
		
	}
	
	public static void storeCreditLimit(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.bcl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.bcl+" VALUES('"+jsonObject.getString("clid")+"','"+jsonObject.getString("partner_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("limits")+"')");
			
		}
		
	}

	public static void storeActivities(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.subactivties+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.subactivties+" VALUES('"+jsonObject.getString("activity_id")+"','"+jsonObject.getString("subid")+"','"+jsonObject.getString("parent")+"','"+jsonObject.getString("activity")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("type")+"','"+jsonObject.getString("typeid")+"')");
			
		}
		
	}
	
	public static void storecategory(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.cattbl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.cattbl+" VALUES('"+jsonObject.getString("category_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("name")+"','"+jsonObject.getString("parent")+"','"+jsonObject.getString("level")+"')");
		}
		
	}
	
	public static void storeoldprc(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.oldprc+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.oldprc+" VALUES('"+jsonObject.getString("uni_id")+"','"+jsonObject.getString("product_name")+"','"+jsonObject.getString("price")+"','"+jsonObject.getString("order_id")+"','"+jsonObject.getString("sender")+"','"+jsonObject.getString("receiver")+"','"+jsonObject.getString("date")+"')");
		}
		
	}
	
	public static void storeQuote(Context c,JSONArray Data) throws SQLException, JSONException
	{
		try{
		db.execSQL("DELETE FROM "+Utils.quote+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.quote+" VALUES('"+jsonObject.getString("quotation_id")+"','"+jsonObject.getString("sender_id")+"','"+jsonObject.getString("sub_sender_id")+"','"+jsonObject.getString("reciever_id")+"','"+jsonObject.getString("sub_receiver_id")+"','"+jsonObject.getString("quotation_text")+"','"+jsonObject.getString("productname")+"','"+jsonObject.getString("conditions")+"','"+jsonObject.getString("path")+"','"+jsonObject.getString("type")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("expiry")+"')");
			
		}
		}finally{
		
		}
	}
	
	public static void storeOrders(Context c,JSONArray Data) throws SQLException, JSONException
	{
	
		db.execSQL("DELETE FROM "+Utils.orders+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			Utils.doDownload("attachments/","feed_"+jsonObject.getString("fid")+".jpg");
			db.execSQL("INSERT INTO "+Utils.orders+" VALUES('"+jsonObject.getString("order_id")+"','"+jsonObject.getString("sender_id")+"','"+jsonObject.getString("sub_sender_id")+"','"+jsonObject.getString("receiver_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("sub_receiver_id")+"','"+jsonObject.getString("delivery_type")+"','"+jsonObject.getString("transport_type")+"','"+jsonObject.getString("product_data")+"','"+jsonObject.getString("order_price")+"','"+jsonObject.getString("sender_remarks")+"','"+jsonObject.getString("receiver_remarks")+"','"+jsonObject.getString("accept_status")+"','"+jsonObject.getString("attachment_id")+"','"+jsonObject.getString("order_read_by_receiver")+"','"+jsonObject.getString("dispatched")+"','"+jsonObject.getString("dispatched_date")+"','"+jsonObject.getString("dispatched_time")+"','"+jsonObject.getString("confirm_received")+"','"+jsonObject.getString("confirm_date")+"','"+jsonObject.getString("confirm_time")+"','"+jsonObject.getString("order_date")+"','"+jsonObject.getString("order_time")+"','"+jsonObject.getString("tremarks")+"','"+jsonObject.getString("courier")+"','"+jsonObject.getString("courier_tracking_no")+"','"+jsonObject.getString("tdate")+"','"+jsonObject.getString("ttime")+"','"+jsonObject.getString("feedback")+"','"+jsonObject.getString("fremarks")+"','"+jsonObject.getString("fdate")+"','"+jsonObject.getString("ftime")+"','"+jsonObject.getString("fid")+"','"+jsonObject.getString("ignore_status")+"','"+jsonObject.getString("quote_ref")+"','"+jsonObject.getString("hidefields")+"','0','"+jsonObject.getString("approve_status")+"','"+jsonObject.getString("schedule_date")+"','"+jsonObject.getString("ordtype")+"','"+jsonObject.getString("cancel")+"','"+jsonObject.getString("attachment")+"')");
			
		}
		
	}
	
	public static void updateOrder(Context c,JSONArray Data,String oid) throws SQLException, JSONException
	{
		db.execSQL("DELETE FROM "+Utils.orders+" where order_id='"+oid+"'");
		
		JSONObject jsonObject = Data.getJSONObject(0); 
		db.execSQL("INSERT INTO "+Utils.orders+" VALUES('"+jsonObject.getString("order_id")+"','"+jsonObject.getString("category")+"','"+jsonObject.getString("sender_id")+"','"+jsonObject.getString("sub_sender_id")+"','"+jsonObject.getString("receiver_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("sub_receiver_id")+"','"+jsonObject.getString("quantity")+"','"+jsonObject.getString("product_name")+"','"+jsonObject.getString("variant")+"','"+jsonObject.getString("plength")+"','"+jsonObject.getString("pbreath")+"','"+jsonObject.getString("pthickness")+"','"+jsonObject.getString("pweight")+"','"+jsonObject.getString("delivery_type")+"','"+jsonObject.getString("transport_type")+"','"+jsonObject.getString("order_price")+"','"+jsonObject.getString("sender_remarks")+"','"+jsonObject.getString("receiver_remarks")+"','"+jsonObject.getString("accept_status")+"','"+jsonObject.getString("attachment_id")+"','"+jsonObject.getString("order_read_by_receiver")+"','"+jsonObject.getString("dispatched")+"','"+jsonObject.getString("dispatched_date")+"','"+jsonObject.getString("dispatched_time")+"','"+jsonObject.getString("confirm_received")+"','"+jsonObject.getString("confirm_date")+"','"+jsonObject.getString("confirm_time")+"','"+jsonObject.getString("order_date")+"','"+jsonObject.getString("order_time")+"')");

	}
	
	public static void storeGroups(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.grptbl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.grptbl+" VALUES('"+jsonObject.getString("group_id")+"','"+jsonObject.getString("group_name")+"','"+jsonObject.getString("order_limit")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("description")+"')");
			
		}
		
	}
	
	public static void storeOrderProducts(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.order_products+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.order_products+" VALUES('"+jsonObject.getString("id")+"','"+jsonObject.getString("order_id")+"','"+jsonObject.getString("product_id")+"','"+jsonObject.getString("categories")+"','"+jsonObject.getString("qty")+"','"+jsonObject.getString("remarks")+"','"+jsonObject.getString("sender")+"','"+jsonObject.getString("receiver")+"')");
			
		}
		
	}
	
	
	
	public static void storeGroupsRelations(Context c,JSONArray Data) throws SQLException, JSONException
	{
	
		db.execSQL("DELETE FROM "+Utils.grprltbl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.grprltbl+" VALUES('"+jsonObject.getString("rid")+"','"+jsonObject.getString("group_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("user")+"','"+jsonObject.getString("obj")+"')");
			
		}
		
	}
	
	public static void storeProducts(Context c,JSONArray Data) throws SQLException, JSONException
	{
	String img="default_product.png";
		db.execSQL("DELETE FROM "+Utils.bp+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			if(!jsonObject.getString("img").equals(""))img=jsonObject.getString("img");
			db.execSQL("INSERT INTO "+Utils.bp+" VALUES('"+jsonObject.getString("product_id")+"','"+jsonObject.getString("product_name")+"','"+jsonObject.getString("catid")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("mrp")+"','"+jsonObject.getString("description")+"','"+img+"','"+jsonObject.getString("catnm")+"')");
			Utils.doDownload("attachments/",img);
		}
		
	}
	
	public static void updateProducts(Context c,JSONArray Data) throws SQLException, JSONException
	{
	        JSONObject jsonObject = Data.getJSONObject(0); 
		    db.execSQL("DELETE FROM "+Utils.bp+" where product_id='"+jsonObject.getString("product_id")+"'");
			db.execSQL("INSERT INTO "+Utils.bp+" VALUES('"+jsonObject.getString("product_id")+"','"+jsonObject.getString("product_name")+"','"+jsonObject.getString("catid")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("mrp")+"','"+jsonObject.getString("description")+"','"+jsonObject.getString("catnm")+"')");
		

	}
	
	
	public static void storeProductsSpecs(Context c,JSONArray Data) throws SQLException, JSONException
	{
	
		db.execSQL("DELETE FROM "+Utils.bps+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.bps+" VALUES('"+jsonObject.getString("id")+"','"+jsonObject.getString("product_id")+"','"+jsonObject.getString("user_id")+"','"+jsonObject.getString("name")+"','"+jsonObject.getString("value")+"')");
			
		}
		
	}
	public static String Cname(Context c,String id)
	{
		String rtn="";
		Cursor cb=null;
		try{
		cb=getData(c,Utils.cattbl," where category_id in ("+id+")");
	while(cb.moveToNext())
	{
		rtn+=cb.getString(2)+" ";
	}
		}finally{if(cb!=null)cb.close();}
		return rtn;
	}
	public static void storeSearch(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.seasrchusr+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			
			db.execSQL("INSERT INTO "+Utils.seasrchusr+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("email")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("reference")+"','"+jsonObject.getString("business")+"','"+jsonObject.getString("payment_policy")+"','"+jsonObject.getString("city")+"','"+jsonObject.getString("state")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");
			Utils.doDownload("profile/","user"+jsonObject.getString("user_id")+".jpg");
		}
		
	}
	public static void storePartners(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.partners+"");
		
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			
			db.execSQL("INSERT INTO "+Utils.partners+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("email")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("reference")+"','"+jsonObject.getString("business")+"','"+jsonObject.getString("payment_policy")+"','"+jsonObject.getString("city")+"','"+jsonObject.getString("state")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("sec_email")+"','"+jsonObject.getString("sec_mobile")+"','"+jsonObject.getString("company_name")+"','"+jsonObject.getString("country")+"')");
			Utils.doDownload("profile/","user"+jsonObject.getString("user_id")+".jpg");
		}
		
	}
	
	public static void storeSubPartners(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
		db.execSQL("DELETE FROM "+Utils.partners_subs+"");
		
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			
			db.execSQL("INSERT INTO "+Utils.partners_subs+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("parent")+"','"+jsonObject.getString("full_name")+"')");
		}
		
	}
	
	public static void storeMessages(Context c,JSONArray Data) throws SQLException, JSONException
	{
		db.execSQL("DELETE FROM "+Utils.msgtbl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.msgtbl+" VALUES('"+jsonObject.getString("message_id")+"','"+jsonObject.getString("sender_id")+"','"+jsonObject.getString("receiver_id")+"','"+jsonObject.getString("common_obj")+"','"+jsonObject.getString("message")+"','"+jsonObject.getString("parent")+"','0','0','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("parent_nm")+"','"+jsonObject.getString("sender_nm")+"','"+jsonObject.getString("receiver_nm")+"','"+jsonObject.getString("sender_receipt")+"','"+jsonObject.getString("receiver_recipt")+"')");
			
		}
		
	}
	
	public static void storeSubs(Context c,JSONArray Data) throws SQLException, JSONException
	{
	
		db.execSQL("DELETE FROM "+Utils.bsu+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.bsu+" VALUES('"+jsonObject.getString("user_id")+"','"+jsonObject.getString("full_name")+"','"+jsonObject.getString("username")+"','"+jsonObject.getString("password")+"','"+jsonObject.getString("mobile")+"','"+jsonObject.getString("designation")+"','"+jsonObject.getString("department")+"','"+jsonObject.getString("permission")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"')");
			
		}
		
		
	}
	
	public static void storeCollection(Context c,JSONArray Data) throws SQLException, JSONException
	{
	
		db.execSQL("DELETE FROM "+Utils.bpcl+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.bpcl+" VALUES('"+jsonObject.getString("id")+"','"+jsonObject.getString("uid")+"','"+jsonObject.getString("userid")+"','"+jsonObject.getString("partner")+"','"+jsonObject.getString("amount")+"','"+jsonObject.getString("remarks")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("mode")+"')");
			
		}
		
		
	}
	
	public static void storePy(Context c,JSONArray Data) throws SQLException, JSONException
	{
	try{
			db.execSQL("DELETE FROM "+Utils.py+"");
		for(int i=0;i<Data.length();i++)
		{
			JSONObject jsonObject = Data.getJSONObject(i); 
			db.execSQL("INSERT INTO "+Utils.py+" VALUES('"+jsonObject.getString("payment_id")+"','"+jsonObject.getString("sender_id")+"','"+jsonObject.getString("sub_sender_id")+"','"+jsonObject.getString("receiver_id")+"','"+jsonObject.getString("sub_receiver_id")+"','"+jsonObject.getString("payment")+"','"+jsonObject.getString("payment_type")+"','"+jsonObject.getString("remarks")+"','"+jsonObject.getString("date")+"','"+jsonObject.getString("time")+"','"+jsonObject.getString("status")+"','"+jsonObject.getString("sender_nm")+"','"+jsonObject.getString("receiver_nm")+"')");
			
		}
	}finally{
	
	}
		
		
	}
	
	
	public static void UpdateLogin(Context c,JSONArray Data) throws SQLException, JSONException
	{
		
			JSONObject jsonObject = Data.getJSONObject(0); 
			db.execSQL("UPDATE "+Utils.usr+" SET sent_requests='"+jsonObject.getString("sent_requests")+"',pending_requests='"+jsonObject.getString("pending_requests")+"',accepted='"+jsonObject.getString("accepted")+"' where user_id='"+jsonObject.getString("user_id")+"'");

		
		
	}

	public static void StoreUserData(Context c,JSONObject jsonObj) throws JSONException
	{
		Utils.doDownload("profile/","user"+Utils.getDefaults("user_id", c)+".jpg");
		JSONArray Data  = jsonObj.getJSONArray("categories");
		storecategory(c,Data);
		
		JSONArray Data3 = jsonObj.getJSONArray("partners");
		storePartners(c,Data3);
		
		JSONArray Data3a= jsonObj.getJSONArray("subpartners");
		storeSubPartners(c,Data3a);
		
		JSONArray Data2 = jsonObj.getJSONArray("groups");
		storeGroups(c,Data2);
		
		JSONArray Data10= jsonObj.getJSONArray("quotes");	
		storeQuote(c,Data10);
		
		JSONArray Data4 = jsonObj.getJSONArray("user");
		UpdateLogin(c,Data4);
		
		JSONArray Data1 = jsonObj.getJSONArray("subs");		
		DBHelper.storeSubs(c,Data1);
		
		JSONArray Data6 = jsonObj.getJSONArray("grouprel");
		storeGroupsRelations(c,Data6);
		
		JSONArray Data7 = jsonObj.getJSONArray("messages");
		storeMessages(c,Data7);
		
		JSONArray Data8 = jsonObj.getJSONArray("orders");
		storeOrders(c,Data8);
		
		JSONArray Data9 = jsonObj.getJSONArray("py");
		storePy(c,Data9);
		
		JSONArray Data12 = jsonObj.getJSONArray("nots");
		storeNotifications(c,Data12);
		
		JSONArray Data13 = jsonObj.getJSONArray("mnots");
		storePromo(c,Data13);
		
		JSONArray Data11 = jsonObj.getJSONArray("credit");
		storeCreditLimit(c,Data11);
		
	
	
	}
	
	public static void clearData(Context c,Context d)
	{

		Utils.setDefaults("subid",   "0", c);
		Utils.setDefaults("user_id", "0", c);
		Activity activity = (Activity) d;
		activity.stopService(new Intent(activity, Services.class));
		activity.stopService(new Intent(activity, MsgService.class));
		activity.stopService(new Intent(activity, ReminderService.class));
		activity.stopService(new Intent(activity, PendingTaskReminder.class));
		activity.stopService(new Intent(activity, CreditService.class));
		activity.stopService(new Intent(activity, SubServices.class));
		activity.deleteDatabase("gb_b2b");


	}
	
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	public static String getGroupIdByName(Context c,String nm)
	{
		String str="";
		Cursor d=null;
		try{
		d=getData(c,Utils.grptbl," where group_name='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(0);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getNameByID(Context c,String nm)
	{
		String str="";
		Cursor d=null;
		try{
	    d=getData(c,Utils.partners," where partner_id='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(1);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getCompanyByID(Context c,String nm)
	{
		String str="";
		Cursor d=null;
		try{
		d=getData(c,Utils.partners," where partner_id='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(14);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getQuoteNameByID(Context c,String nm)
	{
		String str="";
		Cursor d=null;
		try{
			d=getData(c,Utils.quote," where quotation_id='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(6);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getIDByName(Context c,String nm)
	{
		
		String str="";
		Cursor d=null;
		try{
		
		d=getData(c,Utils.partners," where full_name='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(0);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	
	public static String getIDBySubName(Context c,String nm)
	{
		
		String str="";
		Cursor d=null;
		try{
		
		d=getData(c,Utils.bsu," where full_name='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(0);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getNameBySubID(Context c,String nm)
	{
		
		String str="";
		Cursor d=null;
		try{
		
		d=getData(c,Utils.bsu," where user_id='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(1);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	public static String getNAMEByPRODUCTID(Context c,String nm)
	{
		
		String str="";
		Cursor d=null;
		try{
		
		d=getData(c,Utils.bp," where product_id='"+nm+"'");
		if(d.moveToFirst())
		{
			str=d.getString(1);
		}
		}finally{if(d!=null)d.close();}
		return str;
	}
	
	
	public static int getUserGroupCount(Context c,String gid)
	{
		Cursor d=null;
		try{
		d=getData(c,Utils.grprltbl," where group_id='"+gid+"'");
		}finally{}
		return d.getCount();
	}
	
	public static int getGroupCount(Context c)
	{
		
		Cursor	d=getData(c,Utils.grptbl,"");
		
		
		return d.getCount();
	}
	
	public static int getTblCount(Context c,String tbl,String cond)
	{
		Cursor d=getData(c,tbl,cond);

		return d.getCount();
	}
	
	public static int getAcceptedUsers(Context c,String id)
	{
		int cnt=0;
		Cursor d=null;
		try{
		d=getData(c,Utils.usr," where user_id='"+Utils.getDefaults("user_id", c)+"'");
		if(d.getCount()!=0 && d.moveToFirst()){
		 String []chk1=d.getString(16).split("[,]");
		 if (Arrays.asList(chk1).contains(id)) {
    		 cnt=1;
			}
		}
		}finally{if(d!=null)d.close();}
		
		return cnt;
	}

	public static int getSentUsers(Context c,String id)
	{
		int cnt=0;
		Cursor d=null;
		try{
		d=getData(c,Utils.usr," where user_id='"+Utils.getDefaults("user_id", c)+"'");
		if(d.getCount()!=0 && d.moveToFirst()){
		 String []chk1=d.getString(14).split("[,]");
		 if (Arrays.asList(chk1).contains(id)) {
    		 cnt=1;
			}
		}}finally{if(d!=null)d.close();}
		
		return cnt;
	}
	public static String getOldPrice(Context c,String uid,String oid)
	{
		
		String str="";
		Cursor d=null;
	     try{
		d=getData(c,Utils.orders," where receiver_id='"+uid+"' and order_id!='"+oid+"'");
		if(d.moveToFirst())
		{
			str=d.getString(9);
		}
		if(str.equals(""))str="N/A";
	     }finally{if(d!=null)d.close();}
		
		return str;
	}
	
	public static String getParentCat(Context c,String cid)
	{
		
		String str="";
		Cursor d=null;
	     try{
		d=getData(c,Utils.cattbl," where category_id='"+cid+"'");
		if(d.moveToFirst())
		{
			str=d.getString(2)+"-"+d.getString(3)+"-"+d.getString(4);
		}
		if(str.equals(""))str="0";
	     }finally{if(d!=null)d.close();}
		
		return str;
	}
	
	
	 public static String getPendingTasks(Context c)
	 {
		 String rtn="";
		 
		 String str1="";
		 String str2="";
		 String str3="";
		 Cursor a=null;
		 Cursor b=null;
		 Cursor d=null;
		    try{
			a=getData(c,Utils.orders," where accept_status=0 and ignore_status=0 and receiver_id='"+Utils.getDefaults("user_id", c)+"'");
			if(a.getCount()!=0){str1="Orders: "+a.getCount();}
		    }finally{if(a!=null)a.close();}
		    
			try{b=getData(c,Utils.py," where receiver_id='"+Utils.getDefaults("user_id", c)+"' and status=0");
			if(b.getCount()!=0){str2="Payments: "+b.getCount();}
	         }finally{if(b!=null)b.close();}
	 
			try{d=getData(c,Utils.usr," where user_id='"+Utils.getDefaults("user_id", c)+"'");
			if(d.getCount()!=0 && d.moveToFirst()){
				if(!d.getString(15).equals("")){
				 String []chk1=d.getString(15).split("[,]");

				 str3="Requests: "+chk1.length;
				}
			}
            }finally{if(d!=null)d.close();}
			
			
			
			if(str1.equals("") && str2.equals("") && str3.equals(""))rtn="2";
			else
				rtn=str1+" \n "+str2+" \n "+str3;
			return rtn;
	 }
	 
	
 public static String getOldPriceByName(Context c,String nm,String sender,String receiver,String oid)
 {
	 String opc="n/a";
	 Cursor a=null;
	 try{a=getData(c,Utils.oldprc," where product_name='"+nm+"' and sender='"+sender+"' and receiver='"+receiver+"' and order_id !='"+oid+"'");
	 if(a.getCount()!=0 && a.moveToFirst()){
		 opc=a.getString(2);
	 }
	 }finally{if(a!=null)a.close();}
	 return opc;
 }
 
	 public static void updateCreditOrder(String oid)
	 {
		 db.execSQL("UPDATE "+Utils.orders+" SET credit=1 where order_id='"+oid+"'"); 
	 }
 
 public static int getCreditFromSender(Context c,String uid)
 {
	 int opc=0;
	 Cursor a=null;
	 try{a=getData(c,Utils.bcl," where partner_id='"+uid+"'");
	 if(a.getCount()!=0 && a.moveToFirst()){
		 opc=Integer.parseInt(a.getString(3));
	 }
	 }finally{if(a!=null)a.close();}
	 return opc;
 }
 
 
 public static int getunreadMesgs(Context c,String uid)
 {
	 int opc=0;
	 Cursor a=null;
	 try{a=getData(c,Utils.msgtbl," where sender_id='"+uid+"' and receiver_id='"+Utils.getDefaults("user_id", c)+"' and receiver_recipt=0");
	opc=a.getCount();
	 }finally{if(a!=null)a.close();}
	 return opc;
 }
 
 public static int getproductscounts(Context c,String catid)
 {
	 int opc=0;
	 Cursor a=null;
	 try{a=getData(c,Utils.bp," where catid='"+catid+"'");
	opc=a.getCount();
	 }finally{if(a!=null)a.close();}
	 return opc;
 }
 
 public static int checkHaveSub(Context c,String catid)
 {
	 int opc=0;
	 Cursor a=null;
	 try{a=getData(c,Utils.cattbl," where parent='"+catid+"'");
	opc=a.getCount();
	 }finally{if(a!=null)a.close();}
	 return opc;
 }
}
