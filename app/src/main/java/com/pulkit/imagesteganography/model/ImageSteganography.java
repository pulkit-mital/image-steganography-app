package com.pulkit.imagesteganography.model;

import android.graphics.Bitmap;

import com.pulkit.imagesteganography.utilitie.Crypto;
import com.pulkit.imagesteganography.utilitie.Utils;

public class ImageSteganography {

    private String message;
    private String secretKey;
    private String encryptedMessage;
    private Bitmap image;
    private Bitmap encodedImage;
    private byte[] encryptedZip;
    private boolean encoded;
    private boolean decoded;
    private boolean wrongSecretKey;

    public ImageSteganography() {
        this.encoded = false;
        this.decoded = false;
        this.wrongSecretKey = true;
        this.message = "";
        this.secretKey = "";
        this.encryptedMessage = "";
        this.image = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encodedImage = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encryptedZip = new byte[0];
    }

    public ImageSteganography(String message, String secretKey, Bitmap image) {
        this.message = message;
        this.secretKey = Utils.convertKeyTo128bit(secretKey);
        this.image = image;
        this.encryptedZip = message.getBytes();
        this.encryptedMessage = encryptMessage(message, this.secretKey);
        this.encoded = false;
        this.decoded = false;
        this.wrongSecretKey = true;
        this.encodedImage = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
    }

    public ImageSteganography(String secretKey, Bitmap image) {
        this.secretKey = Utils.convertKeyTo128bit(secretKey);
        this.image = image;
        this.encoded = false;
        this.decoded = false;
        this.wrongSecretKey = true;
        this.message = "";
        this.encryptedMessage = "";
        this.encodedImage = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encryptedZip = new byte[0];

    }

    private static String encryptMessage(String message, String secretKey) {
        String encryptedMessage = "";
        if (!Utils.isEmpty(message)) {
            if (!Utils.isEmpty(secretKey)) {
                try {
                    encryptedMessage = Crypto.encryptMessage(message, secretKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                encryptedMessage = message;
            }
        }

        return encryptedMessage;
    }

    public static String decryptMessage(String message, String secretKey) {
        String decyptedMessage = "";
        if (!Utils.isEmpty(message)) {
            if (!Utils.isEmpty(secretKey)) {
                try {
                    decyptedMessage = Crypto.decryptMessage(message, secretKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                decyptedMessage = message;
            }
        }

        return decyptedMessage;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(Bitmap encodedImage) {
        this.encodedImage = encodedImage;
    }

    public byte[] getEncryptedZip() {
        return encryptedZip;
    }

    public void setEncryptedZip(byte[] encryptedZip) {
        this.encryptedZip = encryptedZip;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public boolean isDecoded() {
        return decoded;
    }

    public void setDecoded(boolean decoded) {
        this.decoded = decoded;
    }

    public boolean isWrongSecretKey() {
        return wrongSecretKey;
    }

    public void setWrongSecretKey(boolean wrongSecretKey) {
        this.wrongSecretKey = wrongSecretKey;
    }
}
