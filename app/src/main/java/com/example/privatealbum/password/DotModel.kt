package com.example.privatealbum.password

import android.graphics.Rect
import android.graphics.RectF

//封装每个点的数据模型
data class DotModel(
    var number: Int = 0,
    var radius:Float = 0f,
    var state: DotState = DotState.NORMAL,
    var cx: Float = 0f,
    var cy: Float = 0f
){
    fun getRect():RectF{
        return RectF(cx-radius,cy-radius,cx+radius,cy+radius)
    }
}

//记录每个点的状态
enum class DotState{
    NORMAL,SELECTED
}

//记录当前绘制的密码是消失还是提示出错
enum class PasswordStatus{
    NORMAL,ERROR
}