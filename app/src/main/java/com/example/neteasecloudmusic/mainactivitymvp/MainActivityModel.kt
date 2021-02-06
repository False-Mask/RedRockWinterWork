package com.example.neteasecloudmusic.mainactivitymvp

import android.content.Context
import android.util.Log
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel
import com.example.neteasecloudmusic.mytools.net.SendNetRequest
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//sp
class MainActivityModel : MainActivityContract.MainActivityIModel{
    //数据对象
    var loginResult = ByPhoneModel.LoginResult()

    val baseUrl = "http://sandyz.ink:3000"

    val TAG="MainActivityModel"

    val spName=UserFragment.USER_BASIC_SP_NAME
    val sp=MyApplication.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE)
    var sendNetRequest=SendNetRequest()
    override fun loginAuto() {
        var isLogin=sp.getBoolean("is_login",false)
        if (isLogin){
            var phoneNumber=sp.getString("user_phone_number","NULL")
            var password=sp.getString("user_password","NULL")
            login(phoneNumber!!,password!!)
        }
    }

    //发送登陆请求
    fun login(phoneNumber: String, passwordText: String){
        //http请求貌似不太安全(配了啥我也不懂)
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        var string: String = "/login/cellphone"
        sendNetRequest.sendGetRequest("$baseUrl$string?phone=$phoneNumber&password=$passwordText"
                , object : SendNetRequest.Back {
            override fun onResponded(resultBody: String) {
                Log.d(TAG, "login onResponded: ")
                var gson = Gson()
                var type=object : TypeToken<ByPhoneModel.LoginResult>(){}.type
                loginResult = gson.fromJson(resultBody, type)
                Log.d(TAG, "onResponded: ")

                if (loginResult.code==200){
                    Log.d(TAG, "自动登陆成功"+Thread.currentThread().name)
                    //Looper.prepare()
                        //MyToast().sendToast(MyApplication.getContext(),"自动登陆成功",Toast.LENGTH_SHORT)
                }else{
                    Log.d(TAG, "自动登陆失败"+Thread.currentThread().name+loginResult.code)
                    //MyToast().sendToast(MyApplication.getContext(),"自动登陆失败",Toast.LENGTH_SHORT)
                }
            }

            override fun onFailed(respondCode: Int) {
                Log.d(TAG, "login onFailed: ")
            }

        })
    }


}