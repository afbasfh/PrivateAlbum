package com.example.privatealbum.home.thumb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.privatealbum.R
import com.example.privatealbum.databinding.FragmentMediaPickerBinding
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.helpers.pixFragment
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio

class MediaPickerFragment : Fragment() {
    lateinit var binding: FragmentMediaPickerBinding
    private val albumArgs:MediaPickerFragmentArgs by navArgs()
    private val model:ThumbViewModel by activityViewModels()
    private val album by lazy {
        albumArgs.ablum
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPickerBinding.inflate(inflater)

        showPixImagePicker()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showPixImagePicker() {
        val options = Options().apply{
            ratio = Ratio.RATIO_4_3                                    //Image/video capture ratio
            count = 3                                                   //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            videoOptions.videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode =  if (album.type == ALBUM_TYPE_IMAGE) {
                Mode.Picture
            }else {
                Mode.Video
            }
        }

        val fragment = pixFragment(options){
            when(it.status){
                PixEventCallback.Status.SUCCESS ->{
                    //立刻返回
                    //获取图片或者视频数据 保存
                    //findNavController().navigateUp()
                    it.data.forEach { uri ->
                        //判断是图片还是视频
                        if (album.type == ALBUM_TYPE_IMAGE){
                            model.saveImage(album,uri)
                        }else{
                            model.saveVideo(album,uri)
                        }
                    }
                    findNavController().navigateUp()
                }
                PixEventCallback.Status.BACK_PRESSED ->{

                }
            }
        }

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rootContainerInMediaPicker,fragment)
        transaction.commit()
    }
}