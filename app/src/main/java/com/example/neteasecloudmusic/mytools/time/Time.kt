package com.example.neteasecloudmusic.mytools.time

fun changMsIntoMinutesAndSecond(duration: Int):String{
    //秒数
    val seconds=duration/1000
    //分钟数目
    val minutes=seconds/60
    //显示的second余数
    val secondTail=seconds%60

    val realMinutes=if (minutes<10){
        "0$minutes"
    }else{
        minutes.toString()
    }

    val realSeconds=if (secondTail<10){
        "0$secondTail"
    }else{
        secondTail.toString()
    }
    return "$realMinutes:$realSeconds"
}
object Time{

}