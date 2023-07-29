package com.example.privatealbum.home.thumb

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.privatealbum.R
import com.example.privatealbum.databinding.LayoutThumbItemBinding
import com.example.privatealbum.db.ThumbImage

class ThumbAdapter(val model: ThumbViewModel): RecyclerView.Adapter<ThumbAdapter.MyViewHodler>() {
    private var thumbImageList = emptyList<ThumbImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutThumbItemBinding.inflate(inflater)
        return MyViewHodler(binding)
    }
    override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
        holder.bind(thumbImageList[position],model)
    }
    override fun getItemCount(): Int {
        return return thumbImageList.size
    }

    class MyViewHodler(val binding:LayoutThumbItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(thumbImage: ThumbImage, model: ThumbViewModel){
            val filePath = model.getThumImageFilePath(model.currentAlbum.value!!.albumName,thumbImage.imageName)
            Glide.with(binding.root)
                .load(filePath)
                .into(binding.imageView)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<ThumbImage>){
        thumbImageList = newData
        notifyDataSetChanged()
    }
}