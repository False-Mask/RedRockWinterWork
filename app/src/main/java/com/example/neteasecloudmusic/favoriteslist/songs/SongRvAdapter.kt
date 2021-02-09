package com.example.neteasecloudmusic.favoriteslist.songs


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.recyclerview.favorites.list
import kotlinx.android.synthetic.main.rv_song_list_item.view.*

var songList: MutableList<Song> = mutableListOf()
class SongRvAdapter : RecyclerView.Adapter<SongRvAdapter.Holder>() {
    private lateinit var view:View
    //必须初始化
    lateinit var listener:OnClickListener

    //内部类
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var view:View
        lateinit var songPosition:TextView
        lateinit var songName:TextView
        lateinit var singerName:TextView


        init {
            try {
                view=itemView
                songPosition=itemView?.song_position
                songName=itemView?.song_name
                singerName=itemView?.singer_name
            }catch (e:Exception){
                Log.e("RV Holder", ": (多半是空指针异常)",e )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        view=when(viewType){
            MyViewType.SongView.ordinal->LayoutInflater.from(parent.context).inflate(R.layout.rv_song_list_item,parent,false)
            MyViewType.FirstView.ordinal->LayoutInflater.from(parent.context).inflate(R.layout.rv_song_first,parent,false)
            else->LayoutInflater.from(parent.context).inflate(R.layout.rv_song_last,parent,false)
        }
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun getItemViewType(position: Int): Int {
       return when(position){
           0-> MyViewType.FirstView.ordinal
           list.size-1->MyViewType.LastView.ordinal
           else->MyViewType.SongView.ordinal
       }
    }

    override fun onBindViewHolder(holder: SongRvAdapter.Holder, position: Int) {
        //改变视图

        when(position){
            //标题栏
            0->{
                holder.view.setOnClickListener {
                    listener.titleClicked(view)
                }
            }
            //最后的项目
            songList.size-1 ->{
                holder.view?.setOnClickListener {
                    listener.tailClicked(view)
                    Log.e("最后一个白板", "onBindViewHolder: " )
                }
            }
            //song
            else->{
                    holder.singerName.text = songList[position].artist
                    holder.songName.text= songList[position].songName
                    holder.songPosition.text=position.toString()
                    holder.view.setOnClickListener{
                        listener.songClicked(view,position)
                    }
            }
        }
    }

    //
    fun setList(list: MutableList<Song>){
        //头是前面的head
        //尾是结束白框框
        list.add(0, Song())
        list.add(Song())
        songList=list
    }

    //添加Click的接口回调
    fun setOnItemClickListener(mListener: OnClickListener){
        this.listener=mListener
    }

    interface OnClickListener {
        fun songClicked(view:View,position: Int)
        fun titleClicked(view:View)
        fun tailClicked(view:View)
    }

    enum class MyViewType{
        FirstView,SongView,LastView
    }

}