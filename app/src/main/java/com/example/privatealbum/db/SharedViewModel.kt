package com.example.privatealbum.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.privatealbum.DEFAULT_COVER_URL
import com.example.privatealbum.file.FileMananger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application)
    :AndroidViewModel(application) {
    //管理BottomNavigationView的显示或者隐藏
    var shouldShowBottomNavView = MutableLiveData(false)

    //保存所有相册信息
    var imageAlbumList = MutableLiveData<List<Album>>(emptyList())
    var videoAlbumList = MutableLiveData<List<Album>>(emptyList())
    //数据库仓库对象
    private val repository = Repository(application.applicationContext)
    //文件管理器对象
    private val fileManager = FileMananger.getInstance(application)
    //保存当前需要添加的相册是什么类型
    var type = ALBUM_TYPE_IMAGE

    //相册删除按钮默认是不显示 有内容再显示
    var shouldShowDeleteInAlbum = MutableLiveData(false)
    //记录需要删除相册的数组
    private var deleteAlbumList = arrayListOf<Album>()
    //记录当前列表的状态 编辑模式 还是 正常状态
    var isEditMode = MutableLiveData(false)


    //添加需要删除的相册
    fun addAlbumToDeleteList(album:Album){ //添加到删除相册中
        deleteAlbumList.add(album)
        shouldShowDeleteInAlbum.postValue(true)
    }
    fun deleteAlbumFromDeleteList(album: Album){ //从删除列表中删除
        deleteAlbumList.remove(album)
        shouldShowDeleteInAlbum.postValue(deleteAlbumList.size > 0)
    }
    fun isAlbumInDeleteList(album: Album):Boolean{ //判断相册是否在删除列表中`
        return deleteAlbumList.contains(album)
    }
    fun clearDeleteList(){  //清空删除列表中的相册
        deleteAlbumList.clear()
        shouldShowDeleteInAlbum.postValue(false)
    }

    //获取相册
    fun loadAlbumsWithType(albumType:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadAlbumWithType(albumType)
            result.collect{
                if (albumType == ALBUM_TYPE_IMAGE) {
                    imageAlbumList.postValue(it)
                }else{
                    videoAlbumList.postValue(it)
                }
            }
        }
    }
    //插入相册
    fun addAlbum(name:String,type: Int){
        //构建相册对象
        val album = Album(0, name, getApplication<Application>().DEFAULT_COVER_URL, 0, type = type
        )

        viewModelScope.launch(Dispatchers.IO){
            //数据库插入一条相册记录
            repository.addAlbum(album)

            //文件里面创建相册
            fileManager.createAlbum(album)
        }
    }

    //删除相册
    fun deleteSelectedAlbum(){
        if (deleteAlbumList.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                //数据库中删除
                repository.deleteAlbums(deleteAlbumList)
                //文件中删除
                fileManager.deleteAlbums(deleteAlbumList)
                deleteAlbumList.clear()
            }
        }
    }
}
















