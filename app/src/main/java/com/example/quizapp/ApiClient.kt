package com.example.quizapp

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiClient {
    // 192.168.1.2 = IP komputera w sieci lokalnej (fizyczny telefon);
    // dla emulatora zmień na 10.0.2.2
    const val BASE_URL = "http://192.168.1.7/quiz_api/"

    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()
}