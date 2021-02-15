package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music

import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewHolderFactory

data class MusicDataBig(
        var text: String="",
        var image:Int=0
):ViewData{
    override fun getType(): Int {
        return ViewHolderFactory.MUSIC_FAVORITES_BIG
    }


}