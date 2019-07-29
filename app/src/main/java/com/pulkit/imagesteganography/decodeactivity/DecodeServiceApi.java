package com.pulkit.imagesteganography.decodeactivity;


import com.pulkit.imagesteganography.model.ImageSteganography;

import io.reactivex.Observable;

public interface DecodeServiceApi {

    Observable<ImageSteganography> decodeImage(ImageSteganography imageSteganography);
}
