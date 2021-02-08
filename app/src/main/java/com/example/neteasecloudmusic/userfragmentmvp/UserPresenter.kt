package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneContract
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel
import com.example.neteasecloudmusic.loginactivity.loginbyphone.LoginCathe
import com.example.neteasecloudmusic.loginactivity.loginbyphone.MyLoginCathe
import com.example.neteasecloudmusic.mytools.filedownload.filesPath
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.net.netJob
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.toast.MyToast
import java.io.File

class UserPresenter(fragment:UserFragment) :UserContract.UserIPresenter{
    //供给其他Presenter调用
    var USER_BASIC_SP_NAME= UserFragment.USER_BASIC_SP_NAME
    val TAG="UserPresenter"

    var view=fragment
    var model=UserModel()
    //修改名称头像
    fun changeIconAndName(icon: File?, name: String?,phoneNumber:String?,password:String?) {
        view.initIconAndName(icon,name)
    }

    //保存用户数据后 用户进行登陆
    override fun initView(sp: SharedPreferences) {
        var isLogin=sp.getBoolean("is_login",false)
        if (isLogin){
            Log.d(TAG, "initView: 读取本地的用户缓存信息")
            var myData= readObjectFile(MyLoginCathe) as LoginCathe
            view.changeUserTitle(myData.nickname,myData.headShowImage)
        }
        //歌单

    }
    //Rv被点击了
    override fun rvItemClicked(v: View, position: Int) {
        view.favoritesClicked(v,position)
        MyToast().sendToast(mContext,"${position}被点击",Toast.LENGTH_SHORT)
    }
}