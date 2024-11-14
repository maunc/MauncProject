package com.us.maunc.ui.activity.localmusic

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.bumptech.glide.request.RequestOptions
import com.maunc.jetpackmvvm.ext.dp2px
import com.us.maunc.App
import com.us.maunc.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */

object LocalMusicComm {
    fun isAudioFile(file: File): Boolean {
        val audios = listOf("mp3", "wav", "aac", "flac", "ogg", "m4a")
        return audios.contains(file.extension.lowercase())
    }

    fun getMediaDuration(filePath: String): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLong() ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        } finally {
            retriever.release()
        }
    }

    fun getAlbumCover(filePath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val art = retriever.embeddedPicture
            if (art != null) {
                BitmapFactory.decodeByteArray(art, 0, art.size)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    fun setAudioCorners(round: Float = 0f): RequestOptions {
        return RequestOptions.bitmapTransform(
            RoundedCornersTransformation(
                App.getInstance().dp2px(round),
                0, RoundedCornersTransformation.CornerType.ALL
            )
        )
            .placeholder(R.drawable.icon_fm)
    }
}

fun String?.toMD5(): String? {
    return try {
        if (this == null) return null
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(this.toByteArray())
        val no = BigInteger(1, messageDigest)
        no.toString(16).padStart(32, '0')
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@SuppressLint("DefaultLocale")
fun Long.convertDuration(): String {
    val minute = 1 * 60
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        this < day -> {
            String.format("%02d:%02d", this / minute, this % 60)
        }

        else -> {
            String.format("%02d:%02d:%02d", this / hour, (this % hour) / minute, this % 60)
        }
    }
}