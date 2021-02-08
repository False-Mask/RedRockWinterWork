package com.example.neteasecloudmusic.recyclerview.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.R
//list为 recyclerView中的Item的集合
//声明顶层 方便修改
var list:MutableList<Favorites> = mutableListOf()
class RvAdapter : RecyclerView.Adapter<RvAdapter.MyHolder>() {
    var myCall:Call?=null
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var name:TextView=itemView.findViewById(R.id.favorite_item_title)
            var trackCount:TextView=itemView.findViewById(R.id.favorite_item_count)
            var coverImage:ImageView=itemView.findViewById(R.id.favorite_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.MyHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.favorites_rv_item,parent ,false)
        view.setOnClickListener{
            myCall?.onItemClicked(view,viewType)
        }
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RvAdapter.MyHolder, position: Int) {
        var item=list.get(position)
        holder.apply {
            name.text=item.name
            trackCount.text="${item.trackCount}首"
            Glide.with(MyApplication.getContext()).load(item.images).into(coverImage)
        }
    }
    fun setOnItemClicked(call:Call){
        this.myCall=call
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface Call{
        fun onItemClicked(view:View,position: Int)
    }
}