package com.example.neteasecloudmusic.myview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyBannerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var TAG="MyBannerView"

    var mWidth=0f
    var mHeight=0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth=w.toFloat()
        mHeight=h.toFloat()

        Log.d(TAG, "onSizeChanged: "+w+"  "+h)

    }
}