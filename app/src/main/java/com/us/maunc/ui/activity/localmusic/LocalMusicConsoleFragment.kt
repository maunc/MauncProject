package com.us.maunc.ui.activity.localmusic

import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseFragment
import com.us.maunc.databinding.FragmentLocalMusicConsoleBinding

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicConsoleFragment :
    BaseFragment<LocalMusicConsoleVM, FragmentLocalMusicConsoleBinding>() {

    fun newInstance(data: LocalMusicFileData): LocalMusicConsoleFragment {
        val args = Bundle()
        args.putSerializable("data", data)
        val fragment = LocalMusicConsoleFragment()
        fragment.arguments = args
        return fragment
    }

    val localMusicFileData: LocalMusicFileData
        get() {
            return arguments?.getSerializable("data") as LocalMusicFileData
        }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.localMusicTitle.isSelected = true
        mDatabind.localMusicTitle.text = localMusicFileData.name
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }
}