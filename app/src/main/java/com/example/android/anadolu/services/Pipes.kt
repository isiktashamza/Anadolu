package com.example.android.anadolu.services

import com.google.gson.annotations.SerializedName
import java.util.*

//pipeName
//dangerLevel
class Pipes(pipes: List<PipeInfo>){

    @SerializedName("pipes")
    val _pipes : List<PipeInfo> = pipes
}

data class PipeInfo(
    val pipeName:String,
    val dangerLevel: Int,
    val lastUpdated: Date
)