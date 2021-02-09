package com.example.neteasecloudmusic.favoriteslist.songs

import java.io.File
import java.io.Serializable

data class Songs(
    var artists:MutableList<String> = mutableListOf(),
    var image:MutableList<File> = mutableListOf(),
    var songName:MutableList<String> = mutableListOf(),
    var songid:MutableList<String> = mutableListOf()
):Serializable{
    companion object{
        const val serialVersionUID = 123L
    }
}

data class Song(var artist:String="",
                var image:File?=null,
                var songName:String="",
                var songId:String=""):Serializable{
    companion object{
        const val serialVersionUID = 124L
    }
}