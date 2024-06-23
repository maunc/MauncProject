package com.us.maunc.ui.activity.localmusic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.R
import com.us.maunc.databinding.ActivityLocalMusicBinding
import com.us.maunc.ui.dialog.common.CommonDialog

class LocalMusicActivity : BaseVmActivity<LocalMusicVM, ActivityLocalMusicBinding>() {

    companion object {
        const val TAG = "LocalMusicActivity"
    }

    private val localMusicAdapter by lazy {
        LocalMusicAdapter(mutableListOf())
    }
    private val commonDialog by lazy {
        CommonDialog()
    }

    private var audioService: LocalMusicService? = null
    private var isBound = false

    override fun initView(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.navigationBarColor = Color.BLACK
        mDatabind.viewModel = mViewModel
        mDatabind.localMusicTitle.isSelected = true
        LocalMusicAnim.initRotateAnim(mDatabind.localMusicCover)
        mDatabind.localMusicRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDatabind.localMusicRecyclerView.adapter = localMusicAdapter
        mViewModel.getLocalDirectory(mViewModel.lastPath, LocalMusicType.AUDIO)
        localMusicAdapter.setOnLocalMusicItemClickListener {
            when (it.type) {
                LocalMusicType.FOLDER -> {
                    mViewModel.pathStack.push(mViewModel.lastPath)
                    mViewModel.getLocalDirectory(it.path, LocalMusicType.AUDIO)
                    mViewModel.lastPath = it.path
                }

                LocalMusicType.AUDIO -> {
                    setPlayList(localMusicAdapter.data)
                    playerAudio(it)
                }
            }
        }
        mDatabind.localMusicPlay.setOnClickListener {
            mViewModel.isPlayFlag.value = !mViewModel.isPlayFlag.value
        }
        startMusicService()
    }

    private fun setPlayList(data: List<LocalMusicFileData>?) {
        data?.also { list ->
            list.filter { it.type == LocalMusicType.AUDIO }.let {
                audioService?.setTrackList(it)
            }
        }
    }

    private fun playerAudio(data: LocalMusicFileData) {
        Log.i(TAG, "playerAudio: =======null=${audioService == null}")
        // 设置音频列表
        audioService?.play(data)
        refreshUI()
    }

    private fun startMusicService() {
        val intent = Intent(this, LocalMusicService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected: ===============>")
            val binder = service as LocalMusicService.AudioBinder
            audioService = binder.getService()
            isBound = true
            Log.v(TAG, "onServiceConnected: $audioService")

            audioService?.takeIf { it.isPlaying() }?.let {
                refreshUI()
            }
            audioService?.setOnAudioChange {
                audioService?.let {
                    if (it.isPlaying()) {
                        refreshUI()
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected: ")
            isBound = false
        }
    }

    private fun refreshUI() {
        mViewModel.isPlayFlag.value = audioService?.isPlaying() ?: false
        mViewModel.targetAudioName.value = audioService?.getAudioName() ?: ""
        mViewModel.targetAudioCoverKey.value = audioService?.getAudioCoverKey() ?: ""
    }

    override fun createObserver() {
        mViewModel.localTargetDirectory.observe(this) {
            localMusicAdapter.setData(it)
        }
        mViewModel.showDialogFlag.observe(this) {
            if (it) {
                commonDialog.show(supportFragmentManager, "tg")
            } else {
                commonDialog.dismissAllowingStateLoss()
            }
        }
        mViewModel.targetAudioCoverKey.observe(this) { coverKey ->
            Glide.with(this).load(LocalMusicBimapCache.getBitmap(coverKey) ?: R.drawable.icon_fm)
                .apply(LocalMusicComm.setAudioCorners(100f))
                .into(mDatabind.localMusicCover)
        }
        mViewModel.isPlayFlag.observe(this) { isPlayFlag ->
            LocalMusicAnim.refreshRotateAnim(isPlayFlag)
            audioService?.let {
                if (isPlayFlag) {
                    it.resume()
                } else {
                    it.pause()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (mViewModel.pathStack.isEmpty()) {
            super.onBackPressed()
        }
        if (mViewModel.pathStack.isNotEmpty()) {
            val path = mViewModel.pathStack.pop()
            mViewModel.getLocalDirectory(path, LocalMusicType.AUDIO)
            mViewModel.lastPath = path
        }
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        LocalMusicAnim.forceRotateAnim()
        super.onDestroy()
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

}