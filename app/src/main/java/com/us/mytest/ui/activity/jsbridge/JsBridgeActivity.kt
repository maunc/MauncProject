package com.us.mytest.ui.activity.jsbridge

import android.os.Bundle
import com.google.gson.Gson
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.mytest.databinding.ActivityJsBridgeBinding

class JsBridgeActivity : BaseVmActivity<JsBridgeVM, ActivityJsBridgeBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //创建js接口
        mDatabind.mWebView.addJavascriptInterface(
            JsBridgeInterFace(
                mDatabind.mWebView.callbacks,
            ), "WebViewJavascriptBridge"  //这个是js调用android的接口名称  js与Android约定
        )
        mDatabind.mWebView.setGson(Gson())
        //加载html
        mDatabind.mWebView.loadUrl("file:///android_asset/JsToAndroidDemo.html")
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }
}