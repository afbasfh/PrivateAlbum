package com.example.privatealbum.advertisement

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import com.example.privatealbum.R
import com.example.privatealbum.SdkMoreThanM
import com.example.privatealbum.dp2pxF
import com.example.privatealbum.getResourceColor

class TimeOutJumpView: View {
    private val mStrokeWidth = context.dp2pxF(5)
    //背景的画笔
    private val bgPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.LTGRAY
        strokeWidth = mStrokeWidth
    }
    //前景的画笔
    private val arcPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = getResourceColor(context,R.color.green)
        strokeWidth = mStrokeWidth
    }
    //绘制文本的画笔
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = context.dp2pxF(15)
    }

    //圆的半径
    private var mRadius = 0f
    //绘制的文本
    private val mText = "跳转"
    //文本的起始点
    private var mTextX = 0f
    private var mTextY = 0f
    //圆弧的弧度
    private var sweepAngel = 90f
    //动画对象
    private lateinit var circleProgressAnimator:ValueAnimator
    //点击回调事件
    var onClickListener:((TimeOutJumpView)->Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //计算圆的半径
        mRadius = (width - 2*mStrokeWidth)/2
        val rect = getTextSize(mText,textPaint)
        mTextX = (width-rect.width())/2f
        mTextY = getTextBaseline(textPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        //绘制灰色圆形背景
        canvas?.let {
            it.drawCircle(width/2f,height/2f,mRadius,bgPaint) //绘制灰色圆环
            it.drawText(mText,mTextX,mTextY,textPaint) //绘制文字
            it.drawArc(                   //绘制弧形进度
                mStrokeWidth,mStrokeWidth,width-mStrokeWidth,height-mStrokeWidth,
                270f,sweepAngel,false,arcPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            //回调事件
            onClickListener?.let {
                it(this)
            }
        }
        return true
    }

    /**测量字符串的尺寸(宽 高)*/
    private fun getTextSize(text:String, paint: Paint):Rect{
        val rect = Rect()
        paint.getTextBounds(text,0,text.length,rect)
        return rect
    }
    //计算字体baseline坐标
    //centerY + (bottom - top)/2 - bottom
    private fun getTextBaseline(paint: Paint):Float{
        val fontMetrics = paint.fontMetrics
        val bottom = fontMetrics.bottom
        val top = fontMetrics.top

        return height/2f + (bottom+Math.abs(top))/2f - bottom
    }
    //启动动画
    fun startTimer(aDuration: Long,onAniatorEnd:()->Unit = {}){
        circleProgressAnimator = ValueAnimator.ofFloat(0f,360f).apply {
            duration = aDuration
            interpolator = LinearInterpolator()
            addUpdateListener {
                sweepAngel = it.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                onAniatorEnd()
            })
            start()
        }
    }
    fun stopTimer(){
        circleProgressAnimator.cancel()
    }
}
















