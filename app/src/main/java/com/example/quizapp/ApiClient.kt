package com.example.quizapp

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiClient {
    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()
}