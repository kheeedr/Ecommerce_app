package com.khedr.ecommerce.ui.activities.aboutUs;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAboutUsBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        b.btAboutBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id== R.id.bt_about_back){
            finish();
        }
    }
}