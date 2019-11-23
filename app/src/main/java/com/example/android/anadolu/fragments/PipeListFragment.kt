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
import com.example.android.anadolu.adapters.PipeViewAdapter
import com.example.android.anadolu.services.ApiClient
import com.example.android.anadolu.services.ApiInterface
import com.example.android.anadolu.services.Pipes
import retrofit2.Call
import retrofit2.Response

class PipeListFragment : Fragment(), PipeViewAdapter.PipeClickListener {
    override fun getItem(position: Int) {

    }

    val LOG_TAG = PipeListFragment::class.java.name

    var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    var userId = "5dd8fdc77a591b098cd721bb"

    var pipeList : Pipes = Pipes(ArrayList())

    var dataFetched = false

    private lateinit var roomName : String
    lateinit var pipeRecyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        roomName = arguments!!.getString("roomName")!!

        pipeRecyclerView = getView()?.findViewById(R.id.pipe_list) as RecyclerView
        pipeRecyclerView.layoutManager = LinearLayoutManager(context)
        pipeRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        if(dataFetched.not()){
            getPipes()
        }
        else {
            pipeRecyclerView.adapter = PipeViewAdapter(pipeList, this, roomName)
        }
    }

    fun getPipes(){
        apiInterface.getPipes(userId,roomName).enqueue(object : retrofit2.Callback<Pipes>{
            override fun onFailure(call: Call<Pipes>, t: Throwable) {
                Log.i(LOG_TAG, "on failure")
            }

            override fun onResponse(
                call: Call<Pipes>,
                response: Response<Pipes>
            ) {
                dataFetched = true
                pipeList = response.body()!!
                pipeRecyclerView.adapter = PipeViewAdapter(pipeList, this@PipeListFragment, roomName)
            }
        })
    }

}
