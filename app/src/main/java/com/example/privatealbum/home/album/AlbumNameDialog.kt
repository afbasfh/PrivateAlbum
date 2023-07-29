package com.example.privatealbum.home.album

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.privatealbum.databinding.AlbumNameDialogBinding


object AlbumNameDialog{
    private lateinit var dialog:AlertDialog
    fun show(context: Context, onOk:(String)->Unit = {}, onCancel:()->Unit = {}){
        val inflater = LayoutInflater.from(context)
        val binding = AlbumNameDialogBinding.inflate(inflater)
        dialog = AlertDialog
            .Builder(context)
            .setView(binding.root)
            .setCancelable(true)
            .create()
        binding.sureBtn.setOnClickListener {
            if (binding.nameEditText.text.isNotEmpty()){
                onOk(binding.nameEditText.text.toString())
                dialog.dismiss()
            }else {
                Toast.makeText(context,"相册名不能为空",Toast.LENGTH_LONG).show()
            }
        }
        binding.cancelBtn.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }
        dialog.show()
    }
    fun hide(){
        dialog.dismiss()
    }
}