package com.module.swpay;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by wanglu on 2019/1/16.
 */
public class PayityUnity {
    public static boolean isUseCLanguage = false;
    public static String app_name = "";
    public static String app_id = "";
    public static String app_key = "";
    public static String app_packagename = "";

    public static int screenW = 1280;
    public static int screenH = 720;
    public final static float origonScreenW = 1280.0f;
    public final static float origonScreenH = 720.0f;
    public static float scaleX = 1;
    public static float scaleY = 1;



    public static String getLocalMacAddress() {
        // TODO Auto-generated method stub
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress ip = (InetAddress) address.nextElement();
                    if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) ||
                            ip.isLoopbackAddress()) {
                        continue;
                    }
                    if (ip.isSiteLocalAddress()) {
                        mac = ni.getHardwareAddress();
                    } else if (!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                sb.append(parseByte(mac[i]));
            }
            return sb.substring(0, sb.length() - 1).replace("0", "");
        } else {
            return "000000FFFFFF";
        }
    }

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result.trim();
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }
            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String parseByte(byte b) {
        // TODO Auto-generated method stub
        String string = "00" + Integer.toHexString(b);
        return string.substring(string.length() - 3);
    }



    public static String getUUID(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static Properties loadProperties(Context context, String fileName){
        InputStream is = null;
        Properties properties = null;
        BufferedReader bufferedReader = null;
        try {
            properties = new Properties();
            is = context.getApplicationContext().getAssets().open(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            properties.load(bufferedReader);
        } catch (IOException e) {
//            e.printStackTrace();
            is = null;
            properties = null;
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            is = null;
            bufferedReader = null;

        }
        return properties;
    }


    private static InputStream is = null;
    private static  boolean isGetHttp = false;
    private static  OkHttpClient httpClient = null;
    /**
     * 执行URL，返回InputStream
     * @param url
     * @param xmlStr
     * @return
     */
    public synchronized static InputStream doURL(String url, String xmlStr){

            if (httpClient == null) {
                OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)   //设置连接超时时间
                        .writeTimeout(30, TimeUnit.SECONDS)      //设置写入超时时间
                        .readTimeout(30, TimeUnit.SECONDS)      //设置读取超时时间
                        .retryOnConnectionFailure(false);
                httpClient = httpBuilder.build();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), xmlStr);
            final Request.Builder builder = new Request.Builder().post(requestBody).url(url);
            builder.addHeader("Content-Type", "application/xml;charset=utf-8");
            System.out.println("\nrequstBody=" + xmlStr);
            isGetHttp = false;
            is = null;
            Request request = null;
            Call call = null;
            Response response = null;
            InputStream istream = null;
            try {
                request = builder.build();
                call = httpClient.newCall(request);
                response = call.execute();
                byte[] dates = null;
                if (response.isSuccessful() && response.code() == 200) {
                    istream = response.body().byteStream();
                }
                is = istream;
                isGetHttp = true;

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (request != null) {
                    request = null;
                }
//                    if(response!=null) {
//                        response.close();
//                    }
                response = null;
            }

            return is;

    }
    //

}
