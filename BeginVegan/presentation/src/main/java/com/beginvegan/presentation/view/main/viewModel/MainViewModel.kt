package com.beginvegan.presentation.view.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.core_fcm.useCase.FcmTokenUseCase
import com.beginvegan.domain.model.map.RecommendRestaurant
import com.beginvegan.domain.useCase.map.restaurant.GetNearRestaurantUseCase
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
class MainViewModel @Inject constructor(
    private val homeUserInfoUseCase: HomeUserInfoUseCase,
    private val getNearRestaurantUseCase: GetNearRestaurantUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase
) : ViewModel() {

    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> get() = _nickName.asStateFlow()

    private val _userLevel = MutableStateFlow("")
    val userLevel: StateFlow<String> get() = _userLevel.asStateFlow()

    private val _tipsMoveToRecipe = MutableLiveData(false)
    val tipsMoveToRecipe: LiveData<Boolean> = _tipsMoveToRecipe
    fun setTipsMoveToRecipe(isMove: Boolean) {
        _tipsMoveToRecipe.value = isMove
    }

    private val _fromTest = MutableLiveData(false)
    val fromTest: LiveData<Boolean> = _fromTest
    fun setFromTest(isMove: Boolean) {
        _fromTest.value = isMove
    }

    private val _fromMyMagazine = MutableLiveData(false)
    val fromMyMagazine: LiveData<Boolean> = _fromMyMagazine
    fun setFromMyMagazine(isMove: Boolean) {
        _fromMyMagazine.value = isMove
    }

    private val _mapMoveToReview = MutableLiveData(false)
    val mapMoveToReview: LiveData<Boolean> = _mapMoveToReview
    fun setMapMoveToReview(isMove: Boolean) {
        _mapMoveToReview.value = isMove
    }

    private val _recommendRestaurantList = MutableStateFlow<List<RecommendRestaurant>>(emptyList())
    val recommendRestaurantList: StateFlow<List<RecommendRestaurant>> get() = _recommendRestaurantList.asStateFlow()


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
                    _userLevel.value = userInfo.userLevel
                }
        }
    }

    fun getRecommendRestaurant() {
        viewModelScope.launch(Dispatchers.IO) {
            getNearRestaurantUseCase.getNearRestaurantWithOutPermission(3)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getRecommendRestaurant Exception")
                }
                .collect { recommendList ->
                    Timber.d("getRecommendRestaurant list $recommendList")
                    _recommendRestaurantList.value = recommendList
                }
        }
    }

    fun postFcmPush(){
        viewModelScope.launch(Dispatchers.IO) {
            fcmTokenUseCase.postFcmMessage(
                "가나다라마바사","테스트 메시지 입니다.","MYPAGE",1,"REVIEW_RECOMMEND",userLevel = null
            )
        }
    }

}