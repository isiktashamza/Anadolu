package com.example.android.anadolu.services

import com.google.gson.annotations.SerializedName
import java.util.*

class PipeInformation(pipe: List<Pressure>, condition:String, open: Boolean?){

    @SerializedName("data")
    val _pipeInfo : List<Pressure> = pipe

    @SerializedName("condition")
    val _condition : String = condition

    @SerializedName("open")
    val _open : Boolean? = open
}

data class Pressure(
    val date: Date,
    val pressure: Double
)