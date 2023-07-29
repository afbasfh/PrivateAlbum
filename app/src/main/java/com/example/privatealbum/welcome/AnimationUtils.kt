package com.example.privatealbum.welcome

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import com.example.privatealbum.dp2pxF
import com.google.android.material.bottomappbar.BottomAppBar
import java.time.Duration

fun Animation.setAnimationStatusChangeListener(
    onStart:(Animation?)->Unit = {},
    onEnd:(Animation?)->Unit = {},
    onRepeat:(Animation?)->Unit = {}
){
    this.setAnimationListener(object:Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {
            onStart(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            onRepeat(animation)
        }
    })
}

fun View.startAnimationWithListener(
    resId: Int,
    onStart:(Animation?)->Unit = {},
    onEnd:(Animation?)->Unit = { },  //声明方法  给默认实现
    onRepeat:(Animation?)->Unit = {}
){
    val animation = AnimationUtils.loadAnimation(this.context,resId)
    animation.setAnimationListener(object:Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {
            onStart(animation)
        }
        override fun onAnimationEnd(animation: Animation?) {
            //调用方法
            onEnd(animation)
        }
        override fun onAnimationRepeat(animation: Animation?) {
            onRepeat(animation)
        }
    })
    this.startAnimation(animation)

}


//左右摆动的动画效果
fun View.startShakeAnimation(offsetX:Float,time:Long){
    ObjectAnimator.ofFloat(
        this,
        "translationX",
        0f,
        -offsetX,
        2 * offsetX,
        0f
    ).apply {
        duration = time
        repeatCount = 2
        repeatMode = ObjectAnimator.RESTART
        interpolator = AccelerateInterpolator()
        start()
    }
}

//菜单列表弹出动画
fun startPopUpAnimation(targets: List<View>, space:Int){
    val spacePx = targets[0].context.dp2pxF(space)

    targets.forEachIndexed { index, view ->
        ObjectAnimator.ofFloat(
            view,
            "translationY",
            -(index+1) * (spacePx+view.height)).apply {
            duration = 500
            interpolator = BounceInterpolator()
            startDelay = index*100L
            start()
        }
    }
}

//菜单列表弹出动画
fun startPopBackAnimation(targets: List<View>){
    targets.forEachIndexed { index, view ->
        ObjectAnimator.ofFloat(
            view,
            "translationY",
            0f).apply {
            duration = 500
            interpolator = BounceInterpolator()
            startDelay = index*100L
            start()
        }
    }
}

fun showBottomNavigationView(bottomAppBar:BottomAppBar){
    bottomAppBar.alpha = 1f
    ObjectAnimator.ofFloat(
        bottomAppBar,
        "translationY",
        bottomAppBar.height.toFloat(),
        0f
    ).apply {
        duration = 500
        start()
    }
}
fun hideBottomNavigationView(bottomAppBar: BottomAppBar){
    ObjectAnimator.ofFloat(
        bottomAppBar,
        "translationY",
        0f,
        bottomAppBar.height.toFloat()
    ).apply {
        duration = 500
        start()
    }
}

fun showFloatingActionButton(view: View){
    view.isEnabled = true
    ObjectAnimator.ofFloat(
        view,
        "alpha",
        1f
    ).apply {
        duration = 500
        start()
    }
}

fun hideFloatingActionButton(view: View){
    view.isEnabled = false
    ObjectAnimator.ofFloat(
        view,
        "alpha",
        0f
    ).apply {
        duration = 500
        start()
    }
}



