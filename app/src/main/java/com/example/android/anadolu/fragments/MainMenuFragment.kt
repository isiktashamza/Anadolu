package com.example.android.anadolu.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

import com.example.android.anadolu.R
import com.example.android.anadolu.adapters.RoomViewAdapter
import com.example.android.anadolu.services.ApiClient
import com.example.android.anadolu.services.ApiInterface
import com.example.android.anadolu.services.Rooms
import com.example.android.anadolu.services.User
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response


class MainMenuFragment : Fragment() {

    var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    val LOG_TAG = MainMenuFragment::class.java.name

    var userId = "5dd8fdc77a591b098cd721bb"

    lateinit var user: User

    lateinit var welcome_text : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcome_text = view.findViewById(R.id.welcome_text) as TextView

        getUser()


        val pipeButton = view.findViewById(R.id.pipe_button) as Button
        pipeButton.setOnClickListener {
            findNavController().navigate(R.id.RoomFragment)
        }

    }
    fun handleWelcome(user: User){
        welcome_text.text = "Welcome " + user._user._username
    }
    private fun getUser(){
        apiInterface.getUser(userId).enqueue(object : retrofit2.Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i(LOG_TAG,t.message!!)
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                response.body()?.let { handleWelcome(it) }
            }
        })
    }

}

