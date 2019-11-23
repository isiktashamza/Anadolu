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
import android.widget.TextView
import com.example.android.anadolu.R
import com.example.android.anadolu.services.ApiClient
import com.example.android.anadolu.services.ApiInterface
import com.example.android.anadolu.services.PipeInformation
import com.example.android.anadolu.services.Pressure

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Response


class PipeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val LOG_TAG = PipeFragment::class.java.name

    private var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    var userId = "5dd8fdc77a591b098cd721bb"

    var pipeName = ""
    var roomName = ""

    lateinit var datum : List<Pressure>

    private lateinit var graphView : GraphView
    private lateinit var spinner: Spinner

    private lateinit var timeDiff: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pipe, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.spinner) as Spinner
        graphView = view.findViewById(R.id.graph) as GraphView
        timeDiff = view.findViewById(R.id.time_diff) as TextView

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
        pipeName = arguments!!.getString("pipeName")!!
        roomName = arguments!!.getString("roomName")!!
        getPipeData(pipeName, roomName,HOUR)

    }

    override fun onNothingSelected(nothing: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        getPipeData(pipeName,roomName,spinner.selectedItem.toString())
    }

    private fun getPipeData(pipeName: String, roomName: String,time:String){
        apiInterface.getPipeData(userId,roomName,pipeName, time).enqueue(object: retrofit2.Callback<PipeInformation>{
            override fun onFailure(call: Call<PipeInformation>, t: Throwable) {
                Log.i(LOG_TAG,"on failure")
            }

            override fun onResponse(
                call: Call<PipeInformation>,
                response: Response<PipeInformation>
            ) {
                response.body()?.let { createGraph(it,time) }
            }
        })
    }

    fun createGraph(list: PipeInformation, time: String){

        datum = list._pipeInfo

        val values = arrayOfNulls<DataPoint>(datum.size)
        for(index in datum.indices){
            val pressure = datum[index]
            values[index] = DataPoint(
                index/1.0,
                pressure.pressure
            )
        }

        when(time){
            HOUR -> timeDiff.text = resources.getString(R.string.graph_one_hour_ago)
            DAY -> timeDiff.text = resources.getString(R.string.graph_one_day_ago)
            WEEK -> timeDiff.text = resources.getString(R.string.graph_one_week_ago)
            else -> timeDiff.text = resources.getString(R.string.graph_one_month_ago)
        }
        graphView.gridLabelRenderer.isHorizontalLabelsVisible = false
        graphView.viewport.isYAxisBoundsManual = true

        val max = datum.maxBy { it.pressure }!!.pressure
        val min = datum.minBy { it.pressure }!!.pressure

        graphView.viewport.setMinY(min-2.0)
        graphView.viewport.setMaxY(max+2.0)

        val series = LineGraphSeries<DataPoint>(values)
        graphView.removeAllSeries()
        graphView.addSeries(series)

    }
    companion object{
        const val HOUR = "Last one hour"
        const val DAY = "Last one day"
        const val WEEK = "Last one week"

    }
}
