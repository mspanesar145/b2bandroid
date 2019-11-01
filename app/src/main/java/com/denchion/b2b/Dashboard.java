package com.denchion.b2b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Dashboard extends Fragment {

	public Dashboard(){}

	ListView dbiconslist;
	List<HashMap<String, String>> dbicons  = new ArrayList<HashMap<String, String>>();
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.gb_dashboard2, container, false);
        

        dbiconslist =(ListView) rootView.findViewById(R.id.dbicons);

    	loadIcons();
        return rootView;
	}
	
	public void loadIcons() {
		int[] dbicon = {
				R.drawable.icon_product, R.drawable.icon_order, R.drawable.icon_quote, R.drawable.icon_message,
				R.drawable.icon_payment, R.drawable.icon_partners, R.drawable.collection, R.drawable.icon_sub, R.drawable.icon_group, R.drawable.remind, R.drawable.reminders, R.drawable.offers, R.drawable.icon_cloud
		};


		String[] dbttl = {
				"Products", "Orders", "Quotations", "Messages", "Payments", "Partners", "Collection", "Sub Users", "Groups", "Reminders", "Notifications", "info/promo offers", "Backup & Upload"
		};

		String[] dbtbl = {
				Utils.grptbl, Utils.orders, Utils.quote, Utils.msgtbl, Utils.py, Utils.partners, Utils.bpcl, Utils.bsu, Utils.bps, Utils.reminder, Utils.reminder, Utils.reminder, "Cloud Upload & Backup"
		};

		ArrayList<Integer> dbcon = new ArrayList<Integer>();
		ArrayList<String> dbtl = new ArrayList<String>();
		ArrayList<String> dbtbls = new ArrayList<String>();
		String[] chk1 = Utils.getDefaults("permission", getActivity().getApplicationContext()).split("[ ]");
		for (int j = 0; j < dbicon.length; j++) {
			if (!Utils.getDefaults("subid", getActivity().getApplicationContext()).equals("0")) {
				if (Arrays.asList(chk1).contains(j + "")) {
					if (j != 5) {
						dbcon.add(dbicon[j]);
						dbtl.add(dbttl[j]);
						dbtbls.add(dbtbl[j]);
					}
				}
			} else {
				dbcon.add(dbicon[j]);
				dbtl.add(dbttl[j]);
				dbtbls.add(dbtbl[j]);
			}
		}

		for (int i = 0; i < dbcon.size(); i++) {


			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("uname", dbtl.get(i).toUpperCase(Locale.getDefault()));
			if (i == 12) hm.put("location", "Backup & Upload");
			else hm.put("location", "");//hm.put("location","Total: "+cnt+" "+dbtl.get(i));
			hm.put("img", String.valueOf(dbcon.get(i)));
			hm.put("content_id", i + "");
			dbicons.add(hm);
		}

		String[] from = {"uname", "location", "img", "content_id"};
		int[] to = {R.id.u_title, R.id.u_type, R.id.listimg, R.id.content_id};
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), dbicons, R.layout.dash_layout, from, to);

		dbiconslist.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		// Click event for single list row

		dbiconslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				ListView lv = (ListView) arg0;
				TextView fishtextview = (TextView) arg0.getChildAt(arg2 - lv.getFirstVisiblePosition()).findViewById(R.id.u_title);
				String fieldname = fishtextview.getText().toString();
				switch (fieldname) {
					case "PRODUCTS":
						opt();
						break;
					case "QUOTATIONS":
						((B2BDashboard) getActivity()).actions(7);
						break;
					case "ORDERS":
						((B2BDashboard) getActivity()).actions(1);
						break;
					case "MESSAGES":
						((B2BDashboard) getActivity()).actions(4);
						break;
					case "PARTNERS":
						((B2BDashboard) getActivity()).actions(3);
						break;
					case "PAYMENTS":
						((B2BDashboard) getActivity()).actions(6);
						break;
					case "SUB USERS":
						((B2BDashboard) getActivity()).actions(8);
						break;
					case "GROUPS":
						((B2BDashboard) getActivity()).actions(9);
						break;
					case "REMINDERS":
						((B2BDashboard) getActivity()).actions(11);
						break;
					case "BACKUP & UPLOAD":
						((B2BDashboard) getActivity()).actions(2);
						break;
					case "COLLECTION":
						((B2BDashboard) getActivity()).actions(13);
						break;
					case "NOTIFICATIONS":
						((B2BDashboard) getActivity()).actions(14);
						break;
					case "INFO/PROMO OFFERS":
						((B2BDashboard) getActivity()).actions(15);
						break;
				}
			}
		});
	}

	public void opt() {
		final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		//inflate view for alertdialog since we are using multiple views inside a viewgroup (root = Layout top-level) (linear, relative, framelayout etc..)
		View view = inflater.inflate(R.layout.prompts6a, (ViewGroup) getActivity().findViewById(R.id.layout_root_new2));

		Button button1 = (Button) view.findViewById(R.id.btn1);
		Button button2 = (Button) view.findViewById(R.id.btn2);


		button1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				((B2BDashboard) getActivity()).actions(10);
				alert.dismiss();
			}
		});


		button2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				((B2BDashboard) getActivity()).actions(5);
				alert.dismiss();
			}
		});
		alert.setView(view);
		alert.show();
	}
	
}
        
