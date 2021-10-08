/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.khedr.ecommerce.R;

public abstract class Animations {
    public static void motoProgressbar(Context context, boolean isLoading, View motoLayout, View viewUnderMoto, View back){
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
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(fadeIn);
        new Handler().postDelayed(view::clearAnimation, fadeIn.getDuration());
    }

    public static void animFadeIn(Context context, View view, int duration) {
        view.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
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

    public static void animMotoGone(Context context, View motoLayout, View back) {
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

    public static void motoJumpAndFade(Context context, View motoLayout, View ground, View back) {
        motoLayout.setVisibility(View.VISIBLE);
        animJumpAndFade(context, motoLayout);
        animEndToStart(context, ground);
    }

    public static void animEndToStart(Context context, View view) {
        Animation endToStart = AnimationUtils.loadAnimation(context, R.anim.end_to_start);
        view.startAnimation(endToStart);
    }
}
