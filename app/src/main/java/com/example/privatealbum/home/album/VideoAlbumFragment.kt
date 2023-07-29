package com.example.privatealbum.home.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.privatealbum.MainActivity
import com.example.privatealbum.databinding.FragmentVideoAlbumBinding
import com.example.privatealbum.db.ALBUM_TYPE_VIDEO
import com.example.privatealbum.db.SharedViewModel
import com.example.privatealbum.dp2pxI

class VideoAlbumFragment : Fragment() {
    lateinit var binding:FragmentVideoAlbumBinding
    lateinit var adapter: AlbumAdapter
    val model:SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoAlbumBinding.inflate(inflater,container,false)
        binding.model = model
        binding.clickEvents = ClickEvents()
        binding.lifecycleOwner = viewLifecycleOwner

        model.videoAlbumList.observe(viewLifecycleOwner){
            AlbumAdapter.setData(adapter, it)
        }
        model.loadAlbumsWithType(ALBUM_TYPE_VIDEO)

        Log.v("pxd","video:$model")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AlbumAdapter()
        adapter.setModelAndLifeCycleOwner(model,viewLifecycleOwner)

        //显示 BottomAppBar 和 FloatingActionButton
        model.shouldShowBottomNavView.postValue(true)

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(requireActivity().dp2pxI(4)))
        binding.recyclerView.adapter = adapter
    }
}