package com.pulkit.imagesteganography.decodeactivity;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.model.ImageSteganography;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DecodePresenter implements DecodeContract.Presenter {

    private DecodeContract.View view;
    private DecodeRepository decodeRepository;
    private Disposable disposable;

    public DecodePresenter(DecodeContract.View view, DecodeRepository decodeRepository) {
        this.view = view;
        this.decodeRepository = decodeRepository;
    }

    @Override
    public void decodeImage(String secretKey, Bitmap encodedImage) {
        decodeRepository.decodeImage(secretKey, encodedImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageSteganography>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ImageSteganography imageSteganography) {
                        view.showDecodedMessage(imageSteganography.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
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
