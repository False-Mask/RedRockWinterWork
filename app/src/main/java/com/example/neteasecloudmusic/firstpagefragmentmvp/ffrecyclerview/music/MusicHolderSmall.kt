package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.Holder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import kotlinx.android.synthetic.main.multi_song_small_item.view.*


class MusicHolderSmall(itemView: View) : Holder(itemView) {
    var textView: TextView?=null
    var imageView:ImageView?=null
    init {
        imageView=itemView.multi_song_small_image
        textView=itemView.multi_song_small_text
    }
    override fun setBindView(holder: MutableList<ViewData>, position: Int) {
        val date = holder[position] as MusicDataSmall
        Glide.with(itemView).load(date.image).into(imageView!!)
        textView?.text=date.text
    }

}