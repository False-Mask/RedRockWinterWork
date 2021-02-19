package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.view.PlayPauseBar
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(),SearchContract.SearchIView,IServiceBindView{

   companion object{
       const val TAG="SearchActivity"
   }
    private var animator: ObjectAnimator?=null
    val presenter=SearchPresenter(this)
    private val connection=presenter as ServiceConnection

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_play_or_pause.addOnViewClickListener(presenter)

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
        reMovePre(presenter)
        unbindService(connection)
}

    override fun onStart() {
        super.onStart()
        addView(this)
        addPresenter(presenter)
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
            Glide.with(this).load(imageUrl)
                    .error(R.drawable.music_place_holder)
                    .placeholder(R.drawable.music_place_holder).apply(RequestOptions.bitmapTransform(CircleCrop())).into(search_song_image)
        search_song_text.text=songName
        val status=search_play_or_pause.status
        if (status==PlayPauseBar.PlayStatus.Playing){
            if (getCurrentPosition()<=0){
                start()
            }else{
                resume(currentTime.toFloat()/duration)
            }

        }else if (status==PlayPauseBar.PlayStatus.Pausing){
            iconChangeToPause()
        }
    }

    override fun setMusicMaxProgress(duration: Int) {

    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun iconChangeToPause() {
        search_play_or_pause.status=PlayPauseBar.PlayStatus.Pausing
    }

    override fun setBufferedProgress(percent: Int) {

    }

    override fun resume(percent: Float) {
        search_play_or_pause.status=PlayPauseBar.PlayStatus.Playing
        search_play_or_pause.progressPercent=percent
    }

    override fun loading() {
        search_play_or_pause.status=PlayPauseBar.PlayStatus.Loading
        animator=ObjectAnimator.ofFloat(search_play_or_pause,"angle",0f,360f)
        animator?.duration=1000
        animator?.repeatCount=-1
        animator?.interpolator=LinearInterpolator()
        animator?.start()
    }

    override fun start() {
        animator?.cancel()
        search_play_or_pause.status=PlayPauseBar.PlayStatus.Playing
    }
}