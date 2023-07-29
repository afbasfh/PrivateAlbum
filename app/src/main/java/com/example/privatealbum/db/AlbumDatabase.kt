package com.example.privatealbum.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Album::class, ThumbImage::class],
    version = 1,
    exportSchema = false
)
abstract class AlbumDatabase: RoomDatabase() {
    //获取一系列数据库访问的接口实现类
    abstract fun albumDao():AlbumDao
    companion object{
        @Volatile
        private var INSTANCE:AlbumDatabase? = null
        fun getInstance(context: Context):AlbumDatabase{
            if (INSTANCE != null){
                return INSTANCE!!
            }
            synchronized(this){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AlbumDatabase::class.java,
                        "album_db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}
















