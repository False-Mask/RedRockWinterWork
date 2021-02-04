package com.example.neteasecloudmusic.userfragmentmvp

import android.widget.Toast
import com.example.neteasecloudmusic.MainActivity
import kotlinx.coroutines.*

class UserLoginPresenter (activity:UserFragment): UserLoginContract.ByPhoneIPresenter{
        var view=activity
        var model=LoginModel()
//        override fun loginClicked(phoneNumber: String, passwordText: String) {
//                //view.progressOn()
//                model.login(phoneNumber,passwordText,object : LoginModel.LoginBack{
//                        override fun onFinished(account: LoginModel.LoginResult) {
//                                if (account.code!=200){
//                                        view.sendToast("登陆失败")
//                                }
//                        }
//
//                })
//
//        }
}