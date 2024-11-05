package com.beginvegan.presentation.view.image.gallery.view

import android.view.View
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogPhotoSelectBinding

class PhotoSelectDialog(
    private val isProfileImage: Boolean
): BaseDialogFragment<DialogPhotoSelectBinding>(DialogPhotoSelectBinding::inflate) {
    interface DialogPhotoSelectClickListener{
        fun onClickCamera()
        fun onClickGallery()
        fun onClickDefault()
    }

    private var listener: DialogPhotoSelectClickListener? = null

    override fun init() {
        hasProfileImage()
        binding.btnGallery.setOnClickListener {
            onGallery()
        }
        binding.btnCamera.setOnClickListener {
            onCamera()
        }
        binding.btnDefault.setOnClickListener {
            onDefault()
        }
    }
    private fun onCamera(){
        listener?.onClickCamera()
    }
    private fun onGallery(){
        listener?.onClickGallery()
    }
    private fun onDefault(){
        listener?.onClickDefault()
    }

    private fun hasProfileImage(){
        if(isProfileImage){
            binding.btnDefault.visibility = View.VISIBLE
        }else{
            binding.btnDefault.visibility = View.GONE
        }
    }

    fun setDialogClickListener(listener: DialogPhotoSelectClickListener){
        this.listener = listener
    }
}