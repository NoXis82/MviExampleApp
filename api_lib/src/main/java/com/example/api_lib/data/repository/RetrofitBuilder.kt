package com.example.api_lib.data.repository

import com.example.api_lib.data.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"

    fun apiHelper(okHttpClient: OkHttpClient): ApiService {
        return getRetrofit(okHttpClient).create(ApiService::class.java)
    }

    private fun getRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

}