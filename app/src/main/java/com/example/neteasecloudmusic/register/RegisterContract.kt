package com.example.neteasecloudmusic.register

interface RegisterContract {
    interface RegisterIModel{
        fun sendCaptcha(phoneNumber: String)

    }
    interface RegisterIView {
        fun waitingForResult()
    }
    interface RegisterIPresenter {
        fun isRegisted(phoneNumber: String):Boolean
        /*
        captcha: 验证码

        phone : 手机号码

        password: 密码

        nickname: 昵称
         */
        fun sendRegisterRequest(captcha:String,phone:String,password:String,nickname:String)
        fun sendCaptcha(phoneNumber:String)
    }
}