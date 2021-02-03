package com.example.neteasecloudmusic.loginactivity.loginbyphone

import com.example.neteasecloudmusic.MainActivity
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
                                        //头像
                                        var icon=model.loginResult.profile?.avatarUrl
                                        //名称
                                        var name=model.loginResult.profile?.nickname
                                        UserPresenter(MainActivity.secondFragment).changeIconAndName(icon, name)
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