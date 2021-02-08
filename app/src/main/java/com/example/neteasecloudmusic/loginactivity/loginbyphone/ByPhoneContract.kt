package com.example.neteasecloudmusic.loginactivity.loginbyphone

interface ByPhoneContract {
    interface ByPhoneIModel{
        //登陆
        fun login(phoneNumber: String, passwordText: String): String

    }

    interface ByPhoneIView{
        fun sendToast(toString: String)
        //打开进度条
        fun progressOn()
        //关闭进度条
        fun progressOff()
    }

    interface ByPhoneIPresenter{
        fun loginClicked(phoneNumber: String, passwordText: String)

    }
}