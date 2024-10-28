package com.beginvegan.presentation.view.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.useCase.map.search.GetRestaurantSearchResultUseCase
import com.beginvegan.domain.useCase.userInfo.HomeUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VeganMapResultViewModel @Inject constructor(
    private val homeUserInfoUseCase: HomeUserInfoUseCase,
    private val getRestaurantSearchResultUseCase: GetRestaurantSearchResultUseCase
) : ViewModel() {
    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> get() = _nickName.asStateFlow()

    private val _searchResultList = MutableStateFlow<List<VeganMapRestaurant>>(mutableListOf())
    val searchResultList: StateFlow<List<VeganMapRestaurant>> get() = _searchResultList


    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            homeUserInfoUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getUserInfo Exception")
                }
                .collect { userInfo ->
                    Timber.d("getUserInfo $userInfo")
                    _nickName.value = userInfo.nickName
                }
        }
    }

    fun getRestaurantSearchResult(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ) {
        viewModelScope.launch {
            getRestaurantSearchResultUseCase.invoke(page, latitude, longitude, searchWord, filter)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getRestaurantSearchResult Exception")
                }.collect { veganRestaurantList ->
                    Timber.d("getRestaurantSearchResult list $veganRestaurantList")
                    _searchResultList.value = veganRestaurantList
                }
        }
    }
}