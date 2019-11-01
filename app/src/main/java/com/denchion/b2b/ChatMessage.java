package com.denchion.b2b;

import android.content.Context;

public class ChatMessage {
    public boolean left;
    public String message;
    public String type;
    public String nm;
    public Context c;

    public ChatMessage(boolean left, String message, String type,String nm,Context c) {
        super();
        this.left = left;
        this.message = message;
        this.type = type;
        this.nm = nm;
        this.c = c;
    }
    
    
}