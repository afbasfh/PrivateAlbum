package com.example.privatealbum.home.thumb

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import com.example.privatealbum.db.Album
import com.example.privatealbum.db.Repository
import com.example.privatealbum.db.ThumbImage
import com.example.privatealbum.file.FileMananger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class  ThumbViewModel(application:Application): AndroidViewModel(application){
    var menuList = emptyList<FloatingActionButton>()
    private val repository = Repository(application)
    private val fileManager = FileMananger.getInstance(application)

    //当前的一个相册
    var currentAlbum = MutableLiveData<Album>()
    //当前相册的所有缩略图
    var thumbImageList = MutableLiveData<List<ThumbImage>>(emptyList())

    //查询一个相册信息
    fun loadAlbum(albumId: Int){
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.getAlbumInfo(albumId)
            result.collect{
                if(it.number > 1) {
                    currentAlbum.postValue(it)
                }
            }
        }
    }
    //查询相册里面的 所有图片
    fun loadAlbumThumbImages(albumId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllImagesWithAlbumId(albumId)
            result.collect{
                thumbImageList.postValue(it)
            }
        }
    }

    //获取相册中文件的完整路径
    fun getThumImageFilePath(albumName:String, fileName:String):String{
        return fileManager.getThumImageFilePath(albumName, fileName)
    }

    //保存一张图片
    fun saveImage(album: Album, uri: Uri){
        saveMedia(album, uri)
    }

    //保存一个视频
    fun saveVideo(album: Album, uri: Uri){
        saveMedia(album, uri)
    }

    private fun saveMedia(album: Album, uri: Uri){
        //uri -> InputStream -> 内存 -> OutputStream -> file
        val name = getNameFromTime("jpg")

        //数据库中插入图片
        viewModelScope.launch(Dispatchers.IO) {
            //保存到文件中
            if (album.type == ALBUM_TYPE_IMAGE) {
                fileManager.insertImage(uri, album.albumName, name)
            }else{
                fileManager.insertVideo(uri, album.albumName, name)
            }

            //更新相册信息
            album.number = album.number+1
            if (album.number == 1){
                album.coverUrl = fileManager.getFileUrl(album.albumName,name)
            }
            repository.updateAlbum(album)

            //数据库插入
            repository.insertImage(ThumbImage(name, album.id))
        }
    }
    private fun getNameFromTime(type:String):String{
        val timeStr =SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.CHINESE).format(Date())
        return "$timeStr.$type"
    }
}