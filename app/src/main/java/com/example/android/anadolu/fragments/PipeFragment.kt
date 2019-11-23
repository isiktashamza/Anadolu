package com.example.android.anadolu.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.android.anadolu.R
import com.example.android.anadolu.services.ApiClient
import com.example.android.anadolu.services.ApiInterface
import com.example.android.anadolu.services.PipeInformation
import com.example.android.anadolu.services.Pressure

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Response
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class PipeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    val LOG_TAG = PipeFragment::class.java.name

    var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    var userId = "5dd8fdc77a591b098cd721bb"


    lateinit var datum : List<Pressure>

    private lateinit var graphView : GraphView
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.android.anadolu.R.layout.fragment_pipe, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.spinner) as Spinner
        graphView = view.findViewById(R.id.graph) as GraphView

        //spinner creation
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            context!!,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        //data request
        val pipeName = arguments!!.getString("pipeName")
        val roomName = arguments!!.getString("roomName")
        pipeName?.let { roomName?.let { it1 -> getPipeData(it, it1) } }

    }

    override fun onNothingSelected(nothing: AdapterView<*>?) {
        setGraphBoundaries(HOUR)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        setGraphBoundaries(spinner.selectedItem.toString())
    }

    private fun getPipeData(pipeName: String, roomName: String){
        apiInterface.getPipeData(userId,roomName,pipeName).enqueue(object: retrofit2.Callback<PipeInformation>{
            override fun onFailure(call: Call<PipeInformation>, t: Throwable) {
                Log.i(LOG_TAG,"on failure")
            }

            override fun onResponse(
                call: Call<PipeInformation>,
                response: Response<PipeInformation>
            ) {
                response.body()?.let { createGraph(it) }
            }
        })
    }

    private fun createSeries(vals: List<Pressure>){
        val values = arrayOfNulls<DataPoint>(vals.size)
        for(index in vals.indices){
            val pressure = vals[index]
            values[index] = DataPoint(
                index/1000.0,
                pressure.pressure
            )
        }
        val series = LineGraphSeries<DataPoint>(values)
        graphView.removeAllSeries()
        graphView.addSeries(series)

    }
    fun createGraph(list: PipeInformation){

        datum = list._pipeInfo

        createSeries(datum)

        graphView.viewport.isXAxisBoundsManual = true
        graphView.viewport.setMinX(0.0)
        graphView.viewport.setMaxX(datum.size/1000.0)
        graphView.viewport.isYAxisBoundsManual = true
        graphView.viewport.setMinY(20.0)
        graphView.viewport.setMaxY(60.0)
        setGraphBoundaries(DAY)

    }

    private fun setGraphBoundaries(time:String){

        if(::datum.isInitialized){
            var filtered = ArrayList<Pressure>()

            when(time){
                HOUR -> filtered.filter { pres-> (Calendar.getInstance().timeInMillis-pres.date.time)< HOUR_MS }
                DAY -> filtered.filter { pres ->(Calendar.getInstance().timeInMillis-pres.date.time)< DAY_MS }
                WEEK -> filtered.filter { pres -> (Calendar.getInstance().timeInMillis-pres.date.time)< WEEK_MS }
                MONTH -> filtered.filter { pres -> (Calendar.getInstance().timeInMillis-pres.date.time)< MONTH_MS }
            }
            createSeries(filtered)
        }

    }


    companion object{
        val HOUR = "Last one hour"
        val DAY = "Last one day"
        val WEEK = "Last one week"
        val MONTH = "Last one month"
        val MONTH_MS = 259200000
        val WEEK_MS = 60480000
        val DAY_MS = 8640000
        val HOUR_MS = 360000

    }
}
