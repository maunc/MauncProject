package com.us.maunc.ui.activity.localmusic

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.maunc.jetpackmvvm.base.BaseActivity
import com.us.maunc.R
import com.us.maunc.databinding.ActivityLocalMusicBinding
import com.us.maunc.ui.dialog.common.CommonDialog

class LocalMusicActivity : BaseActivity<LocalMusicVM, ActivityLocalMusicBinding>() {

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

    private val reHandler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                mViewModel.consoleFragments[msg.arg1].localMusicFileData.let {
                    audioService?.playMedia(it)
                }
                refreshUI()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.navigationBarColor = Color.BLACK
        mDatabind.viewModel = mViewModel
        LocalMusicAnim.initRotateAnim(mDatabind.localMusicCover)
        mDatabind.localMusicRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDatabind.localMusicRecyclerView.adapter = localMusicAdapter
        //获取文件列表
        mViewModel.getLocalDirectory(mViewModel.lastPath, LocalMusicType.AUDIO)
        //文件列表点击
        localMusicAdapter.setOnLocalMusicItemClickListener {
            when (it.type) {
                LocalMusicType.FOLDER -> {
                    mViewModel.pathStack.push(mViewModel.lastPath)
                    mViewModel.getLocalDirectory(it.path, LocalMusicType.AUDIO)
                    mViewModel.lastPath = it.path
                }

                LocalMusicType.AUDIO -> {
                    audioService?.addTrackList(it)
                }
            }
        }
        //播放按钮
        mDatabind.localMusicPlay.setOnClickListener {
            if (audioService?.getAudioTracks()?.isEmpty()!!) {
                Log.e(TAG, "列表中没有音乐")
                return@setOnClickListener
            }
            mViewModel.isPlayFlag.value = !mViewModel.isPlayFlag.value
        }
        //列表按钮
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
        //控制台ViewPage
        val localMusicConsoleAdapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return mViewModel.consoleFragments.size
                }

                override fun createFragment(position: Int): Fragment {
                    return mViewModel.consoleFragments[position]
                }
            }
        mDatabind.localMusicConsoleVp.adapter = localMusicConsoleAdapter
        mDatabind.localMusicConsoleVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //防止UI线程太卡而发送Message
                val obtain = Message.obtain()
                obtain.what = 0
                obtain.arg1 = position
                reHandler.sendMessageDelayed(obtain, 650)
            }
        })
        registerAddTrackReceiver()
        startMusicService()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerAddTrackReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction("addTrackLocalMusic")
            addAction("selectTrackLocalMusic")
            addAction("nextTrackLocalMusic")
            addAction("previousLocalMusic")
        }
        registerReceiver(addTrackReceiver, intentFilter)
    }

    private val addTrackReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            val localMusicFileData = intent?.getSerializableExtra("data") as LocalMusicFileData?
            if (action == "addTrackLocalMusic") {
                refreshConsoleFragment(localMusicFileData, true)
            }
            if (action == "selectTrackLocalMusic") {
                refreshConsoleFragment(localMusicFileData, false)
            }
            if (action == "nextTrackLocalMusic") {
                refreshConsoleFragment(localMusicFileData, false)
            }
            if (action == "previousLocalMusic") {
                refreshConsoleFragment(localMusicFileData, false)
            }
            Log.e(TAG, "addTrackReceiver: $action")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initConsoleFragment() {
        if (audioService?.getAudioTracks()?.isNotEmpty()!!) {
            goneDefaultTv()
            for (localMusicFileData in audioService?.getAudioTracks()!!) {
                mViewModel.consoleFragments.add(
                    LocalMusicConsoleFragment().newInstance(
                        localMusicFileData
                    )
                )
            }
            mDatabind.localMusicConsoleVp.adapter?.notifyDataSetChanged()
            mDatabind.localMusicConsoleVp.currentItem = selectConsoleVpPosition()
        }
    }

    private fun refreshConsoleFragment(data: LocalMusicFileData?, isAdd: Boolean) {
        goneDefaultTv()
        if (data == null) {
            return
        }
        if (isAdd) {
            mViewModel.consoleFragments.add(LocalMusicConsoleFragment().newInstance(data))
            val selectConsoleVpPosition = selectConsoleVpPosition()
            mDatabind.localMusicConsoleVp.adapter?.notifyItemInserted(selectConsoleVpPosition)
            mDatabind.localMusicConsoleVp.currentItem = selectConsoleVpPosition
        } else {
            mDatabind.localMusicConsoleVp.currentItem = selectConsoleVpPosition()
        }
        Log.e(TAG, "refreshConsoleFragment")
    }

    private fun selectConsoleVpPosition(): Int {
        var pos = 0
        for ((index, localMusicConsoleFragment) in mViewModel.consoleFragments.withIndex()) {
            if (localMusicConsoleFragment.localMusicFileData.name == audioService?.getAudioName()) {
                pos = index
                break
            }
        }
        return pos
    }

    private fun goneDefaultTv() {
        if (mDatabind.localMusicTitle.visibility == View.VISIBLE) {
            mDatabind.localMusicTitle.visibility = View.GONE
        }
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
            initConsoleFragment()
            Log.v(TAG, "onServiceConnected: $audioService")
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
        unregisterReceiver(addTrackReceiver)
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