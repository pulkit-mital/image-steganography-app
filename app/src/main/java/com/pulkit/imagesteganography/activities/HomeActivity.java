package com.pulkit.imagesteganography.activities;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.pulkit.imagesteganography.R;
import com.pulkit.imagesteganography.databinding.ActivityHomeBinding;
import com.pulkit.imagesteganography.navigation.Navigate;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initializeToolbar();
    }

    private void initializeToolbar() {
        setSupportActionBar(activityHomeBinding.layoutToolbar.toolBar);
    }

    public void encodeImage(View view) {
        Navigate.toEncodeActivity(this);
    }

    public void decodeImage(View view){
        Navigate.toDecodeActivity(this);
    }

}

