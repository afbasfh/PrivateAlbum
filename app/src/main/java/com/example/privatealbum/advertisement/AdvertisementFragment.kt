package com.example.privatealbum.advertisement

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.privatealbum.R
import com.example.privatealbum.databinding.FragmentAdvertisementBinding
import com.example.privatealbum.delayNavigate
import com.example.privatealbum.welcome.startAnimationWithListener


class AdvertisementFragment : Fragment() {

    lateinit var binding: FragmentAdvertisementBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvertisementBinding.inflate(
            inflater,container,false)

        Glide
            .with(this)
            .load(Network.fetchImage())
            .into(binding.advImageView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timeoutJumpView.onClickListener = {
            it.stopTimer()
        }
    }
    override fun onResume() {
        super.onResume()
        //缩小动画
        binding.advImageView.startAnimationWithListener(R.anim.image_scale_anim)
        //开启定时任务
        binding.timeoutJumpView.startTimer(
            3000,
            onAniatorEnd = {
                findNavController().delayNavigate(
                    R.id.action_advertisementFragment_to_unlockFragment,
                    lifecycleScope
                )
            })
    }
}

















