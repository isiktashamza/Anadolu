package com.example.android.anadolu.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.android.anadolu.R
import com.example.android.anadolu.adapters.RoomViewAdapter
import com.example.android.anadolu.services.ApiClient
import com.example.android.anadolu.services.ApiInterface
import com.example.android.anadolu.services.Rooms
import retrofit2.Call
import retrofit2.Response

class RoomFragment : Fragment(), RoomViewAdapter.RoomClickListener {
    override fun getItem(position: Int) {
    }

    val LOG_TAG = RoomFragment::class.java.name

    var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    var userId = "5dd8fdc77a591b098cd721bb"

    var roomList : Rooms = Rooms(ArrayList())

    var dataFetched = false

    lateinit var roomRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomRecyclerView = getView()?.findViewById(R.id.room_list) as RecyclerView
        roomRecyclerView.layoutManager = LinearLayoutManager(context)
        roomRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        if(dataFetched.not()){
            getRooms()
        }
        else{
            roomRecyclerView.adapter = RoomViewAdapter(roomList, this)
        }


    }

    fun getRooms(){
        apiInterface.getRoomsList(userId).enqueue(object : retrofit2.Callback<Rooms>{
            override fun onFailure(call: Call<Rooms>, t: Throwable) {
                Log.i(LOG_TAG,t.message)
            }

            override fun onResponse(
                call: Call<Rooms>,
                response: Response<Rooms>
            ) {
                dataFetched = true
                roomList = response.body()!!
                roomRecyclerView.adapter = RoomViewAdapter(roomList, this@RoomFragment)
            }
        })
    }

}
