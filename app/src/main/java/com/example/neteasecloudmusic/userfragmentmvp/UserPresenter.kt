package com.example.neteasecloudmusic.userfragmentmvp

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel

class UserPresenter(fragment:UserFragment) :UserContract.UserIPresenter{
    //供给其他Presenter调用

    var view=fragment
    var model=UserModel()
    //修改名称头像
    fun changeIconAndName(icon: String?, name: String?) {
        view.initIconAndName(icon,name)
    }


}