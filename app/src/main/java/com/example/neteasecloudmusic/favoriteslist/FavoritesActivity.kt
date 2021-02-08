package com.example.neteasecloudmusic.favoriteslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R

class FavoritesActivity : AppCompatActivity(),FavoritesContract.FavoritesIView{
    var presenter=FavoritesPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        //初始化
        //发送网络请求啥的啊(获取点击的position)
        var position=intent.extras?.getInt("position")
        initView(position)
    }

    private fun initView(position: Int?) {
        presenter.getSongs(position)
    }

}