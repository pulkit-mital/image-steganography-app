package com.pulkit.imagesteganography.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pulkit.imagesteganography.R;
import com.pulkit.imagesteganography.navigation.Navigate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                Navigate.toHomeActivity(SplashActivity.this);
                finish();
            }

        }, 3 * 1000);
    }
}
