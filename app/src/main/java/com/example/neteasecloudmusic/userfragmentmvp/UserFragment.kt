package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.LoginActivity
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.useui.UserUiActivity
import kotlinx.android.synthetic.main.second_fragment_layout.*

class UserFragment(mactivity:MainActivity) : Fragment() ,UserContract.UserIView{
    //获取context
    var mcontext=mactivity
    var presenter=UserPresenter(this)
    var loginPresenter=UserLoginPresenter(this)
    var activity=mactivity
    //sp数据库实例(方便抓数据)
    var sp: SharedPreferences? = mcontext.getSharedPreferences(USER_BASIC_SP_NAME, Context.MODE_PRIVATE)
    //sp数据库的名称
    companion object{
        val USER_BASIC_SP_NAME="user_basic_data"
    }

    //view被刚刚创建的时候
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.second_fragment_layout,container,false)
        return view
    }

    //当view被创建出来的时候
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user_head_show_icon.setOnClickListener {
           var isLogin= sp?.getBoolean("is_login",false)
            if (!isLogin!!){
                var intent=Intent(context,LoginActivity::class.java)
                startActivity(intent)
            }else{
                var intent=Intent(context,UserUiActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //未登录时候的初始化界面
    override fun initIconAndName(icon: String?, name: String?,phoneNumber:String?,password:String?) {
        //更改用户的头像
        Glide.with(this).load(icon).into(user_head_show_icon)
        //更改用户的名称
        user_name.text=name
        //存入sp数据库

        context?.getSharedPreferences(USER_BASIC_SP_NAME,Context.MODE_PRIVATE)
            ?.put {
                putBoolean("is_login",true)
                putString("user_name",name)
                putString("user_icon_url",icon)
                putString("user_phone_number",phoneNumber)
                putString("user_password",password)
            }
    }

    //当用户点击了“我的”选项时候对Fragment的界面进行更新
    override fun initView() {
        //Presenter起来干活啦~
            presenter.initView(sp!!)
    }

    //改变user的界面
    override fun changeUserTitle(username: String?, userIconUrl: String?) {
        user_name.text=username
        Glide.with(MainActivity.secondFragment).load(userIconUrl).into(user_head_show_icon)
    }
///////////////////////////////////////////////////////////////////////////////////////
    fun login() {
        sp!!.apply {
         if (getBoolean("is_login",false)){
             var x =   getString("user_phone_number","NULL")
             var y =   getString("user_password","NULL")
             //loginPresenter.loginClicked(x!!,y!!)
         }
        }
    }


}
