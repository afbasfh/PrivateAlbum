package com.example.privatealbum.home.thumb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.privatealbum.R
import com.example.privatealbum.databinding.FragmentThumbImageBinding
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import com.example.privatealbum.db.Album
import com.example.privatealbum.dp2pxI
import com.example.privatealbum.home.album.SpacingItemDecoration

class ThumbImageFragment : Fragment() {
    private lateinit var binding:FragmentThumbImageBinding
    private val albumArgs by navArgs<ThumbImageFragmentArgs>()
    private val model:ThumbViewModel by activityViewModels()
    private lateinit var thumbAdapter:ThumbAdapter

    private val album:Album by lazy {
        val albumArgs = albumArgs.album
        model.currentAlbum.postValue(albumArgs)
        albumArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThumbImageBinding.inflate(inflater)
        model.menuList = listOf(binding.libraryBtn,binding.cameraBtn)
        binding.model = model
        binding.clickEvents = ThumClickEvents()
        binding.lifecycleOwner = viewLifecycleOwner

        model.currentAlbum.observe(viewLifecycleOwner){
            //封面信息
            Glide.with(this).load(it.coverUrl).into(binding.coverImageView)
        }
        model.thumbImageList.observe(viewLifecycleOwner){
            thumbAdapter.setData(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //加载相册信息
        model.loadAlbum(album.id)
        model.loadAlbumThumbImages(album.id)

        thumbAdapter = ThumbAdapter(model)
        binding.recyclerView.apply {
            adapter = thumbAdapter
            layoutManager = GridLayoutManager(requireActivity(),3)
            addItemDecoration(SpacingItemDecoration(requireActivity().dp2pxI(4)))
        }

        binding.titleTextView.text = album.albumName
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        //选择图片或者视频
        binding.addBtn.setOnClickListener {
            val action = ThumbImageFragmentDirections.actionThumbImageFragmentToMediaPickerFragment(album)
            findNavController().navigate(action)
        }
    }
}