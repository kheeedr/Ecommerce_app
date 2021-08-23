package com.khedr.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.user.UserApiResponse;

import java.util.Locale;

public abstract class UserUtils {
    static SharedPreferences pref;

    public static boolean isSignedIn(Context context) {
        return getPref(context).getBoolean(context.getString(R.string.pref_status), false);
    }

    public  static SharedPreferences getPref(Context context){
        if (pref==null) {
            pref = context.getSharedPreferences("logined", 0);
        }
        return pref;

    }
    public static String getUserToken(Context context){

        return getPref(context).getString(context.getString(R.string.pref_user_token), "");
    }



    public static void saveUserProfileToShared(UserApiResponse response, Context context, String image) {

        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor pen = pref.edit();

        pen.putBoolean(context.getString(R.string.pref_status), true);
        pen.putInt(context.getString(R.string.pref_user_id), response.getData().getId());
        pen.putString(context.getString(R.string.pref_user_name), response.getData().getName());
        pen.putString(context.getString(R.string.pref_user_email), response.getData().getEmail());
        pen.putString(context.getString(R.string.pref_user_phone), response.getData().getPhone());
        pen.putBoolean(context.getString(R.string.pref_is_image_ready), image != null);
        pen.putString(context.getString(R.string.pref_user_image), image);
        pen.putString(context.getString(R.string.pref_user_image_url), response.getData().getImage());
        pen.putInt(context.getString(R.string.pref_user_points), response.getData().getPoints());
        pen.putString(context.getString(R.string.pref_user_credit), String.format(Locale.US,"%.2f",response.getData().getCredit()));
        pen.putString(context.getString(R.string.pref_user_token), response.getData().getToken());

        pen.apply();
    }
    public static String getUserName(Context context){
        if (isSignedIn(context)) {
            return getPref(context).getString(context.getString(R.string.pref_user_name), "null");
        }
        else {
            return "You should login first";
        }
    }
    public static String getUserPhone(Context context){
        if (isSignedIn(context)) {
            return getPref(context).getString(context.getString(R.string.pref_user_phone), "null");
        }
        else {
            return "You should login first";
        }
    }
}
