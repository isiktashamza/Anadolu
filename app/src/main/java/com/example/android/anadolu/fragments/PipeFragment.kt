package com.example.android.anadolu.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class PipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.android.anadolu.R.layout.fragment_pipe, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val graphView = view.findViewById(com.example.android.anadolu.R.id.graph) as GraphView


        val datum = data()
        val series = LineGraphSeries<DataPoint>(datum)
        graphView.addSeries(series)
        graphView.viewport.isXAxisBoundsManual = true
        graphView.viewport.setMinX(datum.size-50.0)
        graphView.viewport.setMaxX(datum.size.toDouble())

        graphView.viewport.isYAxisBoundsManual = true
        graphView.viewport.setMinY(0.0)
        graphView.viewport.setMaxY(32.0)

        graphView.viewport.isScrollable = true
        graphView.viewport.isScalable = true
        graphView.viewport.setScrollableY(true)
        graphView.viewport.setScalableY(true)
    }

    private fun data(): Array<DataPoint?> {
            val n = 500  //to find out the no. of data-points
        val values =
            arrayOfNulls<DataPoint>(n)     //creating an object of type DataPoint[] of size 'n'
        for (i in 0 until n) {
            val v = DataPoint(
                i.toDouble(),
                ((i*i)%31).toDouble()
            )
            values[i] = v
        }
        return values
    }

}
