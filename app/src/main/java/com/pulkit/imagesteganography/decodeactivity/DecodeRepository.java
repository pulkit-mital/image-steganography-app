package com.pulkit.imagesteganography.decodeactivity;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.model.ImageSteganography;

import io.reactivex.Observable;

public class DecodeRepository {

    private DecodeServiceApi decodeServiceApi;

    public DecodeRepository(DecodeServiceApi decodeServiceApi) {
        this.decodeServiceApi = decodeServiceApi;
    }

    public Observable<ImageSteganography> decodeImage(String secretKey, Bitmap encodedImage){
        return decodeServiceApi.decodeImage(new ImageSteganography(secretKey, encodedImage));
    }
}
