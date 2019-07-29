package com.pulkit.imagesteganography.decodeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.pulkit.imagesteganography.R;
import com.pulkit.imagesteganography.databinding.ActivityDecodeBinding;
import com.pulkit.imagesteganography.navigation.Navigate;
import com.pulkit.imagesteganography.utilitie.Constants;
import com.pulkit.imagesteganography.utilitie.PermissionUtils;

public class DecodeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDecodeBinding activityDecodeBinding;
    private PermissionUtils permissionUtils;
    private DecodeViewModel decodeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDecodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_decode);
        activityDecodeBinding.setLifecycleOwner(this);
        decodeViewModel = ViewModelProviders.of(this).get(DecodeViewModel.class);
        decodeViewModel.setClickListener(this);
        activityDecodeBinding.setDecodeViewModel(decodeViewModel);
        initializeToolbar();
        intializeData();
    }


    private void initializeToolbar() {
        setSupportActionBar(activityDecodeBinding.layoutToolbar.toolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void intializeData() {
        permissionUtils = new PermissionUtils(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                if (permissionUtils.isPermissionGranted(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})) {
                    Navigate.openImageChooserDecode(this);
                } else {
                    permissionUtils.showPermissionDialog(Constants.RequestCode.REQUEST_READ_STORAGE_PERMISSION);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        decodeViewModel.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
