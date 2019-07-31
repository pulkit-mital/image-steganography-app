package com.pulkit.imagesteganography.encodeactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.pulkit.imagesteganography.R;
import com.pulkit.imagesteganography.databinding.ActivityEncodeBinding;
import com.pulkit.imagesteganography.navigation.Navigate;
import com.pulkit.imagesteganography.utilitie.Constants;
import com.pulkit.imagesteganography.utilitie.PermissionUtils;

public class EncodeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityEncodeBinding activityEncodeBinding;
    private PermissionUtils permissionUtils;
    private EncodeViewModel encodeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEncodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_encode);
        activityEncodeBinding.setLifecycleOwner(this);
        encodeViewModel = ViewModelProviders.of(this).get(EncodeViewModel.class);
        encodeViewModel.setClickListener(this);
        activityEncodeBinding.setEncodeViewModel(encodeViewModel);
        initializeToolbar();
        intializeData();

    }

    private void initializeToolbar() {
        setSupportActionBar(activityEncodeBinding.layoutToolbar.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void intializeData() {
        permissionUtils = new PermissionUtils(this);
        encodeViewModel.setPermissionUtils(permissionUtils);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                if (permissionUtils.isPermissionGranted(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})) {
                    Navigate.openImageChooser(this);
                } else {
                    permissionUtils.showPermissionDialog(Constants.RequestCode.REQUEST_READ_STORAGE_PERMISSION);
                }
                break;
            case R.id.btn_share:
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), encodeViewModel.getOriginalImage().getValue(), "encoded", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                startActivity(Intent.createChooser(intent, "Share"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        encodeViewModel.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case Constants.RequestCode.REQUEST_WRITE_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    encodeViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case Constants.RequestCode.REQUEST_READ_STORAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Navigate.openImageChooser(this);
                }
                break;

        }
    }
}
