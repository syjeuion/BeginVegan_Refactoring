package com.beginvegan.presentation.view.image.camera.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _isCameraPermissionGranted = MutableLiveData<Boolean>()
    val isCameraPermissionGranted: LiveData<Boolean> get() = _isCameraPermissionGranted

    private val _isStoragePermissionGranted = MutableLiveData<Boolean>()
    val isStoragePermissionGranted: LiveData<Boolean> get() = _isStoragePermissionGranted

    fun updateCameraPermissionState(granted: Boolean) {
        _isCameraPermissionGranted.value = granted
    }

    fun updateStoragePermissionState(granted: Boolean) {
        _isStoragePermissionGranted.value = granted
    }
}