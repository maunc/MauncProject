package com.maunc.jetpackmvvm.ext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.maunc.jetpackmvvm.BaseApp
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 判断网络是否弱
 */
fun Context.checkNetworkInWeakState(): Boolean {
    // 获取当前活跃的网络
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork: Network? = connectivityManager?.activeNetwork
        // 获取当前网络的能力
        val networkCapabilities: NetworkCapabilities? =
            connectivityManager?.getNetworkCapabilities(activeNetwork)
        // 判断网络是否处于弱网环境
        networkCapabilities?.let {
            !it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ||
                    !it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: run { false }
    } else {
        false
    }
}

/**
 * 检查网络
 */
fun Context.checkNetworkAvailable(): Boolean {
    if (null == connectivityManager) return false
    val info = connectivityManager?.activeNetworkInfo
    return null != info && info.isAvailable
}

/**
 * check is3G
 */
fun Context.check3G(): Boolean {
    val activeNetInfo = connectivityManager?.activeNetworkInfo
    return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE)
}

/**
 * isWifi
 */
fun Context.checkWifi(): Boolean {
    val activeNetInfo = connectivityManager?.activeNetworkInfo
    return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI)
}

/**
 * is2G
 */
fun Context.check2G(): Boolean {
    val activeNetInfo = connectivityManager?.activeNetworkInfo
    return (activeNetInfo != null
            && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE
            || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS
            || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_CDMA))
}

/**
 * 检查是否启用无障碍服务
 */
fun Context.checkAccessibilityServiceEnabled(serviceName: String): Boolean {
    val settingValue = Settings.Secure.getString(
        applicationContext.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    var result = false
    val splitter = TextUtils.SimpleStringSplitter(':')
    while (splitter.hasNext()) {
        if (splitter.next().equals(serviceName, true)) {
            result = true
            break
        }
    }
    return result
}

/**
 * 判断当前 录音设备是否可用
 */
fun checkRecordEnable(): Boolean {
    var available = true
    if (ActivityCompat.checkSelfPermission(
            BaseApp.instance,
            Manifest.permission.RECORD_AUDIO
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        return false
    }
    val recorder = AudioRecord(
        MediaRecorder.AudioSource.MIC, 44100,
        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_DEFAULT, 44100
    )
    try {
        if (recorder.recordingState != AudioRecord.RECORDSTATE_STOPPED) {
            available = false
        }
        recorder.startRecording()
        if (recorder.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
            recorder.stop()
            available = false
        }
        recorder.stop()
    } finally {
        recorder.release()
    }
    return available
}

/**
 * 判断用户是否打开系统定位服务
 */
fun Context.checkLocationEnabled(): Boolean {
    var locationMode = 0
    var locationProviders: String
    try {
        locationMode = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.LOCATION_MODE
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        return false
    }
    return locationMode != Settings.Secure.LOCATION_MODE_OFF
}

fun getIPAddress(): String? {
    if (!BaseApp.instance.checkNetworkAvailable()) {
        return null
    }
    if (BaseApp.instance.checkWifi() || BaseApp.instance.check3G()) { //当前使用2G/3G/4G网络
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val into = en.nextElement()
                val enumIpAddress = into.inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val inetAddress = enumIpAddress.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
    return null
}