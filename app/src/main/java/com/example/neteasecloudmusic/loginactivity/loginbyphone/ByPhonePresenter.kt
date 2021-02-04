package com.example.neteasecloudmusic.loginactivity.loginbyphone

import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import com.example.neteasecloudmusic.userfragmentmvp.UserPresenter
import kotlinx.coroutines.*

class ByPhonePresenter (activity:LoginByPhoneActivity): ByPhoneContract.ByPhoneIPresenter{
        var view=activity
        var model=ByPhoneModel()
        override fun loginClicked(phoneNumber: String, passwordText: String) {
                view.progressOn()
                model.login(phoneNumber,passwordText)
                GlobalScope.launch {
                        withContext(Dispatchers.Main){
                                delay(4000)
                                if (model.loginResult.code==200){
                                        view.sendToast("登陆成功")
                                        view.progressOff()
                                        //退出activity
                                        ActivityController.Static.finishAll()

                                        //登陆逻辑处理完成了 现在就是得告诉 fragment 叫它更新界面
                                        //头像 名称
                                        model.loginResult.profile?.apply {
                                                UserPresenter(MainActivity.secondFragment).changeIconAndName(avatarUrl
                                                ,nickname,phoneNumber,passwordText)
                                        }
                                }
                                //登陆失败
                                else{
                                        view.sendToast("用户名或密码错误")
                                        view.progressOff()
                                }
                        }
                }
        }


}