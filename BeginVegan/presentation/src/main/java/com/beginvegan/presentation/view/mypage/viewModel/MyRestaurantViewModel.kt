package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
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
class MyRestaurantViewModel @Inject constructor(
    private val myScrapUseCase: MypageMyScrapUseCase
) : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage:StateFlow<Int> get() = _currentPage

    private val _myRestaurantState =
        MutableStateFlow<NetworkResult<MutableList<MypageMyRestaurantItem>>>(NetworkResult.Loading)
    val myRestaurantState: StateFlow<NetworkResult<MutableList<MypageMyRestaurantItem>>> = _myRestaurantState

    fun setMyRestaurantList(newList: MutableList<MypageMyRestaurantItem>) {
        val oldList = (_myRestaurantState.value as? NetworkResult.Success)?.data
        val addedList =
            if(oldList.isNullOrEmpty()) newList
            else (oldList + newList).toMutableList()

        _myRestaurantState.value = NetworkResult.Success(addedList)
    }

    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList

    private val _isRestaurantEmpty = MutableLiveData(false)
    val isRestaurantEmpty: LiveData<Boolean> = _isRestaurantEmpty

    fun reSetVieModel() {
        _isContinueGetList.value = true
        setMyRestaurantList(mutableListOf())
        _isRestaurantEmpty.value = false
        _currentPage.value = 0
    }

    fun getMyRestaurant(latitude: String, longitude: String) {
        if(!isContinueGetList.value!!) return

        viewModelScope.launch {
            if(currentPage.value == 0) _myRestaurantState.value = NetworkResult.Loading
            myScrapUseCase.getMyRestaurantList(currentPage.value, latitude, longitude).collectLatest {
                it.onSuccess { list ->
                    if (list.isEmpty()) {
                        if (currentPage.value == 0) {
                            _isRestaurantEmpty.value = true
                            _myRestaurantState.value = NetworkResult.Empty
                        }
                        _isContinueGetList.value = false
                    }
                    else setMyRestaurantList(list.toMutableList())
                }.onFailure {e ->
                    _myRestaurantState.value = NetworkResult.Error(e)
                }
                _currentPage.value++
            }
        }
    }
}