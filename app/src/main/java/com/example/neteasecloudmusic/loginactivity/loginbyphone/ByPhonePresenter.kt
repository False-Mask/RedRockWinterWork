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
const val MyLoginCatheObjectFileName="LoginResult"
const val BackGroundName="BackGround"
class ByPhonePresenter (activity:LoginByPhoneActivity): ByPhoneContract.ByPhoneIPresenter{
        val TAG="ByPhonePresenter"
        var view=activity
        var model=ByPhoneModel()
        //登陆按钮被点击
        //只是实现了 用户名 电话号码 用户密码 用户头像 cookie的获取与缓存
        //同时还改了一下登陆的状态
        override fun loginClicked(phoneNumber: String, passwordText: String) {
                //进度条转起来
                view.progressOn()
                val url=model.login(phoneNumber,passwordText)
                netThread.launch(Dispatchers.IO) {
                        try {
                                val respondBody=sendGetRequest(url)
                                loginResult=Gson().fromJson(respondBody,ByPhoneModel.LoginResult::class.java)
                                //
                                if (loginResult.code==200){
                                        //下载头像
                                        downLoadImage(HeadShowName, loginResult.profile?.avatarUrl!!)
                                        //下载背景
                                        downLoadImage(BackGroundName, loginResult.profile?.backgroundUrl!!)
                                        //将用户的缓存数据通过文件流下载下来
                                        val mData=LoginCathe(File("$imagePath/$HeadShowName.jpg")
                                        , loginResult.profile?.nickname!!,phoneNumber,passwordText, File("$imagePath/$BackGroundName.jpg"), loginResult.cookie)
                                        downLoadObjectFile(MyLoginCatheObjectFileName,mData)
                                        Log.d(TAG, "loginClicked: LoginCathe下载完成")
                                }

                                Log.d(TAG, "loginClicked: $loginResult")
                        }catch (e:Exception){
                                Log.e(TAG, e.printStackTrace().toString(),e)
                        }
                        //切线程到Main修改界面
                        withContext(Dispatchers.Main){
                                //登陆成功了
                                if (loginResult.code==200){
                                        view.sendToast("登陆成功")
                                        //ProgressBar停下来
                                        view.progressOff()

                                        //退出activity
                                        ActivityController.Static.finishAll()

                                        //登陆逻辑处理完成了 现在就是得告诉 fragment 叫它更新界面
                                        //头像 名称
                                        loginResult.profile?.apply {
                                                UserPresenter(MainActivity.secondFragment).changeIconAndName(File("$imagePath/$HeadShowName.jpg")
                                                        ,nickname,phoneNumber,passwordText)
                                        }
                                        MainActivity.firstFragment.presenter.initRecyclerView()
                                        //初始化歌单列表 还自动登陆欸
                                        MainActivity.presenter.loginAuto()
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