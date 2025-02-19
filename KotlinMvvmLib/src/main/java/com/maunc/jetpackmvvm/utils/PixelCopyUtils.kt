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
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

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

    /**
     * 保存Bitmap到本地
     */
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

    /**
     * 保存网络图片到本地
     */
    fun saveUrlGallery(
        context: Context,
        url: String
    ): Boolean {
        //返回出一个URI
        val insert = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            /*  这里可以默认不写 默认保存在  */
            ContentValues()
        ) ?: return false //为空的话 直接失败返回了
        //这个打开了输出流  直接保存图片就好了
        context.contentResolver.openOutputStream(insert).use { os ->
            os ?: return false
            val x = download(url, os)
            return x
        }
        return false
    }


    private fun download(
        url: String,
        os: OutputStream
    ): Boolean {
        val url = URL(url)
        (url.openConnection() as HttpURLConnection).also { conn ->
            conn.requestMethod = "GET"
            conn.connectTimeout = 5 * 1000
            if (conn.responseCode == 200) {
                conn.inputStream.use { ins ->
                    val buf = ByteArray(2048)
                    var len: Int
                    while (ins.read(buf).also { len = it } != -1) {
                        os.write(buf, 0, len)
                    }
                    os.flush()
                }
                return true
            } else {
                return false
            }
        }
    }
}