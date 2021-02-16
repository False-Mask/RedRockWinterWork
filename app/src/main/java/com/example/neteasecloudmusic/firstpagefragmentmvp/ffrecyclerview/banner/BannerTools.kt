package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.bannerThread
import com.example.neteasecloudmusic.mytools.changeDipIntoFloat
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
object BannerTools {
    private var flag=false
    private var isAddFirstTime=true
    fun bannerAutomatic(viewPager: ViewPager?, data:MutableList<NetBannerData>) {
        if (!flag){
            //开始刷新以后不再允许刷新
            flag=true
            bannerThread.launch(Dispatchers.IO) {
                var current=viewPager?.currentItem?:0
                while (true){
                    delay(3000)
                    current = if (current==data.size-1) 0 else current+1
                    withContext(Dispatchers.Main){
                        viewPager?.setCurrentItem(current,true)
                    }
                    Log.e("TAG", "开始banner了$current")
                }
                //刷新终止后允许刷新
                flag=false
            }
        }
    }

    fun addPoints(
        pointGroup: LinearLayout?,
        imageList: MutableList<ImageView>,
        size: Int
    ) {
        if (isAddFirstTime){
            isAddFirstTime=false
            for (i in 0 until size){
                val image= ImageView(mContext)
                image.setImageResource(R.drawable.point_false)
                image.layoutParams= ViewGroup.LayoutParams(changeDipIntoFloat(15f).toInt(), changeDipIntoFloat(15f).toInt())
                pointGroup?.bringToFront()
                pointGroup?.addView(image)
                imageList.add(image)
            }
        }
    }
}