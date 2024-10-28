package com.beginvegan.presentation.view.restaurant.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.map.Menus
import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.model.mypage.MypageUserInfo
import com.beginvegan.domain.model.review.RestaurantReview
import com.beginvegan.domain.useCase.map.restaurant.GetRestaurantDetailUseCase
import com.beginvegan.domain.useCase.mypage.MypageUserInfoUseCase
import com.beginvegan.domain.useCase.review.RestaurantReviewUseCase
import com.beginvegan.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val restaurantReviewUseCase: RestaurantReviewUseCase,
    private val mypageUserInfoUseCase: MypageUserInfoUseCase,
) : ViewModel() {

    private val _userInfo = MutableLiveData<MypageUserInfo>()
    val userInfo: LiveData<MypageUserInfo> = _userInfo

    private val _restaurantDetail = MutableStateFlow(RestaurantDetail())
    val restaurantDetail: StateFlow<RestaurantDetail> get() = _restaurantDetail


    private val _menuList = MutableStateFlow<List<Menus>>(mutableListOf())
    val menuList: StateFlow<List<Menus>> get() = _menuList

    private val _reviewList = MutableStateFlow<List<RestaurantReview>>(mutableListOf())
    val reviewList: StateFlow<List<RestaurantReview>> get() = _reviewList

    private val _isMoreMenus = MutableLiveData<Boolean>(false)
    val isMoreMenus: LiveData<Boolean> get() = _isMoreMenus

    private val _isMoreReviews = MutableLiveData<Boolean>(false)
    val isMoreReviews: LiveData<Boolean> get() = _isMoreReviews

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState

    private fun getUserInfo() {
        viewModelScope.launch {
            mypageUserInfoUseCase.invoke().onSuccess {
                _userInfo.value = it
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }

    fun getRestaurantDetail(restaurantId: Long, latitude: String, longitude: String) {

        viewModelScope.launch {
            getRestaurantDetailUseCase.invoke(restaurantId, latitude, longitude)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getRestaurantDetail Exception")
                }
                .collect { restaurantDetail ->
                    Timber.d("getRestaurantDetail data $restaurantDetail")
                    _restaurantDetail.value = restaurantDetail
                    _menuList.value = restaurantDetail.menus
                    getUserInfo()
                }
        }
    }

    fun getRestaurantReviewList(restaurantId: Long, page: Int, isPhoto: Boolean, filter: String) {
        viewModelScope.launch {
            restaurantReviewUseCase.getReviewByRestaurantId(restaurantId, page, isPhoto, filter)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getRestaurantReviewList Exception")
                }
                .collect { reviewList ->
                    Timber.d("getRestaurantReviewList data $reviewList")
                    _reviewList.value = reviewList
                }
        }
    }

    fun deleteRestaurantReview(reviewId: Long) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                restaurantReviewUseCase.deleteReview(reviewId)
                _deleteState.value = UiState.Success(true)
            } catch (e: Exception) {
                _deleteState.value = UiState.Failure(e.message ?: "Unknown Error")
            }

        }
    }
}