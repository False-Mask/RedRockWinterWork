package com.example.neteasecloudmusic.bannerwebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.neteasecloudmusic.R
import kotlinx.android.synthetic.main.activity_banner_web.*

class BannerWebActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_web)
        //获取url
        //val bannerData=intent.extras?.get("bannerData") as BannerData
        my_banner_web_view.settings.apply {
            //支持javascript
            javaScriptEnabled=true
            //自动加载图片
            loadsImagesAutomatically=true
            //缩放至屏幕大小
            loadWithOverviewMode=true
        }
        my_banner_web_view.webViewClient= WebViewClient()
        //my_banner_web_view.loadUrl(bannerData.url)
    }
}