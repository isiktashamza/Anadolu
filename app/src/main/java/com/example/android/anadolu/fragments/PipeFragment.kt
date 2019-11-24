package com.example.android.anadolu.fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.size
import com.example.android.anadolu.R
import com.example.android.anadolu.services.*

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Response


class PipeFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private val LOG_TAG = PipeFragment::class.java.name

    private var apiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)

    var userId = "5dd8fdc77a591b098cd721bb"

    var pipeName = ""
    var roomName = ""

    var isPaused = false
    private var open = true
    lateinit var datum : List<Pressure>

    private lateinit var graphView : GraphView
    private lateinit var spinner: Spinner

    private lateinit var timeDiff: TextView
    private lateinit var now: TextView
    private lateinit var switchValve: ImageView
    private lateinit var nothing : LinearLayout
    private lateinit var message_text : TextView
    private lateinit var progressBar : ProgressBar

    private var lastChosenTime = HOUR


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        now = view.findViewById(R.id.now_time) as TextView
        nothing = view.findViewById(R.id.nothing_layout) as LinearLayout
        message_text = view.findViewById(R.id.message_text) as TextView
        progressBar = view.findViewById(R.id.progres_bar) as ProgressBar
        switchValve = view.findViewById(R.id.switchValve) as ImageView
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
        getPipeData(pipeName, roomName,lastChosenTime)
    }

    override fun onNothingSelected(nothing: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        lastChosenTime = spinner.selectedItem.toString()
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
                response.body()?.let {
                    createGraph(it,time)
                    setSwitch(it._open)
                }
            }
        })
    }

    private fun setSwitch(_open: Boolean?) {
        if(_open != null && _open) {
            switchValve.setColorFilter(Color.BLUE)
            open = true
        }
        else switchValve.setColorFilter(Color.RED)
        switchValve.setOnClickListener {
            apiInterface.switchValve(userId, pipeName).enqueue(object: retrofit2.Callback<Success>{
                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Log.i(LOG_TAG,"on failure")
                }
                override fun onResponse(
                    call: Call<Success>,
                    response: Response<Success>
                ) {
                    if(open) switchValve.setColorFilter(Color.RED)
                    else switchValve.setColorFilter(Color.BLUE)
                    open = !open
                }
            })
        }
    }

    fun createGraph(list: PipeInformation, time: String){

        if(list._condition.isNotEmpty()){
            handleMessage(list._condition)
        }
        lastChosenTime = time
        datum = list._pipeInfo
        if(datum.isEmpty()){
            if(lastChosenTime == MONTH){
                graphView.visibility = View.GONE
                spinner.visibility = View.GONE
                timeDiff.visibility = View.GONE
                now.visibility = View.GONE
                nothing.visibility = View.VISIBLE
            }
            else{
                nothing.visibility = View.GONE
                getPipeData(pipeName,roomName,MONTH)
            }

        }
        else{
            progressBar.visibility = View.GONE
            graphView.visibility = View.VISIBLE
            spinner.visibility = View.VISIBLE
            timeDiff.visibility = View.VISIBLE
            now.visibility = View.VISIBLE
            nothing.visibility = View.GONE
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
            series.isDrawBackground= true
            graphView.removeAllSeries()
            graphView.addSeries(series)

        }


    }

    private fun handleMessage(date: String){
        if(date == "good"){
            message_text.text = resources.getString(R.string.good_health)
        }
        else{
            message_text.text = resources.getString(R.string.possible_congestion,date)

        }
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        progressBar.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if(isPaused){
            isPaused = false
            getPipeData(pipeName,roomName,lastChosenTime)
        }
    }
    companion object{
        const val HOUR = "Last one hour"
        const val DAY = "Last one day"
        const val WEEK = "Last one week"
        const val MONTH = "Last one month"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh -> onRefresh()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onRefresh(){
        progressBar.visibility = View.VISIBLE
        getPipeData(pipeName,roomName,lastChosenTime)
    }
}
