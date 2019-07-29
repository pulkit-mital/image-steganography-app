package com.pulkit.imagesteganography.utilitie;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    public static String encryptMessage(String message, String secretKey) throws Exception {

        SecretKeySpec aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(message.getBytes());

        return Base64.encodeToString(encrypted, 0);

    }

    public static String decryptMessage(String encryptedMessage, String secretKEy) throws Exception {

        SecretKeySpec aesKey = new SecretKeySpec(secretKEy.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decoded = Base64.decode(encryptedMessage.getBytes(), 0);
        String decrypted = new String(cipher.doFinal(decoded));

        return decrypted;
    }
}
