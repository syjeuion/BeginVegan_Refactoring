package com.beginvegan.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.useCase.mypage.MypageMyScrapUseCase
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.view.mypage.viewModel.state.MyRecipeState
import com.beginvegan.presentation.view.tips.viewModel.state.RecipeListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
    private val myScrapUseCase: MypageMyScrapUseCase
):ViewModel(){

    //reset
    fun resetViewModel(){
        _isContinueGetList.value = true
        setMyRecipeList(mutableListOf())
        _isRecipeEmpty.value = false
    }

    //RecipeList
    private val _myRecipesState = MutableStateFlow<NetworkResult<MyRecipeState>>(NetworkResult.Loading())
    val myRecipesState: StateFlow<NetworkResult<MyRecipeState>> = _myRecipesState
    private fun addMyRecipeList(newList:MutableList<TipsRecipeListItem>){
        var oldList = _myRecipesState.value.data?.response
        if(oldList==null) oldList = newList
        else oldList.addAll(newList)
        _myRecipesState.value = NetworkResult.Success(
            MyRecipeState(response = oldList, isLoading = false)
        )
    }
    fun setMyRecipeList(newList:MutableList<TipsRecipeListItem>){
        _myRecipesState.value = NetworkResult.Success(
            MyRecipeState(response = newList, isLoading = false)
        )
    }

    fun updateRecipeListItem(position:Int, item: TipsRecipeListItem){
        with(_myRecipesState){
            value.data?.response!![position] = item
            value = NetworkResult.Success(
                MyRecipeState(
                    value.data?.response!!, false
                )
            )
        }
    }
    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList

    private val _isRecipeEmpty = MutableLiveData(false)
    val isRecipeEmpty: LiveData<Boolean> = _isRecipeEmpty

    fun getMyRecipe(page:Int){
        viewModelScope.launch {
            _myRecipesState.value = NetworkResult.Loading()
            myScrapUseCase.getMyRecipeList(page).collectLatest {
                it.onSuccess {list->
                    if(list.isEmpty()) {
                        if(page==0) _isRecipeEmpty.value = true
                        _isContinueGetList.value = false
                    }
                    addMyRecipeList(list.toMutableList())
                }.onFailure {
                    _myRecipesState.value = NetworkResult.Error("getMyMagazineList Failure")
                }
            }
        }
    }

}