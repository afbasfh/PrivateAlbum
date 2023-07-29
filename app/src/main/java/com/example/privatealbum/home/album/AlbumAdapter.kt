package com.example.privatealbum.home.album

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.privatealbum.databinding.AlbumItemBinding
import com.example.privatealbum.db.Album
import com.example.privatealbum.db.SharedViewModel

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.MyViewHolder>() {
    private var albums:List<Album> = emptyList()
    private lateinit var model: SharedViewModel
    private lateinit var owner: LifecycleOwner
    private lateinit var binding:AlbumItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = AlbumItemBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)
        binding.model = model
        binding.album = album
        binding.clickEvents = ClickEvents()
        binding.lifecycleOwner = owner

        if (!model.isAlbumInDeleteList(album)){
            //正在给这个item绑定数据，而且这个item也没有在删除数组里面 这个相册的checkbox还原为正常
            binding.checkBoxInAlbumItem.imageTintList = ColorStateList.valueOf(Color.parseColor("#bfbfbf"))
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class MyViewHolder(val binding:AlbumItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            //加载图片
            Glide
                .with(itemView)
                .load(album.coverUrl)
                .into(binding.coverImageView)
            binding.coverImageView
            binding.nameTextView.text = album.albumName
            binding.countTextView.text = album.number.toString()
            binding.renameBtn.setOnClickListener {
            }
        }
    }

    fun setModelAndLifeCycleOwner(aModel: SharedViewModel,aOwner:LifecycleOwner){
        model = aModel
        owner = aOwner
    }

    companion object {
        @SuppressLint("NotifyDataSetChanged")
        fun setData(albumAdapter: AlbumAdapter, newData:List<Album>){
            albumAdapter.albums = newData
            //刷新界面
            albumAdapter.notifyDataSetChanged()
        }
    }
}