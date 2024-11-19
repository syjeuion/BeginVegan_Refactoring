package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.domain.useCase.mypage.MypageMyScrapUseCase
import com.beginvegan.presentation.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReviewViewModel @Inject constructor(
    private val myScrapUseCase: MypageMyScrapUseCase
):ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage:StateFlow<Int> get() = _currentPage

    private val _myReviewState = MutableStateFlow<NetworkResult<MutableList<MyReview>>>(NetworkResult.Loading)
    val myReviewState: StateFlow<NetworkResult<MutableList<MyReview>>> = _myReviewState
    fun setMyReviewList(newList:MutableList<MyReview>){
        val oldList = (_myReviewState.value as? NetworkResult.Success)?.data
        val addedList =
            if(oldList.isNullOrEmpty()) newList
            else (oldList + newList).toMutableList()

        _myReviewState.value = NetworkResult.Success(addedList)
    }

    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList

    private val _isReviewEmpty = MutableLiveData(false)
    val isReviewEmpty: LiveData<Boolean> = _isReviewEmpty

    fun reSetVieModel(){
        _isContinueGetList.value = true
        setMyReviewList(mutableListOf())
        _isReviewEmpty.value = false
    }

    fun getMyReview(){
        if(!isContinueGetList.value!!) return

        viewModelScope.launch {
            if(currentPage.value == 0) _myReviewState.value = NetworkResult.Loading
            myScrapUseCase.getMyReviewList(currentPage.value).collectLatest {
                it.onSuccess {list->
                    if(list.isEmpty()) {
                        if(currentPage.value==0) {
                            _isReviewEmpty.value = true
                            _myReviewState.value = NetworkResult.Empty
                        }
                        _isContinueGetList.value = false
                    }
                    else setMyReviewList(list.toMutableList())
                }.onFailure {e ->
                    _myReviewState.value = NetworkResult.Error(e)
                }
                _currentPage.value++
            }
        }
    }
}