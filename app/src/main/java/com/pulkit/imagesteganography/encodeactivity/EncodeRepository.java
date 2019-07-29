package com.pulkit.imagesteganography.encodeactivity;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.model.ImageSteganography;

import io.reactivex.Observable;


public class EncodeRepository {

    private EncodeServiceApi encodeServiceApi;

    public EncodeRepository(EncodeServiceApi encodeServiceApi) {
        this.encodeServiceApi = encodeServiceApi;
    }

    public Observable<ImageSteganography> encodeImage(String message, String secretKey, Bitmap originalImage) {
        return encodeServiceApi.getEncodedImage(new ImageSteganography(message, secretKey, originalImage));
    }
}
