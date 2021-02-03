package com.example.neteasecloudmusic.register

import android.util.Log
import android.widget.Toast
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.log

class RegisterPresenter(registerActivity: RegisterActivity) :RegisterContract.RegisterIPresenter{

    var view=registerActivity
    var model=RegisterModel()

    //记录是否调用冷却(发送验证码后120秒冷却)
    var flag=true
    //记录冷却状态下用户的点击次数
    var clickTimes=-1

    override fun isRegisted(phoneNumber: String): Boolean {
            return false
    }

    override fun sendRegisterRequest(
        captcha: String,
        phone: String,
        password: String,
        nickname: String
    ) {
    }

    override fun sendCaptcha(phoneNumber: String): RegisterModel.CaptchaResult {
        return model.sendCaptcha(phoneNumber)
    }

    //接受到按钮点击处理
    override fun registerClicked(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String): Boolean {
        if (nicknameText.isNotEmpty()){
            if (phoneNumber.length==11){
                if (passwordText.isNotEmpty()){
                    if (!judgeIsRegisted(phoneNumber)){
                        if (checkCaptcha(phoneNumber,captchaNumber)){
                            view.progressBarOn()
                            view.sendToast("正在注册请稍后")
                            //注册
                            register(nicknameText,phoneNumber,passwordText,captchaNumber)

                        }else{
                            view.sendToast("验证码错误")
                        }
                    }
                    else{
                        view.sendToast("用户已经被注册")
                    }
                }
                else{
                    view.sendToast("密码不能为空")
                }
            }else{
                view.sendToast("电话号码格式非法")
            }
        }
        else{
            view.sendToast("名称不能为空")
        }
        return false
    }

    override fun sendCaptchaClicked(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()&& phoneNumber.length==11){
            if (flag){
                flag=false
                view.sendToast(R.string.send_captcha_successful)
                //丢锅给sendCaptcha(..)处理
                sendCaptcha(phoneNumber)
                view.changeCaptchaButton()
                if (model.captchaResult.message == "发送验证码超过限制:每个手机号一天只能发5条验证码"){
                    view.sendToast("发送验证码超过限制:每个手机号一天只能发5条验证码")
                }
                Log.d("TAG", "sendCaptchaClicked: "+model.captchaResult.message)
            }
        }else if(!flag){
            view.sendToast(R.string.register_busy)
        }else if (flag && phoneNumber.isEmpty()){
            view.sendToast(R.string.register_number_empty)
        }else{
            view.sendToast(R.string.register_number_length_wrong)
        }
    }

    private fun register(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String) {
            model.register(phoneNumber,passwordText,captchaNumber,nicknameText)
    }

    private fun judgeIsRegisted(phoneNumber: String):Boolean{
        if (model.judgeIsRegisted(phoneNumber).exist==-1) {
            return false
        }
        return true
    }

    private fun checkCaptcha(phoneNumber: String, captchaNumber: String):Boolean {
        var result=model.checkCaptcha(phoneNumber,captchaNumber)
        return result.data
    }

}