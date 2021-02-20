package com.example.neteasecloudmusic.mytools.dimention

import android.util.TypedValue
import com.example.neteasecloudmusic.mytools.filedownload.mContext

fun changeDipIntoFloat(x:Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,x, mContext.resources.displayMetrics)
}
class Dimensions {

}