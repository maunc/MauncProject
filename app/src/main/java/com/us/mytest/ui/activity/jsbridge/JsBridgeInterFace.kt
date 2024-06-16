package com.us.mytest.ui.activity.jsbridge

import android.util.Log
import android.webkit.JavascriptInterface
import com.github.jsbridge.bridge.BridgeWebView
import com.github.jsbridge.handler.OnBridgeCallback

/**
 *ClsFunction：
 *CreateDate：2024/6/16
 *Author：TimeWillRememberUs
 */
class JsBridgeInterFace(
    callback: MutableMap<String, OnBridgeCallback>
) : BridgeWebView.BaseJavascriptInterface(callback) {

    companion object {
        const val TAG = "JsBridgeInterFace"
    }

    override fun send(data: String?): String {
        Log.d(TAG, "send: $data")
        return "Android"
    }

    @JavascriptInterface
    fun initCallBack(data: String, callBackId: String) {
        Log.d(TAG, "initCallBack: $data,$callBackId")
    }
}