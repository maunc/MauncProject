package com.maunc.mvvmhabit.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.maunc.mvvmhabit.BaseApp;

import java.nio.ByteBuffer;

/**
 * Created by goldze on 2017/5/14.
 * 转换相关工具类
 */
public final class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //Byte与Byte的倍数
    private static final int BYTE = 1;
    //KB与Byte的倍数
    private static final int KB = 1024;
    //MB与Byte的倍数
    private static final int MB = 1048576;
    //GB与Byte的倍数
    private static final int GB = 1073741824;

    /**
     * 字节数转合适内存大小
     */
    @NonNull
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < KB) {
            return String.format("%.2fB", (double) byteNum + 0.0005);
        } else if (byteNum < MB) {
            return String.format("%.2fKB", (double) byteNum / KB + 0.0005);
        } else if (byteNum < GB) {
            return String.format("%.2fMB", (double) byteNum / MB + 0.0005);
        } else {
            return String.format("%.2fGB", (double) byteNum / GB + 0.0005);
        }
    }

    /**
     * dp转px
     */
    public static int dp2px(final float dpValue) {
        final float scale = BaseApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = BaseApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = BaseApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = BaseApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将Drawable转化为Bitmap
     */
    public static Bitmap drawable2Bitmap(@NonNull Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将bitmap转化为drawable
     */
    @NonNull
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * 将bitmap转化为byte数组
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int byteCount = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(byteCount);
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }

    /**
     * 将byte数组转化为bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
