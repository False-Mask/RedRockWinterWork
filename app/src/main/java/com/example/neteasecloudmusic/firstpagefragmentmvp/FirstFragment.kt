package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.MultiRvAdapter
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.VpAdapter
import com.example.neteasecloudmusic.mytools.net.netThread
import kotlinx.android.synthetic.main.banner_view_ff.*
import kotlinx.android.synthetic.main.first_fragment_layout.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment(mainActivity: MainActivity) : Fragment(),FirstFragmentContract.FirstFragmentIView
{
    var TAG="FirstFragment"

    private var pointList= mutableListOf<ImageView>()
    var presenter=FragmentPresenter(this)
    var list= mutableListOf<View>()
    private var vpAdapter: VpAdapter?=null
    private var mContext=mainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.first_fragment_layout,container,false)
        Log.e(TAG, "onCreateView: " )
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置adapter
        //val vpView: View =layoutInflater.inflate(R.layout.pager_item, null, false)
        //vpAdapter= VpAdapter(mutableListOf(vpView, vpView, vpView))
        //my_banner.adapter=vpAdapter
        var multiRvAdapter:MultiRvAdapter
        val bannerData=BannerData()
        var layoutManager=GridLayoutManager(context,6)
        //发送获取其他的数据
        netThread.launch (IO){
            bannerData.netList=presenter.getBanner()

            //初始化multiRvAdapter
            multiRvAdapter=MultiRvAdapter(mutableListOf(bannerData))
            multiRvAdapter.setGridSpan(layoutManager)
            withContext(Main){
                //设置adapter
                recycler_view_ff.adapter=multiRvAdapter
                recycler_view_ff.layoutManager=layoutManager
                //数据的改变
                multiRvAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            //当FirstFragment浮现的时候

        }
    }
}