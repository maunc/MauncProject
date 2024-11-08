package com.maunc.jetpackmvvm.utils

import android.Manifest
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.provider.Settings
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.maunc.jetpackmvvm.BaseApp

/**
 *ClsFunction：
 *CreateDate：2024/5/26
 *Author：TimeWillRememberUs
 */
object AppUtils {

    fun getDimens(@DimenRes dimens: Int): Int {
        return try {
            BaseApp.instance.resources.getDimensionPixelOffset(dimens)
        } catch (e: Resources.NotFoundException) {
            0
        }
    }

    fun getDrawable(@DrawableRes drawable: Int): Drawable? {
        return try {
            ResourcesCompat.getDrawable(BaseApp.instance.resources, drawable, null)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getString(@StringRes string: Int): String {
        return try {
            BaseApp.instance.getString(string)
        } catch (e: Resources.NotFoundException) {
            ""
        }
    }

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

    /**
     * 判断当前 录音设备是否可用
     */
    fun isRecordEnable(): Boolean {
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
    fun isLocationEnabled(): Boolean {
        var locationMode = 0
        var locationProviders: String
        try {
            locationMode = Settings.Secure.getInt(
                BaseApp.instance.contentResolver,
                Settings.Secure.LOCATION_MODE
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }
}