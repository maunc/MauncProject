package com.maunc.jetpackmvvm.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.annotation.RequiresApi

/**
 * 获取屏幕宽度
 */
fun Context.screenWidth() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 * 可以选择是否带状态栏
 */
@SuppressLint("InternalInsetResource")
fun Context.screenHeight(isAddStatusBar: Boolean): Int {
    val heightPixels = resources.displayMetrics.heightPixels
    if (isAddStatusBar) {
        var statusBarHeight = 0
        val resourceId =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return heightPixels + statusBarHeight
    }
    return heightPixels
}

/**
 * 设置状态栏文字是否为黑色
 */
fun Activity.setDeviceDarkStatusBar(bDark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        //修改状态栏颜色只需要这行代码
        window.statusBarColor =
            Color.parseColor("#FFFFFF")//这里对应的是状态栏的颜色，就是style中colorPrimaryDark的颜色
        var vis = decorView.systemUiVisibility
        vis = if (bDark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    }
}

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String, label: String = "JetpackMvvm") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
}

@RequiresApi(Build.VERSION_CODES.N)
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}

/**
 * 获取设备名称
 */
fun getDeviceName(): String {
    return try {
        Build.MODEL
    } catch (e: Exception) {
        ""
    }
}

/**
 * 获取当前手机Android系统版本号
 */
fun getDeviceAndroidVersion(): String {
    return try {
        Build.VERSION.RELEASE
    } catch (e: Exception) {
        ""
    }
}

/**
 * 获取手机Android API等级（30、31 ...）
 */
fun getDeviceAndroidApiLevel(): Int {
    return try {
        Build.VERSION.SDK_INT
    } catch (e: Exception) {
        0
    }
}

/**
 * 获取手机厂商名
 */
fun getDeviceManufacturer(): String {
    return try {
        Build.MANUFACTURER
    } catch (e: java.lang.Exception) {
        ""
    }
}

/**
 * 获得设备序列号(sn号)
 */
@SuppressLint("HardwareIds", "PrivateApi")
fun getDeviceSerialNumber(): String? {
    var serial: String? = ""
    try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) { //8.0+
            serial = Build.SERIAL
        } else { //8.0-
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        }
    } catch (e: java.lang.Exception) {
        return ""
    }
    return serial
}


