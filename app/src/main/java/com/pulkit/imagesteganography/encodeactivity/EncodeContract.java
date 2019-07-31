package com.pulkit.imagesteganography.encodeactivity;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.model.ImageSteganography;

public interface EncodeContract {

    interface View {
        void showEncodeImage(Bitmap encodedImage);
        void showError(String message);
    }

    interface Presenter {

        void encodeImage(String message, String secretKey, Bitmap originalImage);
        void clearSubscription();
    }
}
