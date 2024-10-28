package com.beginvegan.presentation.view.image.gallery.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beginvegan.presentation.view.image.gallery.model.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(): ViewModel(){

    private val _permissionState = MutableLiveData<Boolean?>(null)
    val permissionState: LiveData<Boolean?> get() = _permissionState

    private val _imageList = MutableLiveData<MutableList<GalleryImage>>(mutableListOf())
    val imageList: LiveData<MutableList<GalleryImage>> get() = _imageList

    private val _selectImage = MutableLiveData<GalleryImage?>(null)
    val selectImage: LiveData<GalleryImage?> get() = _selectImage

    private val _resultImage = MutableLiveData<GalleryImage>()
    val resultImage: LiveData<GalleryImage> get() = _resultImage

    fun updateResultImage(resultImage: GalleryImage){
        _resultImage.value = resultImage
    }
    fun updateSelectImage(imageData: GalleryImage){
        _selectImage.value = imageData
    }
    fun updatePermissionState(isPermission: Boolean){
        _permissionState.value = isPermission
    }
    fun fetchGalleryImage(imageList: MutableList<GalleryImage>){
        _imageList.value = imageList
        Timber.d("fetch ImageList $_imageList ")
    }
}