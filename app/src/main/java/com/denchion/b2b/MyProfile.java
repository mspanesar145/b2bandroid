package com.denchion.b2b;

import java.io.File;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MyProfile extends Fragment{

	public MyProfile(){}
   
	 TextView txt1;
	 TextView txt1a;
	 TextView txt2;
	 TextView txt3;
	 TextView txt3a;
	 TextView txt4;
	 TextView txt4a;
	 TextView txt5;
	 TextView txt5a;
	 TextView txt5b;
	 TextView txt7;
	 TextView txt8;
	 ImageView Image;
	 Button  bt;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
		
         final View rootView = inflater.inflate(R.layout.gb_my_profile, container, false);
         
         ImageView imgView = (ImageView) rootView.findViewById(R.id.imglogo);
 	
  		File imgFile = new  File(Environment.getExternalStorageDirectory().getPath()+"/.b2b/dp/user"+Utils.getDefaults("user_id", getActivity().getApplicationContext())+".jpg");
  	    Picasso.with(getActivity().getApplicationContext()).load(imgFile).error(R.drawable.noprofile).placeholder(R.drawable.noprofile).into(imgView);
         txt1=(TextView)rootView.findViewById(R.id.detailtxt1);
         txt1a=(TextView)rootView.findViewById(R.id.detailtxt1a);
         txt2=(TextView)rootView.findViewById(R.id.detailtxt2);
         txt3=(TextView)rootView.findViewById(R.id.detailtxt3);
         txt3a=(TextView)rootView.findViewById(R.id.detailtxt3a);
         txt4=(TextView)rootView.findViewById(R.id.detailtxt4);
         txt4a=(TextView)rootView.findViewById(R.id.detailtxt4a);
         txt5=(TextView)rootView.findViewById(R.id.detailtxt5);
         txt5a=(TextView)rootView.findViewById(R.id.udetailtxt5a);
         txt5b=(TextView)rootView.findViewById(R.id.udetailtxt5b);
         txt7=(TextView)rootView.findViewById(R.id.detailtxt7);
         txt8=(TextView)rootView.findViewById(R.id.udetailtxt5c);
         
         
         Cursor c=DBHelper.getData(getActivity().getApplicationContext(),Utils.usr,"");
         
         if(c.moveToFirst())
         {
        	 txt1.setText(c.getString(1));
        	 txt1a.setText(c.getString(19));
        	 txt2.setText(c.getString(2));
        	 txt3.setText(c.getString(3));
        	 txt3a.setText(c.getString(17));
        	 txt4.setText(c.getString(5));
        	 txt4a.setText(c.getString(18));
        	 txt5.setText(c.getString(6));
        	 txt5a.setText(c.getString(12));
        	 txt5b.setText(c.getString(13));
        	 txt7.setText(Utils.parseDateToddMMyyyy2(c.getString(10)));
        	 txt8.setText(c.getString(20));
         }
         
         bt=(Button)rootView.findViewById(R.id.edtp);
         
         bt.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), EditProfile.class);
                 startActivity(intent);
           
             }
         });
      
         return rootView;
	}
	
	
}