package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_song_ui.*

class SearchActivity : AppCompatActivity(),SearchContract.SearchIView,IServiceBindView{

   companion object{
       const val TAG="SearchActivity"
   }

    val presenter=SearchPresenter(this)
    private val connection=presenter as ServiceConnection

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        search_text_sa.apply {
            isFocusable=true
            isFocusableInTouchMode=true
            requestFocus()
            imeOptions= EditorInfo.IME_ACTION_SEARCH
            val inputManager= com.example.neteasecloudmusic.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(search_text_sa,0)
        }

        val mAdapter=SearchRvAdapter()
        button_1.setOnClickListener{
            val keyboard=search_text_sa.text.toString()
            search_rv.adapter= mAdapter
            search_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

            presenter.beginSearch(keyboard,mAdapter)
        }


    }

//断开连接
override fun onPause() {
        super.onPause()
        reMoveView(this)
        unbindService(connection)
}

    override fun onStart() {
        super.onStart()
        addView(this)
        val intent=Intent(this,MyMusicService::class.java)
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }


    override fun serviceRefresh(
        songName: String,
        singer: String,
        imageUrl: String,
        duration: Int,
        currentTime: Int,
        songId: String
    ) {
            Glide.with(this).load(imageUrl).apply(RequestOptions.bitmapTransform(CircleCrop())).into(search_song_image)
        search_song_text.text=songName
        if (getIsPlaying()){
            search_play_or_pause.setImageResource(R.drawable.play)
        }else{
            iconChangeToPause()
        }
    }

    override fun setMusicMaxProgress(duration: Int) {

    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun iconChangeToPause() {
        search_play_or_pause.setImageResource(R.drawable.pause)
    }

    override fun setBufferedProgress(percent: Int) {

    }
}