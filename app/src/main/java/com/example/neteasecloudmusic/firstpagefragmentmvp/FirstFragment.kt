package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mainactivitymvp.BannerDataObjectName
import com.example.neteasecloudmusic.mainactivitymvp.mainActivitySp
import com.example.neteasecloudmusic.mytools.changeDipIntoFloat
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.myview.BannerData
import com.example.neteasecloudmusic.myview.VpAdapter
import com.example.neteasecloudmusic.myview.viewList
import kotlinx.android.synthetic.main.first_fragment_layout.*
import kotlinx.android.synthetic.main.pagerview.*
import kotlinx.android.synthetic.main.pagerview.view.*

class FirstFragment(mainActivity: MainActivity) : Fragment(),FirstFragmentContract.FirstFragmentIView
        ,ViewPager.OnPageChangeListener{
    private var pointList= mutableListOf<ImageView>()
    var presenter=FragmentPresenter(this)
    var list= mutableListOf<View>()
    private var vpAdapter:VpAdapter?=null
    private var mContext=mainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.first_fragment_layout,container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置adapter
        val vpView: View =layoutInflater.inflate(R.layout.pagerview, null, false)
        vpAdapter=VpAdapter(mutableListOf(vpView,vpView,vpView))
        my_banner.adapter=vpAdapter
    }

    override fun initView() {

        try {
            if(mainActivitySp.getBoolean("is_banner_cathe",false)){
                var bannerList= readObjectFile(BannerDataObjectName) as MutableList<BannerData>
                var count=bannerList.size
                //先清空一下
                viewList.clear()
                for (i in 0 until count){
                    var myview = LayoutInflater.from(mContext).inflate(R.layout.pagerview, null, false).also {
                        it.banner_image.setImageURI(Uri.fromFile(bannerList[i].pic))
                    }
                    viewList.add(myview)
                }
                //添加点C
                for(i in 0 until viewList.size){
                    var imageView=ImageView(mContext)
                    imageView.layoutParams= ViewGroup.LayoutParams(changeDipIntoFloat(15f).toInt(), changeDipIntoFloat(15f).toInt())
                    imageView.setImageResource(R.drawable.point_false)
                    //添加到LinearLayout中
                    //不知道为啥视图无法动态插入
                    point_list.addView(imageView)
                    pointList.add(imageView)
                }
                vpAdapter?.notifyDataSetChanged()
                //开始轮播
                if(!presenter.isBannering){
                    presenter.isBannering=true
                    presenter.doBanner(my_banner)
                }



                //添加pager改变的监听
                my_banner.addOnPageChangeListener(this)
                ////////////////////////暂时缓着
                my_banner.setOnClickListener{
                    viewList.indexOf(view)

                }
                //图片加载完成
            }
        }catch (e:Exception){
            MyToast().sendToast(mContext,"Banner解析错误",Toast.LENGTH_SHORT)
        }
    }

    //presenter通知fragment修改banner
    override fun changeBanner(now: Int) {
        my_banner.setCurrentItem(now,true)
    }

    //Pager的listener
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //复原
        for (x in pointList){
            x.setImageResource(R.drawable.point_false)
        }
        //跟踪banner的改变
        pointList[position].setImageResource(R.drawable.point_true)
    }

    override fun onPageSelected(position: Int) {}
}