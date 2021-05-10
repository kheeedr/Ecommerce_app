package com.khedr.ecommerce.ui;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySearchBinding;
import com.khedr.ecommerce.ui.adapters.SearchSuggestionsAdapter;
import com.khedr.ecommerce.utils.UiUtils;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySearchBinding b;
    SearchSuggestionsAdapter suggestionsAdapter;
    MutableLiveData<boolean[]> isSucceeded=new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= DataBindingUtil.setContentView(this,R.layout.activity_search);
        b.btSearchCancel.setOnClickListener(this);
        suggestionsAdapter=new SearchSuggestionsAdapter(this);

        b.rvSearchSuggestion.setAdapter(suggestionsAdapter);

        b.svSearchEt.requestFocus();
        b.svSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //performSearch();
                UiUtils.shortToast(this,"searching..., "+ v.getText().toString());
                return true;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    @Override
    public void onClick(View v) {
        if (v==b.btSearchCancel){
            finish();
        }
    }
}