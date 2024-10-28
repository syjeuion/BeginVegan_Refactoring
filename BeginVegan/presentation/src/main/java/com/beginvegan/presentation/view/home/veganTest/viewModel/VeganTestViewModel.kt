package com.beginvegan.presentation.view.home.veganTest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.useCase.veganType.PatchVeganTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VeganTestViewModel @Inject constructor(
    private val veganTypeUseCase: PatchVeganTypeUseCase
): ViewModel() {

    private val _patchVeganTypeState = MutableStateFlow(false)
    val patchVeganTypeState:StateFlow<Boolean> = _patchVeganTypeState

    private val _userVeganTypeNum = MutableLiveData<Int>()
    val userVeganTypeNum: LiveData<Int> = _userVeganTypeNum

    fun setUserVeganTypeNum(veganTypeNum:Int){
        _userVeganTypeNum.value = veganTypeNum
    }

    fun patchVeganType(type:String, veganType:String) {
        viewModelScope.launch {
            veganTypeUseCase.invoke(type, veganType).onSuccess {
                _patchVeganTypeState.value = true
            }.onFailure {
                _patchVeganTypeState.value = false
            }
        }
    }

}