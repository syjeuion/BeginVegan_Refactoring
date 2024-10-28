package com.beginvegan.presentation.view.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.useCase.map.restaurant.GetNearRestaurantMapUseCase
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
class VeganMapViewModel @Inject constructor(
    private val homeUserInfoUseCase: HomeUserInfoUseCase,
    private val getNearRestaurantMapUseCase: GetNearRestaurantMapUseCase
) : ViewModel() {

    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> get() = _nickName.asStateFlow()

    private val _restaurantList = MutableStateFlow<List<VeganMapRestaurant>>(mutableListOf())
    val restaurantList: StateFlow<List<VeganMapRestaurant>> get() = _restaurantList

    fun fetchNearRestaurantMap(page: Int, latitude: Double, longitude: Double) {
        Timber.d("fetchNearRestaurantMap")
        val formattedLatitude = String.format("%.6f", latitude)
        val formattedLongitude = String.format("%.6f", longitude)
        viewModelScope.launch {
            getNearRestaurantMapUseCase.invoke(page, formattedLatitude, formattedLongitude)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "fetchNearRestaurantMap Exception")
                }
                .collect { veganRestaurantList ->
                    Timber.d("fetchNearRestaurantMap list $veganRestaurantList")
                    _restaurantList.value = veganRestaurantList
                }
        }

    }
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


}