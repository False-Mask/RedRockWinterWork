package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
class VpAdapter(var viewList: MutableList<View>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        //本地数据
//        view.setOnClickListener {
//            bannerThread.launch(Dispatchers.IO) {
//                val x= readObjectFile(BannerDataObjectName) as MutableList<MainActivityModel.Banner>
//                val index=viewList.indexOf(view)
//                if (x[index].url!="NULL"){
//                    val intent=Intent(context,BannerWebActivity::class.java)
//                    intent.putExtra("bannerData",x[index])
//                    withContext(Dispatchers.Main){
//                        context?.startActivity(intent)
//                    }
//                }
//            }
//        }
      return view ==`object`
    }

    fun setPagerList(viewList: MutableList<View>){
        this.viewList=viewList
    }
    fun getPagerList(): MutableList<View> {
        return viewList
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