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
var list:MutableList<Favorites> = mutableListOf()
class RvAdapter(var mlist: MutableList<Favorites>) : RecyclerView.Adapter<RvAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var name:TextView=itemView.findViewById(R.id.favorite_item_title)
            var trackCount:TextView=itemView.findViewById(R.id.favorite_item_count)
            var coverImage:ImageView=itemView.findViewById(R.id.favorite_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.MyHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.favorites_rv_item,parent ,false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RvAdapter.MyHolder, position: Int) {
        var item=list.get(position)
        holder.apply {
            name.text=item.name
            trackCount.text="第${item.trackCount}个"
            Glide.with(MyApplication.getContext()).load(item.images).into(coverImage)
        }
    }

}