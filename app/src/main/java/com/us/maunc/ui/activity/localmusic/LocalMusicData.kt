package com.us.maunc.ui.activity.localmusic

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
data class LocalMusicFileData(
    override val itemType: Int,
    var type: LocalMusicType = LocalMusicType.FOLDER,
    var name: String = "",
    var path: String = "",
    var duration: Long = 0,
    var coverKey: String = "",
    var playing: Boolean = false
) : Serializable, MultiItemEntity