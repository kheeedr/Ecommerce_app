package com.khedr.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.user.UserApiResponse;

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
        pen.putString(context.getString(R.string.pref_user_credit), String.valueOf(response.getData().getCredit()));
        pen.putString(context.getString(R.string.pref_user_token), response.getData().getToken());

        pen.apply();
    }
}
