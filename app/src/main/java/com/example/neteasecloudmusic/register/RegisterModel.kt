package com.example.neteasecloudmusic.register

import com.example.neteasecloudmusic.mytools.net.SendNetRequest

//顶层声明
//数据
var captchaResult: RegisterModel.CaptchaResult = RegisterModel.CaptchaResult()
var judgeIsRegisted= RegisterModel.JudgeIsRegisted()
var checkCaptchaResult= RegisterModel.CheckCaptchaResult()
var registerResult= RegisterModel.RegisterResult()

class RegisterModel :RegisterContract.RegisterIModel{
    var TAG="RegisterModel"

    //主机名 除去了末尾的 "/"
    val baseUrl="http://sandyz.ink:3000"
    //网络请求工具类
    var sendNetRequest= SendNetRequest()

    //发送验证码
    override fun sendCaptcha(phoneNumber: String): Pair<String, String> {
        //http请求貌似不太安全(配了啥我也不懂)
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        //发送验证码的接口地址
        var string:String="/captcha/sent"
        //发送网络请求

//        sendNetRequest.sendPostRequest("${baseUrl}${string}","phone=${phoneNumber}",object :Back{
//            override fun onResponded(resultBody: String) {
//                Log.d(TAG, "sendCaptcha onResponded: ")
//                var gson=Gson()
//                captchaResult=gson.fromJson<CaptchaResult>(resultBody,CaptchaResult::class.java)
//                Log.d(TAG, "onResponded: \n"+captchaResult.toString())
//            }
//
//            override fun onFailed(respondCode: Int) {
//                Log.d(TAG, "sendCaptcha "+"onFailed: ")
//            }
//
//        })
//        return captchaResult

        Pair("${baseUrl}${string}","phone=${phoneNumber}")
        return Pair("${baseUrl}${string}","phone=${phoneNumber}")
    }

    //检验是否被注册
    override fun judgeIsRegisted(phoneNumber: String): Pair<String, String> {
//        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        var string:String="/cellphone/existence/check"
//        sendNetRequest.sendPostRequest("$baseUrl$string"
//                ,"phone=$phoneNumber"
//                ,object :Back{
//            override fun onResponded(resultBody: String) {
//                Log.d(TAG, "judgeIsRegisted onResponded: ")
//                var gson=Gson()
//                var judgeIsRegisted= gson.fromJson<JudgeIsRegisted>(resultBody
//                        ,JudgeIsRegisted::class.java)
//                Log.d(TAG, "onResponded: \n"+judgeIsRegisted.toString())
//
//            }
//
//            override fun onFailed(respondCode: Int) {
//                Log.d(TAG, "judgeIsRegisted onFailed: ")
//            }
//
//        })
        return Pair("$baseUrl$string","phone=$phoneNumber")
    }

    //检验验证码是否正确
    override fun checkCaptcha(phoneNumber: String, captchaNumber: String): Pair<String, String> {
//        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        var string:String="/captcha/verify"
//        sendNetRequest.sendPostRequest("$baseUrl$string","phone=$phoneNumber&captcha=$captchaNumber",object : Back{
//            override fun onResponded(resultBody: String) {
//                Log.d(TAG, "checkCaptcha onResponded: ")
//                var gson=Gson()
//                checkCaptchaResult=gson.fromJson<CheckCaptchaResult>(resultBody,CheckCaptchaResult::class.java)
//                Log.d(TAG, "onResponded: \n"+checkCaptchaResult.toString())
//            }
//
//            override fun onFailed(respondCode: Int) {
//                Log.d(TAG, "checkCaptcha onFailed: ")
//            }
//
//        })

        return Pair("$baseUrl$string","phone=$phoneNumber&captcha=$captchaNumber")
    }

    override fun register(phoneNumber: String, passwordText: String, captchaNumber: String, nicknameText: String): Pair<String, String> {



//        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        var string:String="/register/cellphone"
//        sendNetRequest.sendPostRequest("$baseUrl$string","phone=$phoneNumber" +
//                "&password=$passwordText&captcha=$captchaNumber&nickname=$nicknameText",object :Back{
//            override fun onResponded(resultBody: String) {
//                var gson=Gson()
//                registerResult=gson.fromJson<RegisterResult>(resultBody,RegisterResult::class.java)
//                Log.d(TAG, "register onResponded: \n"+registerResult.toString())
//                //
//            }
//
//            override fun onFailed(respondCode: Int) {
//                Log.d(TAG, "register onFailed: ")
//            }
//
//        })

        return Pair("$baseUrl$string","phone=$phoneNumber&password=$passwordText&captcha=$captchaNumber&nickname=$nicknameText")
    }


    //检测是否被注册的返回数据
    class JudgeIsRegisted{
        var exist:Int = 0
        var nickname:String = ""
        var hasPassword:Boolean = false
        var code:Int = 0
        override fun toString(): String {
            return "JudgeIsRegisted(exist=$exist, nickname='$nickname', hasPassword=$hasPassword, code=$code)"
        }
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
    //检测验证码的返回数据
    class CheckCaptchaResult {
        var message:String = ""
        var code:Int = 0
        var data:Boolean=false
        override fun toString(): String {
            return "CheckCaptchaResult(message='$message', code=$code, data=$data)"
        }
    }
    //注册返回的结果
    class RegisterResult{
        var msg:String = ""
        var code:Int = 0
        var message:String = ""
        override fun toString(): String {
            return "RegisterResult(msg='$msg', code=$code, message='$message')"
        }
    }
}