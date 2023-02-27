/**
 * @(#)MD5Util.java, 2011-5-17. Copyright 2011 Effiker, Inc. All rights
 *                   reserved. EFFIKER PROPRIETARY/CONFIDENTIAL. Use is subject
 *                   to license terms.
 */
package com.auto.track.sdk.base;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util {

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
//            Log.e("Fail to get message digest!", e);
            return null;
        }
    }

    /**
     * 获取字符串的MD5码
     * 
     * @param plainText
     * @return MD5的16进制字符串表示
     */
    public static String getMD5String(String plainText) {
        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(plainText.getBytes());
        byte bytes[] = messageDigest.digest();
        String str = toHexString(bytes);
        return str;
    }

    /**
     * 获取文件的MD5码(效率比另一种方法高，但是不能处理大于1G的文件)
     * 
     * @param input
     * @return MD5的16进制字符串表示
     */
    public static String getMD5String(byte[] input) {
        // long time1 = System.currentTimeMillis();

        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(input);
        byte bytes[] = messageDigest.digest();
        String str = toHexString(bytes);
        return str;

        // long time2 = System.currentTimeMillis();
        // System.out.println("method1:" + (time2 - time1));
    }

    /**
     * 获取文件的MD5码(效率比另一种方法高，但是不能处理大于1G的文件)
     * 
     * @param file
     * @return MD5的16进制字符串表示
     * @throws IOException
     */
    public static String getMD5String(File file) throws IOException {
        // long time1 = System.currentTimeMillis();

        FileInputStream in = null;
        FileChannel ch = null;
        MappedByteBuffer byteBuffer = null;

        MessageDigest messageDigest = getMessageDigest();

        try {
            in = new FileInputStream(file);
            ch = in.getChannel();
            byteBuffer = ch
                    .map(FileChannel.MapMode.READ_ONLY, 0, file.length());

            messageDigest.update(byteBuffer);
            byte bytes[] = messageDigest.digest();
            String str = toHexString(bytes);
            return str;
        } finally {
            if (ch != null) {
                try {
                    ch.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // long time2 = System.currentTimeMillis();
        // System.out.println("method1:" + (time2 - time1));
    }

    /**
     * 获取文件的MD5码(效率比另一种方法低，但是可以处理大于1G的文件)
     * 
     * @param file
     * @return MD5的16进制字符串表示
     * @throws IOException
     */
    public static String getMD5String2(File file) throws IOException {
        // long time1 = System.currentTimeMillis();

        MessageDigest messageDigest = getMessageDigest();

        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
//            byte[] buffer = new byte[1024];
            byte[] buffer = new byte[1024*3];

            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
        } finally {
            fis.close();
        }

        byte bytes[] = messageDigest.digest();
        String str = toHexString(bytes);

        // long time2 = System.currentTimeMillis();
        // System.out.println("method2:" + (time2 - time1));

        return str;
    }

    /**
     * 获取字符串的MD5码
     * 
     * @param plainText
     * @return Long[0]为MD5低64位的Long表示，Long[1]为MD5高64位的Long表示
     */
    public static Long[] getMD5Longs(String plainText) {
        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(plainText.getBytes());
        byte bytes[] = messageDigest.digest();
        Long[] result = toLongs(bytes);
        return result;
    }

    /**
     * 获取文件的MD5码(效率比另一种方法高，但是不能处理大于1G的文件)
     * 
     * @param file
     * @return Long[0]为MD5低64位的Long表示，Long[1]为MD5高64位的Long表示
     * @throws IOException
     */
    public static Long[] getMD5Long(File file) throws IOException {
        // long time1 = System.currentTimeMillis();

        @SuppressWarnings("resource")
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());

        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(byteBuffer);
        byte bytes[] = messageDigest.digest();
        Long[] result = toLongs(bytes);

        // long time2 = System.currentTimeMillis();
        // System.out.println("method1:" + (time2 - time1));

        return result;
    }

    /**
     * 获取文件的MD5码(效率比另一种方法低，但是可以处理大于1G的文件)
     * 
     * @param file
     * @return MD5的16进制字符串表示
     * @throws IOException
     */
    public static Long[] getMD5Long2(File file) throws IOException {
        // long time1 = System.currentTimeMillis();

        MessageDigest messageDigest = getMessageDigest();

        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];

            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
        } finally {
            fis.close();
        }

        byte bytes[] = messageDigest.digest();
        Long[] result = toLongs(bytes);

        // long time2 = System.currentTimeMillis();
        // System.out.println("method2:" + (time2 - time1));

        return result;
    }

    /**
     * @param args
     */
    private static char[] hexChar = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
        'e', 'f'
    };

    private static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0 || "null".equals(str));
    }
    /**
     * @param bytes
     * @return
     */
    private static String toHexString(byte[] bytes) {
        // long time1 = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexChar[(bytes[i] & 0xf0) >>> 4]);
            sb.append(hexChar[bytes[i] & 0x0f]);
        }

        // long time2 = System.currentTimeMillis();
        // System.out.println("bite:" + (time2 - time1));

        return sb.toString();
    }

    /**
     * @param bytes
     * @return
     */
    @Deprecated
    @SuppressWarnings("unused")
    private static String toHexString1(byte[] bytes) {
        // long time1 = System.currentTimeMillis();

        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < bytes.length; offset++) {
            i = bytes[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }

        // long time2 = System.currentTimeMillis();
        // System.out.println("no byte:" + (time2 - time1));

        return buf.toString();
    }

    /**
     * @param md5Str
     * @return
     */
    public static Long toLong(String md5Str) {
        if (isBlank(md5Str) || md5Str.length() != 16) {
//            LogUtil.e("[md5 error.md5=" + md5Str + "]");
            return null;
        }

        long high = Long.parseLong(md5Str.substring(0, 8), 16);
        long low = Long.parseLong(md5Str.substring(8, 16), 16);
        long aa = (high << 32) + low;
        return aa;
    }

    /**
     * @param md5Str
     * @return
     */
    public static Long[] toLongs(String md5Str) {
        if (isBlank(md5Str) || md5Str.length() != 32) {
//            LogUtil.e("[md5 error.md5=" + md5Str + "]");
            return null;
        }

        Long[] result = new Long[2];
        result[1] = toLong(md5Str.substring(0, 16));
        result[0] = toLong(md5Str.substring(16, 32));
        return result;
    }

    public static Long[] toLongs(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
//            LogUtil.e("[md5 bytes error.bytes=" + bytes + "]");
            return null;
        }

        Long[] result = new Long[2];
        long tmp;

        long l = 0L;
        for (int i = 0; i < 8; i++) {
            tmp = bytes[i] & 0xff;
            l = l | tmp << (8 * (7 - i));
        }
        result[1] = l;

        l = 0L;
        for (int i = 8; i < 16; i++) {
            tmp = bytes[i] & 0xff;
            l = l | tmp << (8 * (7 + 8 - i));
        }
        result[0] = l;

        return result;
    }

    public static void main(String[] args) throws IOException {

        // System.out.println(getMD5String("e10adc3949ba59abbe56e057f20f883e"));
        System.out.println(MD5Util.getMD5String("123"));
        System.out.println(MD5Util.getMD5String(MD5Util.getMD5String("123")));

    }

    /**
     * @param md5
     * @return
     */
    public static String toHexString(Long md5) {
        String result = Long.toHexString(md5);
        while (result.length() < 16) {
            result = "0" + result;
        }
        return result;
    }

}
