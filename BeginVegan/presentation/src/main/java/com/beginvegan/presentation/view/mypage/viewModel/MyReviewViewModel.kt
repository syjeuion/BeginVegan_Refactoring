package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.domain.useCase.mypage.MypageMyScrapUseCase
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.view.mypage.viewModel.state.MyReviewState
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
    private val _myReviewState = MutableStateFlow<NetworkResult<MyReviewState>>(
        NetworkResult.Loading())
    val myReviewState: StateFlow<NetworkResult<MyReviewState>> = _myReviewState
    fun setMyReviewList(list:MutableList<MyReview>){
        _myReviewState.value = NetworkResult.Success(
            MyReviewState(list, false)
        )
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

    fun getMyReview(page:Int){
        viewModelScope.launch {
            myScrapUseCase.getMyReviewList(page).collectLatest {
                it.onSuccess {list->
                    if(list.isEmpty()) {
                        if(page==0) _isReviewEmpty.value = true
                        _isContinueGetList.value = false
                    }
                    else setMyReviewList(list.toMutableList())
                }.onFailure {
                    _myReviewState.value = NetworkResult.Error("getMyReviewList Failure")
                }
            }
        }
    }
}