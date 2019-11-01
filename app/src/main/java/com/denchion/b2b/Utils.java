package com.denchion.b2b;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.EditText;



@TargetApi(Build.VERSION_CODES.KITKAT) @SuppressLint({ "SimpleDateFormat", "NewApi" }) 
public class Utils {
	long  downloadedsize, filesize;
	public static final double SPACE_KB = 1024;
	public static final double SPACE_MB = 1024 * SPACE_KB;
	public static final double SPACE_GB = 1024 * SPACE_MB;
	public static final double SPACE_TB = 1024 * SPACE_GB;
	
	public static String states2[] = {"No States Available"};
	
	
	public static String states[] = {

        "Andaman and Nicobar Islands",
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chandigarh",
        "Chhattisgarh",
        "Dadra and Nagar Haveli",
        "Daman and Diu",
        "Delhi",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jammu and Kashmir",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Lakshadweep",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Odisha",
        "Puducherry",
            "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttarakhand",
        "Uttar Pradesh",
        "West Bengal"
	
	};
	
	public static String country[] = {
			"Afghanistan",
			"Albania",
			"Algeria",
			"Andorra",
			"Angola",
			"Antigua and Barbuda",
			"Argentina",
			"Armenia",
			"Aruba",
			"Australia",
			"Austria",
			"Azerbaijan",
			"Bahamas",
			"Bahrain",
			"Bangladesh",
			"Barbados",
			"Belarus",
			"Belgium",
			"Belize",
			"Benin",
			"Bhutan",
			"Bolivia",
			"Bosnia and Herzegovina",
			"Botswana",
			"Brazil",
			"Brunei ",
			"Bulgaria",
			"Burkina Faso",
			"Burma",
			"Burundi",
			"Cambodia",
			"Cameroon",
			"Canada",
			"Cape Verde",
			"Central African Republic",
			"Chad",
			"Chile",
			"China",
			"Colombia",
			"Comoros",
			"Congo, Democratic Republic of the",
			"Congo, Republic of the",
			"Costa Rica",
			"Cote d`Ivoire",
			"Croatia",
			"Cuba",
			"Curacao",
			"Cyprus",
			"Czech Republic",
			"Denmark",
			"Djibouti",
			"Dominica",
			"Dominican Republic",
			"East Timor",
			"Ecuador",
			"Egypt",
			"El Salvador",
			"Equatorial Guinea",
			"Eritrea",
			"Estonia",
			"Ethiopia",
			"Fiji",
			"Finland",
			"France",
			"Gabon",
			"Gambia",
			"Georgia",
			"Germany",
			"Ghana",
			"Greece",
			"Grenada",
			"Guatemala",
			"Guinea",
			"Guinea-Bissau",
			"Guyana",
			"Haiti",
			"Holy See",
			"Honduras",
			"Hong Kong",
			"Hungary",
			"Iceland",
			"India",
			"Indonesia",
			"Iran",
			"Iraq",
			"Ireland",
			"Israel",
			"Italy",
			"Jamaica",
			"Japan",
			"Jordan",
			"Kazakhstan",
			"Kenya",
			"Kiribati",
			"Korea, North",
			"Korea, South",
			"Kosovo",
			"Kuwait",
			"Kyrgyzstan",
			"Laos",
			"Latvia",
			"Lebanon",
			"Lesotho",
			"Liberia",
			"Libya",
			"Liechtenstein",
			"Lithuania",
			"Luxembourg",
			"Macau",
			"Macedonia",
			"Madagascar",
			"Malawi",
			"Malaysia",
			"Maldives",
			"Mali",
			"Malta",
			"Marshall Islands",
			"Mauritania",
			"Mauritius",
			"Mexico",
			"Micronesia",
			"Moldova",
			"Monaco",
			"Mongolia",
			"Montenegro",
			"Morocco",
			"Mozambique",
			"Namibia",
			"Nauru",
			"Nepal",
			"Netherlands",
			"Netherlands Antilles",
			"New Zealand",
			"Nicaragua",
			"Niger",
			"Nigeria",
			"North Korea",
			"Norway",
			"Oman",
			"Pakistan",
			"Palau",
			"Palestinian Territories",
			"Panama",
			"Papua New Guinea",
			"Paraguay",
			"Peru",
			"Philippines",
			"Poland",
			"Portugal",
			"Qatar",
			"Romania",
			"Russia",
			"Rwanda",
			"Saint Kitts and Nevis",
			"Saint Lucia",
			"Saint Vincent and the Grenadines",
			"Samoa ",
			"San Marino",
			"Sao Tome and Principe",
			"Saudi Arabia",
			"Senegal",
			"Serbia",
			"Seychelles",
			"Sierra Leone",
			"Singapore",
			"Sint Maarten",
			"Slovakia",
			"Slovenia",
			"Solomon Islands",
			"Somalia",
			"South Africa",
			"South Korea",
			"South Sudan",
			"Spain",
			"Sri Lanka",
			"Sudan",
			"Suriname",
			"Swaziland ",
			"Sweden",
			"Switzerland",
			"Syria",
			"Taiwan",
			"Tajikistan",
			"Tanzania",
			"Thailand",
			"Timor-Leste",
			"Togo",
			"Tonga",
			"Trinidad and Tobago",
			"Tunisia",
			"Turkey",
			"Turkmenistan",
			"Tuvalu",
			"Uganda",
			"Ukraine",
			"United Arab Emirates",
			"United Kingdom",
			"Uruguay",
			"Uzbekistan",
			"Vanuatu",
			"Venezuela",
			"Vietnam",
			"Yemen",
			"Zambia",
			"Zimbabwe"
	
	};
	
	
	public static String base ="https://denchion.com/b2bwebdata/";
	
