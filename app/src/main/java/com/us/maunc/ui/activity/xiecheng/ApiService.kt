package com.us.maunc.ui.activity.xiecheng

import retrofit2.http.GET

interface ApiService {

    @GET("rand.qinghua")
    suspend fun getTestNetData(): TestData
}