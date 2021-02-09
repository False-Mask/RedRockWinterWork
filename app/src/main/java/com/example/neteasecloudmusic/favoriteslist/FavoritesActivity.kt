package com.example.neteasecloudmusic.favoriteslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter
import com.example.neteasecloudmusic.favoriteslist.songs.songList
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import com.example.neteasecloudmusic.mytools.filedownload.mContext
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.rv_song_first.*

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

    //初始化view视图
    private fun initView(position: Int?) {
        //设置recyclerview的基本属性
        var songRvAdapter=SongRvAdapter()
        //加入一个空的集合
        songRvAdapter.setList(mutableListOf())
        song_list_rv.adapter=songRvAdapter
        song_list_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
       // songRvAdapter.notifyDataSetChanged()
        presenter.getSongs(position,songRvAdapter)
        //设置recyclerview字item点击监听 presenter实现
        songRvAdapter.setOnItemClickListener(object : SongRvAdapter.OnClickListener{
            override fun songClicked(view: View, position: Int) {
                var intent= Intent(this@FavoritesActivity, SongUiActivity::class.java)
                if(position>=1&&position!= songList.size){
                    //放入点击的位置
                    intent.putExtra("position",position)
                    //放入一个对象主要是有关song的信息
                    intent.putExtra("song", songList[position])
                    //开了
                    startActivity(intent)
                }
            }

            override fun titleClicked(view: View) {

            }

            override fun tailClicked(view: View) {

            }

        })
    }

    //添加进度条
    override fun progressBarOn() {
        song_loading.visibility= View.VISIBLE
    }

    override fun progressBarOff() {
        song_loading.visibility=View.GONE
    }

}