	public final static boolean isValidEmail(CharSequence target) {
		  return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	
	public static int rand()
	{
		Random rnd = new Random();  
		 int randno = rnd.nextInt(100000);  
		return randno;
	}

	
	public static void setDefaults(String key, String value, Context context) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
	
	
	public static void clearDefaults(Context context) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    prefs.edit().clear().commit();
	}
	
	 public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, null);
		}
	 
	 public static boolean isConnectingToInternet(Context _context){
	        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null)
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                       return true;
	                      }
	 
	          }
	          return false;
	    }
	 
	 public static String getMimeType(String url) {
		    String type = null;
		    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		    if (extension != null) {
		        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		    }
		    return type;
		}

	 public static String setCorrectTimeFormat(String time)
	 {
		 String str="";
	       try {
		 SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
	       SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
	
			Date date = parseFormat.parse("10:30 PM");
			str=displayFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       return str;
	 }
	 
	 public static String parseDateToddMMyyyy(String time) {
		 Date date = null;
		    String str = null;
		   
		if(time==null || time==""){ time="0000-00-00";}
      
		    String inputPattern = "yyyy-mm-dd";
		    String outputPattern = "dd-mm-yyyy";
		  
		  SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		  SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		    
		 
		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    if(time.equals("0000-00-00"))str="n/a";
		    return str;
		}
	 
	 public static String getPreviousDate(String inputDate){
	        //inputDate = "9-1-2016"; // for example 
	        SimpleDateFormat  format = new SimpleDateFormat("dd-MM-yyyy");  
	        try {  
	            Date date = format.parse(inputDate); 
	            Calendar c = Calendar.getInstance();
	            c.setTime(date);

	            c.add(Calendar.DATE, 45);
	            inputDate = format.format(c.getTime());
	            Log.d("asd", "selected date : "+inputDate);

	            System.out.println(date);  
	        } catch (Exception e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	              inputDate ="";
	        }
	        return inputDate;
	    }
	 
	  public static String parseDateToddMMyyyy2(String time) {
		    String inputPattern = "yyyy-mm-dd";
		    String outputPattern = "dd-mm-yyyy";
		  SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		  SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		    Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    return str;
		}
	  
	  public static String parseDateToddMMyyyy4(String time) {
		    String inputPattern = "yyyy-mm-dd";
		    String outputPattern = "dd/mm/yyyy";
		  SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		  SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		    Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    return str;
		}
	  
	  public static String parseDateToddMMyyyy3(String time) {
		    String outputPattern = "yyyy-mm-dd";
		    String inputPattern  = "yyyy-m-d";
		  SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		  SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		    Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    return str;
		}
	 
	 public static String ConvertTime(String time) {
		   /* String inputPattern = "HH:mm:ss";
		    String outputPattern = "h:mm a";
		  SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		  SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		    Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }*/
		    return time;
		}
	 
	 public static void doDownload(final String folder,final String fileName) {
	        Thread dx = new Thread() {

	            @SuppressWarnings("unused")
				public void run() {
	            	  //File root = android.os.Environment.getExternalStorageDirectory();               
	                  File dir = new File (Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"); 
	                  if(dir.exists()==false) {
	                          dir.mkdirs();
	                     }
	            	  //Save the path as a string value

	                try {
	                        URL url = new URL(base+"images/"+folder+fileName);
	                        Log.i("FILE_NAME", "File name is "+fileName);
	                        Log.i("FILE_URLLINK", "File URL is "+url);
	                        URLConnection connection = url.openConnection();
	                        connection.connect();
	                        // this will be useful so that you can show a typical 0-100% progress bar
	                       //int fileLength = connection.getContentLength();

	                        // download the file
	                        InputStream input = new BufferedInputStream(url.openStream());                   
	                        OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/"+fileName);

	                        byte data[] = new byte[1024];
	                        long total = 0;
	                        int count;
	                        while ((count = input.read(data)) != -1) {
	                            total += count;

	                            output.write(data, 0, count);
	                        }

	                        output.flush();
	                        output.close();
	                        input.close();
	                        
	                    } catch (Exception e) {
	                    	 e.printStackTrace();
	                    	 //Log.i("ERROR ON DOWNLOADING FILES", "ERROR IS" +e);
	                    }
	            }
	        };
	        dx.start();      
	    }
	 public static boolean isInternetAvailable() {
	        try {
	            InetAddress ipAddr = InetAddress.getByName(base); //You can replace it with your name

	            if (ipAddr.equals("")) {
	                return false;
	            } else {
	                return true;
	            }

	        } catch (Exception e) {
	            return false;
	        }

	    }
	 
	 public static void setActionBar(Context c,String heading) {
		    // TODO Auto-generated method stub
		 	Activity activity = (Activity) c;
			ActionBar actionBar = activity.getActionBar();
		    /*actionBar.setHomeButtonEnabled(true);
		    actionBar.setDisplayHomeAsUpEnabled(true);
		    actionBar.setDisplayShowHomeEnabled(true);
		    actionBar.setHomeButtonEnabled(true);
		    actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.titlebackgroundcolor)));
		    actionBar.setTitle(heading);
		    actionBar.show();*/

		}
		
	 
	 public static String nconcat(int a,int b){
			String x="";
			if(a<b) x=a+"_"+b;
			else x=b+"_"+a;
			return x;
			}
	 
	 public void afterTextChanged(Editable s,EditText edit) {
	     String result = s.toString().replaceAll("\\{", "");
	     if (!s.toString().equals(result)) {
	          edit.setText(result); // "edit" being the EditText on which the TextWatcher was set
	          edit.setSelection(result.length()); // to set the cursor at the end of the current text             
	     }
	 }
	  
	 
	 @TargetApi(Build.VERSION_CODES.KITKAT) public static String getPath(final Context context, final Uri uri) {
		 
	        // check here to KITKAT or new version
	        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	 
	        // DocumentProvider
	        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	 
	            // ExternalStorageProvider
	            if (isExternalStorageDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];
	 
	                if ("primary".equalsIgnoreCase(type)) {
	                    return Environment.getExternalStorageDirectory() + "/"
	                            + split[1];
	                }
	            }
	            // DownloadsProvider
	            else if (isDownloadsDocument(uri)) {
	 
	                final String id = DocumentsContract.getDocumentId(uri);
	                final Uri contentUri = ContentUris.withAppendedId(
	                        Uri.parse("content://downloads/public_downloads"),
	                        Long.valueOf(id));
	 
	                return getDataColumn(context, contentUri, null, null);
	            }
	            // MediaProvider
	            else if (isMediaDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];
	 
	                Uri contentUri = null;
	                if ("image".equals(type)) {
	                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	                } else if ("video".equals(type)) {
	                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	                } else if ("audio".equals(type)) {
	                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	                }
	 
	                final String selection = "_id=?";
	                final String[] selectionArgs = new String[] { split[1] };
	 
	                return getDataColumn(context, contentUri, selection,
	                        selectionArgs);
	            }
	        }
	        // MediaStore (and general)
	        else if ("content".equalsIgnoreCase(uri.getScheme())) {
	 
	            // Return the remote address
	            if (isGooglePhotosUri(uri))
	                return uri.getLastPathSegment();
	 
	            return getDataColumn(context, uri, null, null);
	        }
	        // File
	        else if ("file".equalsIgnoreCase(uri.getScheme())) {
	            return uri.getPath();
	        }
	 
	        return null;
	    }
	 
	 public static String getDataColumn(Context context, Uri uri,
             String selection, String[] selectionArgs) {

	Cursor cursor = null;
	final String column = "_data";
	final String[] projection = { column };
	
	try {
	cursor = context.getContentResolver().query(uri, projection,
	selection, selectionArgs, null);
	if (cursor != null && cursor.moveToFirst()) {
	final int index = cursor.getColumnIndexOrThrow(column);
	return cursor.getString(index);
	}
	} finally {
	if (cursor != null)
	cursor.close();
	}
	return null;
	}
	
	/**
	* @param uri
	*            The Uri to check.
	* @return Whether the Uri authority is ExternalStorageProvider.
	*/
	public static boolean isExternalStorageDocument(Uri uri) {
	return "com.android.externalstorage.documents".equals(uri
	.getAuthority());
	}
	
	/**
	* @param uri
	*            The Uri to check.
	* @return Whether the Uri authority is DownloadsProvider.
	*/
	public static boolean isDownloadsDocument(Uri uri) {
	return "com.android.providers.downloads.documents".equals(uri
	.getAuthority());
	}
	
	/**
	* @param uri
	*            The Uri to check.
	* @return Whether the Uri authority is MediaProvider.
	*/
	public static boolean isMediaDocument(Uri uri) {
	return "com.android.providers.media.documents".equals(uri
	.getAuthority());
	}
	
	/**
	* @param uri
	*            The Uri to check.
	* @return Whether the Uri authority is Google Photos.
	*/
	public static boolean isGooglePhotosUri(Uri uri) {
	return "com.google.android.apps.photos.content".equals(uri
	.getAuthority());
	}
	
	public static String get_count_of_days(String Start_date_String)
	{


    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

       Date Start_CovertedDate=null;
      try
      {
    	  Start_CovertedDate = dateFormat.parse(Start_date_String);

      } catch (ParseException e)
      {
          e.printStackTrace();
      }

     
      Calendar e_cal = Calendar.getInstance();
      e_cal.setTime(Start_CovertedDate);


      int e_year = e_cal.get(Calendar.YEAR);
      int e_month = e_cal.get(Calendar.MONTH);
      int e_day = e_cal.get(Calendar.DAY_OF_MONTH);
      
      final Calendar c = Calendar.getInstance();
      int   c_year  = c.get(Calendar.YEAR);
      int   c_month = c.get(Calendar.MONTH);
      int  c_day   = c.get(Calendar.DAY_OF_MONTH);

      Calendar date1 = Calendar.getInstance();
      Calendar date2 = Calendar.getInstance();

      date1.clear();
      date1.set(c_year, c_month, c_day);
      date2.clear();
      date2.set(e_year, e_month, e_day);

      long diff =   date1.getTimeInMillis()-date2.getTimeInMillis();

      float dayCount = (float) diff / (24 * 60 * 60 * 1000);


      return ((int) dayCount+"");
      
}


