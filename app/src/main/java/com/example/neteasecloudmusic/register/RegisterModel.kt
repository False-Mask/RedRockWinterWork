package com.example.neteasecloudmusic.register

import android.util.Log
import com.example.neteasecloudmusic.mytools.net.SendNetRequest
import com.example.neteasecloudmusic.mytools.net.SendNetRequest.Back
import com.google.gson.Gson

class RegisterModel :RegisterContract.RegisterIModel{
    var TAG="RegisterModel"
    var captcha=CaptchaResult()
    //主机名 除去了末尾的 "/"
    val baseUrl="http://sandyz.ink:3000"
    //网络请求工具类
    var sendNetRequest= SendNetRequest()

    override fun sendCaptcha(phoneNumber: String) {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        //发送验证码的接口地址
        var string:String="/captcha/sent"
        //发送网络请求

        sendNetRequest.sendPostRequest("${baseUrl}${string}","phone=${phoneNumber}",object :Back{
            override fun onResponded(resultBody: String) {
                Log.d(TAG, "sendCaptcha "+"onResponded: ")
                var gson=Gson()
                captcha=gson.fromJson<CaptchaResult>(resultBody,CaptchaResult::class.java)
                Log.d(TAG, "onResponded: \n"+captcha.toString())
            }

            override fun onFailed(respondCode: Int) {
                Log.d(TAG, "sendCaptcha "+"onFailed: ")
            }

        })
    }

    fun sendRegister(
        captcha: String,
        phone: String,
        password: String,
        nickname: String
    ) {

    }

    //发送短信的返回结果
    class CaptchaResult{
        var code:Int = 0
        var message:String = ""
        var data:Boolean = false
        override fun toString(): String {
            return "CaptchaResult(code=$code, message='$message', data=$data)"
        }

    }
}