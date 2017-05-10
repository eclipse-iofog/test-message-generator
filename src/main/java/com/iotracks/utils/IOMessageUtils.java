package com.iotracks.utils;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils class for convenient encoding and decoding for IOMessage
 * <p>
 * Created by forte on 3/23/16.
 *
 * @author ilaryionava
 */
public class IOMessageUtils {

    private static final Logger log = Logger.getLogger(IOMessageUtils.class.getName());

    /**
     * Method to encode byte array to base64 format.
     *
     * @param data - array of bytes to be encoded
     * @return byte[]
     */
    public static byte[] encodeBase64(byte[] data) {
        if (data == null) {
            return new byte[0];
        }
        return Base64.getEncoder().encode(data);
    }

    /**
     * Method to decode byte array from base64 format.
     *
     * @param data - array of bytes to be decoded
     * @return byte[]
     */
    public static byte[] decodeBase64(byte[] data) {
        if (data == null) {
            return new byte[0];
        }
        try {
            return Base64.getDecoder().decode(data);
        } catch (IllegalArgumentException e) {
            log.log(Level.WARNING, "Error while decoding base64 bytes.", e);
            return new byte[0];
        }
    }

    public static String generateID() {
        return "IOMSID_" + (long) Math.floor(Math.random() * 1000 * 1000);
    }
}
