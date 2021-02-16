package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.Holder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import kotlinx.android.synthetic.main.multi_song_small_item.view.*


class MusicSmallHolder(itemView: View) : Holder(itemView) {
    var textView: TextView?=null
    var imageView:ImageView?=null
    init {
        imageView=itemView.multi_song_small_image
        textView=itemView.multi_song_small_text
    }
    override fun setBindView(holder: MutableList<ViewData>, position: Int) {
        val date = holder[position] as MusicSmallData
        Glide.with(itemView).load(date.image).into(imageView!!)
        textView?.text=date.text

        itemView.setOnClickListener{
            val intent=Intent(context,FavoritesActivity::class.java)
            intent?.putExtra("playListId",date.id)
            context?.startActivity(intent)
        }
    }

}