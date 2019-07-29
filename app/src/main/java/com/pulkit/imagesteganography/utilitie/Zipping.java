package com.pulkit.imagesteganography.utilitie;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Zipping {

    public static byte[] compress(String message) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(message.length());
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(message.getBytes());
        gzipOutputStream.close();

        byte[] compressed = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return compressed;
    }


    public static String decompress(byte[] compressed) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, Charset.forName("ISO-8859-1")));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        gzipInputStream.close();
        byteArrayInputStream.close();

        return stringBuilder.toString();
    }
}
