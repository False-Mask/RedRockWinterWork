package com.example.neteasecloudmusic.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),RegisterContract.RegisterIView{
    var flag=true
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
            var phoneNumber=phone.text.toString()
            //点击button发送验证码之后冷却120秒
            if (phoneNumber.isNotEmpty()&& phoneNumber.length==11){
                if (flag){
                    flag=false
                    MyToast().sendToast(this,R.string.send_captcha_successful,Toast.LENGTH_SHORT)

                    //交给Presenter处理
                    registerPresenter.sendCaptcha(phoneNumber)

                    registerPresenter.sendCaptcha(phoneNumber)
                    Thread{
                        for (i in 120 downTo 0){
                            Thread.sleep(1000)
                            runOnUiThread{
                                send_captcha_button.text= i.toString()
                            }
                        }
                        runOnUiThread{
                            flag=true
                            send_captcha_button.text=resources.getString(R.string.button_send_captcha)
                        }
                    }.start()
                }
            }else if(!flag){
                MyToast().sendToast(this,R.string.register_busy,Toast.LENGTH_SHORT)
            }else if (flag && phoneNumber.isEmpty()){
                MyToast().sendToast(this,R.string.register_number_empty,Toast.LENGTH_SHORT)
            }else{
                MyToast().sendToast(this,R.string.register_number_length_wrong,Toast.LENGTH_SHORT)
            }
        }



        //注册
        register_button.setOnClickListener{

        }

    }

    override fun waitingForResult() {

    }
}