package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music

import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.Holder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import kotlinx.android.synthetic.main.multi_top_item.view.*

class TopHolder(itemView: View) : Holder(itemView) {
    var textView:TextView?=null
    var imageView:ImageView?=null
    init {
        textView=itemView.top_text_view
        imageView=itemView.top_image_view
    }
    override fun setBindView(holder: MutableList<ViewData>, position: Int) {
        val data=holder[position] as TopData
        textView?.text=data.text
        Glide
                .with(itemView)
                .load(data.image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                .into(imageView!!)
        itemView?.setOnClickListener {
            val intent=Intent(context,FavoritesActivity::class.java)

            intent.putExtra("playListId",data.id)
            ///
            intent.putExtra("useLocalCathe",false)

            context?.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
        }
    }

}