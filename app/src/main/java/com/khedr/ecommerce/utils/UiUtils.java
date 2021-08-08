package com.khedr.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;

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
    public static void motoProgressbar(Context context,boolean isLoading,View motoLayout,View viewUnderMoto,View back){
        if (isLoading) {
           back.setVisibility(View.VISIBLE);
          animJumpAndFade(context, motoLayout);
          animEndToStart(context, viewUnderMoto);
        } else {
          animMotoGone(context, motoLayout, back);
        }
    }

    public static void animZoomOut(Context context, View view) {
        Animation zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoomout);
        view.startAnimation(zoomOut);
        new Handler().postDelayed(() -> {
            view.clearAnimation();
            view.setVisibility(View.GONE);
        }, zoomOut.getDuration());

    }

    public static void animZoomIn(Context context, View view) {
        view.setVisibility(View.VISIBLE);
        Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        view.startAnimation(zoomIn);
        new Handler().postDelayed(view::clearAnimation, zoomIn.getDuration());
    }

    public static void animFadeIn(Context context, View view) {
        view.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
        view.startAnimation(fadeIn);
        new Handler().postDelayed(view::clearAnimation, fadeIn.getDuration());
    }

    public static void animFadeIn(Context context, View view, int duration) {
        view.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
        fadeIn.setDuration(duration);
        view.startAnimation(fadeIn);
        new Handler().postDelayed(view::clearAnimation, fadeIn.getDuration());
    }

    public static void animFadeOut(Context context, View view) {
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);
        view.startAnimation(fadeOut);
        new Handler().postDelayed(() -> {
            view.clearAnimation();
            view.setVisibility(View.GONE);
        }, fadeOut.getDuration());
    }

    public static void animCenterToEnd(Context context, View view) {
        Animation moveToEnd = AnimationUtils.loadAnimation(context, R.anim.center_to_end);
        view.startAnimation(moveToEnd);
        view.setVisibility(View.GONE);
    }
    public static void animMotoGone(Context context, View motoLayout,View back) {
        Animation moveToEnd = AnimationUtils.loadAnimation(context, R.anim.center_to_end);
        motoLayout.startAnimation(moveToEnd);
        motoLayout.setVisibility(View.GONE);
        animFadeOut(context,back);
    }

    public static void animJumpAndFade(Context context, View view) {
        Animation jump = AnimationUtils.loadAnimation(context, R.anim.jump_and_fade);
        view.startAnimation(jump);
    }

    public static void motoJumpAndFade(Context context, View motoLayout, View ground) {
        motoLayout.setVisibility(View.VISIBLE);
        animJumpAndFade(context, motoLayout);
        animEndToStart(context, ground);
    }
    public static void motoJumpAndFade(Context context, View motoLayout, View ground,View back) {
        motoLayout.setVisibility(View.VISIBLE);
        animJumpAndFade(context, motoLayout);
        animEndToStart(context, ground);
    }

    public static void animEndToStart(Context context, View view) {
        Animation endToStart = AnimationUtils.loadAnimation(context, R.anim.end_to_start);
        view.startAnimation(endToStart);
    }

    public static void getImageViaUrl(Context context, String url, ImageView imageView, View progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        UiUtils.animJumpAndFade(context, progressBar);
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
        }).into(imageView);


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
        }).into(imageView);


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
        String appLang = UserUtils.getPref(context).getString(context.getString(R.string.pref_app_language), "");
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
    } public static void textError(TextInputLayout editText, String errorMessage) {

        editText.setError(errorMessage);

    }
    public static void makeTvPlusOne(TextView textView){
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
