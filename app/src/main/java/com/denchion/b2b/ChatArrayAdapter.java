package com.denchion.b2b;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    public List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    
    Context context;

	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }
    

    public void reset() {
    	chatMessageList.clear();
    }
    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
       final  ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        
        OnClickListener SaveView = new OnClickListener() {
            @SuppressLint("NewApi")
           public void onClick(View v) 
            {
            	if(chatMessageObj.type.equals("2")){
            		new AlertDialog.Builder(chatMessageObj.c)
            		.setMessage("Do you Really Want to Download!")
            		.setIcon(android.R.drawable.ic_dialog_alert)
            		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            		    public void onClick(DialogInterface dialog, int whichButton) {
            		    	   new DownloadFileFromURL(chatMessageObj.c).execute(chatMessageObj.nm);	
            		    }})
            		 .setNegativeButton(android.R.string.no, null).show();
      
            
            	}
        
            }
        };
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);
        }else{
            row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        chatText.setOnClickListener(SaveView);
        return row;
    }


	

	
}
