package com.goldze.mvvmhabit.utils;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.goldze.mvvmhabit.base.BaseApp;

public class AppUtils {

    public static int getDimens(@DimenRes int dimens) {
        try {
            return BaseApp.getInstance().getResources().getDimensionPixelOffset(dimens);
        } catch (Resources.NotFoundException e) {
            return 0;
        }
    }

    @Nullable
    public static Drawable getDrawable(@DrawableRes int drawable) {
        try {
            return ResourcesCompat.getDrawable(BaseApp.getInstance().getResources(), drawable, null);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    @NonNull
    public static String getString(@StringRes int string) {
        try {
            return BaseApp.getInstance().getString(string);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    @NonNull
    public static String getAppName() {
        PackageManager pm = BaseApp.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            int labelRes = applicationInfo.labelRes;
            return BaseApp.getInstance().getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Nullable
    public static String getVersionName() {
        try {
            PackageManager packageManager = BaseApp.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApp.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getAppVersionCode() {
        int versionCode = 0;
        try {
            PackageManager pm = BaseApp.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断当前 录音设备是否可用
     */
    public static boolean isRecordEnable() {
        boolean available = true;
        if (ActivityCompat.checkSelfPermission(BaseApp.getInstance(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_DEFAULT, 44100);
        try {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                available = false;
            }
            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
                available = false;
            }
            recorder.stop();
        } finally {
            recorder.release();
        }
        return available;
    }

    /**
     * 判断用户是否打开系统定位服务
     */
    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(BaseApp.getInstance().getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(BaseApp.getInstance().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    //权限常量  方法封装
}
