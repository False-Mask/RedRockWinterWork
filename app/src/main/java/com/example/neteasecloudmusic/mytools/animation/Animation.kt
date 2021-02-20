package com.example.neteasecloudmusic.mytools.animation

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.example.neteasecloudmusic.mytools.filedownload.imagePath

fun startRotate(){
    for (animation in animationList){
        val isStarted=animation.isStarted
        if (!isStarted){
            animation?.start()
            Log.e("Rotate", "start")
        }else{
            animation?.resume()
            Log.e("Rotate", "resume" + animationList.size
            )
        }
    }
}

fun pauseRotate(){
    for (x in animationList){
        x.pause()
    }
    Log.e("Rotate", "pause"+ viewList.size )
}

fun createRotate(view: View){
    if (view in viewList) return
    val x=ObjectAnimator.ofFloat(view,"rotation",0f,360f)
        x?.apply {
            duration=20000
            repeatCount=-1
            interpolator=LinearInterpolator()
        }
    animationList.add(x)
    viewList.add(view)

    Log.e("Rotate", "Created")
}
fun removeRotate(view:View){
    val x= viewList.indexOf(view)
    viewList.remove(view)
    animationList[x]?.cancel()
    animationList.removeAt(x)
    Log.e("Rotate", "Removed" )
}

private var animationList= mutableListOf<ObjectAnimator>()
private var viewList = mutableListOf<View>()
object Animation {

}