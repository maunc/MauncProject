package com.us.maunc.ui.activity.xiecheng

import retrofit2.http.GET

interface ApiService {

    @GET("testJson/userList")
    suspend fun getTestNetData(): MutableList<TestData>
}