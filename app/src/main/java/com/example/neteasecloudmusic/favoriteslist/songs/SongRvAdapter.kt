package com.example.neteasecloudmusic.favoriteslist.songs

import android.app.Service
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.FavoritesActivity
import com.example.neteasecloudmusic.favoriteslist.playListDetailsResult
import com.example.neteasecloudmusic.mytools.musicservice.ServiceSong
import kotlinx.android.synthetic.main.rv_song_first.view.*
import kotlinx.android.synthetic.main.rv_song_list_item.view.*
import java.net.ServerSocket
import kotlin.concurrent.fixedRateTimer

var songList: MutableList<Song> = mutableListOf()
private var netSongList:MutableList<ServiceSong> = mutableListOf()
class SongRvAdapter : RecyclerView.Adapter<SongRvAdapter.Holder>() {
    lateinit var view: View

    //必须初始化
    lateinit var listener: OnClickListener

    //var mPosition:Int = 0

    //内部类
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var songPosition:TextView?=null
        var view:View?=null
        var songName:TextView?=null
        var singerName:TextView?=null

        var favoritesImage:ImageView?=null
        var favoritesName:TextView?=null
        var creatorHeadShows:ImageView?=null
        var briefIntroduction:TextView?=null
        init {
            //不知道为啥貌似每一个布局都会进入
            //而且一旦出现空就会报错
            try {
                //歌词面板
                view=itemView
                songPosition = itemView.song_position
                singerName=itemView.singer_name
                songName=itemView.song_name
            }catch (e:Exception){
                Log.e("Holder", "未初始化异常 ", e)
                //首item面板
                favoritesImage=itemView.favorite_image
                favoritesName=itemView.favorite_name
                creatorHeadShows=itemView.song_list_head_show
                briefIntroduction=itemView.brief_introduction
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        view = when (viewType) {
            //歌曲面板
            MyViewType.SongView.ordinal -> LayoutInflater.from(parent.context).inflate(R.layout.rv_song_list_item, parent, false)
            //首页
            MyViewType.FirstView.ordinal -> LayoutInflater.from(parent.context).inflate(R.layout.rv_song_first, parent, false)
            //最后一页
            else -> LayoutInflater.from(parent.context).inflate(R.layout.rv_song_last, parent, false)
        }
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return if (FavoritesActivity.useLocalCathe){
            songList.size
        }else{
            netSongList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        //mPosition=position
        if (FavoritesActivity.useLocalCathe){
            return when (position) {
                0 -> MyViewType.FirstView.ordinal
                songList.size - 1 -> MyViewType.LastView.ordinal
                else -> MyViewType.SongView.ordinal
            }
        }else{
            return when(position){
                0 -> MyViewType.FirstView.ordinal
                netSongList.size - 1 -> MyViewType.LastView.ordinal
                else -> MyViewType.SongView.ordinal
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //本地加载
        if (FavoritesActivity.useLocalCathe){
            when (position) {
                //标题栏
                0 -> {
                    holder.apply {
                        view?.let { this.favoritesImage?.let { it1 -> Glide.with(it).load(playListDetailsResult.playlist.coverImgUrl).into(it1) } }
                        view?.let { creatorHeadShows?.let { it1 -> Glide.with(it).load(playListDetailsResult.playlist.creator.avatarUrl).into(it1) } }
                        favoritesName?.text  = playListDetailsResult.playlist.creator.nickname
                    }
                    holder.view?.setOnClickListener {
                        listener.titleClicked(view)
                    }
                }
                //最后的项目
                songList.size - 1 -> {
                    holder.view?.setOnClickListener {
                        listener.tailClicked(view)
                        Log.e("最后一个白板", "onBindViewHolder: ")
                    }
                }
                //song
                else -> {
                        holder.singerName!!.text   = songList[position].artist
                        holder.songName!!.text = songList[position].songName
                        holder.songPosition!!.text = position.toString()
                        holder.view!!.setOnClickListener{
                            listener.songClicked(view, position)
                        }
                }
            }
        }


        //网络加载
        else{
            when (position) {
                //标题栏
                0 -> {
                    holder.apply {
                        view?.let { this.favoritesImage?.let { it1 -> Glide.with(it).load(playListDetailsResult.playlist.coverImgUrl).into(it1) } }
                        view?.let { creatorHeadShows?.let { it1 -> Glide.with(it).load(playListDetailsResult.playlist.creator.avatarUrl).into(it1) } }
                        favoritesName?.text  = playListDetailsResult.playlist.creator.nickname
                    }
                    holder.view?.setOnClickListener {
                        listener.titleClicked(view)
                    }
                }
                //最后的项目
                netSongList.size - 1-> {
                    holder.view?.setOnClickListener {
                        listener.tailClicked(view)
                        Log.e("最后一个白板", "onBindViewHolder: ")
                    }
                }
                //song
                else -> {
                        holder.singerName!!.text   = netSongList[position].artist
                        holder.songName!!.text = netSongList[position].songName
                        holder.songPosition!!.text = position.toString()
                        holder.view!!.setOnClickListener{
                            listener.songClicked(view, position)
                        }
                    Log.e("TAG", "onBindViewHolder: ")
                }
            }
        }

    }

    fun setNetList(list: MutableList<ServiceSong>){
        netSongList.clear()
        netSongList.add(0,ServiceSong())
        for (x in list){
            netSongList.add(x)
        }
        netSongList.add(ServiceSong())
    }

    //设置本地的list
    fun setLocalList(list: MutableList<Song>) {
        //头是前面的head
        //尾是结束白框框
        //去除前后的Song()
        songList.clear()
        songList.add(0,Song())
        for (x in list){
            songList.add(x)
        }
        songList.add(Song())
    }

    //添加Click的接口回调
    fun setOnItemClickListener(mListener: OnClickListener) {
        this.listener = mListener
    }

    interface OnClickListener {
        fun songClicked(view: View, position: Int)
        fun titleClicked(view: View)
        fun tailClicked(view: View)
    }

    enum class MyViewType {
        FirstView, SongView, LastView
    }
}