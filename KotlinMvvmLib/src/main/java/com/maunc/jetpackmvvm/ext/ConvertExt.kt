package com.maunc.jetpackmvvm.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.nio.ByteBuffer


/**
 * dp转px
 */
fun Context.dp2px(dpValue: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp转px
 */
fun Context.sp2px(spValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * px转sp
 */
fun Context.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 将Drawable转化为Bitmap
 */
fun Drawable.drawable2Bitmap(): Bitmap {
    val width: Int = intrinsicWidth
    val height: Int = intrinsicHeight
    val bitmap = Bitmap.createBitmap(
        width, height, if (getOpacity() != PixelFormat.OPAQUE) {
            Bitmap.Config.ARGB_8888
        } else {
            Bitmap.Config.RGB_565
        }
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, width, height)
    draw(canvas)
    return bitmap
}

/**
 * 将bitmap转化为drawable
 */
fun Bitmap.bitmap2Drawable(): Drawable {
    return BitmapDrawable(this)
}

/**
 * 将bitmap转化为byte数组
 */
fun Bitmap.bitmap2Bytes(): ByteArray? {
    val byteCount = byteCount
    val buffer = ByteBuffer.allocate(byteCount)
    copyPixelsToBuffer(buffer)
    return buffer.array()
}

/**
 * 将byte数组转化为bitmap
 */
fun ByteArray.bytes2Bitmap(): Bitmap? {
    if (this.isEmpty()) {
        return null
    }
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}