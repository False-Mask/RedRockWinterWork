package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.loginactivity.LoginActivity
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.recyclerview.favorites.RvAdapter
import com.example.neteasecloudmusic.useui.UserUiActivity
import kotlinx.android.synthetic.main.second_fragment_layout.*
import java.io.File

//Model层需要用到
var rvAdapter=RvAdapter()
//sp数据库实例(方便抓数据)
var sp: SharedPreferences? = mContext.getSharedPreferences(UserFragment.USER_BASIC_SP_NAME, Context.MODE_PRIVATE)
class UserFragment(mactivity:MainActivity) : Fragment() ,UserContract.UserIView {
    //获取context
    var mcontext=mactivity
    var presenter=UserPresenter(this)
    var activity=mactivity

    //sp数据库的名称
    var TAG="UserFragment"
    companion object{
        val USER_BASIC_SP_NAME="user_basic_data"
    }

    //view被刚刚创建的时候
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.second_fragment_layout,container,false)

        Log.e(TAG, "onCreateView: ")
        return view
    }

    //当view被创建出来的时候
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //设置adapter
        favorites_list.adapter= rvAdapter
        favorites_list.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        //用户头像被点击
        user_head_show_icon.setOnClickListener {
           val isLogin= sp?.getBoolean("is_login",false)
            if (!isLogin!!){
                val intent=Intent(context,LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent=Intent(context,UserUiActivity::class.java)
                startActivity(intent)
            }
        }
        //RecyclerView被点击
        rvAdapter.setOnItemClicked(object : RvAdapter.Call{
            override fun onItemClicked(view: View, position: Int) {
                presenter.rvItemClicked(view,position)
            }

        })
    }

    //未登录时候的初始化界面
    override fun initIconAndName(icon: File?, name: String?) {
        //更改用户的头像
        Glide.with(this).load(icon).into(user_head_show_icon)
        //更改用户的名称
        user_name.text=name
        //存入sp数据库
        context?.getSharedPreferences(USER_BASIC_SP_NAME,Context.MODE_PRIVATE)
            ?.put {
                putBoolean("is_login",true)
            }
    }

    //当用户点击了“我的”选项时候对Fragment的界面进行更新
    override fun initView() {
        //Presenter起来干活啦~
        presenter.initView(sp!!)
    }

    override fun favoritesClicked(v: View, position: Int, intent: Intent) {
        //把当前歌单的图片和创建者的头像 名字给FavoritesActivity
        //intent.putExtra("creator_name", playListDetailsResult.playlist.creator.)
        startActivity(intent)
    }

    //改变user的界面
    override fun changeUserTitle(username: String?, userIconUrl: File?) {
        user_name.text=username
        Glide.with(MainActivity.secondFragment).load(userIconUrl).into(user_head_show_icon)
    }
}
