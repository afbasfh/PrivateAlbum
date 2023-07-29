package com.example.privatealbum

import android.content.Context
import android.graphics.RectF
import android.os.Build
import androidx.navigation.NavController
import kotlinx.coroutines.*
import java.time.Duration

fun Context.dp2pxI(dp:Int):Int {
    return (resources.displayMetrics.density*dp).toInt()
}
fun Context.dp2pxF(dp:Int):Float {
    return resources.displayMetrics.density*dp
}

/**大于23*/
fun SdkMoreThanM():Boolean{
    return Build.VERSION.SDK_INT > Build.VERSION_CODES.M
}

/**获取color资源文件中的颜色*/
fun getResourceColor(context: Context, resId:Int):Int{
    return if (SdkMoreThanM()){
        context.resources.getColor(resId,null)
    }else{
        context.resources.getColor(resId)
    }
}


//延迟跳转
fun NavController.delayNavigate(id:Int,scope:CoroutineScope){
    scope.launch(Dispatchers.IO) {
        delay(500)
        withContext(Dispatchers.Main){
            navigate(id)
        }
    }
}

//实现Rect copy
fun RectF.copy(srcRect:RectF){
    left = srcRect.left
    top = srcRect.top
    right = srcRect.right
    bottom = srcRect.bottom
}

//延迟操作
fun postDelay(duration: Long, callBack:()->Unit = {}){
    CoroutineScope(Dispatchers.IO).launch {
        delay(duration)
        withContext(Dispatchers.Main){
            callBack()
        }
    }
}