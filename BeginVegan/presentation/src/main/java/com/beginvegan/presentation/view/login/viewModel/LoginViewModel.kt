package com.beginvegan.presentation.view.login.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.core_fcm.useCase.FcmTokenUseCase
import com.beginvegan.domain.useCase.auth.SignInUseCase
import com.beginvegan.domain.useCase.device.IsFirstRunUseCase
import com.beginvegan.domain.useCase.device.UpdateFirstRunRecordUseCase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val isFirstRunUseCase: IsFirstRunUseCase,
    private val saveFirstRunRecordUseCase: UpdateFirstRunRecordUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase
) : ViewModel() {

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.d("KaKao Login | CallBack 로그인 실패 $error")
        } else if (token != null) {
            Timber.d("KaKao Login | CallBack 로그인 성공 ${token.accessToken}")
            fetchKakaoUserData()

        }
    }

    val isFirstRun = MutableLiveData<Boolean>()

    private val _loginState = MutableLiveData(false)
    val loginState: LiveData<Boolean> = _loginState

    var additionalInfoProvided: Boolean = false

    private fun signIn(email: String, providerId: String) {
        viewModelScope.launch {
            signInUseCase.invoke(email, providerId).onSuccess {
                Timber.d("$it")
                additionalInfoProvided = it.additionalInfo
                _loginState.value = true
            }.onFailure {
                _loginState.value = false
            }
        }
    }

    fun kakaoLogin(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            Timber.d("카카오 로그인: isKakaoTalkLoginAvailable ")
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                Timber.d("카카오 로그인: loginWithKakaoTalk ")

                if (error != null) {
                    Timber.d("카카오톡으로 로그인 실패 $error")
                    Timber.d("Keyhash: ${Utility.getKeyHash(context)}")
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
                } else if (token != null) {
                    Timber.d("카카오톡으로 로그인 성공 ${token.accessToken}")
                    fetchKakaoUserData()
                }
            }
        } else {
            Timber.d("카카오 계정 로그인")
            UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
        }
    }

    private fun fetchKakaoUserData() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Timber.d("KaKao User 사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Timber.d(
                    "KaKao User 사용자 정보 요청 성공\n회원번호: ${user.id}\n이메일: ${user.kakaoAccount?.email}"
                )
                user.kakaoAccount?.email?.let { signIn(it, user.id.toString()) }
            }
        }
        checkHasFcmToken()
    }


    /**
     * HasFcmToken 체크
     */
    private fun checkHasFcmToken() {
        viewModelScope.launch {
            fcmTokenUseCase.getHasFcmToken().onSuccess {
                if (!it) fcmTokenUseCase.resetToken()
            }.onFailure {
                Timber.e("getHasFcmToken 에러")
            }
        }
    }

    fun checkFirstRun() {
        viewModelScope.launch(Dispatchers.IO) {
            val firstRun = isFirstRunUseCase()
            isFirstRun.postValue(firstRun)
        }
    }

    fun fetchFirstRunRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            saveFirstRunRecordUseCase()
        }
    }

}
