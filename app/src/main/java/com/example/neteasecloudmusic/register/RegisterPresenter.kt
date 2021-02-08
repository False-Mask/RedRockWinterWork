package com.example.neteasecloudmusic.register

import android.util.Log
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ActivityController
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendPostRequest
import com.google.gson.Gson
import kotlinx.coroutines.*

class RegisterPresenter(registerActivity: RegisterActivity) :RegisterContract.RegisterIPresenter{
    val TAG="RegisterPresenter"
    var view=registerActivity
    var model=RegisterModel()

    //记录是否调用冷却(发送验证码后120秒冷却)
    var flag=true
    //记录冷却状态下用户的点击次数
    var clickTimes=-1

    //注册按钮被点击
    override fun registerClicked(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String): Boolean {
//        if (nicknameText.isNotEmpty()){
//            if (phoneNumber.length==11){
//                if (passwordText.isNotEmpty()){
//                    if (!judgeIsRegisted(phoneNumber)){
//                        if (checkCaptcha(phoneNumber,captchaNumber)){
//                            view.progressBarOn()
//                            view.sendToast("正在注册请稍后")
//                            //注册
//                            register(nicknameText,phoneNumber,passwordText,captchaNumber)
//
//                        }else{
//                            view.sendToast("验证码错误")
//                        }
//                    }
//                    else{
//                        view.sendToast("用户已经被注册")
//                    }
//                }
//                else{
//                    view.sendToast("密码不能为空")
//                }
//            }else{
//                view.sendToast("电话号码格式非法")
//            }
//        }
//        else{
//            view.sendToast("名称不能为空")
//        }
        if (nicknameText.isNotEmpty()){
            if (phoneNumber.isNotEmpty()&&phoneNumber.length==11){
                if (passwordText.isNotEmpty()){
                    if (captchaNumber.isNotEmpty()){
                        view.progressBarOn()
                        view.sendToast("正在注册请稍后")
                        //注册
                        register(nicknameText,phoneNumber,passwordText,captchaNumber)
                    }
                }
            }else if (phoneNumber.isEmpty()){
                view.sendToast("电话号码不能为空")
            }else{
                view.sendToast("电话号码格式非法")
            }
        }else {
            view.sendToast("名称不能为空")
        }
        return false
    }

//发送验证码按钮被点击
    override fun sendCaptchaClicked(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()&& phoneNumber.length==11){
            if (flag){
                flag=false
                view.sendToast(R.string.send_captcha_successful)
                //丢锅给sendCaptcha(..)处理
                sendCaptcha(phoneNumber)
                view.changeCaptchaButton()
                if (captchaResult.message == "发送验证码超过限制:每个手机号一天只能发5条验证码"){
                    view.sendToast("发送验证码超过限制:每个手机号一天只能发5条验证码")
                }
                Log.d("TAG", "sendCaptchaClicked: "+captchaResult.message)
            }
        }else if(!flag){
            view.sendToast(R.string.register_busy)
        }else if (flag && phoneNumber.isEmpty()){
            view.sendToast(R.string.register_number_empty)
        }else{
            view.sendToast(R.string.register_number_length_wrong)
        }
    }

    //发送验证码
    override fun sendCaptcha(phoneNumber: String){
        var (url,tail)=model.sendCaptcha(phoneNumber)
        //不卡线程
        netThread.launch (Dispatchers.IO){
            try {
                var respondBody=sendPostRequest(url,tail)
                captchaResult=Gson().fromJson(respondBody,RegisterModel.CaptchaResult::class.java)
                Log.d(TAG, "验证码发送完毕")
            }catch (e:Exception){ }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //处理注册逻辑
    private fun register(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String) {
        //注册所需网址
        var (url1,tail1)=model.register(phoneNumber,passwordText,captchaNumber,nicknameText)
        //注册所需的验证验证码
        var (url2,tail2)=model.checkCaptcha(phoneNumber, captchaNumber)
        //注册所需检验是否被注册
        var (url3,tail3)=model.judgeIsRegisted(phoneNumber)

        //发送请求
        netThread.launch (Dispatchers.IO){
            //判断手机是否被注册
            var respondBody1:Deferred<String>?=null
            var respondBody2:Deferred<String>?=null
            //并发2条请求
            try {
                respondBody1=async {  sendPostRequest(url1,tail1)}
            }catch (e:Exception){}
            //判断验证码是否正确
            try {
                respondBody2=async {  sendPostRequest(url2,tail2)}
            }catch (e:Exception){}
            //解析数据
            judgeIsRegisted=Gson().fromJson(respondBody1?.await(),RegisterModel.JudgeIsRegisted::class.java)
            checkCaptchaResult= Gson().fromJson(respondBody2?.await(),RegisterModel.CheckCaptchaResult::class.java)
            Log.d(TAG, "register: $checkCaptchaResult")
            Log.d(TAG, "register: $judgeIsRegisted")
            //exist==1表示已经注册
            if (judgeIsRegisted.code==1){
                view.sendToast("该手机号已经被注册")
            }else{
                if (checkCaptchaResult.message!="验证码错误"){
                    //没被注册而且 验证码正确 发送
                    try {
                        var respondBody3=sendPostRequest(url3,tail3)
                        Log.d(TAG, "register: 3"+respondBody3)
                        registerResult=Gson().fromJson(respondBody3,RegisterModel.RegisterResult::class.java)
                        //注册完成 切线程 溜了
                        //这里得加个if判断
                        withContext(Dispatchers.Main){
                            view.progressBarOff()
                            view.sendToast("注册完成")
                            //退出了
                            ActivityController.Static.finishAll()
                        }
                    }catch (e:Exception){ }
                }else{
                    view.sendToast("验证码错误")
                }
            }
        }
    }

}