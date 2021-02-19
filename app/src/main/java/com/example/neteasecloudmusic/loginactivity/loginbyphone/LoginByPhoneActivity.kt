package com.example.neteasecloudmusic.loginactivity.loginbyphone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login_by_phone.*

class LoginByPhoneActivity : AppCompatActivity() ,ByPhoneContract.ByPhoneIView{

    var TAG="LoginByPhoneActivity"
    var presenter=ByPhonePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_phone)
        ActivityController.Static.addActivity(this)
        //返回
        back_image_phone.setOnClickListener{
            finish()
        }
        //注册账号
        register_user_account.setOnClickListener{
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        //用户登陆
        login_button.setOnClickListener{
            Log.d(TAG, "onCreate: ")

            //获取edit的手机和密码
            val phoneNumber=edit_phone_1.text.toString()
            val passwordText=edit_phone_2.text.toString()
            presenter.loginClicked(phoneNumber,passwordText)
        }

        //忘记密码
        forget_password.setOnClickListener{

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.Static.removeActivity(this)
    }

    override fun sendToast(toString: String) {
        MyToast().sendToast(this,toString,Toast.LENGTH_SHORT)
    }

    override fun progressOn() {
        login_progress_bar.apply {
            visibility=View.VISIBLE
            bringToFront()
        }

        //把控件移到上面 (貌似没起作用)
//        login_progress_bar.bringToFront()
        window.setFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun progressOff() {
        login_progress_bar.visibility=View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}