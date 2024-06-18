package com.maunc.jetpackmvvm.ext

import android.content.ClipData
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi

/**
 * 获取屏幕宽度
 */
val Context.screenWidth get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight get() = resources.displayMetrics.heightPixels

/**
 * dp值转换为px
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * dp值转换为px
 */
fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String, label: String = "JetpackMvvm") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
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

@RequiresApi(Build.VERSION_CODES.N)
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}

/**
 * 判断网络是否弱
 */
fun isNetworkInWeakState(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // 获取当前活跃的网络
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork: Network? = connectivityManager.activeNetwork
        // 获取当前网络的能力
        val networkCapabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(activeNetwork)
        // 判断网络是否处于弱网环境
        networkCapabilities?.let {
            !it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ||
                    !it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: run { false }
    } else {
        false
    }
}


