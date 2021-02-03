package com.example.neteasecloudmusic.userfragmentmvp

import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel

class UserPresenter(fragment:UserFragment) :UserContract.UserIPresenter{
    //供给其他Presenter调用

    var USER_BASIC_SP_NAME= UserFragment.USER_BASIC_SP_NAME
    val TAG="UserPresenter"

    var view=fragment
    var model=UserModel()
    //修改名称头像
    fun changeIconAndName(icon: String?, name: String?) {
        view.initIconAndName(icon,name)
    }

    //保存用户数据后 用户进行登陆
    override fun initView(sp: SharedPreferences) {

        var isLogin=sp.getBoolean("is_login",false)
        if (isLogin){
            Log.d(TAG, "initView: 读取本地的用户缓存信息")
            var username=sp.getString("user_name","NULL")
            var userIconUrl=sp.getString("user_icon_url","NULL")
            if (userIconUrl!="NULL"){
                view.changeUserTitle(username,userIconUrl)
            }
        }
    }


}