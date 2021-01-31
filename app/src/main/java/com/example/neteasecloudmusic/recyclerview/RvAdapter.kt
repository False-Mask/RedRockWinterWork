package com.example.neteasecloudmusic.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class RvAdapter(var list: ArrayList<Any>) : RecyclerView.Adapter<RvAdapter.Holder>(){

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.Holder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RvAdapter.Holder, position: Int) {
        TODO("Not yet implemented")
    }

}