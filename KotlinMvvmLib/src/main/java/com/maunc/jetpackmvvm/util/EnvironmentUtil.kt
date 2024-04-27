package com.maunc.jetpackmvvm.util

import android.util.Log
import java.io.*

object EnvironmentUtil {

    private const val TAG = "EnvironmentUtil"

    //新域名
    private const val NEW_TEST_URL = "https://gw.test.xuexizhiwang.com/" //测试
    private const val NEW_SCALE_URL = "https://gw.pre.xuexizhiwang.com/" //灰度
    private const val NEW_PRO_URL = "https://gwpad.xuexizhiwang.com/" //平板正式
//    private const val NEW_PRO_URL = "https://gwsdb.xuexizhiwang.com/" //扫读笔正式

    //旧域名
    private const val TEST_URL = "http://api3.test.jiumentongbu.com/" //测试
    private const val SCALE_URL = "https://gray.xuexizhiwang.com/" //灰度
    private const val PRO_URL = "https://apipad.xuexizhiwang.com/" //平板正式
//    private const val PRO_URL = "https://apisdb.xuexizhiwang.com/" //扫读笔正式

    //测试token
    private const val TEST_TOKEN = "login_jxwdevice20001855wfq5ca0eir4qcmbvyd6k7lpxl28llqb4" //测试token

    var platformVersion = "release" // 测试:test 灰度:gray 正式:release
    private var token: String? = null

    /**
     * 初始化检测
     */
    fun setPlatFormType() {
        try {
            val file = File("/sdcard/config.ini")
            val inputStream = FileInputStream(file)
            platformVersion = readFileContent(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d(TAG, "platformVersion=$platformVersion")
        }
    }

    /**
     * 获取新请求url
     */
    fun getNewBaseUrl(): String {
        return when (platformVersion) {
            "test" -> NEW_TEST_URL
            "gray" -> NEW_SCALE_URL
            else -> NEW_PRO_URL
        }
    }

    /**
     * 获取请求url
     */
    fun getBaseUrl(): String {
        return when (platformVersion) {
            "test" -> TEST_URL
            "gray" -> SCALE_URL
            else -> PRO_URL
        }
    }

    fun getSignKey(): String {
        return when (platformVersion) {
            "test" -> "YxCcUzDWRz1HVkj1Xtofkilb5KrnJAxs"
            "gray" -> "PZbKVKQUavLmswFznRnuACg9u2o8pmhL"
            else -> "PZbKVKQUavLmswFznRnuACg9u2o8pmhL"
        }
    }

    /**
     * 设置token
     */
    fun setToken(token: String?) {
        Log.d(TAG, "setToken: token=${token}")
        EnvironmentUtil.token = token
    }

    /**
     * 获取token
     */
    fun getToken(): String {
        return token ?: ""
    }

    private fun readFileContent(inputStream: InputStream): String {
        val inputStreamReader = InputStreamReader(inputStream)
        var reader: BufferedReader? = null
        val sbf = StringBuilder()
        try {
            reader = BufferedReader(inputStreamReader)
            var tempStr: String?
            while (reader.readLine().also { tempStr = it } != null) {
                sbf.append(tempStr)
            }
            reader.close()
            return sbf.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
            }
        }
        return sbf.toString()
    }

}