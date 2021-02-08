package com.example.neteasecloudmusic.register

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),RegisterContract.RegisterIView{


    //发送验证码的放回数据(虽然好像基本上没怎么用)
    var result:RegisterModel.CaptchaResult= RegisterModel.CaptchaResult()
    //发送的参数
    var phoneNumber:String=""
    var passwordText:String=""
    var nicknameText:String=""
    var captchaNumber:String=""
    //Presenter
    var registerPresenter=RegisterPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //返回
        back_image_register.setOnClickListener{
            finish()
        }
        //发送验证码
        send_captcha_button.setOnClickListener{
            phoneNumber=phone.text.toString()
            registerPresenter.sendCaptchaClicked(phoneNumber)
        }



        //注册
        register_button.setOnClickListener{
            phoneNumber=phone.text.toString()
            passwordText=password.text.toString()
            captchaNumber=captcha.text.toString()
            nicknameText=nickname.text.toString()
            //丢锅给Presenter干
            registerPresenter.registerClicked(nicknameText,phoneNumber,passwordText,captchaNumber)
        }

    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun sendToast(s: Int) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun progressBarOn() {
        register_loading.visibility=View.VISIBLE
    }

    override fun progressBarOff() {
        register_loading.visibility=View.GONE

    }

    override fun changeCaptchaButton() {
        Thread{
            for (i in 120 downTo 0){
                Thread.sleep(1000)
                runOnUiThread{
                    send_captcha_button.text= i.toString()
                }
            }
            runOnUiThread{
                registerPresenter.flag=true
                send_captcha_button.text=resources.getString(R.string.button_send_captcha)
            }
        }.start()
    }
}