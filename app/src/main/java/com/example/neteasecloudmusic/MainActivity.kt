package com.example.neteasecloudmusic
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.neteasecloudmusic.mytools.myretrofit.FastJsonData
import com.example.neteasecloudmusic.mytools.net.SendNetRequest
import com.example.neteasecloudmusic.mytools.myretrofit.getsData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() ,IView{
    
    var TAG="MainActivity"
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun changeUserIcon(view: View) {
        var manager=supportFragmentManager
        var transaction=manager.beginTransaction()
        //初始化
        findViewById<TextView>(R.id.user_text).setTextColor(Color.BLACK)
        findViewById<TextView>(R.id.music_text).setTextColor(Color.BLACK)
        findViewById<ImageView>(R.id.bottom_user).setImageResource(R.drawable.user_icon_1)
        findViewById<ImageView>(R.id.bottom_music).setImageResource(R.drawable.music_icon_1)

        //变色
        when(view.id){
            R.id.user_text,R.id.bottom_user-> {
                findViewById<TextView>(R.id.user_text).setTextColor(resources.getColor(R.color.bottom_text_color))
                findViewById<ImageView>(R.id.bottom_user).setImageResource(R.drawable.user_icon_2)
//                transaction.add(R.id.frameLayout,SecondFragment())
                transaction.replace(R.id.frameLayout,SecondFragment())
                transaction.commit()
            }
            R.id.bottom_music,R.id.music_text->{
                findViewById<ImageView>(R.id.bottom_music).setImageResource(R.drawable.music_icon_2)
                findViewById<TextView>(R.id.music_text).setTextColor(resources.getColor(R.color.bottom_text_color))
                transaction.replace(R.id.frameLayout,FirstFragment())
                transaction.commit()
            }

        }
    }
}