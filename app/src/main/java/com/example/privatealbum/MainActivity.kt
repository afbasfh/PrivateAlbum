package com.example.privatealbum

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.privatealbum.databinding.ActivityMainBinding
import com.example.privatealbum.db.ALBUM_TYPE_IMAGE
import com.example.privatealbum.db.ALBUM_TYPE_VIDEO
import com.example.privatealbum.db.SharedViewModel
import com.example.privatealbum.home.album.ClickEvents
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.helpers.pixFragment
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio

class MainActivity : AppCompatActivity() {
        lateinit var binding:ActivityMainBinding
    private val model:SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding.model = model
        binding.clickEvent = ClickEvents()
        binding.lifecycleOwner = this

        //去掉BottomNavigationView的背景
        binding.bottomNavigationView.background = null
        //让第二个menuitem不能被点击
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        //给BottomNavigationView设置navController
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

        //监听选项的点击事件
        binding.bottomNavigationView.setOnItemSelectedListener {
            //控制不重复点击
            if (it.itemId == R.id.imageAlbumFragment
                && binding.bottomNavigationView.selectedItemId != R.id.imageAlbumFragment){
                model.type = ALBUM_TYPE_IMAGE
                navHostFragment.navController.navigate(R.id.imageAlbumFragment)

            }

            if (it.itemId == R.id.videoAlbumFragment
                && binding.bottomNavigationView.selectedItemId != R.id.videoAlbumFragment){
                model.type = ALBUM_TYPE_VIDEO
                navHostFragment.navController.navigate(R.id.videoAlbumFragment)
            }
            true
        }

        requestReadPermission()

    }
    fun requestReadPermission(){
        val result = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        if (result != PackageManager.PERMISSION_GRANTED){
            val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

            }
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

















