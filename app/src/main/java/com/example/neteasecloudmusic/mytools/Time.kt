package com.example.neteasecloudmusic.mytools

fun changMsIntoMinutesAndSecond(duration: Int):String{
    //秒数
    var seconds=duration/1000
    //分钟数目
    var minutes=seconds/60
    //显示的second余数
    var secondTail=seconds%60

    var realMinutes=if (minutes<10){
        "0$minutes"
    }else{
        minutes.toString()
    }

    var realSeconds=if (secondTail<10){
        "0$secondTail"
    }else{
        secondTail.toString()
    }
    return "$realMinutes:$realSeconds"
}
class Time{

}