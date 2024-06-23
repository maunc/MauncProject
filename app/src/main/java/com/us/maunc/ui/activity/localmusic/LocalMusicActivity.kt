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

    private val localMusicTracksDialog by lazy {
        LocalMusicTracksDialog(mutableListOf())
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
                    audioService?.addTrackList(it)
                    refreshUI()
                }
            }
        }
        mDatabind.localMusicPlay.setOnClickListener {
            if (audioService?.getAudioTracks()?.isEmpty()!!) {
                Log.e(TAG, "列表中没有音乐")
                return@setOnClickListener
            }
            mViewModel.isPlayFlag.value = !mViewModel.isPlayFlag.value
        }
        mDatabind.localMusicShowPlayList.setOnClickListener {
            if (audioService?.getAudioTracks()!!.isEmpty()) {
                Log.e(TAG, "列表中没有音乐")
                return@setOnClickListener
            }
            localMusicTracksDialog.show(supportFragmentManager, "LocalMusicTracksDialog")
            localMusicTracksDialog.setData(
                audioService?.getAudioTracks()!!,
                audioService?.getAudioPath() ?: ""
            )
        }
        startMusicService()
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
                .apply(LocalMusicComm.setAudioCorners(200f))
                .into(mDatabind.localMusicCover)
        }
        mViewModel.isPlayFlag.observe(this) { isPlayFlag ->
            LocalMusicAnim.refreshRotateAnim(isPlayFlag)
            audioService?.let {
                if (isPlayFlag) {
                    it.resumeMedia()
                } else {
                    it.pauseMedia()
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