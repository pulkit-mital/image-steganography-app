package com.pulkit.imagesteganography.encodeactivity;

import com.pulkit.imagesteganography.model.ImageSteganography;

import io.reactivex.Observable;

public interface EncodeServiceApi {

    Observable<ImageSteganography> getEncodedImage(ImageSteganography imageSteganography);
}
