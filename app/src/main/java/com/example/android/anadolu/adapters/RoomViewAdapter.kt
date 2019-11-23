package com.example.android.anadolu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.anadolu.R
import com.example.android.anadolu.services.Rooms

class RoomViewAdapter(var roomList: Rooms, var itemClick: RoomClickListener) : RecyclerView.Adapter<RoomViewAdapter.RoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item,parent,false)
        return RoomViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return roomList._rooms.size
    }

    interface RoomClickListener {
        fun getItem(position: Int)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(roomList, position)
    }

    class RoomViewHolder(itemView: View, private var itemClick: RoomClickListener) : RecyclerView.ViewHolder(itemView) {

        private val roomNameView = itemView.findViewById(R.id.room_name) as TextView
        fun bindData(roomList: Rooms, position: Int){
            val roomName = roomList._rooms[position]
            roomNameView.text = roomName

            roomNameView.setOnClickListener {
                itemClick.getItem(adapterPosition)
                itemView.findNavController().navigate(R.id.PipeListFragment, bundleOf(
                    "roomName" to roomList._rooms[adapterPosition]
                ))
            }
        }
    }
}
