package com.maunc.mvvmhabit.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;

import com.maunc.mvvmhabit.base.BaseApp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by goldze on 2017/5/14.
 */
public final class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取设备屏幕宽度
     */
    public static int getDeviceWidth() {
        return BaseApp.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备屏幕高度，可以选择带上状态高度
     */
    @SuppressLint("InternalInsetResource")
    public static int getDeviceHeight(boolean isAddStatusBar) {
        int heightPixels = BaseApp.getInstance().getResources().getDisplayMetrics().heightPixels;
        if (isAddStatusBar) {
            int statusBarHeight = 0;
            int resourceId = BaseApp.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = BaseApp.getInstance().getResources().getDimensionPixelSize(resourceId);
            }
            return heightPixels + statusBarHeight;
        }
        return heightPixels;
    }

    /**
     * 获取设备名称
     */
    public static String getDeviceName() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前手机Android系统版本号
     */
    public static String getDeviceAndroidVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机Android API等级（30、31 ...）
     */
    public static int getDeviceAndroidApiLevel() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取手机厂商名
     */
    public static String getDeviceManufacturer() {
        try {
            return Build.MANUFACTURER;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获得设备序列号(sn号)
     */
    @SuppressLint({"HardwareIds", "PrivateApi"})
    public static String getDeviceSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获得设备硬件唯一标识
     * fixme imei + android_id + serial + 硬件uuid
     */
    @NonNull
    public static String getDeviceId() {
        StringBuilder sbDeviceId = new StringBuilder();
        String imei = getDeviceImei();
        String androidId = getDeviceAndroidId();
        String serial = getDeviceSerialNumber();
        String uuid = getDeviceUUId().replace("-", "");
        //追加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //追加androidid
        if (androidId != null && androidId.length() > 0) {
            sbDeviceId.append(androidId);
            sbDeviceId.append("|");
        }
        //追加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //追加硬件uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }
        //生成SHA1，统一DeviceId长度
        if (sbDeviceId.length() > 0) {
            byte[] hash;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                messageDigest.reset();
                messageDigest.update(sbDeviceId.toString().getBytes("UTF-8"));
                hash = messageDigest.digest();
            } catch (Exception e) {
                hash = "".getBytes();
            }
            if (hash != null && hash.length > 0) {
                StringBuilder sb = new StringBuilder();
                String stmp;
                for (byte datum : hash) {
                    stmp = (Integer.toHexString(datum & 0xFF));
                    if (stmp.length() == 1)
                        sb.append("0");
                    sb.append(stmp);
                }
                String deviceId = sb.toString().toUpperCase(Locale.CHINA);
                if (deviceId != null && deviceId.length() > 0) {
                    //返回最终的DeviceId
                    return deviceId;
                }
            }
        }
        //如果以上硬件标识数据均无法获得，
        //则DeviceId默认使用系统随机数，这样保证DeviceId不为空
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取设备的IMEI码， 需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceImei() {
        try {
            TelephonyManager tm = (TelephonyManager)
                    BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            if (PermissionChecker.checkSelfPermission(BaseApp.getInstance(),
                    Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED) {
                return tm.getDeviceId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备的AndroidId
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceAndroidId() {
        try {
            return Settings.Secure.getString(BaseApp.getInstance().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 获得设备硬件uuid,使用硬件信息，计算出一个随机数
     */
    @NonNull
    @SuppressLint("HardwareIds")
    public static String getDeviceUUId() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径
     * <p>先用shell，shell失败再普通方法获取，一般是/storage/emulated/0/</p>
     */
    @Nullable
    public static String getSDCardPath() {
        if (!isSDCardEnable()) return null;
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();
        BufferedReader bufferedReader = null;
        try {
            Process p = run.exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while ((lineStr = bufferedReader.readLine()) != null) {
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray.length >= 5) {
                        return strArray[1].replace("/.android_secure", "") + File.separator;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 判断外置sd卡是否挂载
     */
    public static boolean isExistCard() {
        boolean result = false;
        StorageManager storageManager = (StorageManager) BaseApp.getInstance().getSystemService(Context.STORAGE_SERVICE);
        try {
            Method method1 = StorageManager.class.getMethod("getVolumeList");
            method1.setAccessible(true);
            Object[] arrays = (Object[]) method1.invoke(storageManager);
            if (arrays != null) {
                for (Object temp : arrays) {
                    Method mRemoveable = temp.getClass().getMethod("isRemovable");
                    Boolean isRemovable = (Boolean) mRemoveable.invoke(temp);
                    if (isRemovable) {
                        Method getPath = temp.getClass().getMethod("getPath");
                        String path = (String) mRemoveable.invoke(temp);
                        Method getState = storageManager.getClass().getMethod("getVolumeState", String.class);
                        String state = (String) getState.invoke(storageManager, path);
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取SD卡剩余空间
     */
    @Nullable
    public static String getSDCardFreeSpace() {
        if (!isSDCardEnable()) return null;
        StatFs stat = new StatFs(getSDCardPath());
        long blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();
        blockSize = stat.getBlockSizeLong();
        return ConvertUtils.byte2FitMemorySize(availableBlocks * blockSize);
    }

    /**
     * 获取SD卡信息
     */
    @Nullable
    public static String getSDCardInfo() {
        if (!isSDCardEnable()) return null;
        SDCardInfo sd = new SDCardInfo();
        sd.isExist = true;
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        sd.totalBlocks = sf.getBlockCountLong();
        sd.blockByteSize = sf.getBlockSizeLong();
        sd.availableBlocks = sf.getAvailableBlocksLong();
        sd.availableBytes = sf.getAvailableBytes();
        sd.freeBlocks = sf.getFreeBlocksLong();
        sd.freeBytes = sf.getFreeBytes();
        sd.totalBytes = sf.getTotalBytes();
        return sd.toString();
    }

    private static class SDCardInfo {
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;
        long blockByteSize;
        long totalBytes;
        long freeBytes;
        long availableBytes;

        @Override
        public String toString() {
            return "isExist=" + isExist +
                    "\ntotalBlocks=" + totalBlocks +
                    "\nfreeBlocks=" + freeBlocks +
                    "\navailableBlocks=" + availableBlocks +
                    "\nblockByteSize=" + blockByteSize +
                    "\ntotalBytes=" + totalBytes +
                    "\nfreeBytes=" + freeBytes +
                    "\navailableBytes=" + availableBytes;
        }
    }
}
