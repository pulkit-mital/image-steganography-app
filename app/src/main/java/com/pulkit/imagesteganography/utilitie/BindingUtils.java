package com.pulkit.imagesteganography.utilitie;

import android.graphics.Bitmap;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;

public class BindingUtils {

    @BindingAdapter("imageBitmap")
    public static void setImageBitmap(AppCompatImageView imageView, MutableLiveData<Bitmap> bitmapMutableLiveData) {
        imageView.setImageBitmap(bitmapMutableLiveData.getValue());

    }

    @BindingAdapter("snackMessage")
    public static void setImageBitmap(ConstraintLayout constraintLayout, String message) {
        if(!Utils.isEmpty(message)) {
            Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
        }

    }
}
