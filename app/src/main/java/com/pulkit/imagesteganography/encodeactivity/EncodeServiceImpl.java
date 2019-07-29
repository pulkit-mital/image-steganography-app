package com.pulkit.imagesteganography.encodeactivity;

import android.graphics.Bitmap;
import android.os.Environment;

import com.pulkit.imagesteganography.model.ImageSteganography;
import com.pulkit.imagesteganography.utilitie.EncodeDecode;
import com.pulkit.imagesteganography.utilitie.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class EncodeServiceImpl implements EncodeServiceApi {

    @Override
    public Observable<ImageSteganography> getEncodedImage(ImageSteganography imageSteganography) {
        return Observable.create(new ObservableOnSubscribe<ImageSteganography>() {
            @Override
            public void subscribe(ObservableEmitter<ImageSteganography> emitter) throws Exception {

                try {
                    ImageSteganography result = new ImageSteganography();
                    Bitmap bitmap = imageSteganography.getImage();
                    int originalHeight = bitmap.getHeight();
                    int originalWidth = bitmap.getWidth();

                    //splitting bitmap
                    List<Bitmap> sourceList = Utils.splitImage(bitmap);
                    List<Bitmap> encodeList = EncodeDecode.encodeMessage(sourceList, imageSteganography.getEncryptedMessage());

                    //free memory
                    for (Bitmap bitmap1 : sourceList) {
                        bitmap1.recycle();
                    }

                    //java garbage collector
                    System.gc();

                    //merging the split encoded image
                    Bitmap encodedBitmap = Utils.mergeImage(encodeList, originalHeight, originalWidth);

                    result.setEncodedImage(encodedBitmap);
                    result.setEncoded(true);
                    saveToInternalStorage(encodedBitmap, emitter);

                    emitter.onNext(result);


                } catch (Exception ex) {
                    emitter.onError(ex);
                }
            }
        });
    }

    private void saveToInternalStorage(Bitmap bitmapImage, ObservableEmitter<ImageSteganography> emitter) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".png"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (FileNotFoundException e) {
            emitter.onError(e);
            e.printStackTrace();
        } catch (IOException e) {
            emitter.onError(e);
            e.printStackTrace();
        }
    }
}
