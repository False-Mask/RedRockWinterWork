package com.example.neteasecloudmusic.favoriteslist.songui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import kotlinx.android.synthetic.main.activity_song_ui.*
import kotlinx.android.synthetic.main.rv_song_list_item.*

class SongUiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_ui)
        //获取activity的数据
        var song=intent.extras?.get("song") as Song
        var position=intent.extras?.getInt("position")
        //初始化视图了
        initView(song,position)

        //设置点击事件的处理
        //返回键
        song_ui_back_icon.setOnClickListener{
            finish()
        }
        //  播放/暂停键
        play_pause_icon.setOnClickListener{

        }
        //下一首
        the_next_song_icon.setOnClickListener{

        }

        //上一首
        the_last_song_icon.setOnClickListener{

        }
    }

    private fun initView(song: Song, position: Int?) {
        //初始化text
        song_ui_song_name.text=song.songName
        song_ui_singer_name.text=song.artist
        //加载图片
        Glide.with(this).load(song.image).into(song_ui_song_image_view)
    }
}