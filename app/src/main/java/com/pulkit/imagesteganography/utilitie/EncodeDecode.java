package com.pulkit.imagesteganography.utilitie;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Message;

import com.pulkit.imagesteganography.model.MessageDecodingStatus;
import com.pulkit.imagesteganography.model.MessageEncodingStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EncodeDecode {

    private static final String END_MESSAGE_COSTANT = "#!@";
    private static final String START_MESSAGE_COSTANT = "@!#";
    private static final int[] binary = {16, 8, 0};
    private static final byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static final int[] toShift = {6, 4, 2, 0};

    /**
     * This method represent the core of 2 bit Encoding
     *
     * @return : byte encoded pixel array
     * @parameter :  integer_pixel_array {The integer RGB array}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     * @parameter : messageEncodingStatus {object}
     */
    private static byte[] encodeMessage(int[] integerPixelArray, int imageColumns, int imageRows, MessageEncodingStatus messageEncodingStatus) {
        // RGb channels
        int channels = 3;

        int shiftIndex = 4;

        //creating result byte_array
        byte[] result = new byte[imageRows * imageColumns * channels];
        int resultIndex = 0;

        for (int row = 0; row < imageRows; row++) {
            for (int col = 0; col < imageColumns; col++) {

                //2D matrix in 1D
                int element = row * imageColumns + col;

                byte temp;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {

                    if (!messageEncodingStatus.isMessageEncoded()) {
                        // Shifting integer value by 2 in left and replacing the two least significant digits with the message_byte_array values..
                        temp = (byte) ((((integerPixelArray[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((messageEncodingStatus.getByteArrayMessage()[messageEncodingStatus.getCurrentMessageIndex()] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6

                        if (shiftIndex % toShift.length == 0) {
                            messageEncodingStatus.incrementMessageIndex();
                        }

                        if (messageEncodingStatus.getCurrentMessageIndex() == messageEncodingStatus.getByteArrayMessage().length) {
                            messageEncodingStatus.setMessageEncoded(true);
                        }
                    } else {
                        //Simply copy the integer to result array
                        temp = (byte) ((((integerPixelArray[element] >> binary[channelIndex]) & 0xFF)));
                    }
                    result[resultIndex++] = temp;

                }

            }
        }

        return result;
    }

    /**
     * This method implements the above method on the list of chunk image list.
     *
     * @return : Encoded list of chunk images
     * @parameter : splitted_images {list of chunk images}
     * @parameter : encrypted_message {string}
     */
    public static List<Bitmap> encodeMessage(List<Bitmap> splittedImages, String encryptedMessage) {

        List<Bitmap> result = new ArrayList<>(splittedImages.size());

        //Adding start and end message constants to the encruptyed message
        encryptedMessage = encryptedMessage + END_MESSAGE_COSTANT;
        encryptedMessage = START_MESSAGE_COSTANT + encryptedMessage;

        //getting byte array from string
        byte[] byteEnvryptedMessage = encryptedMessage.getBytes(Charset.forName("ISO-8859-1"));

        //Message Encoding status
        MessageEncodingStatus messageEncodingStatus = new MessageEncodingStatus(encryptedMessage, byteEnvryptedMessage);

        for (Bitmap bitmap : splittedImages) {
            if (!messageEncodingStatus.isMessageEncoded()) {
                //getting bitmap width and height
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                //Making 1D integer pixel array
                int[] oneD = new int[width * height];
                bitmap.getPixels(oneD, 0, width, 0, 0, width, height);

                //getting bitmap density
                int density = bitmap.getDensity();

                //encoding image
                byte[] encodedImage = encodeMessage(oneD, width, height, messageEncodingStatus);

                //converting byte image array to intege arra
                int[] oneDMod = Utils.byteArrayToIntArray(encodedImage);

                Bitmap encodedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                encodedBitmap.setDensity(density);

                int masterIndex = 0;

                //setting pixel values of above bitmap
                for (int j = 0; j < height; j++) {
                    for (int i = 0; i < width; i++) {

                        encodedBitmap.setPixel(i, j, Color.argb(0xFF,
                                oneDMod[masterIndex] >> 16 & 0xFF,
                                oneDMod[masterIndex] >> 8 & 0xFF,
                                oneDMod[masterIndex++] & 0xFF));
                    }
                }

                result.add(encodedBitmap);
            } else {
                result.add(bitmap.copy(bitmap.getConfig(), false));
            }
        }

        return result;

    }

    /**
     * This is the decoding method of 2 bit encoding.
     *
     * @return : Void
     * @parameter : byte_pixel_array {The byte array image}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     * @parameter : messageDecodingStatus {object}
     */
    private static void decodeMessage(byte[] bytePixelArrays, int imageColumns,
                                      int imageRows, MessageDecodingStatus messageDecodingStatus) {

        //encrypted message
        Vector<Byte> byteEncryptedMessage = new Vector<>();

        int shiftIndex = 4;

        byte tmp = 0x00;


        for (byte bytePixelArray : bytePixelArrays) {


            //get last two bits from byte_pixel_array
            tmp = (byte) (tmp | ((bytePixelArray << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));

            if (shiftIndex % toShift.length == 0) {
                //adding temp byte value
                byteEncryptedMessage.addElement(tmp);


                //converting byte value to string
                byte[] nonso = {byteEncryptedMessage.elementAt(byteEncryptedMessage.size() - 1)};
                String str = new String(nonso, Charset.forName("ISO-8859-1"));

                if (messageDecodingStatus.getMessage().endsWith(END_MESSAGE_COSTANT)) {


                    //fixing ISO-8859-1 decoding
                    byte[] temp = new byte[byteEncryptedMessage.size()];

                    for (int index = 0; index < temp.length; index++)
                        temp[index] = byteEncryptedMessage.get(index);


                    String string = new String(temp, Charset.forName("ISO-8859-1"));


                    messageDecodingStatus.setMessage(string.substring(0, string.length() - 1));
                    //end fixing

                    messageDecodingStatus.setEnded();

                    break;
                } else {
                    //just add the decoded message to the original message
                    messageDecodingStatus.setMessage(messageDecodingStatus.getMessage() + str);

                    //If there was no message there and only start and end message constant was there
                    if (messageDecodingStatus.getMessage().length() == START_MESSAGE_COSTANT.length()
                            && !START_MESSAGE_COSTANT.equals(messageDecodingStatus.getMessage())) {

                        messageDecodingStatus.setMessage("");
                        messageDecodingStatus.setEnded();

                        break;
                    }
                }

                tmp = 0x00;
            }

        }

        if (!Utils.isEmpty(messageDecodingStatus.getMessage()))
            //removing start and end constants form message

            try {
                messageDecodingStatus.setMessage(messageDecodingStatus.getMessage().substring(START_MESSAGE_COSTANT.length(), messageDecodingStatus.getMessage()
                        .length()
                        - END_MESSAGE_COSTANT.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    /**
     * This method takes the list of encoded chunk images and decodes it.
     *
     * @return : encrypted message {String}
     * @parameter : encodedImages {list of encode chunk images}
     */

    public static String decodeMessage(List<Bitmap> encodedImages) {

        //Creating object
        MessageDecodingStatus messageDecodingStatus = new MessageDecodingStatus();

        for (Bitmap bitmap : encodedImages) {
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];

            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            byte[] byteArray;

            byteArray = Utils.convertArray(pixels);

            decodeMessage(byteArray, bitmap.getWidth(), bitmap.getHeight(), messageDecodingStatus);

            if (messageDecodingStatus.isEnded())
                break;
        }

        return messageDecodingStatus.getMessage();
    }

    /**
     * Calculate the numbers of pixel needed
     *
     * @return : The number of pixel {integer}
     * @parameter : message {Message to encode}
     */
    public static int numberOfPixelForMessage(String message) {
        int result = -1;
        if (message != null) {
            message += END_MESSAGE_COSTANT;
            message = START_MESSAGE_COSTANT + message;
            result = message.getBytes(Charset.forName("ISO-8859-1")).length * 4 / 3;
        }

        return result;
    }
}
