package com.maunc.jetpackmvvm.utils;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ClsFunction：
 * CreateDate：2024/4/23
 * Author：TimeWillRememberUs
 */
public final class EncryptUtils {

    @NonNull
    public static String MD5(@NonNull String string) {
        try {
            byte[] hash;
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    @NonNull
    public static String SHA1(@NonNull String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = digest.digest(str.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : messageDigest) {
                String shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2)
                    hex.append(0);
                hex.append(shaHex);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    @NonNull
    public static String SHA256(@NonNull String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(str.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    @NonNull
    public static String SHA512(@NonNull String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] bytes = messageDigest.digest(str.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte aByte : bytes) {
                String shaHex = Integer.toHexString(0xff & aByte);
                if (shaHex.length() == 1)
                    hex.append('0');
                hex.append(shaHex);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
