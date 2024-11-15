package com.maunc.jetpackmvvm.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi

/**
 * @author Simon
 * @date 2021/5/11
 * @time 6:14 下午
 * @desc
 */
object PixelCopyUtils {

    /**
     * 将View转换成Bitmap
     */
    fun createBitmapFromView(
        window: Window,
        view: View,
        callBack: (Bitmap?, Boolean) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888, true)
            convertLayoutToBitmap(
                window, view, bitmap
            ) { copyResult -> // 如果成功
                if (copyResult == PixelCopy.SUCCESS) {
                    callBack(bitmap, true)
                } else {
                    callBack(null, false)
                }
            }
        } else {
            var bitmap: Bitmap? = null
            // 开启view缓存bitmap
            view.isDrawingCacheEnabled = true
            // 设置view缓存Bitmap质量
            view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            // 获取缓存的bitmap
            val cache: Bitmap = view.getDrawingCache()
            if (!cache.isRecycled) {
                bitmap = Bitmap.createBitmap(cache)
            }
            // 销毁view缓存bitmap
            view.destroyDrawingCache()
            // 关闭view缓存bitmap
            view.setDrawingCacheEnabled(false)
            callBack(bitmap, bitmap != null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertLayoutToBitmap(
        window: Window,
        view: View,
        dest: Bitmap,
        listener: PixelCopy.OnPixelCopyFinishedListener
    ) {
        // 获取layout的位置
        val location = IntArray(2)
        view.getLocationInWindow(location)
        // 请求转换
        PixelCopy.request(
            window,
            Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
            dest, listener, Handler(Looper.getMainLooper())
        )
    }

    fun saveBitmapGallery(
        context: Context,
        bitmap: Bitmap
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val insert = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: return false
            context.contentResolver.openOutputStream(insert).use {
                it ?: return false
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            return true
        } else {
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "title", "desc")
            return true
        }
    }
}