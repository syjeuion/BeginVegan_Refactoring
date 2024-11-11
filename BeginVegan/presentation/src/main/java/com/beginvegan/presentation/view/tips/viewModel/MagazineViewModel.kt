package com.beginvegan.presentation.view.tips.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.tips.TipsMagazineDetail
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.domain.useCase.tips.TipsMagazineUseCase
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.view.tips.viewModel.state.MagazineListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MagazineViewModel @Inject constructor(
    private val tipsMagazineUseCase: TipsMagazineUseCase
) : ViewModel() {

    private val _magazineListState = MutableStateFlow<NetworkResult<MagazineListState>>(NetworkResult.Loading())
    val magazineListState: StateFlow<NetworkResult<MagazineListState>> = _magazineListState
    fun addMagazineList(list:MutableList<TipsMagazineItem>){
        _magazineListState.value = NetworkResult.Success(
            MagazineListState(list, false)
        )
    }

    private fun setMagazineListLoading(){
        _magazineListState.value = NetworkResult.Loading()
    }

    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList

    private val _magazineDetail = MutableLiveData<TipsMagazineDetail?>()
    val magazineDetail: LiveData<TipsMagazineDetail?> = _magazineDetail
    fun setMagazineDetail(item:TipsMagazineDetail?){
        _magazineDetail.value = item
    }

    fun resetViewModel(){
        _isContinueGetList.value = true
        addMagazineList(mutableListOf())
    }

    fun getMagazineList(page:Int) {
        setMagazineListLoading()
        viewModelScope.launch {
            tipsMagazineUseCase.getMagazineList(page).collectLatest {
                 it.onSuccess {result ->
                     if (result.isEmpty()) {
                         _isContinueGetList.value = false
                     } else {
                         addMagazineList(result.toMutableList())
                     }
                 }.onFailure {e->
                     _magazineListState.value = NetworkResult.Error(e.message!!)
                 }
            }
        }
    }

    fun getMagazineDetail(id:Int) {
        viewModelScope.launch {
            tipsMagazineUseCase.getMagazineDetail(id).onSuccess {
                _magazineDetail.value = it
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }
}