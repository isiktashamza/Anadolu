package com.example.android.anadolu.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val BASE_URL: String = "http://10.16.56.136:4083/api/"
    private var retrofit: Retrofit?= null

    fun getApiClient(): Retrofit? {
        if(retrofit==null){
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }
}