package com.example.android.anadolu.services

import com.google.gson.annotations.SerializedName
import java.util.*

class PipeInformation(pipe: List<Pressure>){

    @SerializedName("data")
    val _pipeInfo : List<Pressure> = pipe
}

data class Pressure(
    val date: Date,
    val pressure: Double
)