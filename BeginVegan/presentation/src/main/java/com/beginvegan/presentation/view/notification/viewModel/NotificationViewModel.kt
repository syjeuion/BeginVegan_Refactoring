package com.beginvegan.presentation.view.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.useCase.alarms.UnreadAlarmUseCase
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.view.notification.viewModel.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val unreadAlarmUseCase: UnreadAlarmUseCase
):ViewModel(){

    private val _alarmLists = MutableStateFlow<NetworkResult<NotificationState>>(NetworkResult.Loading())
    val alarmLists: StateFlow<NetworkResult<NotificationState>> = _alarmLists

    init {
        viewModelScope.launch {
            getAlarmList()
        }
    }

    private suspend fun getAlarmList(){
        unreadAlarmUseCase.invoke().catch {
            _alarmLists.value = NetworkResult.Loading()
        }.collectLatest {result ->
            result.onSuccess {lists ->
                _alarmLists.value = NetworkResult.Success(NotificationState(
                    response = lists,
                    isLoading = false
                ))
            }.onFailure {
                _alarmLists.value = NetworkResult.Error("GetAlarms Failed")
            }

//            result ->
//            when(result){
//                -> {
//                    Timber.d("NetworkResult.Success")
//                    _alarmLists.value = NetworkResult.Success(NotificationState(
//                        response = result.data,
//                        isLoading = false
//                    )
//                    )
//                }
//                is NetworkResult.Error ->{
//                    Timber.e("NetworkResult.Error")
//                    _alarmLists.value = NetworkResult.Error(result.message!!)
//                }
//                is NetworkResult.Loading ->{
//                    Timber.e("NetworkResult.Loading")
//                    _alarmLists.value = NetworkResult.Loading(true)
//                }
//            }
        }
    }
}