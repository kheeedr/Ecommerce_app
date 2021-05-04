package com.khedr.ecommerce.ui.operations;

import android.content.Context;
import android.content.SharedPreferences;

import com.khedr.ecommerce.R;

public abstract class UserOperations {


    public static boolean isSignedIn(Context context) {
        return getPref(context).getBoolean(context.getString(R.string.pref_status), false);
    }

    public synchronized static SharedPreferences getPref(Context context){
        return context.getSharedPreferences("logined", 0);
    }
}
