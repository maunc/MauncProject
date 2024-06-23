package com.us.maunc.ui.activity.localmusic

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.databinding.ActivityLocalMusicBinding
import com.us.maunc.ui.dialog.common.CommonDialog

class LocalMusicActivity : BaseVmActivity<LocalMusicVM, ActivityLocalMusicBinding>() {

    private val localMusicAdapter by lazy {
        LocalMusicAdapter(mutableListOf())
    }
    private val commonDialog by lazy {
        CommonDialog()
    }

    override fun initView(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.navigationBarColor = Color.BLACK
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
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(it.path)
                    try {
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    } catch (e: Exception) {
                        Log.e("LocalMusicActivity", e.message.toString())
                    }
                }
            }
        }
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
    }

    override fun onBackPressed() {
        if(mViewModel.pathStack.isEmpty()) {
            super.onBackPressed()
        }
        if (mViewModel.pathStack.isNotEmpty()) {
            val path = mViewModel.pathStack.pop()
            mViewModel.getLocalDirectory(path, LocalMusicType.AUDIO)
            mViewModel.lastPath = path
        }
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

}