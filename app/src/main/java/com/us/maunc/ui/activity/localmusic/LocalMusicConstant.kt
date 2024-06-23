package com.us.maunc.ui.activity.localmusic

import android.os.Environment

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
object LocalMusicConstant {
    //SDCard根目录
    var LOCAL_MUSIC_PATH = "${Environment.getExternalStorageDirectory().path}/"
}

enum class LocalMusicType {
    FOLDER,
    AUDIO
}

object LocalMusicAdapterConstant {
    const val FOLDER = 0
    const val AUDIO = 1
}