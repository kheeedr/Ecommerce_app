/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentNotificationsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationsFragment extends Fragment implements View.OnClickListener {

    FragmentNotificationsBinding b;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         b= DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);

         b.btNotificationsBack.setOnClickListener(this);

         return b.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v==b.btNotificationsBack){
            requireActivity().onBackPressed();
        }
    }
}