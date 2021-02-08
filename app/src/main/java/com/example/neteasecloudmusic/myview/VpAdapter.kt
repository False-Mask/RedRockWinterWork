package com.example.neteasecloudmusic.myview

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.recyclerview.favorites.list

var viewList= mutableListOf<View>()
class VpAdapter(mutableListOf: MutableList<View>) : PagerAdapter() {
    init {
        viewList=mutableListOf
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
      return view ==`object`
    }

    override fun getCount(): Int {
        return viewList.size
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(viewList[position])
        return viewList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList[position])
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}