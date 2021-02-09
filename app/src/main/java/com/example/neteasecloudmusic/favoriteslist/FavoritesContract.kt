package com.example.neteasecloudmusic.favoriteslist

import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter

interface FavoritesContract {
    interface FavoritesIView {
        fun progressBarOn()
        fun progressBarOff()

    }

    interface  FavoritesIModel{
        fun getSongs(favoriteId: String):String

    }
    interface FavoritesIPresenter {
        fun getSongs(position: Int?, songRvAdapter: SongRvAdapter)
    }
}