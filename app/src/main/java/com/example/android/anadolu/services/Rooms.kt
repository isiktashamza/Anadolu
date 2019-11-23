package com.example.android.anadolu.services

import com.google.gson.annotations.SerializedName

class Rooms(rooms: List<String>){

    @SerializedName("rooms")
    val _rooms : List<String> = rooms
}