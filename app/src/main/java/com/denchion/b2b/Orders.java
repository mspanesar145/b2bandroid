package com.denchion.b2b;

import com.denchion.b2b.HttpGetAsyncTask.AsyncResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class Orders extends Activity implements AsyncResponse {


    ProgressDialog dialog;
    JSONObject data = new JSONObject();
    JSONObject jsonObj;
    AsyncResponse delegate = null;
    HttpGetAsyncTask httpGetAsyncTask;
    private int year;
    private int month;
    private int day;
    String todaydate;

    List<HashMap<String, String>> accepted = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> sentorders = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> pendingorders = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> ignoredorders = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> directo = new ArrayList<HashMap<String, String>>();
    ListView sentOrders;
    ListView o_pending;
    ListView o_ignored;
    ListView accept;
    ListView directl;

    String ttp;
    String tb = "1";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.gb_orders);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        todaydate = day + "-" + (month + 1) + "-" + year;
        Bundle bundle = getIntent().getExtras();
        tb = bundle.getString("tb");
        ttp = bundle.getString("ttp");
        Utils.setActionBar(this, "Orders");
        DBHelper.loaddb(getApplicationContext());
        tabHost(Integer.parseInt(tb));
        sentOrders = (ListView) findViewById(R.id.sent);
        o_pending = (ListView) findViewById(R.id.o_pending);
        o_ignored = (ListView) findViewById(R.id.o_ignored);
        accept = (ListView) findViewById(R.id.accept);
        directl = (ListView) findViewById(R.id.o_direct);
        if (Utils.isConnectingToInternet(getApplicationContext()))
            storeOrders();
        else
            Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();

        if (!ttp.equals("")) {
            deleteOrder(ttp, "0", "2");
        }
    }


    public void tabHost(int tb) {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        final TabWidget tabWidget = tabHost.getTabWidget();
        final FrameLayout tabContent = tabHost.getTabContentView();

        // Get the original tab textviews and remove them from the viewgroup.
        TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
        for (int index = 0; index < tabWidget.getTabCount(); index++) {
            originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
        }
        tabWidget.removeAllViews();
        // Ensure that all tab content childs are not visible at startup.
        for (int index = 0; index < tabContent.getChildCount(); index++) {
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }

        for (int index = 0; index < originalTextViews.length; index++) {
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());

            tabSpec.setContent(new TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
            if (tabWidgetTextView.getBackground() == null) {
                tabSpec.setIndicator(tabWidgetTextView.getText());
            } else {
                tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
            }
            tabHost.addTab(tabSpec);
            tabHost.setCurrentTab(tb);
        }
    }


    public void storeOrders() {
        httpGetAsyncTask = new HttpGetAsyncTask(this, 3);
        httpGetAsyncTask.delegate = this;
        httpGetAsyncTask.execute(Utils.FjKtl5RKqCTG2jvq + Utils.getDefaults("user_id", getApplicationContext()));
    }


    public void loadIgnoredOrders(String q) {
        String tsp = "1";
        ignoredorders.clear();
        Cursor d = DBHelper.getData(getApplicationContext(), Utils.orders, q);
        if (d.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No Ignored Orders Found!", Toast.LENGTH_SHORT).show();
        else {
            while (d.moveToNext()) {
                String bid = "";
                if (Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))
                    bid = d.getString(3);
                else bid = d.getString(1);

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uname", "ORDER ID: " + d.getString(0).toUpperCase(Locale.getDefault()));
                hm.put("location", DBHelper.getNameByID(getApplicationContext(), bid));
                hm.put("date", Utils.parseDateToddMMyyyy2(d.getString(21)) + " " + Utils.ConvertTime(d.getString(22)));
                hm.put("content_id", d.getString(0));
                hm.put("rid", bid);
                if (d.getString(18).equals("1")) tsp = "2";
                hm.put("tp", tsp);
                ignoredorders.add(hm);
            }
        }

        String[] from = {"uname", "location", "date", "content_id", "rid"};
        int[] to = {R.id.u_title, R.id.u_type, R.id.u_dt, R.id.content_id, R.id.rid};
        SimpleAdapter adapter = new SimpleAdapter(this, ignoredorders, R.layout.list_layout3, from, to);


        o_ignored.setAdapter(null);
        adapter.notifyDataSetChanged();
        o_ignored.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Click event for single list row

        o_ignored.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                String fieldname = fishtextview.getText().toString();
                Intent intent = new Intent(Orders.this, OrderDetails.class);
                intent.putExtra("id", fieldname);
                intent.putExtra("tp", "3");
                startActivity(intent);
                finish();
            }
        });
	         
	       /*  o_ignored.setOnItemLongClickListener(new OnItemLongClickListener() {
                 public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                         int arg2, long id) {
         			ListView lv = (ListView) arg0;
                     TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                     TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.rid);
                     String gid = fishtextview.getText().toString();
                     String rid = fishtextview2.getText().toString();
                     if(Utils.getDefaults("subid", getApplicationContext()).equals("0")){
                         deleteOrder(gid,rid,"1");
                         }
                    
     				
     				 return true;
     			}
     		});*/
    }

    public void loadSentOrders(String q) {
        sentorders.clear();
        String tsp = "1";
        Cursor d = DBHelper.getData(getApplicationContext(), Utils.orders, q);
        if (d.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No Sent Orders Found!", Toast.LENGTH_SHORT).show();
        else {
            while (d.moveToNext()) {
                String bid = "";
                String nm = "";
                if (Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))
                    bid = d.getString(3);
                else bid = d.getString(1);

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uname", "ORDER ID: " + d.getString(0).toUpperCase(Locale.getDefault()));
                hm.put("location", DBHelper.getNameByID(getApplicationContext(), bid));
                hm.put("date", Utils.parseDateToddMMyyyy2(d.getString(21)) + " " + Utils.ConvertTime(d.getString(22)));
                hm.put("content_id", d.getString(0));
                hm.put("uid", d.getString(3));
                hm.put("rid", bid);
                if (d.getString(37).equals("1")) nm = "Approval Pending";
                else if (d.getString(37).equals("2")) nm = "Saved as Draft";
                else nm = "";
                hm.put("status", nm);
                if (d.getString(18).equals("1")) tsp = "2";
                hm.put("tp", tsp);
                sentorders.add(hm);
            }
        }

        String[] from = {"uname", "location", "date", "content_id", "tp", "status", "uid", "rid"};
        int[] to = {R.id.u_title, R.id.u_type, R.id.u_dt, R.id.content_id, R.id.type, R.id.u_status, R.id.uid, R.id.rid};
        SimpleAdapter adapter = new SimpleAdapter(this, sentorders, R.layout.list_layout3, from, to);


        sentOrders.setAdapter(null);
        adapter.notifyDataSetChanged();
        sentOrders.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Click event for single list row

        sentOrders.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView fishtextview2 = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.type);
                TextView fishtextview3 = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                TextView fishtextview4 = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.uid);
                String fieldname = fishtextview.getText().toString();
                String fieldname2 = fishtextview2.getText().toString();
                String fieldname3 = fishtextview3.getText().toString();
                String fieldname4 = fishtextview4.getText().toString();
                if (fieldname3.equals("Approval Pending")) {
                    PopOptions(fieldname, fieldname2, fieldname4, "1");
                } else if (fieldname3.equals("Saved as Draft")) {
                    PopOptions(fieldname, fieldname2, fieldname4, "2");
                } else {
                    Intent intent = new Intent(Orders.this, OrderDetails.class);
                    intent.putExtra("id", fieldname);
                    intent.putExtra("tp", fieldname2);
                    startActivity(intent);
                    finish();
                }

            }
        });
	         
	       /*  sentOrders.setOnItemLongClickListener(new OnItemLongClickListener() {
                 public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                         int arg2, long id) {
         			ListView lv = (ListView) arg0;
                     TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                     TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.rid);
                     TextView fishtextview3=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                     String gid = fishtextview.getText().toString();
                     String rid = fishtextview2.getText().toString();
                     String fieldname3 = fishtextview3.getText().toString();
                     if(Utils.getDefaults("subid", getApplicationContext()).equals("0")){
                        if(!fieldname3.equals("Approval Pending") || !fieldname3.equals("Saved as Draft")){
                    	 deleteOrder(gid,rid,"1");
                        }
                         }
                    
     				
     				 return true;
     			}
     		});*/
    }


    public void loadPendingOrders(String q) {
        pendingorders.clear();
        Cursor d = DBHelper.getData(getApplicationContext(), Utils.orders, q);
        if (d.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No Pending Orders Found!", Toast.LENGTH_SHORT).show();
        else {
            while (d.moveToNext()) {
                String bid = "";
                if (Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))
                    bid = d.getString(3);
                else bid = d.getString(1);

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uname", "ORDER ID: " + d.getString(0).toUpperCase(Locale.getDefault()));
                hm.put("location", DBHelper.getNameByID(getApplicationContext(), bid));
                hm.put("date", Utils.parseDateToddMMyyyy2(d.getString(21)) + " " + Utils.ConvertTime(d.getString(22)));
                hm.put("content_id", d.getString(0));
                hm.put("rid", bid);
                pendingorders.add(hm);


            }
        }
        String[] from = {"uname", "location", "date", "content_id", "rid"};
        int[] to = {R.id.u_title, R.id.u_type, R.id.u_dt, R.id.content_id, R.id.rid};
        SimpleAdapter adapter = new SimpleAdapter(this, pendingorders, R.layout.list_layout3, from, to);


        o_pending.setAdapter(null);
        adapter.notifyDataSetChanged();
        o_pending.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Click event for single list row

        o_pending.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                String fieldname = fishtextview.getText().toString();
                Intent intent = new Intent(Orders.this, OrderDetails.class);
                intent.putExtra("id", fieldname);
                intent.putExtra("tp", "3");
                startActivity(intent);
                finish();
            }
        });
	         
	       /*  o_pending.setOnItemLongClickListener(new OnItemLongClickListener() {
                 public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                         int arg2, long id) {
         			ListView lv = (ListView) arg0;
                     TextView fishtextview=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                     TextView fishtextview2=(TextView)arg0.getChildAt(arg2-lv.getFirstVisiblePosition()).findViewById(R.id.rid);
                     String gid = fishtextview.getText().toString();
                     String rid = fishtextview2.getText().toString();
                     if(Utils.getDefaults("subid", getApplicationContext()).equals("0")){
                         deleteOrder(gid,rid,"1");
                         }
                    
     				
     				 return true;
     			}
     		});*/
    }

    public void loadAcceptedOrders(String q) {
        accepted.clear();
        Cursor d = DBHelper.getData(getApplicationContext(), Utils.orders, q);

        if (d.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No Accepted Orders Found!", Toast.LENGTH_SHORT).show();
        else {
            while (d.moveToNext()) {

                String bid = "";
                if (Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))
                    bid = d.getString(3);
                else bid = d.getString(1);

                String nm = "";
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uname", "ORDER ID: " + d.getString(0).toUpperCase(Locale.getDefault()));
                hm.put("location", DBHelper.getNameByID(getApplicationContext(), bid));
                hm.put("uid", d.getString(1));
                if (d.getString(12).equals("0")) nm = "Pending";
                else if (d.getString(12).equals("1") && d.getString(15).equals("0") && d.getString(18).equals("0"))
                    nm = "Seller Accepted";
                else if (d.getString(12).equals("5") && d.getString(15).equals("0") && d.getString(18).equals("0")) {
                    if (d.getString(39).equals("1")) nm = "Direct Order";
                    else nm = "Buyer Accepted";
                } else if (d.getString(15).equals("1") && d.getString(18).equals("0"))
                    nm = "Dispatched";
                else if (d.getString(15).equals("1") && d.getString(18).equals("1"))
                    nm = "Delivered";
                else if (d.getString(12).equals("0") && d.getString(33).equals("1")) nm = "Ignored";
                else if (d.getString(12).equals("4")) nm = "Cancelled";

                hm.put("status", nm);
                hm.put("date", Utils.parseDateToddMMyyyy2(d.getString(21)) + " " + Utils.ConvertTime(d.getString(22)));
                hm.put("content_id", d.getString(0));
                hm.put("rid", bid);
                accepted.add(hm);


            }
        }

        String[] from = {"uname", "location", "date", "content_id", "status", "uid", "rid"};
        int[] to = {R.id.u_title, R.id.u_type, R.id.u_dt, R.id.content_id, R.id.u_status, R.id.uid, R.id.rid};
        SimpleAdapter adapter = new SimpleAdapter(this, accepted, R.layout.list_layout3, from, to);


        accept.setAdapter(null);
        adapter.notifyDataSetChanged();
        accept.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Click event for single list row

        accept.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView uuid = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.uid);
                TextView stat = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                String fieldname = fishtextview.getText().toString();
                String uid = uuid.getText().toString();
                String status = stat.getText().toString();
                Intent intent = new Intent(Orders.this, OrderDetails.class);
                intent.putExtra("id", fieldname);
                if (uid.equals(Utils.getDefaults("user_id", getApplicationContext()))) {
                    if (status.equals("Delivered") || status.equals("Cancelled"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Seller Accepted") || status.equals("Partial Accept"))
                        intent.putExtra("tp", "7");
                    else if (status.equals("Buyer Accepted") || status.equals("Direct Order"))
                        intent.putExtra("tp", "1");
                    else if (status.equals("Dispatched")) intent.putExtra("tp", "5");
                } else {
                    if (status.equals("Delivered") || status.equals("Cancelled"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Seller Accepted") || status.equals("Partial Accept"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Buyer Accepted") || status.equals("Direct Order"))
                        intent.putExtra("tp", "4");
                    else if (status.equals("Dispatched")) intent.putExtra("tp", "6");

                }
                startActivity(intent);
            }
        });


        accept.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id) {
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView fishtextview2 = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.rid);
                TextView stat = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                String status = stat.getText().toString();
                String gid = fishtextview.getText().toString();
                String rid = fishtextview2.getText().toString();
                if (Utils.getDefaults("subid", getApplicationContext()).equals("0")) {
                    if (status.equals("Delivered")) {
                        deleteOrder(gid, rid, "1");
                    }
                }

                return true;
            }
        });


    }


    public void loadDirectOrders(String q) {
        directo.clear();
        Cursor d = DBHelper.getData(getApplicationContext(), Utils.orders, q);
        if (d.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No Direct Orders Found!", Toast.LENGTH_SHORT).show();
        else {
            while (d.moveToNext()) {

                String bid = "";
                if (Utils.getDefaults("user_id", getApplicationContext()).equals(d.getString(1)))
                    bid = d.getString(3);
                else bid = d.getString(1);

                String nm = "";
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uname", "ORDER ID: " + d.getString(0).toUpperCase(Locale.getDefault()));
                hm.put("location", DBHelper.getNameByID(getApplicationContext(), bid));
                hm.put("uid", d.getString(1));
                if (d.getString(12).equals("0")) nm = "Pending";
                else if (d.getString(12).equals("1") && d.getString(15).equals("0") && d.getString(18).equals("0"))
                    nm = "Seller Accepted";
                else if (d.getString(12).equals("5") && d.getString(15).equals("0") && d.getString(18).equals("0")) {
                    if (d.getString(39).equals("1")) nm = "Direct Order";
                    else nm = "Buyer Accepted";
                } else if (d.getString(15).equals("1") && d.getString(18).equals("0"))
                    nm = "Dispatched";
                else if (d.getString(15).equals("1") && d.getString(18).equals("1"))
                    nm = "Delivered";
                else if (d.getString(12).equals("0") && d.getString(33).equals("1")) nm = "Ignored";
                else if (d.getString(12).equals("4")) nm = "Cancelled";

                hm.put("status", nm);
                hm.put("date", Utils.parseDateToddMMyyyy2(d.getString(21)) + " " + Utils.ConvertTime(d.getString(22)));
                hm.put("content_id", d.getString(0));
                hm.put("rid", bid);
                directo.add(hm);


            }
        }

        String[] from = {"uname", "location", "date", "content_id", "status", "uid", "rid"};
        int[] to = {R.id.u_title, R.id.u_type, R.id.u_dt, R.id.content_id, R.id.u_status, R.id.uid, R.id.rid};
        SimpleAdapter adapter = new SimpleAdapter(this, directo, R.layout.list_layout3, from, to);


        directl.setAdapter(null);
        adapter.notifyDataSetChanged();
        directl.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Click event for single list row

        directl.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView uuid = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.uid);
                TextView stat = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                String fieldname = fishtextview.getText().toString();
                String uid = uuid.getText().toString();
                String status = stat.getText().toString();
                Intent intent = new Intent(Orders.this, OrderDetails.class);
                intent.putExtra("id", fieldname);
                if (uid.equals(Utils.getDefaults("user_id", getApplicationContext()))) {
                    if (status.equals("Delivered") || status.equals("Cancelled"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Seller Accepted") || status.equals("Partial Accept"))
                        intent.putExtra("tp", "7");
                    else if (status.equals("Buyer Accepted") || status.equals("Direct Order"))
                        intent.putExtra("tp", "1");
                    else if (status.equals("Dispatched")) intent.putExtra("tp", "5");
                } else {
                    if (status.equals("Delivered") || status.equals("Cancelled"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Seller Accepted") || status.equals("Partial Accept"))
                        intent.putExtra("tp", "6");
                    else if (status.equals("Buyer Accepted") || status.equals("Direct Order"))
                        intent.putExtra("tp", "4");
                    else if (status.equals("Dispatched")) intent.putExtra("tp", "6");

                }
                startActivity(intent);
            }
        });


        directl.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id) {
                ListView lv = (ListView) arg0;
                TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.content_id);
                TextView fishtextview2 = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.rid);
                TextView stat = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_status);
                String status = stat.getText().toString();
                String gid = fishtextview.getText().toString();
                String rid = fishtextview2.getText().toString();
                if (Utils.getDefaults("subid", getApplicationContext()).equals("0")) {
                    if (status.equals("Delivered")) {
                        deleteOrder(gid, rid, "1");
                    }
                }

                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.norder:
                Intent homeIntent = new Intent(this, NewOrder.class);
                startActivity(homeIntent);

                break;
            case R.id.sorder:
                Intent homeIntent2 = new Intent(this, SearchOrder.class);
                startActivity(homeIntent2);

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    @Override
    public void processFinish(String output) {


        try {

            jsonObj = new JSONObject(output);

            JSONArray Data = jsonObj.getJSONArray("orders");
            JSONArray Data2 = jsonObj.getJSONArray("oldprc");
            DBHelper.storeOrders(getApplicationContext(), Data);
            DBHelper.storeoldprc(getApplicationContext(), Data2);
            loadSentOrders(" where sender_id='" + Utils.getDefaults("user_id", getApplicationContext()) + "' and  accept_status=0 order by order_id DESC");
            loadPendingOrders(" where accept_status=0 and ignore_status=0 and approve_status=0 and receiver_id='" + Utils.getDefaults("user_id", getApplicationContext()) + "' order by order_id DESC");
            loadAcceptedOrders(" where  ordtype=0 and accept_status in (1,2,4,5) and cancel in (0," + Utils.getDefaults("user_id", getApplicationContext()) + ") order by order_id DESC");
            loadIgnoredOrders(" where accept_status=0 and ignore_status=1 and receiver_id='" + Utils.getDefaults("user_id", getApplicationContext()) + "' order by order_id DESC");
            loadDirectOrders(" where  ordtype=1 and accept_status in (1,2,4,5) and cancel in (0," + Utils.getDefaults("user_id", getApplicationContext()) + ") order by order_id DESC");
            //Toast.makeText(getApplicationContext(), output+"", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void PopOptions(final String oid, final String tp, final String rid, final String dftp) {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                Orders.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Options");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Orders.this,
                android.R.layout.select_dialog_singlechoice);


        if (dftp.equals("1")) arrayAdapter.add("Approve Order");
        else arrayAdapter.add("Send Order");
        arrayAdapter.add("Edit Order");
        arrayAdapter.add("View Details");


        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapter.getItem(which);

                        if (strName.equals("Approve Order") || strName.equals("Send Order")) {
                            httpGetAsyncTask = new HttpGetAsyncTask(Orders.this, 3);
                            httpGetAsyncTask.delegate = Orders.this;
                            httpGetAsyncTask.execute(Utils.ApproveOrder + Utils.getDefaults("user_id", getApplicationContext()) + "/" + Utils.getDefaults("subid", getApplicationContext()) + "/" + rid + "/" + oid);
                        } else if (strName.equals("Edit Order")) {
                            Intent intent = new Intent(Orders.this, EditOrder.class);
                            intent.putExtra("id", oid);
                            intent.putExtra("tp", tp);
                            startActivity(intent);
                            finish();
                        } else if (strName.equals("View Details")) {
                            Intent intent = new Intent(Orders.this, OrderDetails.class);
                            intent.putExtra("id", oid);
                            intent.putExtra("tp", tp);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        builderSingle.show();
    }


    public void deleteOrder(final String oid, final String rid, final String type) {
        String lbl = "Do you really want to send delete Request?";
        if (type.equals("2")) lbl = "Do you want to delete this order ID#" + oid + " ?";
        new AlertDialog.Builder(this)

                .setMessage(lbl)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (Utils.isConnectingToInternet(getApplicationContext())) {
                            try {
                                data.put("uid", Utils.getDefaults("user_id", getApplicationContext()));
                                data.put("rid", rid);
                                data.put("oid", oid);
                                data.put("type", type);
                                data.put("subid", Utils.getDefaults("subid", getApplicationContext()));
                                httpGetAsyncTask = new HttpGetAsyncTask(Orders.this, 3);
                                httpGetAsyncTask.delegate = Orders.this;
                                httpGetAsyncTask.execute(Utils.deleteOrder + URLEncoder.encode(data.toString().replace(" ", "_"), "UTF-8"));
                                if (type.equals("2"))
                                    Toast.makeText(getApplicationContext(), "Order ID#" + oid + " Successfully Backed up on Device and Deleted!", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Order ID#" + oid + " Successfully Delete Request sent and Backed up on Device!", Toast.LENGTH_LONG).show();
                                exportToExcel(false, false, false, true, false, false, false, false, 5, false);

                            } catch (JSONException | UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void exportToExcel(Boolean chk1, Boolean chk2, Boolean chk3, Boolean chk4, Boolean chk5, Boolean chk6, Boolean chk7, Boolean chk8, int tp, Boolean chk9) {

        if (chk1 != false || chk2 != false || chk3 != false || chk4 != false || chk5 != false || chk6 != false || chk7 != false || chk8 != false) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);


            final String fileName = "b2b_" + todaydate + "_" + String.format("%02d-%02d", hour, minute) + ".xls";

            //Saving file in external storage
            File sdCard = Environment.getExternalStorageDirectory();
            final File directory = new File(sdCard.getAbsolutePath() + "/Download/");

            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            //file path
            final File file = new File(directory, fileName);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            Cursor c1 = DBHelper.getData(getApplicationContext(), Utils.cattbl, "");
            Cursor d1 = DBHelper.getData(getApplicationContext(), Utils.grptbl, "");
            Cursor e1 = DBHelper.getData(getApplicationContext(), Utils.partners, "");
            Cursor f1 = DBHelper.getData(getApplicationContext(), Utils.orders, "");
            Cursor h1 = DBHelper.getData(getApplicationContext(), Utils.bsu, " ");
            Cursor ii1 = DBHelper.getData(getApplicationContext(), Utils.bps, " order by product_id ASC");
            Cursor j1 = DBHelper.getData(getApplicationContext(), Utils.py, "");
            Cursor k1 = DBHelper.getJoinData(getApplicationContext(), "qq.*,pt.full_name", Utils.quote + " qq", " Left Outer Join " + Utils.partners + " pt ON pt.partner_id=qq.reciever_id  order by qq.quotation_id DESC");
            try {
                workbook = Workbook.createWorkbook(file, wbSettings);

                WritableSheet sheet = null;
                WritableSheet sheet2 = null;
                WritableSheet sheet3 = null;
                WritableSheet sheet4 = null;
                WritableSheet sheet6 = null;
                WritableSheet sheet7 = null;
                WritableSheet sheet8 = null;
                WritableSheet sheet9 = null;

                if (chk1 == true) sheet = workbook.createSheet("Categories", 0);
                if (chk2 == true) sheet2 = workbook.createSheet("Groups", 1);
                if (chk3 == true) sheet3 = workbook.createSheet("Partners", 2);
                if (chk4 == true) sheet4 = workbook.createSheet("Orders", 3);
                if (chk5 == true) sheet6 = workbook.createSheet("Sub Users", 5);
                if (chk6 == true) sheet7 = workbook.createSheet("Specifications", 6);
                if (chk7 == true) sheet8 = workbook.createSheet("Payments", 7);
                if (chk8 == true) sheet9 = workbook.createSheet("Quotations", 8);


                try {
                    if (chk1 == true) {
                        //tbl 1
                        sheet.addCell(new Label(0, 0, "ID"));
                        sheet.addCell(new Label(1, 0, "Name"));
                        sheet.addCell(new Label(2, 0, "Parent"));
                    }

                    if (chk2 == true) {
                        //tbl 2
                        sheet2.addCell(new Label(0, 0, "GROUP"));
                        sheet2.addCell(new Label(1, 0, "LIMIT"));
                    }

                    if (chk3 == true) {
                        //tbl 3
                        sheet3.addCell(new Label(0, 0, "NAME"));
                        sheet3.addCell(new Label(1, 0, "EMAIL"));
                        sheet3.addCell(new Label(2, 0, "MOBILE"));
                        sheet3.addCell(new Label(3, 0, "ADDRESS"));
                        sheet3.addCell(new Label(4, 0, "CITY"));
                        sheet3.addCell(new Label(5, 0, "STATE"));
                        sheet3.addCell(new Label(6, 0, "DATE"));
                        sheet3.addCell(new Label(7, 0, "TIME"));
                    }

                    if (chk4 == true) {
                        //tbl 4

                        sheet4.addCell(new Label(0, 0, "RECEIVER"));
                        sheet4.addCell(new Label(1, 0, "ORDER ID"));
                        sheet4.addCell(new Label(2, 0, "Transport Type"));
                        sheet4.addCell(new Label(3, 0, "Product Data"));
                        sheet4.addCell(new Label(4, 0, "Sender Remarks"));
                        sheet4.addCell(new Label(5, 0, "Receiver Remarks"));
                        sheet4.addCell(new Label(6, 0, "Accept Status"));
                        sheet4.addCell(new Label(7, 0, "Dispatched_date"));
                        sheet4.addCell(new Label(8, 0, "Dispatched_time"));
                        sheet4.addCell(new Label(9, 0, "Confirm_date"));
                        sheet4.addCell(new Label(10, 0, "Confirm_time"));
                        sheet4.addCell(new Label(11, 0, "Order_date"));
                        sheet4.addCell(new Label(12, 0, "Order_time"));
                        sheet4.addCell(new Label(13, 0, "Transport"));
                        sheet4.addCell(new Label(14, 0, "Tracking no."));
                        sheet4.addCell(new Label(15, 0, "Feedback"));
                        sheet4.addCell(new Label(16, 0, "Remarks"));
                        sheet4.addCell(new Label(17, 0, "Ignore Status"));
                        sheet4.addCell(new Label(18, 0, "Delivery Type"));
                        sheet4.addCell(new Label(19, 0, "Schedule Date"));
                    }

                    if (chk5 == true) {

                        //tbl 6
                        sheet6.addCell(new Label(0, 0, "USERNAME"));
                        sheet6.addCell(new Label(1, 0, "PASSWORD"));
                        sheet6.addCell(new Label(2, 0, "PERMISSION"));
                        sheet6.addCell(new Label(3, 0, "DATE"));
                        sheet6.addCell(new Label(4, 0, "TIME"));
                    }

                    if (chk6 == true) {
                        //tbl 7
                        sheet7.addCell(new Label(0, 0, "NAME"));
                        sheet7.addCell(new Label(1, 0, "VALUE"));
                        sheet7.addCell(new Label(2, 0, "PRODUCT"));
                    }

                    if (chk7 == true) {
                        //tbl 8
                        sheet8.addCell(new Label(0, 0, "USERNAME"));
                        sheet8.addCell(new Label(1, 0, "PASSWORD"));
                        sheet8.addCell(new Label(2, 0, "PERMISSION"));
                        sheet8.addCell(new Label(3, 0, "DATE"));
                        sheet8.addCell(new Label(4, 0, "TIME"));
                    }
                    if (chk8 == true) {
                        sheet9.addCell(new Label(0, 0, "RECEIVER"));
                        sheet9.addCell(new Label(1, 0, "DETAILS"));
                        sheet9.addCell(new Label(2, 0, "PRODUCT"));
                        sheet9.addCell(new Label(3, 0, "CONDITIONS"));
                        sheet9.addCell(new Label(4, 0, "TYPE"));
                        sheet9.addCell(new Label(5, 0, "DATE"));
                        sheet9.addCell(new Label(6, 0, "TIME"));
                    }

                    if (chk1 == true) {
                        int cc = 1;
                        while (c1.moveToNext()) {
                            sheet.addCell(new Label(0, cc, c1.getString(0)));
                            sheet.addCell(new Label(1, cc, c1.getString(2)));
                            sheet.addCell(new Label(2, cc, c1.getString(3)));
                            cc++;
                        }
                        c1.close();

                    }

                    if (chk2 == true) {
                        int dd = 1;
                        while (d1.moveToNext()) {
                            sheet2.addCell(new Label(0, dd, d1.getString(1)));
                            sheet2.addCell(new Label(1, dd, d1.getString(2)));
                            dd++;
                        }
                        d1.close();

                    }
                    if (chk3 == true) {
                        int ee = 1;
                        while (e1.moveToNext()) {
                            sheet3.addCell(new Label(0, ee, e1.getString(1)));
                            sheet3.addCell(new Label(1, ee, e1.getString(2)));
                            sheet3.addCell(new Label(2, ee, e1.getString(3)));
                            sheet3.addCell(new Label(3, ee, e1.getString(4)));
                            sheet3.addCell(new Label(4, ee, e1.getString(8)));
                            sheet3.addCell(new Label(5, ee, e1.getString(9)));
                            sheet3.addCell(new Label(6, ee, e1.getString(10)));
                            sheet3.addCell(new Label(7, ee, e1.getString(11)));
                            ee++;
                        }
                        e1.close();

                    }

                    if (chk4 == true) {
                        int ff = 1;
                        while (f1.moveToNext()) {
                            sheet4.addCell(new Label(0, ff, f1.getString(4)));
                            sheet4.addCell(new Label(1, ff, f1.getString(0)));
                            sheet4.addCell(new Label(2, ff, f1.getString(7)));
                            sheet4.addCell(new Label(3, ff, f1.getString(8)));
                            sheet4.addCell(new Label(4, ff, f1.getString(10)));
                            sheet4.addCell(new Label(5, ff, f1.getString(11)));
                            sheet4.addCell(new Label(6, ff, f1.getString(12)));
                            sheet4.addCell(new Label(7, ff, f1.getString(16)));
                            sheet4.addCell(new Label(8, ff, f1.getString(17)));
                            sheet4.addCell(new Label(9, ff, f1.getString(19)));
                            sheet4.addCell(new Label(10, ff, f1.getString(20)));
                            sheet4.addCell(new Label(11, ff, f1.getString(21)));
                            sheet4.addCell(new Label(12, ff, f1.getString(22)));
                            sheet4.addCell(new Label(13, ff, f1.getString(24)));
                            sheet4.addCell(new Label(14, ff, f1.getString(25)));
                            sheet4.addCell(new Label(15, ff, f1.getString(28)));
                            sheet4.addCell(new Label(16, ff, f1.getString(29)));
                            sheet4.addCell(new Label(17, ff, f1.getString(33)));
                            sheet4.addCell(new Label(18, ff, f1.getString(6)));
                            sheet4.addCell(new Label(19, ff, f1.getString(38)));
                            ff++;
                        }
                        f1.close();
                    }

                    if (chk5 == true) {
                        int hh = 1;
                        while (h1.moveToNext()) {
                            sheet6.addCell(new Label(0, hh, h1.getString(2)));
                            sheet6.addCell(new Label(1, hh, h1.getString(3)));
                            if (h1.getString(3).equals("1"))
                                sheet6.addCell(new Label(2, hh, "ALL ACCESS"));
                            else sheet6.addCell(new Label(2, hh, "LIMITED"));
                            sheet6.addCell(new Label(3, hh, h1.getString(5)));
                            sheet6.addCell(new Label(4, hh, h1.getString(6)));

                            hh++;
                        }
                        h1.close();

                    }

                    if (chk6 == true) {
                        int ii = 1;
                        while (ii1.moveToNext()) {
                            sheet7.addCell(new Label(0, ii, ii1.getString(3)));
                            sheet7.addCell(new Label(1, ii, ii1.getString(4)));
                            sheet7.addCell(new Label(2, ii, DBHelper.getNAMEByPRODUCTID(getApplicationContext(), ii1.getString(1))));

                            ii++;
                        }
                        ii1.close();
                    }

                    if (chk7 == true) {
                        int jj = 1;
                        while (j1.moveToNext()) {
                            sheet8.addCell(new Label(0, jj, j1.getString(2)));
                            sheet8.addCell(new Label(1, jj, j1.getString(3)));
                            sheet8.addCell(new Label(3, jj, j1.getString(5)));
                            sheet8.addCell(new Label(4, jj, j1.getString(6)));

                            jj++;
                        }
                        j1.close();

                    }

                    if (chk8 == true) {
                        int kk = 1;
                        while (k1.moveToNext()) {
                            sheet9.addCell(new Label(0, kk, k1.getString(12)));
                            sheet9.addCell(new Label(1, kk, k1.getString(5)));
                            sheet9.addCell(new Label(2, kk, k1.getString(6)));
                            sheet9.addCell(new Label(3, kk, k1.getString(7)));
                            String nm = "";
                            if (k1.getString(9).equals("1")) nm = "Sent";
                            else nm = "Asked";
                            sheet9.addCell(new Label(4, kk, nm));
                            sheet9.addCell(new Label(5, kk, k1.getString(10)));
                            sheet9.addCell(new Label(6, kk, k1.getString(11)));

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

                //if(tp!=5)openApp(fileName,tp);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Toast.makeText(getApplicationContext(), "Backup Successfully Generated! File saves in download Directory!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Please Select one Option", Toast.LENGTH_SHORT).show();
        }
    }
}
