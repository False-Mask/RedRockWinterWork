package com.example.neteasecloudmusic.userfragmentmvp

interface UserContract {
    interface UserIView{
        fun initIconAndName(icon: String?, name: String?)

    }

    interface UserIModel{

    }

    interface UserIPresenter{
        //更改用户的界面的数据

    }
}