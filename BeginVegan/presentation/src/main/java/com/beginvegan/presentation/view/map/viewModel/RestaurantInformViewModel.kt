package com.beginvegan.presentation.view.map.viewModel

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
class RestaurantInformViewModel @Inject constructor(
    private val informUseCase: InformUseCase
) : ViewModel() {

    private val _informState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val informState: StateFlow<UiState<Boolean>> = _informState

    fun informNewRestaurant(name: String, location: String, content: String) {
        viewModelScope.launch {
            _informState.value = UiState.Loading
            try {
                informUseCase.informNewRestaurant(name, location, content)
                _informState.value = UiState.Success(true)
            } catch (e: Exception) {
                _informState.value = UiState.Failure(e.message ?: "Unknown Error")
            }

        }
    }
}