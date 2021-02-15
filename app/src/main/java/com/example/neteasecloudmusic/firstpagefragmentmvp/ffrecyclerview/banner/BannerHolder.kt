package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.firstpagefragmentmvp.bannerThread
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.Holder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.mytools.changeDipIntoFloat
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import com.example.neteasecloudmusic.userfragmentmvp.rvAdapter
import kotlinx.android.synthetic.main.banner_view_ff.view.*
import kotlinx.android.synthetic.main.pager_item.view.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BannerHolder(itemView: View) : Holder(itemView), ViewPager.OnPageChangeListener {
    //Banner内部含有 viewPager和一个LinearLayout显示正在Banner的进度
    private var viewPager:ViewPager?=null
    private var pointGroup:LinearLayout?=null
    //数据类
    var data=mutableListOf<NetBannerData>()
    //寻找到布局内的viewPager和LinearLayout
    init {
        viewPager=itemView.my_banner
        pointGroup=itemView.point_view
    }


    var imageList= mutableListOf<ImageView>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setBindView(list: MutableList<ViewData>, position: Int) {
        //使得布局初始化
        //banner的数据
        val x=list[position] as BannerData
        val data= x.netList
        //Pager的数据
        val pagerView= mutableListOf<View>()
        val vPAdapter:VpAdapter= VpAdapter(pagerView)
        viewPager?.adapter=vPAdapter
        vPAdapter.notifyDataSetChanged()

        viewPager?.setOnPageChangeListener(this)
        //image的list
        for (i in 0 until  data.size){
            val view=LayoutInflater.from(context).inflate(R.layout.pager_item,null,false)
            Glide.with(view).load(data[i].imageUrl).into(view.banner_image)
            pagerView.add(view)
            vPAdapter.setPagerList(pagerView)
            //提示vPAdapter改变布局
            vPAdapter.notifyDataSetChanged()
            val image=ImageView(mContext)
            image.setImageResource(R.drawable.point_false)
            image.layoutParams= ViewGroup.LayoutParams(changeDipIntoFloat(15f).toInt(), changeDipIntoFloat(15f).toInt())
            pointGroup?.bringToFront()
            pointGroup?.addView(image)
            imageList.add(image)
        }
        //提示更换布局


        //自动轮播
        bannerThread.launch(IO) {
            var current=viewPager?.currentItem?:0
            while (true){
                delay(3000)
                current = if (current==data.size-1) 0 else current+1
                withContext(Main){
                    viewPager?.setCurrentItem(current,true)
                }
                Log.e("TAG", "开始banner了$current")
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        Log.e("TAG", "onPageScrollStateChanged: " )
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        for (x in imageList){
            x.setImageResource(R.drawable.point_false)
        }
        if (imageList.size>=position+1){
            imageList[position].setImageResource(R.drawable.point_true)
        }
        Log.e("TAG", "onPageScrolled: position:$position imageListSize"+imageList.size)
    }

    override fun onPageSelected(position: Int) {
        Log.e("TAG", "onPageSelected: " )
    }

//    fun initView() {
//        try {
//               val bannerList= readObjectFile(BannerDataObjectName) as MutableList<BannerData>
//                val count=bannerList.size
//                //先清空一下
//                viewList.clear()
//                for (i in 0 until count){
//                    val myview = itemView.also {
//                        it.banner_image.setImageURI(Uri.fromFile(bannerList[i].pic))
//                    }
//                    viewList.add(myview)
//                }
//                //添加点C
//                for(i in 0 until viewList.size){
//                    val imageView= ImageView(mContext)
//                    imageView.layoutParams= ViewGroup.LayoutParams(changeDipIntoFloat(15f).toInt(), changeDipIntoFloat(15f).toInt())
//                    imageView.setImageResource(R.drawable.point_false)
//                    //添加到LinearLayout中
//                    //不知道为啥视图无法动态插入
//                    point_list.addView(imageView)
//                    pointList.add(imageView)
//                }
//                vpAdapter?.notifyDataSetChanged()
//                //开始轮播
//                if(!presenter.isBannering){
//                    presenter.isBannering=true
//                    presenter.doBanner(my_banner)
//                }
//
//
//
//                //添加pager改变的监听
//                my_banner.addOnPageChangeListener(this)
//                ////////////////////////暂时缓着
//                my_banner.setOnClickListener{
//                    viewList.indexOf(view)
//
//                }
//                //图片加载完成
//        }catch (e:Exception){
//            MyToast().sendToast(mContext,"Banner解析错误", Toast.LENGTH_SHORT)
//        }
//    }
//
//    //presenter通知fragment修改banner
//     fun changeBanner(now: Int) {
//        my_banner.setCurrentItem(now,true)
//    }
//
//    //Pager的listener
//     fun onPageScrollStateChanged(state: Int) {}
//
//     fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        //复原
//        for (x in pointList){
//            x.setImageResource(R.drawable.point_false)
//        }
//        //跟踪banner的改变
//        pointList[position].setImageResource(R.drawable.point_true)
//    }
//
//    fun onPageSelected(position: Int) {}


//    //获取banner的视图
//    private suspend fun getBanner() {
//        val bannerData=BannerData()
//
//        //获取banner的地址
//        val url=getBannerUrl()
//        //开协程发送网络请求
//        //发送获取banner图片的url
//            try {
//                val resultBody= sendGetRequest(url)
//                //Gson解析
//                //解析banner数据
//                bannerData.banners = Gson().fromJson(resultBody, Banners::class.java)
//                //Banner图的个数
//                val count= bannerData.banners.banners.size
//                //把需要的数据解析出来加到data里面
//                for (i in 0 until count){
//                    bannerData.banners.banners[i].apply {
//                        //每一个banner有一个图片和点击跳转的url
//                        data.add(NetBannerData(pic,url))
//                    }
//                }
                //解析完成就是初始化界面


                //downLoadBannerData(count,bannerList)

                //下载banner对象方便下次登陆时候的寻找
               // downLoadObjectFile(BannerDataObjectName,bannerList)
//                //下载完成
//                mainActivitySp.put {
//                    putBoolean("is_banner_cathe",true)
//                }

//            }catch (e:java.lang.Exception){
//                e.printStackTrace()
//            }
//            //切换Main线程更新Ui
//            withContext(Dispatchers.Main){
//                MainActivity.firstFragment.initView()
//            }
//    }
//
//    //初始化视图
//    private fun changeBindView() {
//        //初始化vp_item的view视图
//        val viewList= mutableListOf<View>()
//        val vpAdapter=VpAdapter(mutableListOf())
//        viewList.clear()
//        for (i in data){
//            var view=LayoutInflater.from(context).inflate(R.layout.pager_item,null,false)
//            Glide.with(view).load(i.imageUrl).into(view.banner_image)
//            //加入小的按钮
//            var image=ImageView(mContext)
//            pointGroup?.addView(image)
//            viewList.add(view)
//            vpAdapter.setPagerList(viewList)
//            viewPager?.adapter=vpAdapter
//            //改变视图
//            vpAdapter.notifyDataSetChanged()
//        }
//    }

//    //下载本地的banner数据
//    private suspend fun downLoadBannerData(count: Int, bannerList: MutableList<LocalBannerData>) {
//        //下载图片
//        for (i in 0 until count){
//            downLoadImage("banner$i", bannerResult.banners[i].pic)
//            bannerList.add(LocalBannerData(File("$imagePath/banner$i.jpg") , bannerResult.banners[i].url?:"NULL"))
//        }
//    }

//    //获取banner地址
//    private fun getBannerUrl(): String {
//        //type为1表示获取的是手机的banner
//        /**
//         * 0: pc
//
//        1: android
//
//        2: iphone
//
//        3: ipad
//         */
//        var string="$baseUrl/banner?type=1"
//        return string
//    }


}