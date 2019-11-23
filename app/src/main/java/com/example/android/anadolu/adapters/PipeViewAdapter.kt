package com.example.android.anadolu.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.anadolu.R

class PipeViewAdapter(var pipeList: ArrayList<String>, var itemClick: PipeClickListener) : RecyclerView.Adapter<PipeViewAdapter.PipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PipeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pipe_list_item,parent,false)
        return PipeViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return pipeList.size
    }

    interface PipeClickListener {
        fun getItem(position: Int)
    }

    override fun onBindViewHolder(holder: PipeViewHolder, position: Int) {
        holder.bindData(pipeList, position)
    }

    class PipeViewHolder(itemView: View, private var itemClick: PipeClickListener) : RecyclerView.ViewHolder(itemView) {

        private val pipeNameView = itemView.findViewById(R.id.pipe_name) as TextView
        fun bindData(pipeList: ArrayList<String>, position: Int){
            val pipeName = pipeList[position]
            pipeNameView.text = pipeName

            pipeNameView.setOnClickListener {
                itemClick.getItem(adapterPosition)
                itemView.findNavController().navigate(R.id.PipeFragment, bundleOf(
                    "pipeName" to pipeList[adapterPosition]
                ))
            }
        }
    }
}
