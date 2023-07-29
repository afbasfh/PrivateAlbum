package com.example.privatealbum

import android.content.Context
import kotlin.random.Random

private val Default_cover_list = listOf(
    "default_cover/f1.png",
    "default_cover/f2.png",
    "default_cover/f3.png",
    "default_cover/f4.png"
)
//默认图片封面的URL地址
val Context.DEFAULT_COVER_URL:String
    get() {
        val index = Random(System.currentTimeMillis()).nextInt(4)
        return "${this.filesDir.path}/${Default_cover_list[index]}"
    }












