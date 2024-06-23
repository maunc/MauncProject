package com.us.maunc.ui.activity.localmusic

import android.graphics.Bitmap
import android.util.LruCache

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
object LocalMusicBimapCache {
    private val cache: LruCache<String, Bitmap>

    init {
        // 获取最大可用内存，并将一部分（例如1/8）分配给缓存
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // 计算每个Bitmap的大小，以KB为单位
                return bitmap.byteCount / 1024
            }
        }
    }

    fun putBitmap(key: String?, bitmap: Bitmap?) {
        if (key == null || bitmap == null) return
        if (cache.get(key) == null) {
            cache.put(key, bitmap)
        }
    }

    fun getBitmap(key: String): Bitmap? {
        return cache.get(key)
    }
}