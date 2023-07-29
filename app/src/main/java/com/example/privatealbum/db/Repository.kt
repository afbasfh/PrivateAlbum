package com.example.privatealbum.db

import android.content.Context
import kotlinx.coroutines.flow.Flow

class Repository(context: Context) {
    private var albumDao:AlbumDao

    init {
        albumDao = AlbumDatabase.getInstance(context).albumDao()
    }

    //加载相册
    suspend fun loadAlbumWithType(type: Int):Flow<List<Album>>{
        return albumDao.getAllAlumsWithType(type)
    }

    //添加相册
    suspend fun addAlbum(album: Album){
        albumDao.insertAlbum(album)
    }

    //删除相册
    suspend fun deleteAlbums(albums: List<Album>){
        albumDao.deleteAlbums(albums)
    }

    //更新相册
    suspend fun updateAlbum(album: Album){
        albumDao.updateAlbum(album)
    }

    //查询相册
    suspend fun getAlbumInfo(albumId:Int):Flow<Album>{
        return albumDao.getAlbumInfo(albumId)
    }

    //添加图片
    suspend fun insertImage(thumbImage: ThumbImage){
        albumDao.insertImage(thumbImage)
    }

    //查询某个相册的所有图片
    suspend fun getAllImagesWithAlbumId(albumId: Int):Flow<List<ThumbImage>>{
        return albumDao.getAllImagesWithAlbumId(albumId)
    }
}










