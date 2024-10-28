package com.beginvegan.presentation.view.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.map.HistorySearch
import com.beginvegan.domain.useCase.map.search.DeleteAllHistorySearchUseCase
import com.beginvegan.domain.useCase.map.search.DeleteHistorySearchUseCase
import com.beginvegan.domain.useCase.map.search.GetAllHistorySearchUseCase
import com.beginvegan.domain.useCase.map.search.InsertHistorySearchUseCase
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
class VeganMapSearchViewModel @Inject constructor(
    private val insertHistorySearchUseCase: InsertHistorySearchUseCase,
    private val deleteHistorySearchUseCase: DeleteHistorySearchUseCase,
    private val getAllHistorySearchUseCase: GetAllHistorySearchUseCase,
    private val deleteAllHistorySearchUseCase: DeleteAllHistorySearchUseCase
) : ViewModel() {

    private val _searchList = MutableStateFlow<List<HistorySearch>>(emptyList())
    val searchList: StateFlow<List<HistorySearch>> get() = _searchList

    fun fetchAllHistory() {
        viewModelScope.launch {
            getAllHistorySearchUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "ViewModel Get HistorySearch Exception")
                    _searchList.value = emptyList()
                }
                .collect { historySearchList ->
                    Timber.d("collect search list $historySearchList")
                    _searchList.value = historySearchList
                }
        }
    }


    fun insertHistory(description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            insertHistorySearchUseCase(description)
        }
    }

    fun deleteHistory(historySearch: HistorySearch) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHistorySearchUseCase(historySearch)
        }
    }

    fun deleteAllHistory(){
        viewModelScope.launch (Dispatchers.IO){
            deleteAllHistorySearchUseCase()
        }
    }
}