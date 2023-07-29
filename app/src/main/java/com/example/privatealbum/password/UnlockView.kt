package com.example.privatealbum.password

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.privatealbum.copy
import com.example.privatealbum.dp2pxF
import com.example.privatealbum.postDelay

class UnlockView: View {
    private val dotPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.argb(150,255,255,255)
    }
    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = context.dp2pxF(5)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        color = Color.argb(220,255,255,255)
    }
    private val dotRadius = context.dp2pxF(8)
    private val mMargin = context.dp2pxF(5)  //与边缘的距离
    private var space = 0f  //两点之间的间距
    private var cx = 0f
    private var cy = 0f
    private val dotlist = arrayListOf<DotModel>() //存储每个点对应的模型数据
    private val dotModel = DotModel() //模型数据
    private var isFristDot = true //记录上一个点
    private var linePath = Path()   //连接线
    private var moveblePath:Path? = null  //某个点到 触摸区域的线
    private var currentDot:DotModel? = null //当前点
    private val passwordBuilder = StringBuilder() //记录密码
    var onPasswordFinishListener:((String)->PasswordStatus)? = null //密码回调

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        space = (width - 2*mMargin - 6*dotRadius)/2
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            //绘制9个点
            for (i in 0..2){
                for (j in 0..2){
                    cx = mMargin+dotRadius+j*(dotRadius*2+space)
                    cy = mMargin+dotRadius+i*(dotRadius*2+space)
                    it.drawCircle(cx,cy, dotRadius, dotPaint)

                    //创建这个点的model
                    dotModel.number = 1 + i*3 + j
                    dotModel.cx = cx
                    dotModel.cy = cy
                    dotModel.radius = dotRadius
                    dotlist.add(dotModel.copy())
                }
            }
            //绘制路径
            it.drawPath(linePath,linePaint)
            //绘制移动的线
            if (moveblePath != null) {
                it.drawPath(moveblePath!!, linePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                val model = findDot(event.x,event.y)
                if (model != null){
                    //确定这条线的起点
                    linePath.moveTo(model.cx,model.cy)
                    model.state = DotState.SELECTED
                    currentDot = model
                    isFristDot = false
                    passwordBuilder.append(model.number)
                }
            }
            MotionEvent.ACTION_MOVE ->{
                val model = findDot(event.x,event.y)
                if (model != null){
                    if (isFristDot){
                        linePath.moveTo(model.cx, model.cy)//确定这条线的起点
                        model.state = DotState.SELECTED
                        currentDot = model
                        isFristDot = false
                        passwordBuilder.append(model.number)
                    }else{
                        if (model.state == DotState.NORMAL) {
                            linePath.lineTo(model.cx, model.cy) //从上一个点到当前点的中心点连成一条线
                            linePath.moveTo(model.cx,model.cy) //当前点作为下一条线段的起点
                            model.state = DotState.SELECTED
                            currentDot = model
                            moveblePath = null
                            passwordBuilder.append(model.number)
                            invalidate()
                        }
                    }
                } else{
                    if (!isFristDot) {
                        //从上一个点到当前触摸点绘制一条线
                        moveblePath = Path()
                        moveblePath?.moveTo(currentDot!!.cx,currentDot!!.cy)
                        moveblePath?.lineTo(event.x, event.y)
                        invalidate()
                    }
                }

            }
            MotionEvent.ACTION_UP ->{
                if (onPasswordFinishListener != null){
                    val result = onPasswordFinishListener!!(passwordBuilder.toString())
                    if (result == PasswordStatus.NORMAL){
                        postDelay(500){
                            clear()
                        }
                    }else{
                        linePaint.color = Color.parseColor("#D83E32")
                        moveblePath = null
                        invalidate()
                        postDelay(1000){
                            linePaint.color =  Color.argb(150,255,255,255)
                            clear()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun clear(){
        linePath = Path()
        moveblePath = null
        passwordBuilder.clear()
        invalidate()

        dotlist.forEach {
            it.state = DotState.NORMAL
        }
        isFristDot = true
        currentDot = null
    }

    //查找x y是否在某个点里面
    private fun findDot(x:Float, y:Float):DotModel?{
        dotlist.forEach { model ->
            if(model.getRect().contains(x,y)){
                return model
            }
        }
        return null
    }
}