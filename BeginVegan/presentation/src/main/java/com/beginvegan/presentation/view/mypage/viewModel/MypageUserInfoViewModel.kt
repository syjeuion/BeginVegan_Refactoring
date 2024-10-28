package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.mypage.MypageUserInfo
import com.beginvegan.domain.useCase.mypage.MypageUserInfoUseCase
import com.beginvegan.domain.useCase.veganType.PatchVeganTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypageUserInfoViewModel @Inject constructor(
    private val mypageUserInfoUseCase: MypageUserInfoUseCase,
    private val patchVeganTypeUseCase: PatchVeganTypeUseCase,
):ViewModel(){

    //Mypage 유저 정보
    private val _userInfo = MutableLiveData<MypageUserInfo>()
    val userInfo:LiveData<MypageUserInfo> = _userInfo

    init {
        getUserInfo()
    }
    private fun getUserInfo(){
        viewModelScope.launch {
            mypageUserInfoUseCase.invoke().onSuccess {
                _userInfo.value = it
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }

    //patchUserVeganType
    fun patchUserVeganType(veganType:String){
        viewModelScope.launch {
            patchVeganTypeUseCase.invoke("MYPAGE",veganType).onSuccess {
                Timber.d("patchUserVeganType onSuccess")
            }.onFailure {
                Timber.e("patchUserVeganType onFailure")
            }
        }
    }
}