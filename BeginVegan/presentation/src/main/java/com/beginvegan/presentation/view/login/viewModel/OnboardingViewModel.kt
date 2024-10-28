package com.beginvegan.presentation.view.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.VeganTypes
import com.beginvegan.domain.useCase.userInfo.SaveUserInfoUseCase
import com.beginvegan.presentation.view.image.gallery.model.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserInfoUseCase: SaveUserInfoUseCase,

) : ViewModel() {

    val nickName = MutableLiveData<String>()
    val veganLevel = MutableLiveData<String>()


    private var _validNickName = MutableLiveData<Boolean>()
    private var _validVeganLevel = MutableLiveData<Boolean>()

    // 이름, 비건 레벨 유효성 검사 LiveData
    val validNickName: LiveData<Boolean> = _validNickName
    val validVeganLevel: LiveData<Boolean> = _validVeganLevel


    // Profile Image LiveData
    private val _profileImageUri = MutableLiveData<GalleryImage?>(null)
    val profileImageUri: LiveData<GalleryImage?> get() = _profileImageUri

    private val _userInfoState = MutableLiveData<Boolean>(false)
    val userInfoState: LiveData<Boolean> get() = _userInfoState

    init {
        _validNickName.value = false
        _validVeganLevel.value = false
    }



    fun updateProfileImageUri(data: GalleryImage?) {
        _profileImageUri.value = data
    }

    fun validateNickName(input: String): Boolean {
        // 입력이 2~12자인지 확인
        if (input.length !in 2..12) {
            return false
        }

        // 입력이 한글 또는 영문으로 이루어져 있는지 확인
        if (!input.all { it in '가'..'힣' || it in 'a'..'z' || it in 'A'..'Z' }) {
            return false
        }

        // 입력에 공백이 포함되어 있는지 확인
        if (input.contains(" ")) {
            return false
        }

        // 모든 조건을 만족하면 true 반환
        return true
    }

    fun setValidVeganLevel() {
        _validVeganLevel.value = true
    }

    fun setValidNickName(check: Boolean) {
        _validNickName.value = check
    }

    fun checkValid(): Boolean {
        return validNickName.value ?: false && validVeganLevel.value ?: false
    }

    fun saveUserInfo(
        nickName: String,
        veganType: String,
    ) {
        viewModelScope.launch {
            var isDefaultBoolean = false
            val imageUri: String? = profileImageUri.value?.imagePath.toString()
            val type = VeganTypes.fromEng(veganType)
            if (imageUri == null) {
                isDefaultBoolean = true
            }

            Timber.d("이미지 URI $imageUri")
            saveUserInfoUseCase.invoke(nickName, type.toString(), isDefaultBoolean, imageUri)
                .onSuccess {
                    Timber.d("${it.message}")
                    _userInfoState.value = true
                }.onFailure {
                    Timber.d("${it.message}")
                    _userInfoState.value = false
                }
        }
    }
}