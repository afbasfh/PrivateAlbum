package com.example.privatealbum.home.album

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import com.example.privatealbum.MainActivity
import com.example.privatealbum.R
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import com.example.privatealbum.db.Album
import com.example.privatealbum.db.SharedViewModel
import com.example.privatealbum.home.album.AlbumNameDialog

class ClickEvents {
    fun addAlbum(view:View, model:SharedViewModel){
        AlbumNameDialog.show(
            view.context,
            onOk = { name ->
                model.addAlbum(name,model.type)
            }
        )
    }

    //点击进入编辑状态
    fun changeRecyclerViewEditMode(view: View, model: SharedViewModel){
        if (model.isEditMode.value == true){
            //退出编辑状态
            model.isEditMode.postValue(false)
            model.clearDeleteList()
        }else{
            //进入编辑状态
            model.isEditMode.postValue(true)
        }
    }

    //某个相册被点击
    @RequiresApi(Build.VERSION_CODES.M)
    fun albumClicked(view: View, model: SharedViewModel, album: Album){
        if (model.isEditMode.value == true){
            //找到checkBox
            val checkbox = view.findViewById<ImageView>(R.id.checkBoxInAlbumItem)
            //判断是选中还是取消选中
            if (model.isAlbumInDeleteList(album)){
                //取消选中
                checkbox.imageTintList = ColorStateList.valueOf(Color.parseColor("#bfbfbf"))
                model.deleteAlbumFromDeleteList(album)
            }else{
                //选中
                checkbox.imageTintList = ColorStateList.valueOf(
                    view.resources.getColor(R.color.teal_200,null))
                model.addAlbumToDeleteList(album)
            }
        }else{
            //进入相册缩略图页面
            val action = if (album.type == ALBUM_TYPE_IMAGE) {
                 ImageAlbumFragmentDirections.actionImageAlbumFragmentToThumbImageFragment(album)
            }else{
                 VideoAlbumFragmentDirections.actionVideoAlbumFragmentToThumbImageFragment(album)
            }
            view.findNavController().navigate(action)

            //隐藏 BottomAppBar 和 FloatingActionButton
            model.shouldShowBottomNavView.postValue(false)
        }
    }

    //相册删除按钮
    fun deleteAlbum(view: View,model: SharedViewModel){
        model.deleteSelectedAlbum()
    }
}









