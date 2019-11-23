package com.example.android.anadolu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.anadolu.R

class RoomViewAdapter(var roomList: ArrayList<String>, var itemClick: RoomClickListener) : RecyclerView.Adapter<RoomViewAdapter.RoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item,parent,false)
        return RoomViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    interface RoomClickListener {
        fun getItem(position: Int)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(roomList, position)
    }

    class RoomViewHolder(itemView: View, private var itemClick: RoomClickListener) : RecyclerView.ViewHolder(itemView) {

        private val roomNameView = itemView.findViewById(R.id.room_name) as TextView
        fun bindData(roomList: ArrayList<String>, position: Int){
            val roomName = roomList[position]
            roomNameView.text = roomName

            roomNameView.setOnClickListener {
                itemClick.getItem(adapterPosition)
                itemView.findNavController().navigate(R.id.PipeListFragment, bundleOf(
                    "roomName" to roomList[adapterPosition]
                ))
            }
        }
    }
}
