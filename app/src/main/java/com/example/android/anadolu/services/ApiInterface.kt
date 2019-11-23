package com.example.android.anadolu.services


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface ApiInterface {

    @GET("rooms")
    fun getRoomsList(@Query("userId") userId:String ): Call<ArrayList<String>>

    @GET("pipes")
    fun getPipes(@Query("userId") userId: String, @Query("roomName") roomName:String): Call<ArrayList<String>>

}