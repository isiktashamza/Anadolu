package com.example.android.anadolu.services

import android.service.autofill.UserData
import com.google.gson.annotations.SerializedName

class User(user: UserInfo){

    @SerializedName("user")
    val _user : UserInfo = user

    class UserInfo(email: String, phone: String, address: String, insuranceP:String, homeAge:Int, username:String){
        @SerializedName("email")
        val _email: String = email

        @SerializedName("phoneNumber")
        val _phone: String = phone

        @SerializedName("address")
        val _address: String = address

        @SerializedName("insurancePolicy")
        val _insurancePolicy: String = insuranceP

        @SerializedName("homeAge")
        val _homeAge : Int = homeAge

        @SerializedName("username")
        val _username: String = username

    }

}

