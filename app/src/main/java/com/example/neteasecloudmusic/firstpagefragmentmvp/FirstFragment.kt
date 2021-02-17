package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.MultiRvAdapter
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerData
import com.example.neteasecloudmusic.firstpagefragmentmvp.search.SearchActivity
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.title.TitleData
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.first_fragment_layout.*

class FirstFragment(mainActivity: MainActivity) : Fragment(),FirstFragmentContract.FirstFragmentIView
{
    var TAG="FirstFragment"
    private var mContext=mainActivity

    //banner点的列表
    //private var pointList= mutableListOf<ImageView>()

    //private var vpAdapter: VpAdapter?=null

    var presenter=FragmentPresenter(this)
    //private val musicDataBig2=MusicDataBig("2",)
    //private val musicDataBig1=MusicDataBig("1",)
    //private val musicDataSmall3=MusicDataSmall("3",)
    //private val musicDataSmall2=MusicDataSmall("2",)
    //private val musicDataSmall1=MusicDataSmall("1",)


    var multiRvAdapter:MultiRvAdapter?=null
    private val bannerData=BannerData()

    //第一个title
    private val titleData1=TitleData("推荐歌曲")
    var list= mutableListOf<View>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.first_fragment_layout,container,false)
        Log.e(TAG, "onCreateView: " )

        return view
    }

    //小型的布局

    //稍微大一点的布局

    //var multiList= mutableListOf<ViewData>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置adapter
        //val vpView: View =layoutInflater.inflate(R.layout.pager_item, null, false)
        //vpAdapter= VpAdapter(mutableListOf(vpView, vpView, vpView))
        //my_banner.adapter=vpAdapter
        //val layoutManager=GridLayoutManager(context,6)
        search_image_sa.setOnClickListener(presenter)
        search_text.setOnClickListener(presenter)

        presenter.initRecyclerView()
//        //发送获取其他的数据
//        netThread.launch (IO){
//            bannerData.netList=presenter.getBanner()
//            //添加banner的数据
//            multiList.add(bannerData)
//
//            //初始化adapter
//            multiRvAdapter=MultiRvAdapter(multiList)
//            //设置布局管理器
//            multiRvAdapter?.setGridSpan(layoutManager)
//
//            //设置adapter和layoutManager
//            recycler_view_ff.adapter=multiRvAdapter
//            recycler_view_ff.layoutManager=layoutManager
//
//            //添加标题
//            multiList.add(TitleData("推荐歌曲"))
//            multiRvAdapter?.setMultiList(multiList)
//            multiRvAdapter?.notifyDataSetChanged()
//
//            //添加
//            //设置recyclerView的list
//
//
//            withContext(Main){
//                multiRvAdapter?.notifyDataSetChanged()
//            }
//
//
//            //标题栏
//
//            //请求获取到推荐歌单
//
//        }

        //刷新的实现丢给presenter干
        swipe_layout.setOnRefreshListener (presenter)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            //当FirstFragment浮现的时候
        }
    }

    override fun sendToast(s: String) {
        MyToast().sendToast(mContext,s,Toast.LENGTH_SHORT)
    }

    override fun setFreshOff() {
        swipe_layout.isRefreshing=false
    }

    override fun loopToSearchActivity() {
        val intent=Intent(mContext,SearchActivity::class.java)
        startActivity(intent)
    }
}