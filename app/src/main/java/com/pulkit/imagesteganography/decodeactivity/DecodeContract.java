package com.pulkit.imagesteganography.decodeactivity;

import android.graphics.Bitmap;

public interface DecodeContract {

    interface View {
        void showDecodedMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void decodeImage(String secretKey, Bitmap encodedImage);
        void clearSubscription();
    }
}
