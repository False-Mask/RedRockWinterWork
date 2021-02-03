package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.LoginActivity
import kotlinx.android.synthetic.main.second_fragment_layout.*

class UserFragment : Fragment() ,UserContract.UserIView{
    var Presenter=UserPresenter(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.second_fragment_layout,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user_head_show_icon.setOnClickListener {
            //测试
            //Toast.makeText(context, "你点击了头像", Toast.LENGTH_SHORT).show()
            var intent=Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initIconAndName(icon: String?, name: String?) {
        //更改用户的头像
        Glide.with(this).load(icon).into(user_head_show_icon)
        //更改用户的名称
        user_name.text=name
    }
}
