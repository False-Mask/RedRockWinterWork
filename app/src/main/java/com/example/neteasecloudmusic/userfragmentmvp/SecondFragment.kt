package com.example.neteasecloudmusic.userfragmentmvp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.LoginActivity
import kotlinx.android.synthetic.main.second_fragment_layout.*

class SecondFragment : Fragment() {
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
}
