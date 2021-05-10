package com.khedr.ecommerce.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySearchBinding;
import com.khedr.ecommerce.operations.UiOperations;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= DataBindingUtil.setContentView(this,R.layout.activity_search);
        b.svSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //performSearch();
                UiOperations.shortToast(this,"searching...");
                return true;
            }
            return false;
        });
    }
}