package com.shyky.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/6/3
 * @since 1.0
 */
public final class HttpUtil {
    private static final HttpUtil INSTANCE = new HttpUtil();

    public interface HttpCallback {
        void onSuccess(int stateCode, String response);

        void onFailure(int stateCode, String message);
    }

    /**
     * 构造方法私有化
     */
    private HttpUtil() {

    }

    public static HttpUtil getInstance() {
        return INSTANCE;
    }

    public static void sendGet(String url, HttpCallback httpCallback) {
        int stateCode = -1;
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setConnectTimeout(3 * 1000);
            con.setReadTimeout(3 * 1000);
            stateCode = con.getResponseCode();
            System.out.println("stateCode = " + stateCode);
            if (!ObjectUtil.isNull(httpCallback)) {
                if (stateCode == HttpURLConnection.HTTP_OK)
                    httpCallback.onSuccess(stateCode, IOUtil.inputStream2String(con.getInputStream()));
                else
                    httpCallback.onFailure(stateCode, IOUtil.inputStream2String(con.getErrorStream()));
            }
        } catch (MalformedURLException e) {
            if (!ObjectUtil.isNull(httpCallback))
                httpCallback.onFailure(stateCode, e.getMessage());
        } catch (IOException e) {
            if (!ObjectUtil.isNull(httpCallback))
                httpCallback.onFailure(stateCode, e.getMessage());
        }
    }

    public static void sendGet(String url, String paramName, String paramValue, HttpCallback httpCallback) {
        sendGet(url + "?" + paramName + "=" + paramValue, httpCallback);
    }

    public static void sendGet(String url, String paramName, boolean paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, char paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, short paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, int paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, long paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, float paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static void sendGet(String url, String paramName, double paramValue, HttpCallback httpCallback) {
        sendGet(url, paramName, TextUtil.toString(paramValue), httpCallback);
    }

    public static HttpUtil addHeader() {
        return getInstance();
    }

    public static void sendPost(String url, String[][] params, HttpCallback httpCallback) {
        int stateCode = -1;
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            // con.setRequestProperty("Referer",
            // "http://192.168.7.85/att/Account/logon.aspx");
            // con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            // con.setRequestProperty("User-Agent",
            // "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML,
            // like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Connection", "Keep-Alive");
            // con.setRequestProperty("Referer", "https://mp.weixin.qq.com/");
            con.setConnectTimeout(3 * 1000);
            con.setReadTimeout(3 * 1000);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < params.length; j++) {
                System.out.println(params[j][0] + "=" + params[j][1]);
                sb.append(params[j][0]).append("=").append(params[j][1]).append("&");
            }
            byte[] bypes = sb.deleteCharAt(sb.length() - 1).toString().getBytes();
            con.getOutputStream().write(bypes);// 输入参数
            stateCode = con.getResponseCode();
            if (!ObjectUtil.isNull(httpCallback)) {
                if (stateCode == HttpURLConnection.HTTP_OK)
                    httpCallback.onSuccess(stateCode, IOUtil.inputStream2String(con.getInputStream()));
                else
                    httpCallback.onFailure(stateCode, IOUtil.inputStream2String(con.getErrorStream()));
            }
        } catch (MalformedURLException e) {
            if (!ObjectUtil.isNull(httpCallback))
                httpCallback.onFailure(stateCode, e.getMessage());
        } catch (IOException e) {
            if (!ObjectUtil.isNull(httpCallback))
                httpCallback.onFailure(stateCode, e.getMessage());
        }
    }

    public static boolean download(String url, String saveDir, String fileName) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            InputStream inStream = con.getInputStream();
            FileOutputStream fos = new FileOutputStream(saveDir + File.separator + fileName);
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fos.write(buffer, 0, byteread);
            }
            fos.flush();
            fos.close();
            inStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}