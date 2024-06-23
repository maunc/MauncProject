package com.us.maunc.ui.activity.localmusic

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.maunc.jetpackmvvm.ext.notificationManager
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicService : Service() {

    private val binder = AudioBinder()
    private val mediaPlayer by lazy { MediaPlayer() }
    private var audioChangeAction: (() -> Unit)? = null
    private var currentTrackIndex = 0
    private var currentAudioName: String = ""
    private var currentCoverKey: String = ""
    private var tracksList = mutableListOf<LocalMusicFileData>()

    override fun onBind(intent: Intent?): IBinder {
        Log.i("LocalMusicService", "onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("LocalMusicService", "onCreate")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("LocalMusicService", "onStartCommand")
        showNotification("启动播放服务")
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun addTrackList(data: LocalMusicFileData) {
        var isExist = false
        var playIndex = 0
        for ((index, localMusicFileData) in tracksList.withIndex()) {
            if (localMusicFileData.name == data.name) {
                isExist = true
                playIndex = index
                break
            }
        }
        if (!isExist) {
            tracksList.add(data)
            currentTrackIndex = tracksList.size - 1
            playMedia()
        } else {
            currentTrackIndex = playIndex
            playMedia()
        }
    }

    private fun playMedia() {
        prepareTrack(currentTrackIndex)
        mediaPlayer.start()
        showNotification("Playing: ${getCurrentName()}")
    }

    fun playMedia(data: LocalMusicFileData) {
        currentAudioName = data.name
        currentCoverKey = data.coverKey
        prepareTrack(data.path)
        mediaPlayer.start()
        showNotification("Playing: ${getCurrentName()}")
    }

    fun pauseMedia() {
        mediaPlayer.pause()
        showNotification("Paused: ${getCurrentName()}")
    }

    fun resumeMedia() {
        mediaPlayer.start()
        showNotification("Resume: ${getCurrentName()}")
    }

    fun playNext() {
        if (tracksList.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % tracksList.size
            playMedia()
            showNotification("Playing: ${getCurrentName()}")
            audioChange()
        }
    }

    fun playPrevious() {
        if (tracksList.isNotEmpty()) {
            currentTrackIndex =
                if (currentTrackIndex - 1 < 0) tracksList.size - 1 else currentTrackIndex - 1
            playMedia()
            showNotification("Playing: ${getCurrentName()}")
            audioChange()
        }
    }

    private fun prepareTrack(index: Int) {
        mediaPlayer.reset()
        val track = tracksList[index]
        currentAudioName = track.name
        currentCoverKey = track.coverKey
        mediaPlayer.setDataSource(track.path)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            playNext()
        }
    }

    private fun prepareTrack(path: String) {
        var playFlag = -1
        for ((index, item) in tracksList.withIndex()) {
            if (item.path == path) {
                playFlag = index
            }
        }
        if (playFlag > 0) {
            currentTrackIndex = playFlag
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            playNext()
        }
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun getDuration() = mediaPlayer.duration

    fun getAudioName() = currentAudioName

    fun getAudioCoverKey() = currentCoverKey

    fun getAudioTracks() = tracksList

    fun getCurrentPosition() = mediaPlayer.currentPosition

    fun seekTo(position: Int) = mediaPlayer.seekTo(position)

    private fun audioChange() {
        audioChangeAction?.let { it() }
    }

    fun setOnAudioChange(action: (() -> Unit)?) {
        this.audioChangeAction = action
    }

    private fun getCurrentName(): String {
        if (tracksList.isNotEmpty() && currentTrackIndex in tracksList.indices) {
            val name = tracksList[currentTrackIndex].name.split("\\.".toRegex())
            return name[0]
        }
        return "未知艺术家"
    }

    private fun showNotification(message: String) {
        val intent = Intent(this, LocalMusicActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, "audio_channel")
//            .setContentTitle("Audio Playback")
            .setContentText(message)
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "audio_channel", "Audio Playback", NotificationManager.IMPORTANCE_LOW
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    inner class AudioBinder : Binder() {
        fun getService(): LocalMusicService = this@LocalMusicService
    }
}