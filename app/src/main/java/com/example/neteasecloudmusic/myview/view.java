package com.example.neteasecloudmusic.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class view extends View {

    float x,y;
    public view(Context context) {
        super(context);
    }

    public view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public view(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        x=w;
        y=h;
    }
}
