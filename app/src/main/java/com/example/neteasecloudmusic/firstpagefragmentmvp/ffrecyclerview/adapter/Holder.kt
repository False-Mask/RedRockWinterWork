package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //holder BindView中需要修改数据
    abstract fun setBindView(holder: MutableList<ViewData>, position: Int)
}