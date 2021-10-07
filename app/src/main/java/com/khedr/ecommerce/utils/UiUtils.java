package com.khedr.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.local.Converters;
import com.khedr.ecommerce.ui.fragments.ToLoginBottomSheetFragment;

import java.util.Locale;

public abstract class UiUtils {

    public static Toast toast;

    public static void shortToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

    }

    public static void showLoginFragment(AppCompatActivity activity, String TAG) {

        ToLoginBottomSheetFragment fragment = new ToLoginBottomSheetFragment();
        fragment.show(activity.getSupportFragmentManager(), TAG);
//        activity.getSupportFragmentManager().executePendingTransactions();
    }

    public static void showLoginFragment(FragmentActivity activity, String TAG) {
        ToLoginBottomSheetFragment fragment = new ToLoginBottomSheetFragment();
        fragment.show(activity.getSupportFragmentManager(), TAG);
    }

    public static void getImageViaUrl(Context context, String url, ImageView imageView, View progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Anim.animJumpAndFade(context, progressBar);
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e
                    , Object model, Target<Drawable> target, boolean isFirstResource) {
                shortToast(context, context.getString(R.string.failed_to_load_image));
                progressBar.clearAnimation();
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target
                    , DataSource dataSource, boolean isFirstResource) {

                progressBar.clearAnimation();
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).timeout(30000).into(imageView);


    }

    public static void getImageViaUrl(Context context, String url, ImageView imageView, boolean isUserImage) {
        SharedPreferences pref = context.getSharedPreferences("logined", 0);

        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e
                    , Object model, Target<Drawable> target, boolean isFirstResource) {
                shortToast(context, context.getString(R.string.failed_to_load_image));
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target
                    , DataSource dataSource, boolean isFirstResource) {
                if (isUserImage) {
                    SharedPreferences.Editor pen = pref.edit();
                    pen.putBoolean(context.getString(R.string.pref_is_image_ready), true);
                    pen.putString(context.getString(R.string.pref_user_image),
                            Converters.fromBitmapToString(((BitmapDrawable) resource).getBitmap()));
                    pen.apply();
                }
                return false;
            }
        }).timeout(30000).into(imageView);


    }

    public static int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    public static void setLocale(Context context) {
        String newLocale = getAppLang(context);
        Locale locale = new Locale(newLocale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getAppLang(Context context) {
        String appLang = UserUtils.getPref(context).getString(context.getString(R.string.pref_app_language), "ar");
        if (!appLang.equals("")) {
            return appLang;
        } else {
            Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
            if (defaultLocale.getLanguage().equals("ar")) {
                return "ar";
            } else {
                return "en";
            }
        }
    }

    public static void textError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }

    public static void textError(TextInputLayout editText, String errorMessage) {

        editText.setError(errorMessage);

    }

    public static void makeTvPlusOne(TextView textView) {
        int oldQ = Integer.parseInt(textView.getText().toString());
        int newQ = oldQ + 1;
        textView.setText(String.valueOf(newQ));
    }

    public static void makeTvMinusOne(TextView textView) {
        int oldQ = Integer.parseInt(textView.getText().toString());
        if (oldQ > 1) {
            int newQ = oldQ - 1;
            textView.setText(String.valueOf(newQ));
        }
    }


}
