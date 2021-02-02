package com.example.neteasecloudmusic.myview

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class VpAdapter (list: List<View>): PagerAdapter() {

    var viewList=list


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
      return view ==`object`
    }

    override fun getCount(): Int {
        return viewList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(viewList.get(position))
        return viewList.get(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList.get(position))
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }



}