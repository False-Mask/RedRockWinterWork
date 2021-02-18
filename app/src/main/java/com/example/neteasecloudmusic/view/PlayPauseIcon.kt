package com.example.neteasecloudmusic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.example.neteasecloudmusic.R
import kotlin.math.sqrt

class PlayPauseIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var click: Click? = null

    //状态
    var status=PlayStatus.Idle
    private val array=context.obtainStyledAttributes(attrs,R.styleable.PlayPauseIcon)

    //view 宽高中最小的一个
    var layoutMin=0
    //view的最外圈半径的长度
    var r=0

    //中心位置的坐标
    var centerX=0f
    var centerY=0f

    //loading的时候的旋转角度
    var angle=0f

    //播放的进度条的百分比
    var progressPercent=0f


///////////////////////////////
    //获取外部圆形的大小 默认2dp
    private val outerCircleSize=array.getDimensionPixelSize(R.styleable.PlayPauseIcon_outer_circle_size,
        changDipIntoFloat(4f).toInt()
    )
    //获取外部圆形的颜色 默认颜色是白色
    private val outerCircleColor=array.getColor(R.styleable.PlayPauseIcon_outer_circle_color,resources.getColor(R.color.default_outer_circle_color))
    private val outerPaint=Paint()

////////////////////////////////
    //外部正在加载的时候的宽度
    private val loadingCircleSize=array.getDimensionPixelSize(R.styleable.PlayPauseIcon_loading_circle_size,
        changDipIntoFloat(4f).toInt()
    )

    //外部加载时候的颜色
    private val loadingCircleColor=array.getColor(R.styleable.PlayPauseIcon_loading_circle_color,resources.getColor(R.color.default_loading_circle_color))
    private val loadingPaint=Paint()


