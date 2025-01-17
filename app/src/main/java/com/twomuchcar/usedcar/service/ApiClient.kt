package com.twomuchcar.usedcar.firebase

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
//    private const val BASE_URL = "http://10.0.2.2:8080"
    private const val BASE_URL = "https://twomuchcar.shop"

    val api: ApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 요청/응답 로그 활성화
        }
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Cookie", "AccessToken=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imtha2FvIDM4NzkyMTkwMDYiLCJyb2xlIjoiUk9MRV9VU0VSIiwiZW1haWwiOiJqYW5naGU5QG5hdmVyLmNvbSIsImlhdCI6MTczNzAzNDAyMywiZXhwIjoxNzM3MTIwNDIzfQ.-JajgtoflauPvj8xhSD0op5OX7Ai-leXO5hF_U0XXzc")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}