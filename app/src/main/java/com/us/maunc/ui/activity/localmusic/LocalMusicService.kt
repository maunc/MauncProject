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
    private var trackList = listOf<LocalMusicFileData>()
    private var currentAudioName: String = ""
    private var currentCoverKey: String = ""

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
        super.onDestroy()
        mediaPlayer.release()
    }

    fun setTrackList(tracks: List<LocalMusicFileData>, position: Int = 0) {
        trackList = tracks
        currentTrackIndex = position
        if (trackList.isNotEmpty()) {
            prepareTrack(currentTrackIndex)
        }
    }

    private fun prepareTrack(index: Int) {
        mediaPlayer.reset()
        val track = trackList[index]
        currentAudioName = track.name
        currentCoverKey = track.coverKey
        mediaPlayer.setDataSource(track.path)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            playNext()
        }
    }

    private fun prepareTrack(path: String) {
        adjustIndex(path)
        mediaPlayer.reset()
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            playNext()
        }
    }

    private fun adjustIndex(path: String) {
        val index = findIndex(path)
        if (index > 0) {
            currentTrackIndex = index
        }
    }

    private fun findIndex(path: String): Int {
        for ((index, item) in trackList.withIndex()) {
            if (item.path == path) return index
        }
        return -1
    }

    fun play() {
        prepareTrack(currentTrackIndex)
        mediaPlayer.start()
        showNotification("Playing: ${getCurrentName()}")
    }

    fun play(data: LocalMusicFileData) {
        currentAudioName = data.name
        currentCoverKey = data.coverKey
        prepareTrack(data.path)
        mediaPlayer.start()
        showNotification("Playing: ${getCurrentName()}")
    }

    fun pause() {
        mediaPlayer.pause()
        showNotification("Paused: ${getCurrentName()}")
    }

    fun resume() {
        mediaPlayer.start()
        showNotification("Resume: ${getCurrentName()}")
    }


    fun playNext() {
        if (trackList.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % trackList.size
            prepareTrack(currentTrackIndex)
            mediaPlayer.start()
            showNotification("Playing: ${getCurrentName()}")
            audioChange()
        }
    }

    fun playPrevious() {
        if (trackList.isNotEmpty()) {
            currentTrackIndex =
                if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
            prepareTrack(currentTrackIndex)
            mediaPlayer.start()
            showNotification("Playing: ${getCurrentName()}")
            audioChange()
        }
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun getDuration() = mediaPlayer.duration

    fun getAudioName() = currentAudioName

    fun getAudioCoverKey() = currentCoverKey

    fun getCurrentPosition() = mediaPlayer.currentPosition

    fun seekTo(position: Int) = mediaPlayer.seekTo(position)

    private fun audioChange() {
        audioChangeAction?.let { it() }
    }

    fun setOnAudioChange(action: (() -> Unit)?) {
        this.audioChangeAction = action
    }

    private fun getCurrentName(): String {
        if (trackList.isNotEmpty() && currentTrackIndex in trackList.indices) {
            val name = trackList[currentTrackIndex].name.split("\\.".toRegex())
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