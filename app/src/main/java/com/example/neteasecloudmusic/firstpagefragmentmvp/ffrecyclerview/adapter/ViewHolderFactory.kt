package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerHolder

class ViewHolderFactory {
    companion object{
       const val BANNER_VIEW=1
       const val TITLE_VIEW=2
       const val MUSIC_FAVORITES=3
        fun getViewHolder(parent: ViewGroup, viewType: Int): Holder {
           return when(viewType){
                1-> BannerHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_view_ff,parent,false))
//                2-> TitleHolder(TODO())
//                3-> MusicHolder(TODO())
               else -> {TODO()}
           }
        }
    }
}