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
import com.beginvegan.presentation.config.enumclass.MainPages
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
    /**
     * RecyclerView List
     */
    private val _recipeListState =
        MutableStateFlow<NetworkResult<MutableList<TipsRecipeListItem>>>(NetworkResult.Loading)
    val recipeListState: StateFlow<NetworkResult<MutableList<TipsRecipeListItem>>> = _recipeListState

    private fun addRecipeList(newList: MutableList<TipsRecipeListItem>){
        val oldList = (_recipeListState.value as? NetworkResult.Success)?.data
        val addedList =
            if(oldList.isNullOrEmpty()) newList
            else (oldList + newList).toMutableList()

        _recipeListState.value = NetworkResult.Success(addedList)
    }
    fun setRecipeList(nowList: MutableList<TipsRecipeListItem>){
        val newList = nowList.map { it.copy() }
        _recipeListState.value = NetworkResult.Success(newList.toMutableList())
    }

    //다음 페이지 요청 여부
    private val _isContinueGetList = MutableLiveData(true)
    val isContinueGetList: LiveData<Boolean> = _isContinueGetList
    fun reSetIsContinueGetList(){
        _isContinueGetList.value = true
    }

    fun getRecipeList(page: Int) {
        viewModelScope.launch {
            if(page == 0) _recipeListState.value = NetworkResult.Loading
            recipeUseCase.getRecipeList(page).collectLatest {
                it.onSuccess { result ->
                    if (result.isEmpty()) {
                        _isContinueGetList.value = false
                        if(page == 0) _recipeListState.value = NetworkResult.Empty
                    }
                    else addRecipeList(result.toMutableList())
                }.onFailure {e ->
                    _recipeListState.value = NetworkResult.Error(e)
                }
            }
        }
    }

    /**
     * update recipe list item
     * Dialog에서 Bookmark 처리 시 RecyclerView에 반영
     */
    fun updateRecipeListItem(position:Int, item: TipsRecipeListItem){
        val oldList = (_recipeListState.value as? NetworkResult.Success)?.data
        oldList?.let {
            val newList = it.toMutableList()
            newList[position] = item
            _recipeListState.value = NetworkResult.Success(newList)
        }
    }

    /**
     * Recipe Detail
     * 레시피 상세 정보 불러오기
     */
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

    // 레시피 snackBar 클릭 시 이동 경로 탐색
    private val _nowFragment = MutableLiveData<MainPages>()
    val nowFragment:LiveData<MainPages> = _nowFragment
    fun setNowFragment(fragment: MainPages){
        _nowFragment.value = fragment
    }

    /**
     * 나를 위한 레시피
     */
    fun getRecipeForMe(page: Int){
        viewModelScope.launch {
            if(page == 0) _recipeListState.value = NetworkResult.Loading
            recipeUseCase.getRecipeMy(page).collectLatest { it ->
                it.onSuccess { result ->
                    if (result.isEmpty()){
                        _isContinueGetList.value = false
                        if(page == 0) _recipeListState.value = NetworkResult.Empty
                    }
                    else addRecipeList(result.toMutableList())
                }.onFailure {e ->
                    _recipeListState.value = NetworkResult.Error(e)
                }
            }
        }
    }

    /**
     * From VeganTest
     * VeganTest에서 온 경우 나를 위한 레시피 리스트 출력
     */
    private val _isFromTest = MutableLiveData(false)
    val isFromTest: LiveData<Boolean> = _isFromTest
    fun setIsFromTest(fromTest:Boolean){
        _isFromTest.value = fromTest
    }
}