public static String bytes2String(long sizeInBytes) {

    NumberFormat nf = new DecimalFormat();
    nf.setMaximumFractionDigits(2);

    try {
        if ( sizeInBytes < SPACE_KB ) {
            return nf.format(sizeInBytes) + " Byte(s)";
        } else if ( sizeInBytes < SPACE_MB ) {
            return nf.format(sizeInBytes/SPACE_KB) + " KB";
        } else if ( sizeInBytes < SPACE_GB ) {
            return nf.format(sizeInBytes/SPACE_MB) + " MB";
        } else if ( sizeInBytes < SPACE_TB ) {
            return nf.format(sizeInBytes/SPACE_GB) + " GB";
        } else {
            return nf.format(sizeInBytes/SPACE_TB) + " TB";
        }
    } catch (Exception e) {
        return sizeInBytes + " Byte(s)";
    }

}

        public static String cattbl="b2b_categories";
		public static String grptbl="b2b_groups";
		public static String usr="b2b_users";
		public static String seasrchusr="b2b_search_users";
		public static String partners="b2b_partners";
		public static String partners_subs="partners_subs";
		public static String orders="b2b_orders";
		public static String grprltbl="b2b_group_relations";
		public static String msgtbl="b2b_conversation";
		public static String bsu="b2b_sub_users";
		public static String bps="b2b_product_specification";
		public static String bp="b2b_products";
		public static String py="b2b_payment_history";
		public static String to="b2b_order_tracking";
		public static String quote="b2b_quotations";
		public static String feedback="b2b_feedback_form";
		public static String order_products="b2b_order_products";
		public static String subactivties="b2b_subusers_activities";
		public static String reminder="b2b_reminders";
		public static String oldprc="b2b_old_product_price";
		public static String bcl="b2b_credit_limit";
		public static String bpcl="b2b_payment_collection_log";
		public static String bn="b2b_notifcations";
		public static String po="b2b_promos";
		 

		public static String loadsearch=base+"app/appsystem/loadSearch/";
	    public static String UrlLogin=base+"app/appsystem/LoginCheck/";
		public static String UrlRegister=base+"app/appsystem/Register/";
		public static String CheckUsername=base+"app/appsystem/CheckUsername/";
		public static String CheckEmail=base+"app/appsystem/CheckEmail/";
		public static String StoreCat=base+"app/appsystem/StoreCat/";
		public static String DelCat=base+"app/appsystem/deleteCat/";
		public static String getUserData=base+"app/appsystem/getUserData/";
		public static String StoreGroup=base+"app/appsystem/StoreGroup/";
		public static String UpdateProfile=base+"app/appsystem/UpdateProfileImage/";
		public static String SendRequest=base+"app/appsystem/SendRequest/";
		public static String chpwd=base+"app/appsystem/chpwd/";
		public static String chvrf=base+"app/appsystem/chvrf/";
		public static String sndcde=base+"app/appsystem/sndcde/";
		public static String webu=base+"app/appsystem/yf1RE9qWEKA3D/";
		public static String webaccept=base+"app/appsystem/AcceptOrder/";
		public static String webu2=base+"app/appsystem/QQRE9qWEKA3D/";
		public static String webuedit=base+"app/appsystem/EDT_yf1RE9qWEKA3D/";
		public static String webupdt=base+"app/appsystem/PDT_yf1RE9qWEKA3D/";
		public static String FjKtl5RKqCTG2jvq=base+"app/appsystem/FjKtl5RKqCTG2jvq/";
		public static String getSubOrders=base+"app/appsystem/getSubOrders/";
		public static String DNAZ62SHEiOsXNpM=base+"app/appsystem/DNAZ62SHEiOsXNpM/";
		public static String gtnt=base+"app/appsystem/gtn/";
		public static String dlnt=base+"app/appsystem/readnt/";
		public static String orderAction=base+"app/appsystem/orderAction/";
		public static String gtmesg=base+"app/appsystem/getMessages/";
		public static String strmesg=base+"app/appsystem/sm/";
		public static String lc=base+"app/appsystem/loadconvo/";
		public static String updateProfile=base+"app/appsystem/updateProfile/";
		public static String adsub=base+"app/appsystem/addSubUsers/";
		public static String Delsuser=base+"app/appsystem/deleteSUser/";
		public static String getSPecs=base+"app/appsystem/gtNewProducts/";
		public static String strpyurl=base+"app/appsystem/storePy/";
		public static String gtpyurl=base+"app/appsystem/gtPy/";
		public static String gtquote=base+"app/appsystem/getQuotes/";
		public static String dispatch=base+"app/appsystem/dispatchOrder/";
		public static String storeFeed=base+"app/appsystem/storeFeed/";
		public static String confirmreceived=base+"app/appsystem/confirmreceived/";
		public static String StoreUsertogroup=base+"app/appsystem/StoreUtog/";
		public static String delGroup=base+"app/appsystem/delGroup/";
		public static String delUserGroup=base+"app/appsystem/delUserGroup/";
		public static String storeSubUsers=base+"app/appsystem/storeSubUsers/";
		public static String getSubs=base+"app/appsystem/getSubs/";
		public static String editSubUsers=base+"app/appsystem/EditSubUsers/";
		public static String getCats=base+"app/appsystem/getCats/";
		public static String deleteSpec=base+"app/appsystem/deleteSpec/";
		public static String readnotifications=base+"app/appsystem/readnotifications/";
		public static String storeMsgAttachment=base+"app/appsystem/storeMsgAttachment/";
		public static String addNewSpec=base+"app/appsystem/addNewSpec/";
		public static String delSpec=base+"app/appsystem/delSpec/";
		public static String DelProduct=base+"app/appsystem/DelProduct/";
		public static String fwdOrdertoSub=base+"app/appsystem/fwdOrdertoSub/";
		public static String getActivities=base+"app/appsystem/getActivities/";
		public static String editGroupNote=base+"app/appsystem/editGroupNote/";
		public static String buyerAccept=base+"app/appsystem/buyerAccept/";
		public static String forgotPwd=base+"app/appsystem/forgotPwd/";
		public static String resetPwd=base+"app/appsystem/resetPwd/";
		public static String storeReminder=base+"app/appsystem/storeReminder/";
		public static String gtReminder=base+"app/appsystem/gtReminder/";
		public static String readReminder=base+"app/appsystem/readReminder/";
		public static String DelRemind=base+"app/appsystem/DelRemind/";
		public static String DelSubUser=base+"app/appsystem/DelSubUser/";
		public static String webul=base+"app/appsystem/PartnerAllProducts/";
		public static String uploadpdf=base+"app/appsystem/uploadPdf/";
		public static String creditlimit=base+"app/appsystem/creditlimit/";
		public static String sendBackup=base+"app/appsystem/sendBackup/";
		public static String setCredit=base+"app/appsystem/setCredit/";
		public static String ApproveOrder=base+"app/appsystem/ApproveOrder/";
		public static String edtOrder=base+"app/appsystem/EditOrderApp/";
		public static String getConversationsUsers=base+"app/appsystem/getConversationsUsers/";
		public static String strcoll=base+"app/appsystem/storeCollection/";
		public static String gtcoll=base+"app/appsystem/getCollection/";
		public static String edtprdt=base+"app/appsystem/updateProductDetailss/";
		public static String DelCollection =base+"app/appsystem/DelCollection/"; 
		public static String deleteOrder =base+"app/appsystem/deleteOrder/"; 
		public static String groupdetails =base+"app/appsystem/groupDetail/";
		public static String createMultiNotifications =base+"app/appsystem/createMultiNotifications/";
		public static String gtmn =base+"app/appsystem/gtmn/";
		


		
}
