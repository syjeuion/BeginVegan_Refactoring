package com.beginvegan.presentation.view.restaurant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.useCase.inform.InformUseCase
import com.beginvegan.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantModifyViewModel @Inject constructor(
    private val modifyUseCase: InformUseCase
) : ViewModel() {
    private val _modifyState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val modifyState: StateFlow<UiState<Boolean>> = _modifyState

    fun modifyRestaurant(restaurantId: Long, content: String) {
        viewModelScope.launch {
            _modifyState.value = UiState.Loading
            try {
                modifyUseCase.informModifyRestaurant(restaurantId, content)
                _modifyState.value = UiState.Success(true)
            } catch (e: Exception) {
                _modifyState.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }
}