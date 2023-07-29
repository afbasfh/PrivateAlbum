package com.example.privatealbum.home.thumb

import android.view.View
import com.example.privatealbum.welcome.startPopBackAnimation
import com.example.privatealbum.welcome.startPopUpAnimation

class ThumClickEvents {
    //添加按钮被点击 弹出或者隐藏菜单列表
    fun changeMenuStatus(view: View, model: ThumbViewModel){
        val tag = view.tag as String
        if (tag == "0"){
            //显示菜单列表
            startPopUpAnimation(model.menuList, 50)
            view.tag = "1"
        }else{
            //收回
            startPopBackAnimation(model.menuList)
            view.tag = "0"
        }
    }
}