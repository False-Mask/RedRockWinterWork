package com.example.neteasecloudmusic.favoriteslist

interface FavoritesContract {
    interface FavoritesIView {

    }

    interface  FavoritesIModel{
        fun getSongs(favoriteId: String):String

    }
    interface FavoritesIPresenter {
        fun getSongs(position: Int?)
    }
}