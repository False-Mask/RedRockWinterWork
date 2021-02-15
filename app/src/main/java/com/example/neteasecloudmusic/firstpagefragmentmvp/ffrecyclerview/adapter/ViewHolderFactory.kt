package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerHolder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music.MusicHolderBig
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music.MusicHolderSmall
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.title.TitleHolder

class ViewHolderFactory {
    companion object{
        const val BANNER_VIEW=1
        const val TITLE_VIEW=2
        const val MUSIC_FAVORITES_SMALL=3
        const val MUSIC_FAVORITES_BIG=4
        fun getViewHolder(parent: ViewGroup, viewType: Int): Holder {
           return when(viewType){
                BANNER_VIEW-> BannerHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_view_ff,parent,false))
                TITLE_VIEW-> TitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.multi_title,parent,false))
                MUSIC_FAVORITES_BIG-> MusicHolderBig(LayoutInflater.from(parent.context).inflate(R.layout.multi_song_big_item,parent,false))
                MUSIC_FAVORITES_SMALL->MusicHolderSmall(LayoutInflater.from(parent.context).inflate(R.layout.multi_song_small_item,parent,false))
               else -> { TODO()}
           }
        }
    }
}