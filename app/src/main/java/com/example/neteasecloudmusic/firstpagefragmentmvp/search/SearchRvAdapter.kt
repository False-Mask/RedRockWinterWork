package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.musicservice.ServiceSong
import kotlinx.android.synthetic.main.search_rv_item.view.*

class SearchRvAdapter : RecyclerView.Adapter<SearchRvAdapter.MyHolder>() {
    private var call: CallBack?=null
    var searchList= mutableListOf<ServiceSong>()

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var positionText:TextView=itemView.song_position
        var songNameText:TextView=itemView.song_name
        var singer:TextView=itemView.singer_name
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRvAdapter.MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_rv_item,parent,false))
    }

    override fun onBindViewHolder(holder: SearchRvAdapter.MyHolder, position: Int) {
        val data=searchList[position]

        holder.apply {
            positionText.text=(position+1).toString()
            songNameText.text=data.songName
            singer.text=data.artist
        }
        //view的点击监听
        holder.itemView.setOnClickListener{
            call?.onItemClicked(it,position)
        }
    }


    fun addItemClickListener(call:CallBack){
        this.call=call
    }

}


interface CallBack{
    fun onItemClicked(v:View,position: Int)
}