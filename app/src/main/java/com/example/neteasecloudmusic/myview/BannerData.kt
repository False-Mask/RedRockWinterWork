package com.example.neteasecloudmusic.myview

import java.io.File
import java.io.Serializable

data class BannerData(
        var pic: File?=null,
        var url:String="NULL"
):Serializable{
    companion object {
        const val serialVersionUID=123456L
    }
}
