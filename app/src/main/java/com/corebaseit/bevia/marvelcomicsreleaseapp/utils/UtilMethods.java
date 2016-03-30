package com.corebaseit.bevia.marvelcomicsreleaseapp.utils;

import android.support.annotation.NonNull;

import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;

public class UtilMethods {

    public static Long generateTimeStamp (){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis() / 1000L;
    }

    public static String generateHash (long timeStamp) {
        return generateMD5Hash(String.valueOf(timeStamp) + Constants.API_PRIVATE_KEY + Constants.API_PUBLIC_KEY);
    }

    private static String generateMD5Hash(@NonNull final String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigestArray[] = digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte messageDigest : messageDigestArray) {
                String h = Integer.toHexString(0xFF & messageDigest);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                sb.append(h);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
