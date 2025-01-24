package com.maunc.jetpackmvvm.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.maunc.jetpackmvvm.BaseApp

/**
 *ClsFunction：
 *CreateDate：2024/5/26
 *Author：TimeWillRememberUs
 */
object AppUtils {

    @JvmStatic
    fun getDimens(@DimenRes dimens: Int): Int {
        return try {
            BaseApp.instance.resources.getDimensionPixelOffset(dimens)
        } catch (e: Resources.NotFoundException) {
            0
        }
    }

    @JvmStatic
    fun getDrawable(@DrawableRes drawable: Int): Drawable? {
        return try {
            ResourcesCompat.getDrawable(BaseApp.instance.resources, drawable, null)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    @JvmStatic
    fun getString(@StringRes string: Int): String {
        return try {
            BaseApp.instance.getString(string)
        } catch (e: Resources.NotFoundException) {
            ""
        }
    }

    @JvmStatic
    fun getAppName(): String {
        val pm: PackageManager = BaseApp.instance.packageManager
        try {
            val packageInfo: PackageInfo =
                pm.getPackageInfo(BaseApp.instance.packageName, 0)
            val applicationInfo = packageInfo.applicationInfo
            val labelRes = applicationInfo.labelRes
            return BaseApp.instance.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    fun getVersionName(): String? {
        try {
            val packageManager: PackageManager = BaseApp.instance.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                BaseApp.instance.packageName, 0
            )
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    fun getAppVersionCode(): Int {
        var versionCode = 0
        try {
            val pm: PackageManager = BaseApp.instance.packageManager
            val pi: PackageInfo = pm.getPackageInfo(BaseApp.instance.packageName, 0)
            versionCode = pi.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getSignatureHashCode(): List<String> {
        val signatureList = mutableListOf<String>()
        try {
            val context = BaseApp.instance
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_SIGNATURES
            )
            for (signature in packageInfo.signatures) {
                signatureList.add(signature.hashCode().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return signatureList
    }
}