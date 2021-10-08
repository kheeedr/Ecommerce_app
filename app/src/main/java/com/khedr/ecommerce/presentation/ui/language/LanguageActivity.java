/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.language;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityLanguageBinding;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LanguageActivity";
    ActivityLanguageBinding b;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_language);
        pref = UserUtils.getPref(this);
        String appLang = pref.getString(getString(R.string.pref_app_language), "ar");
        refreshSelection(appLang);

        b.btLanguageBack.setOnClickListener(this);
        b.layoutLanguageSystem.setOnClickListener(this);
        b.layoutLanguageEnglish.setOnClickListener(this);
        b.layoutLanguageArabic.setOnClickListener(this);

        Locale current = getResources().getConfiguration().locale;
        Log.d(TAG, "mkhedr:" + current.getLanguage());
        Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
        Log.d(TAG, "mkhedr:" + defaultLocale.getLanguage());

    }

    @Override
    public void onClick(View v) {
        if (v == b.btLanguageBack) {
            onBackPressed();
        } else if (v == b.layoutLanguageSystem) {

            Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
            if (!UiUtils.getAppLang(this).equals(defaultLocale.getLanguage())) {
                RestartApp();
            }
            changeLanguage("");
        } else if (v == b.layoutLanguageEnglish) {

            if (!UiUtils.getAppLang(this).equals("en")) {
                RestartApp();
            }
            changeLanguage("en");
        } else if (v == b.layoutLanguageArabic) {
            if (!UiUtils.getAppLang(this).equals("ar")) {
                RestartApp();
            }
            changeLanguage("ar");

        }

    }

    public void changeLanguage(String lang) {
        refreshSelection(lang);
        SharedPreferences.Editor pen = UserUtils.getPref(this).edit();
        pen.putString(getString(R.string.pref_app_language), lang);
        pen.apply();
    }

    public void RestartApp() {

        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finishAffinity();
        startActivity(i);
        finish();

    }

    public void refreshSelection(String lang) {
        if (!lang.equals("")) {
            if (lang.equals("en")) {
                b.ivSystemSelected.setImageResource(R.drawable.ic_empty_circle);
                b.ivEnglishSelected.setImageResource(R.drawable.ic_true);
                b.ivArabicSelected.setImageResource(R.drawable.ic_empty_circle);
            } else if (lang.equals("ar")) {
                b.ivSystemSelected.setImageResource(R.drawable.ic_empty_circle);
                b.ivEnglishSelected.setImageResource(R.drawable.ic_empty_circle);
                b.ivArabicSelected.setImageResource(R.drawable.ic_true);
            }
        } else {
            b.ivSystemSelected.setImageResource(R.drawable.ic_true);
            b.ivEnglishSelected.setImageResource(R.drawable.ic_empty_circle);
            b.ivArabicSelected.setImageResource(R.drawable.ic_empty_circle);
        }
    }
}