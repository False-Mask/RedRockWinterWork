package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music

import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewHolderFactory

data class TopData(
    var text:String="",
    var image:String="",
    var id:String="",
    var updateTime:String=""
) :ViewData{
    override fun getType(): Int {
        return ViewHolderFactory.MUSIC_FAVORITES_TOP
    }

}