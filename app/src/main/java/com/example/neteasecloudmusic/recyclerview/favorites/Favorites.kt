package com.example.neteasecloudmusic.recyclerview.favorites

import java.io.File
import java.io.Serializable

class Favorites(var name:String,var trackCount:Int,var images:File):Serializable{
   //重写序列号 防止代码改动使其出无法反序列化
    companion object {
        const val serialVersionUID = -6666666666666L
    }
}