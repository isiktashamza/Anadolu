package com.example.android.anadolu.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val BASE_URL: String = "http://10.16.56.127:9876/api/"
    private var retrofit: Retrofit?= null

    fun getApiClient(): Retrofit? {
        if(retrofit==null){
            retrofit = Retrofit.Builder().client(OkHttpClient()).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }
}