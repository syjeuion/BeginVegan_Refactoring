package com.beginvegan.presentation.view.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.alarms.AlarmLists
import com.beginvegan.domain.useCase.alarms.UnreadAlarmUseCase
import com.beginvegan.presentation.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val unreadAlarmUseCase: UnreadAlarmUseCase
):ViewModel(){

    private val _alarmLists = MutableStateFlow<NetworkResult<AlarmLists>>(NetworkResult.Loading)
    val alarmLists: StateFlow<NetworkResult<AlarmLists>> = _alarmLists

    init {
        getAlarmList()
    }

    private fun getAlarmList(){
        viewModelScope.launch {
            _alarmLists.value = NetworkResult.Loading
            unreadAlarmUseCase.invoke().collectLatest {
                it.onSuccess { list ->
                    _alarmLists.value = NetworkResult.Success(list)
                }.onFailure { e ->
                    _alarmLists.value = NetworkResult.Error(e)
                }
            }
        }
    }
}