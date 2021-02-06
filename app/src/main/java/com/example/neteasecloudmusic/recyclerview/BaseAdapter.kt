package com.example.neteasecloudmusic.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neteasecloudmusic.R
import java.util.ArrayList

class BaseAdapter<T>(list: ArrayList<T>) : RecyclerView.Adapter<BaseAdapter.Holder>(){
    var playList=list

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.Holder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.rv_song_list_item,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    override fun onBindViewHolder(holder: BaseAdapter.Holder, position: Int) {
        holder.apply {

        }
    }

}