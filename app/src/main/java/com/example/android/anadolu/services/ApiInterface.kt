package com.example.android.anadolu.services


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface ApiInterface {

    @GET("rooms")
    fun getRoomsList(@Query("userId") userId:String ): Call<Rooms>

    @GET("pipes")
    fun getPipes(@Query("userId") userId: String, @Query("roomName") roomName:String): Call<Pipes>

    @GET("pipe")
    fun getPipeData(@Query("userId") userId: String, @Query("roomName") roomName: String, @Query("pipeName") pipeId:String, @Query("time") time:String): Call<PipeInformation>

}