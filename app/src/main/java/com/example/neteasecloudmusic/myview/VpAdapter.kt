package com.example.neteasecloudmusic.myview

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.bannerwebview.BannerWebActivity
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.firstpagefragmentmvp.bannerThread
import com.example.neteasecloudmusic.mainactivitymvp.BannerDataObjectName
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.recyclerview.favorites.list
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var viewList= mutableListOf<View>()
class VpAdapter(mutableListOf: MutableList<View>) : PagerAdapter() {
    init {
        viewList=mutableListOf
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        view.setOnClickListener {
            bannerThread.launch(Dispatchers.IO) {
                val x= readObjectFile(BannerDataObjectName) as MutableList<BannerData>
                val index=viewList.indexOf(view)
                if (x[index].url!="NULL"){
                    val intent=Intent(context,BannerWebActivity::class.java)
                    intent.putExtra("bannerData",x[index])
                    withContext(Dispatchers.Main){
                        context?.startActivity(intent)
                    }
                }
            }
        }
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
        return viewList.indexOf(`object`)
    }
}