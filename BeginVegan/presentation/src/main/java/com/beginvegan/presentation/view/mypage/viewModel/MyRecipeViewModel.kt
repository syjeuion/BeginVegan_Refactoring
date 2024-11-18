package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.useCase.mypage.MypageMyScrapUseCase
import com.beginvegan.presentation.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
    private val myScrapUseCase: MypageMyScrapUseCase
):ViewModel(){

    //reset
    fun resetViewModel(){
        _isContinueGetList.value = true
        _isRecipeEmpty.value = false
        _currentPage.value = 0
    }
    private val _currentPage = MutableStateFlow(0)
    val currentPage:StateFlow<Int> = _currentPage

    //RecipeList
    private val _myRecipesState = MutableStateFlow<NetworkResult<MutableList<TipsRecipeListItem>>>(NetworkResult.Loading)
    val myRecipesState: StateFlow<NetworkResult<MutableList<TipsRecipeListItem>>> = _myRecipesState

    private fun addMyRecipeList(newList: MutableList<TipsRecipeListItem>){
        val oldList = (_myRecipesState.value as? NetworkResult.Success)?.data
        val addedList =
            if(oldList.isNullOrEmpty()) newList
            else (oldList + newList).toMutableList()

        _myRecipesState.value = NetworkResult.Success(addedList)
    }
    fun setMyRecipeList(newList:MutableList<TipsRecipeListItem>){
        _myRecipesState.value = NetworkResult.Success(newList)
    }

    //Dialog Bookmark 클릭 시 update
    fun updateRecipeListItem(position:Int, item: TipsRecipeListItem){
        val oldList = (_myRecipesState.value as? NetworkResult.Success)?.data
        oldList?.let{
            val newList = it.toMutableList()
            newList[position] = item
            _myRecipesState.value = NetworkResult.Success(newList)
        }
    }
    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList

    private val _isRecipeEmpty = MutableLiveData(false)
    val isRecipeEmpty: LiveData<Boolean> = _isRecipeEmpty

    fun getMyRecipe(){
        if(!isContinueGetList.value!!) return

        viewModelScope.launch {
            if(currentPage.value == 0) _myRecipesState.value = NetworkResult.Loading
            myScrapUseCase.getMyRecipeList(currentPage.value).collectLatest {
                it.onSuccess {list->
                    if(list.isEmpty()) {
                        if(_myRecipesState.value is NetworkResult.Loading){
                            _isRecipeEmpty.value = true
                            _myRecipesState.value = NetworkResult.Empty
                        }
                        _isContinueGetList.value = false
                    }
                    else addMyRecipeList(list.toMutableList())
                }.onFailure {e ->
                    _myRecipesState.value = NetworkResult.Error(e)
                }
                _currentPage.value++
            }
        }
    }

}