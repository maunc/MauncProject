package com.maunc.jetpackmvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;

import com.maunc.jetpackmvvm.BaseApp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    /**
     * 检查网络
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApp.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) return false;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return null != info && info.isAvailable();
    }

    /**
     * check is3G
     */
    public static boolean is3G() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApp.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * isWifi
     */
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApp.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * is2G
     */
    public static boolean is2G() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApp.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA);
    }

    @Nullable
    public static String getIPAddress() {
        if (!isNetworkAvailable()) {
            return null;
        }
        if (isWifi() || is3G()) {//当前使用2G/3G/4G网络
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface into = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddress = into.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddress.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
