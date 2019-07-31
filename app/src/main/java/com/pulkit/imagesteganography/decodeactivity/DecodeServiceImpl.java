package com.pulkit.imagesteganography.decodeactivity;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.model.ImageSteganography;
import com.pulkit.imagesteganography.utilitie.EncodeDecode;
import com.pulkit.imagesteganography.utilitie.Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DecodeServiceImpl implements DecodeServiceApi {

    @Override
    public Observable<ImageSteganography> decodeImage(ImageSteganography imageSteganography) {
        return Observable.create(new ObservableOnSubscribe<ImageSteganography>() {
            @Override
            public void subscribe(ObservableEmitter<ImageSteganography> emitter) throws Exception {
                try{

                    ImageSteganography result = new ImageSteganography();
                    //getting bitmap from the fiel
                    Bitmap bitmap = imageSteganography.getImage();

                    //splitting images
                    List<Bitmap> sourceEncodedList = Utils.splitImage(bitmap);

                    //decodeing encrypted zipped message
                    String decodedMessage = EncodeDecode.decodeMessage(sourceEncodedList);
                    if(!Utils.isEmpty(decodedMessage)){
                        result.setDecoded(true);
                    }

                    //decrypting the encoding message
                    String decryptedMessage = ImageSteganography.decryptMessage(decodedMessage, imageSteganography.getSecretKey());
                    if(!Utils.isEmpty(decryptedMessage)){
                        result.setWrongSecretKey(false);
                        result.setMessage(decryptedMessage);
                        for (Bitmap bitm : sourceEncodedList)
                            bitm.recycle();

                        //Java Garbage Collector
                        System.gc();
                    }

                    if(result.isWrongSecretKey()){
                        emitter.onError(new Exception("Please enter correct secret key"));
                    }

                    emitter.onNext(result);


                }catch (Exception ex){
                    emitter.onError(ex);
                }
            }
        });
    }
}
