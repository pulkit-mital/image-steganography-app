package com.pulkit.imagesteganography.utilitie;

import android.graphics.Bitmap;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

public class BindingUtils {

    @BindingAdapter("imageBitmap")
    public static void setImageBitmap(AppCompatImageView imageView, MutableLiveData<Bitmap> bitmapMutableLiveData) {
        imageView.setImageBitmap(bitmapMutableLiveData.getValue());

    }
}
