package com.example.neteasecloudmusic.loginactivity.loginbyphone

import android.util.Log
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.downLoadObjectFile
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.userfragmentmvp.UserPresenter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

const val HeadShowName="UserHeadShow"
const val MyLoginCathe="LoginResult"
const val BackGroundName="BackGround"
class ByPhonePresenter (activity:LoginByPhoneActivity): ByPhoneContract.ByPhoneIPresenter{
        val TAG="ByPhonePresenter"
        var view=activity
        var model=ByPhoneModel()
        override fun loginClicked(phoneNumber: String, passwordText: String) {
                view.progressOn()
                var url=model.login(phoneNumber,passwordText)
                netThread.launch(Dispatchers.IO) {
                        try {
                                var respondBody=sendGetRequest(url)
                                loginResult=Gson().fromJson(respondBody,ByPhoneModel.LoginResult::class.java)
                                //
                                if (loginResult.code==200){
                                        downLoadImage(HeadShowName, loginResult.profile?.avatarUrl!!)
                                        downLoadImage(BackGroundName, loginResult.profile?.backgroundUrl!!)

                                        var mData=LoginCathe(File("$imagePath/$HeadShowName.jpg")
                                        , loginResult.profile?.nickname!!,phoneNumber,passwordText, File("$imagePath/$BackGroundName.jpg"), loginResult.cookie)
                                        downLoadObjectFile(MyLoginCathe,mData)
                                        Log.d(TAG, "loginClicked: LoginCathe下载完成")
                                }

                                Log.d(TAG, "loginClicked: $loginResult")
                        }catch (e:Exception){
                                Log.e(TAG, e.printStackTrace().toString(),e)
                        }
                        withContext(Dispatchers.Main){
                                if (loginResult.code==200){
                                        view.sendToast("登陆成功")
                                        view.progressOff()

                                        //退出activity
                                        ActivityController.Static.finishAll()

                                        //登陆逻辑处理完成了 现在就是得告诉 fragment 叫它更新界面
                                        //头像 名称
                                        loginResult.profile?.apply {
                                                UserPresenter(MainActivity.secondFragment).changeIconAndName(File("$imagePath/$HeadShowName.jpg")
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