//////////////////////////////////
    //播放过程中bar的size 默认4dp
    private val playBarSize=array.getDimensionPixelSize(R.styleable.PlayPauseIcon_play_bar_size,
        changDipIntoFloat(4f).toInt()
    )
    //播放过程中的bar的颜色 默认为白色
    private val playBarColor=array.getColor(R.styleable.PlayPauseIcon_play_bar_color,resources.getColor(R.color.default_play_bar_color))
    private val playBarPaint=Paint()


    //暂停时候的颜色
    private val pauseIconColor=array.getColor(R.styleable.PlayPauseIcon_pause_icon_color,resources.getColor(R.color.default_pause_icon_color))
    private val pausePaint=Paint()


    //播放键的颜色
    private val playIconColor=array.getColor(R.styleable.PlayPauseIcon_play_icon_color, resources.getColor(R.color.default_play_icon_color))
    private val playPaint=Paint()

    init {
        outerPaint.apply {
            isAntiAlias=true
            style=Paint.Style.STROKE
            strokeWidth= outerCircleSize.toFloat()
            color=  outerCircleColor
        }

        loadingPaint.apply {
            isAntiAlias=true
            style=Paint.Style.STROKE
            strokeWidth= loadingCircleSize.toFloat()
            color= loadingCircleColor
            strokeCap= Paint.Cap.ROUND
        }

        playBarPaint.apply {
            isAntiAlias=true
            style=Paint.Style.STROKE
            strokeWidth= playBarSize.toFloat()
            color = playBarColor
            strokeCap= Paint.Cap.ROUND
        }

        playPaint.apply {
            isAntiAlias=true
            color=playIconColor
        }

        pausePaint.apply {
            isAntiAlias=true
            color=pauseIconColor
            strokeCap=Paint.Cap.ROUND
        }
    }

    private fun changDipIntoFloat(x:Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,x,resources.displayMetrics)
    }

    companion object{
        const val TAG="PlayPauseIcon"
    }

    //开始绘图
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Log.e(TAG, "width=$width   height=$height" )
        //绘制最外层的圆形
        //drawOuterCircle(canvas)

        //绘制内部的暂停按钮
        //drawInnerPauseIcon(canvas)
        //绘制内部的播放按钮
        //drawInnerPlayIcon(canvas)

        //外部的加载
        //drawLoadingArc(canvas)
       Log.e(TAG, "Invalidating~~$angle")

        //drawPlayArcBar(canvas)

        when(status){
            PlayStatus.Idle->{
                drawOuterCircle(canvas)
                drawInnerPauseIcon(canvas)
            }
            PlayStatus.Loading->{
                drawLoadingArc(canvas)
                drawInnerPlayIcon(canvas)
            }
            PlayStatus.Playing->{
                drawOuterCircle(canvas)
                drawPlayArcBar(canvas)
                drawInnerPlayIcon(canvas)
            }
            PlayStatus.Pausing->{
                drawOuterCircle(canvas)
                drawPlayArcBar(canvas)
                drawInnerPauseIcon(canvas)
            }
            else->{

            }
        }


        invalidate()
    }

    //绘制正在播放时候的进度条
    private fun drawPlayArcBar(canvas: Canvas?) {
        canvas?.drawArc(centerX-r+loadingCircleSize/4f*3f,centerY-r+loadingCircleSize/4f*3f,centerX+r-loadingCircleSize/4f*3f,centerY+r-loadingCircleSize/4f*3f,-90f,360*progressPercent,false,playBarPaint)
    }

   //画内布的加载视图
    private fun drawLoadingArc(canvas: Canvas?) {
        canvas?.drawArc(centerX-r,centerY-r,centerX+r,centerY+r,
            (-90f+angle),-250f,false,loadingPaint)
    }

    //内部的播放按钮
    private fun drawInnerPlayIcon(canvas: Canvas?) {
        pausePaint.strokeWidth= (r/5.0).toFloat()

        canvas?.drawLine((centerX-r/4.0).toFloat(),(centerY+ sqrt(3f)/5.0*r).toFloat(),(centerX-r/4.0).toFloat(),(centerY- sqrt(3f)/5.0*r).toFloat(),pausePaint)
        canvas?.drawLine((centerX+r/4.0).toFloat(),(centerY+ sqrt(3f)/5.0*r).toFloat(),(centerX+r/4.0).toFloat(),(centerY- sqrt(3f)/5.0*r).toFloat(),pausePaint)
    }

    //画内部的暂停按钮
    private fun drawInnerPauseIcon(canvas: Canvas?) {
        //由于是三角形得用path来画
        val path=Path()
        //移动到左上的点
        path.moveTo((centerX-r/4.0).toFloat(), (centerY+ sqrt(3f)/4.0*r).toFloat())
        //左下
        path.lineTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f)/4.0*r).toFloat())
        //右边
        path.lineTo((centerX+r/2.0).toFloat(), centerY)
        path.close()

        //画
        canvas?.drawPath(path,playPaint)
    }

    private fun drawOuterCircle(canvas: Canvas?) {
        //画圆的时候减去一个圆的宽 免得超出界限
        canvas?.drawCircle((width/2.0).toFloat(),
            (height/2.0).toFloat(), r.toFloat(),outerPaint)
    }

    //测量View的宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode=MeasureSpec.getMode(widthMeasureSpec)
        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        val width=MeasureSpec.getSize(widthMeasureSpec)
        val height=MeasureSpec.getSize(heightMeasureSpec)

        centerX= (width/2.0).toFloat()
        centerY=(height/2.0).toFloat()

        //当有一个是wrap_content的时候
        if (widthMode==MeasureSpec.AT_MOST || heightMode==MeasureSpec.AT_MOST){
            layoutMin=0
        }


        //当都是match_parent或者xdp的时候
        else{
            //确定长宽的最小值(毕竟要画圆形的)
            layoutMin= width.coerceAtMost(height)
            r=layoutMin/2-outerCircleSize
        }
        //尺寸出来了
        setMeasuredDimension(layoutMin,layoutMin)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{

            }
            MotionEvent.ACTION_UP->{
                click?.onPlayPauseViewClick(this)
            }
            MotionEvent.ACTION_MOVE->{

            }else->{

            }
        }
        return true
    }

    fun addOnViewClickListener(click:Click){
        this.click=click
    }

    interface Click{
        fun onPlayPauseViewClick(v:View)
    }


    //枚举类定义 状态的class
    //闲置 正在加载 正在播放 暂停
   enum class PlayStatus {
        Idle,Loading,Playing,Pausing
   }
}