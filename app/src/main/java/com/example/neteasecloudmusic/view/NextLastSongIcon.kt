package com.example.neteasecloudmusic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.neteasecloudmusic.R
import kotlin.math.sqrt

class NextLastSongIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var click: Click? =null
    var direction=Direction.Left

    var drawType=DrawType.Normal

    var centerX=0f
    var centerY=0f

    var r=0f

    var layoutMin=0
    //两个状态的画笔风格
    private val pressPaint=Paint()
    private val normalPaint=Paint()
    private var pressColor:Int=0
    private var normalColor:Int=0

    private var isRight=true

    //获取一些属性

    var array=context.obtainStyledAttributes(attrs,R.styleable.NextSongIcon)

    init {

        //获取一些参数信息
        array.apply {
            pressColor=getColor(R.styleable.NextSongIcon_press_color, context.resources.getColor(R.color.default_pressed_color))
            normalColor=getColor(R.styleable.NextSongIcon_normal_color,context.resources.getColor(R.color.default_normal_color))
            isRight=getBoolean(R.styleable.NextSongIcon_direction_right,true)
        }

        normalPaint.apply {
            isAntiAlias=true
            style=Paint.Style.STROKE
            strokeWidth=changDipIntoFloat(2f)
        }

        pressPaint.apply {
            isAntiAlias=true
            style=Paint.Style.STROKE
            color=pressColor
            strokeCap=Paint.Cap.ROUND
        }



    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测试测量的模式
        var widthMode=MeasureSpec.getMode(widthMeasureSpec)
        var heightMode=MeasureSpec.getMode(heightMeasureSpec)
        //测试测量的宽高
        val width=MeasureSpec.getSize(widthMeasureSpec)
        val height=MeasureSpec.getSize(heightMeasureSpec)
        layoutMin= height.coerceAtMost(width)
        //告诉父布局

        centerX= width/2f
        centerY=  height/2f

        r=layoutMin/2f

        setMeasuredDimension(width,height)
    }
    private val path= Path()
    private val paint=Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (drawType==DrawType.Normal){
            drawNormal(canvas)
        }else if (drawType==DrawType.Pressed){
            drawPressed(canvas)
        }

        //canvas?.save()
        if (!isRight){
            canvas?.rotate(180f,centerX,centerY)
        }else if (isRight){

        }
        //移动到左上的点
        path.moveTo((centerX-r/4.0).toFloat(), (centerY+ sqrt(3f) /4.0*r).toFloat())
        //左下
        //path.quadTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat(),(centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
        path.lineTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
        //右边
        //path.quadTo((centerX+r/2.0).toFloat(), centerY,(centerX+r/2.0).toFloat(), centerY)
        path.lineTo((centerX+r/2.0).toFloat(), centerY)
        path.close()
        canvas?.drawPath(path,paint)
        canvas?.drawLine((centerX+r/2.0).toFloat(),(centerY- sqrt(3f) /3.0*r).toFloat(),(centerX+r/2.0).toFloat(),(centerY+ sqrt(3f) /3.0*r).toFloat(),paint)
        //invalidate()

    }

    private fun drawNormal(canvas: Canvas?) {
//        //由于是三角形得用path来画
//        val path= Path()
//        //移动到左上的点
//        path.moveTo((centerX-r/4.0).toFloat(), (centerY+ sqrt(3f) /4.0*r).toFloat())
//        //左下
//        //path.quadTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat(),(centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
//        path.lineTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
//        //右边
//        //path.quadTo((centerX+r/2.0).toFloat(), centerY,(centerX+r/2.0).toFloat(), centerY)
//        path.lineTo((centerX+r/2.0).toFloat(), centerY)
//        path.close()
//        //
//        canvas?.drawPath(path,normalPaint)

        paint.apply {
            isAntiAlias=true
            style=Paint.Style.FILL
            strokeCap=Paint.Cap.ROUND
            color=normalColor
            strokeWidth=changDipIntoFloat(4f)
        }


//        canvas?.drawLine((centerX+r/2.0).toFloat(),(centerY- sqrt(3f) /3.0*r).toFloat(),(centerX+r/2.0).toFloat(),(centerY+ sqrt(3f) /3.0*r).toFloat(),normalPaint)
    }

    private fun drawPressed(canvas: Canvas?) {
//        //由于是三角形得用path来画
//        val path= Path()
//        //移动到左上的点
//        path.moveTo((centerX-r/4.0).toFloat(), (centerY+ sqrt(3f) /4.0*r).toFloat())
//        //左下
//        //path.quadTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat(),(centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
//        path.lineTo((centerX-r/4.0).toFloat(),(centerY- sqrt(3f) /4.0*r).toFloat())
//        //右边
//        //path.quadTo((centerX+r/2.0).toFloat(), centerY,(centerX+r/2.0).toFloat(), centerY)
//        path.lineTo((centerX+r/2.0).toFloat(), centerY)
//        path.close()
//        //
        //canvas?.drawPath(path,normalPaint)
        paint.apply {
            isAntiAlias=true
            style=Paint.Style.FILL
            strokeCap=Paint.Cap.ROUND
            color=pressColor
            strokeWidth=changDipIntoFloat(4f)
        }
//        canvas?.drawLine((centerX+r/2.0).toFloat(),(centerY- sqrt(3f) /3.0*r).toFloat(),(centerX+r/2.0).toFloat(),(centerY+ sqrt(3f) /3.0*r).toFloat(),pressPaint)

    }


    private fun changDipIntoFloat(x:Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,x,resources.displayMetrics)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_UP -> {
                drawType = DrawType.Normal
                if (event.x>= 0 && event.x<=width && event.y >=0 && event.y<= height){
                    click?.onIconClicked(this)

                }
                invalidate()
                //Log.e("TAG", "up" )
            }

            MotionEvent.ACTION_DOWN -> {
                drawType = DrawType.Pressed
                invalidate()
                //Log.e("TAG", "down")
            }

        }
        return true
    }

    fun doInvalidate(){
        invalidate()
    }


    enum class DrawType{
        Pressed,Normal
    }

    enum class Direction{
        Left,Right
    }

    fun addOnIconClickListener(click:Click){
        this.click=click
    }

    interface Click{
        fun onIconClicked(v:View)
    }

}