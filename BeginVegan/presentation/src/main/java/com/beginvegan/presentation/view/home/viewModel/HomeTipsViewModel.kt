package com.beginvegan.presentation.view.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.model.tips.RecipeDetailPosition
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.domain.useCase.tips.TipsMagazineUseCase
import com.beginvegan.domain.useCase.tips.TipsRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeTipsViewModel @Inject constructor(
    private val magazineUseCase: TipsMagazineUseCase,
    private val recipeUseCase: TipsRecipeUseCase
):ViewModel() {

    private val _homeMagazineList = MutableLiveData<List<TipsMagazineItem>>()
    val homeMagazineList:LiveData<List<TipsMagazineItem>> = _homeMagazineList
    fun getHomeMagazineList(){
        viewModelScope.launch {
            magazineUseCase.getHomeMagazine().onSuccess {
                _homeMagazineList.value = it
            }.onFailure {
                Timber.e("getHomeMagazine onFailure")
            }
        }
    }

    private val _homeRecipeList = MutableLiveData<MutableList<TipsRecipeListItem>>()
    val homeRecipeList:LiveData<MutableList<TipsRecipeListItem>> = _homeRecipeList
    fun getHomeRecipeList(){
        viewModelScope.launch {
            recipeUseCase.getHomeRecipe().onSuccess {
                _homeRecipeList.value = it.toMutableList()
                _homeRecipeListGet.value = true
            }.onFailure {
                Timber.e("getHomeRecipe onFailure")
                _homeRecipeListGet.value = false
            }
        }
    }

    private val _homeRecipeListGet = MutableLiveData(false)
    val homeRecipeListGet: LiveData<Boolean> = _homeRecipeListGet
    fun setHomeRecipeListGet(isGet: Boolean){
        Timber.d("setHomeRecipeListGet: $isGet")
        _homeRecipeListGet.value = isGet
    }

    private val _recipeDetailPosition = MutableLiveData<RecipeDetailPosition>()
    val recipeDetailPosition: LiveData<RecipeDetailPosition> = _recipeDetailPosition
    fun setRecipeDetailPosition(recipeDetailPosition: RecipeDetailPosition){
        val currentList = homeRecipeList.value!!
        currentList[recipeDetailPosition.position] = recipeDetailPosition.item
        _homeRecipeList.value = currentList
        _recipeDetailPosition.value = recipeDetailPosition
    }
}