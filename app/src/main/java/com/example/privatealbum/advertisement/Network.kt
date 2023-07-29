package com.example.privatealbum.advertisement

import android.util.Log
import kotlin.random.Random

object Network {
    private val imageList = listOf(
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Frichtext%2Flarge%2Fpublic%2Fp241945378.jpg&refer=http%3A%2F%2Fimg1.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1662168578&t=a661b6db7f702bc05f872868a0c73e55",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201608%2F29%2F20160829163915_TUeFH.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1662168578&t=ef9bd8808e42f9b880e01c76fc7d15f3",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202102%2F23%2F20210223120430_2iKRv.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1662168578&t=ed8da252078ab0335f35f7d52ca83a4d",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201504%2F17%2F20150417H2910_SQMVA.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1662168578&t=fe60674f6e72dbad72801b19e9aa3bb3"
    )
    /**随机获取一张图片*/
    fun fetchImage():String{
        val index = Random(System.currentTimeMillis()).nextInt(imageList.size)
        return imageList[index]
    }
}