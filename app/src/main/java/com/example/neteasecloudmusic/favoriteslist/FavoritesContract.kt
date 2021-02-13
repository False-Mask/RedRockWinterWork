package com.example.neteasecloudmusic.favoriteslist

import android.widget.TextView
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter

interface FavoritesContract {
    interface FavoritesIView {
        fun progressBarOn()
        fun progressBarOff()
        fun setTextColor(songName: TextView?, songPlayingColor: Int)

    }

    interface  FavoritesIModel{
        fun getSongs(favoriteId: String):String

    }
    interface FavoritesIPresenter {
        fun getSongs(position: Int?, songRvAdapter: SongRvAdapter)
        fun getPlayList(playListId: String,songRvAdapter: SongRvAdapter)
        fun itemClicked(position: Int)
    }
}