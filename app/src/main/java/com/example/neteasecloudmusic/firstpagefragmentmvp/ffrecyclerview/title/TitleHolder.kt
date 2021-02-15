package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.title

import android.view.View
import android.widget.TextView
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.Holder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import kotlinx.android.synthetic.main.multi_title.view.*

class TitleHolder(itemView: View) : Holder(itemView) {
    //布局就一TextView
    var textView:TextView?=null
    init {
        textView=itemView.multi_title_text
    }
    override fun setBindView(holder: MutableList<ViewData>, position: Int) {
        val data=holder[position] as TitleData
        textView?.text = data.text
    }
}