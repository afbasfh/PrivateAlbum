package com.example.privatealbum.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.privatealbum.R
import com.example.privatealbum.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeFragment : Fragment() {
    lateinit var binding:FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(
            inflater,container,false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.titleTextView.startAnimationWithListener(
            R.anim.text_up_scale_anim,
            onEnd = {    //真正的实现
                navigateToNextScreen()

            }
        )
    }

    fun navigateToNextScreen(){
        //3s后切换
        lifecycleScope.launch(Dispatchers.IO){
            delay(3000)
            withContext(Dispatchers.Main){
                findNavController().navigate(R.id.action_welcomeFragment_to_advertisementFragment)
            }
        }
    }
}