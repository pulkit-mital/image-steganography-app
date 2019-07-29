package com.pulkit.imagesteganography.navigation;

import android.content.Context;
import android.content.Intent;

import com.pulkit.imagesteganography.activities.HomeActivity;
import com.pulkit.imagesteganography.decodeactivity.DecodeActivity;
import com.pulkit.imagesteganography.encodeactivity.EncodeActivity;
import com.pulkit.imagesteganography.utilitie.Constants;

public class Navigate {

    public static void toEncodeActivity(Context context) {
        Intent intent = new Intent(context, EncodeActivity.class);
        context.startActivity(intent);
    }

    public static void openImageChooser(Context context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((EncodeActivity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCode.PICK_IMAGE);
    }
    public static void openImageChooserDecode(Context context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((DecodeActivity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCode.PICK_IMAGE);
    }

    public static void toDecodeActivity(Context context) {
        Intent intent = new Intent(context, DecodeActivity.class);
        context.startActivity(intent);
    }

    public static void toHomeActivity(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
