package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.loginactivity.loginbyphone.HeadShowName
import com.example.neteasecloudmusic.loginactivity.loginbyphone.LoginCathe
import com.example.neteasecloudmusic.loginactivity.loginbyphone.MyLoginCatheObjectFileName
import com.example.neteasecloudmusic.loginactivity.loginbyphone.loginResult
import com.example.neteasecloudmusic.mainactivitymvp.playListResult
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

var loginCathe:LoginCathe?=null
class UserPresenter(fragment:UserFragment) :UserContract.UserIPresenter
{
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
        val isLogin=sp.getBoolean("is_login",false)
        if (isLogin){
            Log.d(TAG, "initView: 读取本地的用户缓存信息")
            var bool1:Boolean=false
            try {
                loginCathe=readObjectFile(MyLoginCatheObjectFileName) as LoginCathe
                bool1= loginCathe?.headShowImage?.exists() ?: false
            }catch (e:java.lang.Exception){
                sp.put {
                    putBoolean("is_head_show_exist",false)
                }
                Log.e(TAG, "initView: loginCathe读取出错",e )
            }
            //用户的头像不存在欸那就重新下载呗
            if (!bool1){
                sp.put {
                    putBoolean("is_head_show_exist",false)
                }
                //重新下载
                netThread.launch(Dispatchers.IO) {
                    downLoadImage(HeadShowName, loginResult.profile?.avatarUrl?:"NULL")
                    //设置头像已经下载
                    sp.put {
                        putBoolean("is_head_show_exist",true)
                    }
                    try {
                        loginCathe= readObjectFile(MyLoginCatheObjectFileName) as LoginCathe
                    }catch (e:Exception){
                        Log.e(TAG, "initView: 读取头像信息时候出现问题",e)
                    }
                    //main线程准备一下
                    withContext(Dispatchers.Main){
                        view.changeUserTitle(loginCathe?.nickname, loginCathe?.headShowImage)
                    }
                }
            }
            view.changeUserTitle(loginCathe?.nickname, loginCathe?.headShowImage)
        }
        //歌单

    }
    //Rv被点击了
    override fun rvItemClicked(v: View, position: Int) {
        val intent=Intent(context,FavoritesActivity::class.java)
        //这是收藏夹内的跳转
        //由于收藏夹不知道具体用户的歌单id只能根据用户的登陆id进行处理
        var x=playListResult?.playlist?.get(position)
        x?.apply {
            intent.putExtra("position",position)
            //创建者的头像
            intent.putExtra("avatarUrl",creator?.avatarUrl)
            //名称
            intent.putExtra("nickname",creator?.nickname)
            //name
            intent.putExtra("name",name)
            //描述
            intent.putExtra("description",description)
            //id
            intent.putExtra("playListId",id.toString())
            //是否利用本地缓存
            intent.putExtra("useLocalCathe",false)

            intent.putExtra("coverImgUrl",coverImgUrl)
        }
        //放入一个值判断是否是要先发送一个获取favorites的id
        intent.putExtra("is_send_user_id",true)

        view.favoritesClicked(v,position,intent)
        MyToast().sendToast(mContext,"${position}被点击",Toast.LENGTH_SHORT)
    }
}