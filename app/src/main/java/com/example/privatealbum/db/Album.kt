package com.example.privatealbum.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

const val ALBUM_TYPE_IMAGE = 0
const val ALBUM_TYPE_VIDEO = 1

/**默认表名就是类名*/
//@Entity(tableName = "album_table")
@Entity
data class Album(
    @PrimaryKey(autoGenerate = true)/**主键*/
    val id:Int,
    var albumName:String,
    //@ColumnInfo(name = "cover_url") 设置字段在表里面的名字
    var coverUrl:String,
    var number:Int,
    val type:Int = ALBUM_TYPE_IMAGE
)

@Entity
data class ThumbImage(
    @PrimaryKey(autoGenerate = false)
    val imageName:String,
    val albumId:Int
)