package com.example.mobileappporject.data.remote

import com.example.mobileappporject.data.remote.api.GrammarApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 10.0.2.2 is used by Android Emulator to reach localhost on host machine
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val grammarApi: GrammarApi by lazy {
        retrofit.create(GrammarApi::class.java)
    }
}
