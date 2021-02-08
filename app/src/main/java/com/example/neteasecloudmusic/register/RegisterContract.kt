package com.example.neteasecloudmusic.register

interface RegisterContract {
    interface RegisterIModel{
        //发送短信
        fun sendCaptcha(phoneNumber: String): Pair<String, String>
        //判断手机号是否注册过
        fun judgeIsRegisted(phoneNumber: String): Pair<String, String>
        //判断验证码是否正确
        fun checkCaptcha(phoneNumber: String, captchaNumber: String): Pair<String, String>
        //注册
        fun register(phoneNumber: String, passwordText: String, captchaNumber: String, nicknameText: String): Pair<String, String>

    }
    interface RegisterIView {
        //120秒冷却倒计时
        fun changeCaptchaButton()
        //发送一个toast(也就一行代码没什么说的)
        fun sendToast(s: Int)
        fun sendToast(s: String)
        //让progressbar转起来
        //免得用户以为死机了
        fun progressBarOn()
        fun progressBarOff()
    }
    interface RegisterIPresenter {
        //判断是否注册过
        /*
        captcha: 验证码

        phone : 手机号码

        password: 密码

        nickname: 昵称
         */
        //发送一个注册请求
        //发送验证码
        fun sendCaptcha(phoneNumber:String)
        //当注册按钮被点击
        fun registerClicked(nicknameText: String, phoneNumber: String, passwordText: String, captchaNumber: String): Boolean
        //当发送验证码按钮被点击
        fun sendCaptchaClicked(phoneNumber: String)
    }
}