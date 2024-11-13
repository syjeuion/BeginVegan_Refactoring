package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.domain.useCase.mypage.MypageMyScrapUseCase
import com.beginvegan.presentation.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyMagazineViewModel @Inject constructor(
    private val myScrapUseCase: MypageMyScrapUseCase
):ViewModel() {

    private val _myMagazinesState = MutableStateFlow<NetworkResult<MutableList<MypageMyMagazineItem>>>(NetworkResult.Loading)
    val myMagazineState: StateFlow<NetworkResult<MutableList<MypageMyMagazineItem>>> = _myMagazinesState
    private fun setMyMagazineList(newList:MutableList<MypageMyMagazineItem>){
        val oldList = (_myMagazinesState.value as? NetworkResult.Success)?.data
        val addedList =
            if(oldList.isNullOrEmpty()) newList
            else (oldList + newList).toMutableList()
        Timber.e("addedList: ${addedList.size}")
        _myMagazinesState.value = NetworkResult.Success(addedList)
    }

    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList
    fun reSetVieModel(){
        _isContinueGetList.value = true
        _isMagazineEmpty.value = false
    }

    private val _isMagazineEmpty = MutableLiveData(false)
    val isMagazineEmpty:LiveData<Boolean> = _isMagazineEmpty

    fun getMyMagazine(page:Int){
        viewModelScope.launch {
            if(page == 0) _myMagazinesState.value = NetworkResult.Loading
            myScrapUseCase.getMyMagazineList(page).collectLatest {
                it.onSuccess {list->
                    if(list.isEmpty()) {
                        if(page==0){
                            _isMagazineEmpty.value = true
                            _myMagazinesState.value = NetworkResult.Empty
                        }
                        _isContinueGetList.value = false
                    }
                    else setMyMagazineList(list.toMutableList())
                }.onFailure {e ->
                    _myMagazinesState.value = NetworkResult.Error(e)
                }
            }
        }
    }
}