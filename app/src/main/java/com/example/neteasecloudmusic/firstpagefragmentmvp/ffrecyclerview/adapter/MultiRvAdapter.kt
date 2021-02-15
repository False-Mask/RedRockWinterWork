package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MultiRvAdapter(var list: MutableList<ViewData>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        //获取Holder类
        return ViewHolderFactory.getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        //获取Item的数目
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //设置item的显示信息
        holder.setBindView(list,position)
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].getType()
    }

    fun setGridSpan(grid:GridLayoutManager){
        grid.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val type=list[position].getType()
                return when(type){
                    ViewHolderFactory.BANNER_VIEW->{
                        6
                    }
                    ViewHolderFactory.TITLE_VIEW->{
                        6
                    }
                    ViewHolderFactory.MUSIC_FAVORITES_BIG->{
                        6
                    }
                    ViewHolderFactory.MUSIC_FAVORITES_SMALL->{
                        2
                    }
                    else -> { 0 }
                }
            }

        }
    }
}