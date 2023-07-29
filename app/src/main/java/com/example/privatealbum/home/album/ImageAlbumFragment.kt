package com.example.privatealbum.home.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.privatealbum.MainActivity
import com.example.privatealbum.databinding.FragmentImageAlbumBinding
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import com.example.privatealbum.db.SharedViewModel
import com.example.privatealbum.dp2pxI

class ImageAlbumFragment : Fragment() {
    lateinit var binding:FragmentImageAlbumBinding
    val model:SharedViewModel by activityViewModels()
    lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageAlbumBinding.inflate(inflater)
        binding.model = model
        binding.clickEvents = ClickEvents()
        binding.lifecycleOwner = viewLifecycleOwner

        Log.v("pxd","iamge:$model")

        //监听相册数据
        model.imageAlbumList.observe(viewLifecycleOwner){
            AlbumAdapter.setData(adapter, it)
        }
        //请求加载图片相册
        model.loadAlbumsWithType(ALBUM_TYPE_IMAGE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //显示 BottomAppBar 和 FloatingActionButton
        model.shouldShowBottomNavView.postValue(true)

        adapter = AlbumAdapter()
        adapter.setModelAndLifeCycleOwner(model,viewLifecycleOwner)

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(requireActivity().dp2pxI(4)))
        binding.recyclerView.adapter = adapter
    }
}