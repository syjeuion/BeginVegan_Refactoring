package com.beginvegan.presentation.view.notification.viewModel.state

import com.beginvegan.domain.model.alarms.AlarmLists

data class NotificationState(
    val response: AlarmLists? = null,
    val isLoading: Boolean = false
)
