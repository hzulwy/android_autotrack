package com.auto.track.sdk.base;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {

    /**
     * Gzip 压缩数据
     *
     * @param unGzipStr
     * @return
     */
    public static byte[] compressForGzip(String unGzipStr) {
        if (unGzipStr == null || unGzipStr.trim().length() == 0 || "null".equals(unGzipStr)) {
            return null;
        }
        ByteArrayOutputStream baos=null;
        GZIPOutputStream gzip=null;
        try {
            baos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(baos);
            gzip.write(unGzipStr.getBytes());
            gzip.flush();
            gzip.close();
            //String encode = baos.toString();
            baos.flush();
            baos.close();
            byte[] byteZip = baos.toByteArray();
            //Base64Encoder.encode(encode)
            return byteZip;
        }catch (IOException e) {
            LogUtil.e("compressForGzip:"+e.toString());
        }catch (Exception e){
            LogUtil.e("compressForGzip:"+e.toString());
        }
        finally {
            if (baos!=null){
                try {
                    baos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(gzip!=null){
                try {
                    gzip.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    gzip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * Gzip解压数据
     *
     * @param gzipStr
     * @return
     */
    public static String decompressForGzip(String gzipStr) {
        if (gzipStr == null || gzipStr.trim().length() == 0 || "null".equals(gzipStr)) {
            return null;
        }

        ByteArrayOutputStream out =null;
        ByteArrayInputStream in =null;
        GZIPInputStream gzip=null;
        try {
            byte[] t = Base64.decode(gzipStr);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(t);
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[128];
            int n = 0;
            while ((n = gzip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, n);
            }
            gzip.close();
            in.close();
            out.flush();
            String res=out.toString();
            out.close();
            return res;
        } catch (IOException e) {
            LogUtil.e("decompressForGzip:"+e.toString());
        } catch (Exception e) {
            LogUtil.e("decompressForGzip:"+e.toString());
        }
        finally {
            if(out!=null){
                try {
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(gzip!=null){
                try {
                    gzip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
