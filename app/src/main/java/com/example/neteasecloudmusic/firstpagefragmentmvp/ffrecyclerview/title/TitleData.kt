package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.title

import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewHolderFactory

data class TitleData(var text:String=""):ViewData{
    override fun getType(): Int {
        return ViewHolderFactory.TITLE_VIEW
    }
}