package com.beginvegan.presentation.view.tips.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.tips.RecipeDetailPosition
import com.beginvegan.domain.model.tips.TipsRecipeDetail
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.useCase.tips.TipsRecipeUseCase
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.view.tips.viewModel.state.RecipeListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeUseCase: TipsRecipeUseCase
) : ViewModel() {

    //RecyclerView List
    private val _recipeListState =
        MutableStateFlow<NetworkResult<RecipeListState>>(NetworkResult.Loading())
    val recipeListState: StateFlow<NetworkResult<RecipeListState>> = _recipeListState
    private fun addRecipeList(newList: MutableList<TipsRecipeListItem>){
        var oldList = _recipeListState.value.data?.response
        if(oldList==null) oldList = newList
        else oldList.addAll(newList)
        _recipeListState.value = NetworkResult.Success(
            RecipeListState(response = oldList, isLoading = false)
        )
    }
    fun setRecipeList(nowList: MutableList<TipsRecipeListItem>){
        Timber.d("setRecipeList")
        val newList = nowList.map { it.copy() }
        _recipeListState.value = NetworkResult.Success(
            RecipeListState(response = newList.toMutableList(), isLoading = false)
        )
    }

    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList
    fun reSetIsContinueGetList(){
        _isContinueGetList.value = true
    }

    fun getRecipeList(page: Int) {
        viewModelScope.launch {
            recipeUseCase.getRecipeList(page).collectLatest {
                it.onSuccess { result ->
                    if (result.isEmpty()) {
                        _isContinueGetList.value = false
                    } else {
                        addRecipeList(result.toMutableList())
                    }
                }.onFailure {
                    _recipeListState.value = NetworkResult.Error("getRecipeList Failed")
                }
            }
        }
    }

    //Recipe Detail
    private val _recipeDetailData = MutableLiveData<TipsRecipeDetail>()
    val recipeDetailData: LiveData<TipsRecipeDetail> = _recipeDetailData

    private val _recipeDetailPosition = MutableLiveData<RecipeDetailPosition?>(null)
    val recipeDetailPosition: LiveData<RecipeDetailPosition?> = _recipeDetailPosition
    fun setRecipeDetailPosition(recipeDetailPosition: RecipeDetailPosition){
        _recipeDetailPosition.value = recipeDetailPosition
    }

    fun getRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            recipeUseCase.getRecipeDetail(recipeId).onSuccess {
                _recipeDetailData.value = it
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }
    fun setRecipeDetail(recipeDetail: TipsRecipeDetail){
        _recipeDetailData.value = recipeDetail
    }

    private val _nowFragment = MutableLiveData<String>()
    val nowFragment:LiveData<String> = _nowFragment
    fun setNowFragment(fragment:String){
        _nowFragment.value = fragment
    }

    //나를 위한 레시피
    fun getRecipeForMe(page: Int){
        viewModelScope.launch {
            recipeUseCase.getRecipeMy(page).collectLatest {
                it.onSuccess { result ->
                    if (result.isEmpty()) {
                        _isContinueGetList.value = false
                    } else {
                        addRecipeList(result.toMutableList())
                    }
                }.onFailure {
                    _recipeListState.value = NetworkResult.Error("getRecipeList Failed")
                }
            }
        }
    }

    //From VeganTest
    private val _isFromTest = MutableLiveData(false)
    val isFromTest: LiveData<Boolean> = _isFromTest
    fun setIsFromTest(fromTest:Boolean){
        _isFromTest.value = fromTest
    }
}