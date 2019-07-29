package com.pulkit.imagesteganography.encodeactivity;

import android.graphics.Bitmap;
import android.util.Log;

import com.pulkit.imagesteganography.model.ImageSteganography;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EncodePresenter implements EncodeContract.Presenter{

    private EncodeContract.View view;
    private EncodeRepository encodeRepository;
    private Disposable disposable;

    public EncodePresenter(EncodeContract.View view, EncodeRepository encodeRepository) {
        this.view = view;
        this.encodeRepository = encodeRepository;
    }

    @Override
    public void encodeImage(String message, String secretKey, Bitmap originalImage) {
        encodeRepository.encodeImage(message, secretKey, originalImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageSteganography>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ImageSteganography imageSteganography) {
                        view.showEncodeImage(imageSteganography.getEncodedImage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error: ", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void clearSubscription() {
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
