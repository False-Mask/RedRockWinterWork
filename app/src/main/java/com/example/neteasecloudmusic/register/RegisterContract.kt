package com.example.neteasecloudmusic.register

interface RegisterContract {
    interface RegisterIModel{
        //发送短信
        fun sendCaptcha(phoneNumber: String): RegisterModel.CaptchaResult
        //判断手机号是否注册过
        fun judgeIsRegisted(phoneNumber: String): RegisterModel.JudgeIsRegisted
        //判断验证码是否正确
        fun checkCaptcha(phoneNumber: String, captchaNumber: String): RegisterModel.CheckCaptchaResult
        //注册
        fun register(phoneNumber: String, passwordText: String, captchaNumber: String, nicknameText: String): RegisterModel.RegisterResult

    }
    interface RegisterIView {
        //
        fun waitingForResult()
        //120秒冷却倒计时
        fun changeCaptchaButton()
        //发送一个toast(也就一行代码没什么说的)
        fun sendToast(s: Int)
        fun sendToast(s: String)
        //让progressbar转起来
        //免得用户以为死机了
        fun progressBarOn()
    }
    interface RegisterIPresenter {
        //判断是否注册过
        fun isRegisted(phoneNumber: String):Boolean
        /*
        captcha: 验证码

        phone : 手机号码

        password: 密码

        nickname: 昵称
         */
        //发送一个注册请求
        fun sendRegisterRequest(captcha:String,phone:String,password:String,nickname:String)
        //发送验证码
        fun sendCaptcha(phoneNumber:String): RegisterModel.CaptchaResult
        //当注册按钮被点击
        fun registerClicked(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String): Boolean
        //当发送验证码按钮被点击
        fun sendCaptchaClicked(phoneNumber: String)
    }
}