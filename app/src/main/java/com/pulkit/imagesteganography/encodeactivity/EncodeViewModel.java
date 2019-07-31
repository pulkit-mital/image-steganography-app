package com.pulkit.imagesteganography.encodeactivity;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pulkit.imagesteganography.R;
import com.pulkit.imagesteganography.navigation.Navigate;
import com.pulkit.imagesteganography.utilitie.Constants;
import com.pulkit.imagesteganography.utilitie.PermissionUtils;
import com.pulkit.imagesteganography.utilitie.Utils;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class EncodeViewModel extends AndroidViewModel implements EncodeContract.View {

    private MutableLiveData<String> secretKey = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Bitmap> originalImage = new MutableLiveData<>();
    private MutableLiveData<Integer> shareVisibility = new MutableLiveData<>(View.GONE);
    private MutableLiveData<String> snackbarMessage = new MutableLiveData<>("");
    private View.OnClickListener onClickListener;
    private Application application;
    private EncodePresenter encodePresenter;
    private PermissionUtils permissionUtils;


    public EncodeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        originalImage.postValue(BitmapFactory.decodeResource(application.getResources(), R.drawable.image_placeholder));
        this.encodePresenter = new EncodePresenter(this, new EncodeRepository(new EncodeServiceImpl()));

    }

    public MutableLiveData<String> getSecretKey() {
        return secretKey;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Integer> getShareVisibility() {
        return shareVisibility;
    }

    public MutableLiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }

    public void onUploadClicked(View view) {
        onClickListener.onClick(view);
    }

    public void setClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setPermissionUtils(PermissionUtils permissionUtils) {
        this.permissionUtils = permissionUtils;
    }

    public MutableLiveData<Bitmap> getOriginalImage() {
        return originalImage;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.RequestCode.PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                Bitmap original_image = MediaStore.Images.Media.getBitmap(application.getContentResolver(), filepath);
                originalImage.postValue(original_image);
            } catch (IOException e) {
                Log.d("Error", "Error : " + e);
            }
        }
    }

    public void onEncodeClicked(View view) {

        if (!Utils.isEmpty(message.getValue()) && !Utils.isEmpty(secretKey.getValue())) {
            if (permissionUtils.isPermissionGranted(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})) {
                encodePresenter.encodeImage(message.getValue(), secretKey.getValue(), originalImage.getValue());
            } else {
                permissionUtils.showPermissionDialog(Constants.RequestCode.REQUEST_WRITE_STORAGE);
            }
        } else {
            snackbarMessage.postValue("Please enter message and secret key");
        }

    }

    @Override
    public void showEncodeImage(Bitmap encodedImage) {
        originalImage.postValue(encodedImage);
        snackbarMessage.postValue("Encoded Successfully and saved in downloads with a name Encoded.png");
        shareVisibility.postValue(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        snackbarMessage.postValue(message);
    }

    public void onShareClicked(View view) {
        onClickListener.onClick(view);
    }

    @Override
    protected void onCleared() {
        encodePresenter.clearSubscription();
        super.onCleared();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.RequestCode.REQUEST_WRITE_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            encodePresenter.encodeImage(message.getValue(), secretKey.getValue(), originalImage.getValue());
        } else {
            permissionUtils.showPermissionDialog(Constants.RequestCode.REQUEST_READ_STORAGE_PERMISSION);
        }
    }
}
