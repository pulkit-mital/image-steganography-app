package com.pulkit.imagesteganography.decodeactivity;

import android.app.Application;
import android.content.Intent;
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
import com.pulkit.imagesteganography.utilitie.Constants;
import com.pulkit.imagesteganography.utilitie.Utils;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class DecodeViewModel extends AndroidViewModel implements DecodeContract.View {

    private MutableLiveData<String> secretKey = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Bitmap> encodedImage = new MutableLiveData<>();
    private MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    private View.OnClickListener onClickListener;
    private Application application;
    private DecodePresenter decodePresenter;

    public DecodeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        encodedImage.postValue(BitmapFactory.decodeResource(application.getResources(), R.drawable.image_placeholder));
        this.decodePresenter = new DecodePresenter(this, new DecodeRepository(new DecodeServiceImpl()));
    }

    public MutableLiveData<String> getSecretKey() {
        return secretKey;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Bitmap> getEncodedImage() {
        return encodedImage;
    }

    public MutableLiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }

    public void setClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onUploadClicked(View view) {
        onClickListener.onClick(view);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.RequestCode.PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                Bitmap encodedImage = MediaStore.Images.Media.getBitmap(application.getContentResolver(), filepath);
                this.encodedImage.postValue(encodedImage);
            } catch (IOException e) {
                Log.d("Error", "Error : " + e);
            }
        }
    }

    public void onDecodeClicked(View view) {
        if(!Utils.isEmpty(secretKey.getValue())) {
            decodePresenter.decodeImage(secretKey.getValue(), encodedImage.getValue());
        }else{
            snackbarMessage.postValue("Please enter valid secret key");
        }
    }

    @Override
    protected void onCleared() {
        decodePresenter.clearSubscription();
        super.onCleared();
    }

    @Override
    public void showDecodedMessage(String message) {
        this.message.postValue(message);
        snackbarMessage.postValue("Decoded Successfully!");
    }

    @Override
    public void showError(String message) {
        snackbarMessage.postValue(message);
    }
}
