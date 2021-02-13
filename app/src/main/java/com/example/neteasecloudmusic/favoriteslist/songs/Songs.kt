package com.example.neteasecloudmusic.favoriteslist.songs

import java.io.File
import java.io.Serializable

data class Song(var artist:String="",
                var image:File?=null,
                var songName:String="",
                var songId:String=""):Serializable{
    companion object{
        const val serialVersionUID = 124L
    }
}

data class Songs(
        var songs:MutableList<Song> = mutableListOf()
):Serializable{
    companion object{
        const val serialVersionUID = 125L
    }
}