package com.example.privatealbum.file

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.example.privatealbum.db.Album

class FileMananger private constructor(val context: Context){

    //文件操作对象
    private val fileDao = FileDao(context)

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE:FileMananger? = null

        fun getInstance(context: Context):FileMananger{
            if (INSTANCE != null){
                return INSTANCE!!
            }
            synchronized(this){
                if (INSTANCE == null){
                    INSTANCE = FileMananger(context)
                }
                return INSTANCE!!
            }
        }
    }

    //创建相册
    suspend fun createAlbum(album: Album){
        fileDao.createAlbum(album)
    }

    //删除相册
    suspend fun deleteAlbums(albums: List<Album>){
        fileDao.deleteAlbums(albums)
    }

    //插入图片
    suspend fun insertImage(uri:Uri,albumName:String, fileName:String ){
        context.contentResolver.openInputStream(uri).use {
            it?.let {
                fileDao.insertImage(it,albumName, fileName)
            }
        }
    }

    //插入视频
    suspend fun insertVideo(uri:Uri,albumName:String, fileName:String ){
        context.contentResolver.openInputStream(uri).use {
            it?.let {
                fileDao.insertVideo(it,albumName, fileName)
            }
        }
    }

    //获取相册中某个图片的url地址
    fun getFileUrl(albumName: String,fileName: String):String{
        return fileDao.getFileUrl(albumName, fileName)
    }

    //获取相册中文件的完整路径
    fun getThumImageFilePath(albumName:String, fileName:String):String{
        return fileDao.getThumbFilePath(albumName, fileName)
    }
}
















