package com.us.maunc.ui.activity.localmusic

import androidx.lifecycle.MutableLiveData
import com.maunc.jetpackmvvm.base.BaseViewModel
import com.maunc.jetpackmvvm.ext.launch
import com.maunc.jetpackmvvm.livedata.BooleanLiveData
import com.us.maunc.ui.activity.localmusic.LocalMusicComm.getAlbumCover
import com.us.maunc.ui.activity.localmusic.LocalMusicComm.getMediaDuration
import com.us.maunc.ui.activity.localmusic.LocalMusicComm.isAudioFile
import java.io.File
import java.util.LinkedList

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicVM : BaseViewModel() {

    var lastPath = LocalMusicConstant.LOCAL_MUSIC_PATH

    private var localDirectory = mutableMapOf<String, MutableList<LocalMusicFileData>>()

    var localTargetDirectory = MutableLiveData<MutableList<LocalMusicFileData>>()

    var pathStack = LinkedList<String>()

    var showDialogFlag = BooleanLiveData()

    fun getLocalDirectory(path: String, type: LocalMusicType) {
        val localMusicFileData = localDirectory[path]
        if (localMusicFileData == null) {
            showDialogFlag.value = true
            launch({
                loadLocalFile(File(path), type)
            }, {
                localDirectory[path] = it
                localTargetDirectory.value = localDirectory[path]
                showDialogFlag.value = false
            }, {
                showDialogFlag.value = false
            })
        } else {
            localTargetDirectory.value = localDirectory[path]
            showDialogFlag.value = false
        }
    }

    private fun loadLocalFile(
        directory: File,
        type: LocalMusicType
    ): MutableList<LocalMusicFileData> {
        val fileDataList = mutableListOf<LocalMusicFileData>()
        val mediaFiles = mutableListOf<LocalMusicFileData>()
        directory.listFiles()?.forEach { file ->
            when {
                file.isDirectory -> {
                    val subDirMediaFiles = loadLocalFile(file, type)
                    if (subDirMediaFiles.isNotEmpty()) {
                        fileDataList.add(
                            LocalMusicFileData(
                                itemType = LocalMusicAdapterConstant.FOLDER,
                                type = LocalMusicType.FOLDER,
                                file.name,
                                file.absolutePath
                            )
                        )
                    }
                }

                isAudioFile(file) && type == LocalMusicType.AUDIO -> {
                    val data =
                        LocalMusicFileData(
                            itemType = LocalMusicAdapterConstant.AUDIO,
                            LocalMusicType.AUDIO,
                            file.name,
                            file.absolutePath
                        )
                    data.duration = getMediaDuration(file.absolutePath) / 1000
                    //为了方便数据传递使用图片缓存来统一管理bitmap
                    val coverKey = file.absolutePath.toMD5() ?: ""
                    var bitmap = LocalMusicBimapCache.getBitmap(coverKey)
                    if (bitmap == null) {
                        bitmap = getAlbumCover(file.absolutePath)
                    }
                    LocalMusicBimapCache.putBitmap(coverKey, bitmap)
                    data.coverKey = coverKey
                    mediaFiles.add(data)
                }
            }
        }
        fileDataList.addAll(mediaFiles)
        return fileDataList
    }
}