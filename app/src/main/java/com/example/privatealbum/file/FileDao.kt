package com.example.privatealbum.file

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Log
import android.util.Size
import com.example.privatealbum.db.ALBUM_TYPE_VIDEO
import com.example.privatealbum.db.Album
import com.example.privatealbum.dp2pxI
import java.io.*

class FileDao(val context: Context) {
    //缩略图的尺寸
    private val thumbSize = Size(context.dp2pxI(200),context.dp2pxI(200))
    private val orginSize = Size(context.dp2pxI(500),context.dp2pxI(500*3/4))

    //文件的根路径
    private val albumRootPath = "${context.filesDir.path}/albums"
    //缩略图文件名
    private val thumbnailDirName = "thumb"
    //原图文件名
    private val originDirName = "origin"
    //视频文件名
    private val videoDirName = "video"

    //获取相册对应的路径 files/albums/ios
    private fun getAlbumDirPath(albumName:String) = "${albumRootPath}/$albumName"
    //获取缩略图的路径 files/albums/ios/thumb
    private fun getThumbDirPath(albumName: String) = "${albumRootPath}/$albumName/$thumbnailDirName"
    //获取原图的路径 files/albums/ios/orgin
    private fun getOriginDirPath(albumName: String) = "${albumRootPath}/$albumName/$originDirName"
    //获取视频的路径 files/albums/ios/video
    private fun getVideoDirPath(albumName: String) = "${albumRootPath}/$albumName/$videoDirName"

    //获取相册中文件的完整路径
    fun getFileUrl(albumName: String,fileName: String):String{
        return getOriginFilePath(albumName, fileName)
    }
    //从相册的orgin目录中获取某个文件的完整路径
    private fun getOriginFilePath(albumName: String,fileName: String):String{
        return "${getOriginDirPath(albumName)}/$fileName"
    }
    //从相册的thumb目录中获取某个文件的完整路径
    fun getThumbFilePath(albumName: String,fileName: String):String{
        return "${getThumbDirPath(albumName)}/$fileName"
    }
    //从相册的video目录中获取某个文件的完整路径
    private fun getVideoFilePath(albumName: String,fileName: String):String{
        val mp4FileName = fileName.replace("jpg","mp4",true)
        return "${getVideoDirPath(albumName)}/$mp4FileName"
    }

    //创建相册
    fun createAlbum(album: Album){
        //创建相册目录
        createDirectoryWithPath(getAlbumDirPath(album.albumName))
        //创建缩略图目录
        createDirectoryWithPath(getThumbDirPath(album.albumName))
        //创建原图
        createDirectoryWithPath(getOriginDirPath(album.albumName))
        //判断是否需要创建视频路径
        if (album.type == ALBUM_TYPE_VIDEO){
            createDirectoryWithPath(getVideoDirPath(album.albumName))
        }
    }
    //删除相册
    fun deleteAlbums(albums: List<Album>){
        albums.forEach { album ->
            //thumb
            deleteHoleDirWithPath(getThumbDirPath(album.albumName))
            //origin
            deleteHoleDirWithPath(getOriginDirPath(album.albumName))
            //video
            if (album.type == ALBUM_TYPE_VIDEO){
                deleteHoleDirWithPath(getVideoDirPath(album.albumName))
            }
            //删除相册本身
            deleteHoleDirWithPath(getAlbumDirPath(album.albumName))
        }
    }

    //插入一个视频
    fun insertVideo(inputStream: InputStream,albumName: String,fileName: String){
        //保存视频内容
        val videoFilePath = getVideoFilePath(albumName, fileName)
        writeToFile(inputStream, videoFilePath)

        //原图
        val originFilePath = getOriginFilePath(albumName, fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val orgBitmap = ThumbnailUtils.createVideoThumbnail(File(videoFilePath),orginSize,null)
            writeToFile(orgBitmap,originFilePath)
        }

        //缩略图
        val thumbFilePath = getThumbFilePath(albumName, fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val thumbBitmap = ThumbnailUtils.createVideoThumbnail(File(videoFilePath),thumbSize,null)
            writeToFile(thumbBitmap,thumbFilePath)
        }
    }

    //插入一个图片
    fun insertImage(inputStream: InputStream,albumName: String,fileName: String){
        //保存原图
        val originPath = getOriginFilePath(albumName, fileName)
        writeToFile(inputStream,originPath)

        //缩略图
        val thumbPath = getThumbFilePath(albumName, fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bitmap = ThumbnailUtils.createImageThumbnail(File(originPath),thumbSize,null)
            writeToFile(bitmap,thumbPath)
        }
    }

    private fun writeToFile(bitmap: Bitmap,filePath:String){
        FileOutputStream(File(filePath)).use {  fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
        }
    }

    //将InputStream中的数据写入文件中
    private fun writeToFile(inputStream: InputStream,filePath:String){
        BufferedInputStream(inputStream).use {bis ->
            BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
                val buffer = ByteArray(1024)
                var len = bis.read(buffer,0,1024)
                while (len != -1){
                    bos.write(buffer,0,len)
                    len = bis.read(buffer,0,1024)
                }
                bos.flush()
            }
        }
    }


    //创建一个目录/文件夹
    private fun createDirectoryWithPath(path:String){
        val file = File(path)
        if (!file.exists()){
            file.mkdirs()
        }
    }
    //删除一个目录和目录下面的所有内容
    private fun deleteHoleDirWithPath(path: String){
        //创建path对应的file对象
        val file = File(path)
        if (file.isDirectory){
            //删除目录下所有子文件
            file.list()?.forEach { fileName ->
                //获取文件的完整路径
                File("$path/$fileName").also {
                    it.delete() //删除文件
                }
            }
        }
        file.delete() //删除这个目录
    }
}

















