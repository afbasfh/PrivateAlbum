package com.example.privatealbum.home.album

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.privatealbum.db.SharedViewModel
import com.example.privatealbum.welcome.hideBottomNavigationView
import com.example.privatealbum.welcome.hideFloatingActionButton
import com.example.privatealbum.welcome.showBottomNavigationView
import com.example.privatealbum.welcome.showFloatingActionButton
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

//删除按钮是否显示
@BindingAdapter("shouldShow")
fun ImageView.shouldShow(show:Boolean){
    visibility = if (show){ //编辑状态时 显示删除按钮
        isEnabled = true
        View.VISIBLE
    }else{
        isEnabled = false
        View.INVISIBLE
    }
}

//编辑状态需要显示黑色遮罩和选中的圆点
@BindingAdapter("shouldShowEdit")
fun View.shouldShowEdit(isEdit:Boolean){
    visibility = if (isEdit){
        View.VISIBLE
    }else{
        View.INVISIBLE
    }
}

@BindingAdapter("shouldShow")
fun BottomAppBar.shouldShow(show: Boolean){
    val status = tag as String
    if (show){ //现在需要显示
        if (status == "0") { //之前状态是隐藏
            showBottomNavigationView(this)
            tag = "1"
        }
    }else{
        if (status == "1") { //之前状态是隐藏
            hideBottomNavigationView(this)
            tag = "0"
        }
    }
}

@BindingAdapter("shouldShow")
//0 隐藏   1显示
fun FloatingActionButton.shouldShow(show: Boolean){
    val status = tag as String
    if (show){ //现在需要显示
        if (status == "0") { //之前状态是隐藏
            showFloatingActionButton(this)
            tag = "1"
        }
    }else{
        if (status == "1") { //之前状态是隐藏
            hideFloatingActionButton(this)
            tag = "0"
        }
    }
}
