package com.example.neteasecloudmusic.userfragmentmvp

import android.content.SharedPreferences
import android.view.View
import java.io.File

interface UserContract {
    interface UserIView{
        fun initIconAndName(icon: File?, name: String?)
        fun changeUserTitle(username: String?, userIconUrl: File?)
        fun initView()
        fun favoritesClicked(v: View, position: Int)
    }

    interface UserIModel{

    }

    interface UserIPresenter{
        //更改用户的界面的数据

        fun initView(sp: SharedPreferences)
        fun rvItemClicked(view: View, position: Int)
    }

    interface PlayListIModel{

    }


    interface PlayListPresenter{

    }
}