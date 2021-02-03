package com.example.neteasecloudmusic.userfragmentmvp

import android.content.SharedPreferences

interface UserContract {
    interface UserIView{
        fun initIconAndName(icon: String?, name: String?)

        fun changeUserTitle(username: String?, userIconUrl: String?)
        fun initView()
    }

    interface UserIModel{

    }

    interface UserIPresenter{
        //更改用户的界面的数据

        fun initView(sp: SharedPreferences)
    }
}