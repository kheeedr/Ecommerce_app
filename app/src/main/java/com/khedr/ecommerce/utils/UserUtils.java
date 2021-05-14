package com.khedr.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.khedr.ecommerce.R;

public abstract class UserUtils {


    public static boolean isSignedIn(Context context) {
        return getPref(context).getBoolean(context.getString(R.string.pref_status), false);
    }

    public synchronized static SharedPreferences getPref(Context context){
        return context.getSharedPreferences("logined", 0);
    }
    public static String getUserToken(Context context){

        return getPref(context).getString(context.getString(R.string.pref_user_token), "");
    }
}