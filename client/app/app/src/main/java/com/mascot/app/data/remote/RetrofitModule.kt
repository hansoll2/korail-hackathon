package com.mascot.app.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitModule {

    // 🔥 에뮬레이터에서 로컬 서버 접근
    private const val BASE_URL = "http://192.168.111.91:3000/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // ✅ 중요
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val questApi: QuestApi by lazy {
        retrofit.create(QuestApi::class.java)
    }
}
