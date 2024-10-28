package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.useCase.mypage.MypagePushUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypagePushViewModel @Inject constructor(
    private val mypagePushUseCase: MypagePushUseCase
):ViewModel() {

    //getPushState
    private val _userPushState = MutableLiveData(false)
    val userPushState: LiveData<Boolean> = _userPushState

    fun getPushState(){
        viewModelScope.launch {
            mypagePushUseCase.getPushState().onSuccess {
                Timber.d("getPushState onSuccess")
                _userPushState.value = it
            }.onFailure {
                Timber.e("getPushState onFailure")
            }
        }
    }

    //patchPush
    fun patchPush(){
        viewModelScope.launch {
            mypagePushUseCase.patchPush().onSuccess {
                Timber.d("patchPush onSuccess")
            }.onFailure {
                Timber.e("patchPush onFailure")
            }
        }
    }
}