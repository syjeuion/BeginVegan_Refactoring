package com.beginvegan.presentation.view.tips.view

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.tips.RecipeDetailPosition
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentTipsRecipeBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.view.tips.adapter.TipsRecipeRvAdapter
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Tips - Recipe Tab Fragment
 * RecyclerView
 *
 */
@AndroidEntryPoint
class TipsRecipeFragment : BaseFragment<FragmentTipsRecipeBinding>(FragmentTipsRecipeBinding::inflate) {
    @Inject
    lateinit var bookmarkController:BookmarkController
    @Inject
    lateinit var mainNavigationHandler:MainNavigationHandler
    private lateinit var recipeRvAdapter: TipsRecipeRvAdapter
    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private lateinit var loadingDialog: LoadingDialog

    private var currentPage = 0
    private var isForMe = false
    private var totalCount = 0

    private var typeface:Typeface? = null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        loadingDialog = LoadingDialog.newInstance()

        reset()
        setToggleRecipeForMe()
        setListener()
        setFabButton()

        openDialogRecipeForMe()
    }
    private fun reset(){
        currentPage = 0
        totalCount = 0
        isForMe = false
        recipeViewModel.reSetIsContinueGetList()
        recipeViewModel.setRecipeList(mutableListOf())
        setAdapter()
    }
    //api 호출
    private fun getRecipeList(page:Int){
        recipeViewModel.getRecipeList(page)
        currentPage++
    }
    private fun getRecipeForMeList(page:Int){
        recipeViewModel.getRecipeForMe(page)
        currentPage++
    }
    //RecyclerView Adapter 설정
    private fun setAdapter(){
        recipeRvAdapter = TipsRecipeRvAdapter(
            requireContext(),
            openDialogRecipeDetail,
            changeBookmark
        )
        binding.rvRecipe.adapter = recipeRvAdapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recipeRvAdapter.submitList(mutableListOf())
    }

    /**
     * RecyclerView Click Listener Lambda
     * openDialogRecipeDetail: Item 클릭 시 Dialog 노출
     * changeBookmark: 북마크 처리
     */
    private val openDialogRecipeDetail = { item: TipsRecipeListItem, position: Int ->
        recipeViewModel.getRecipeDetail(item.id)
        recipeViewModel.setNowFragment("RECIPE")
        recipeViewModel.setRecipeDetailPosition(RecipeDetailPosition(position, item))
        TipsRecipeDetailDialog().show(childFragmentManager, "TipsRecipeDetail")
    }
    private val changeBookmark = {isBookmarked:Boolean, data: TipsRecipeListItem, position:Int ->
        updateBookmark(isBookmarked, data, position)
        viewLifecycleOwner.lifecycleScope.launch {
            if(isBookmarked){
                if(bookmarkController.postBookmark(data.id, "RECIPE")){
                    setSnackBar(getString(R.string.toast_scrap_done))
                }
            }else{
                if(bookmarkController.deleteBookmark(data.id, "RECIPE")){
                    setSnackBar(getString(R.string.toast_scrap_undo))
                }
            }
        }
    }
    private fun setListener(){
        //RecyclerView 페이징 처리
        binding.rvRecipe.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                totalCount = recyclerView.adapter?.itemCount?.minus(1)!!
                if(rvPosition == totalCount && recipeViewModel.isContinueGetList.value!!){
                    if(isForMe){
                        getRecipeForMeList(currentPage)
                    }else{
                        getRecipeList(currentPage)
                    }
                }
            }
        })

        //api result 받으면 setRecipeList 실행
        lifecycleScope.launch {
            recipeViewModel.recipeListState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        if(loadingDialog.isAdded) loadingDialog.dismiss()

                        val newList = state.data?.response?.map { it.copy() }
                        recipeRvAdapter.submitList(newList)
                    }
                    is NetworkResult.Error -> {
                        if(loadingDialog.isAdded) loadingDialog.dismiss()
                    }
                }
            }
        }
    }

    //나를 위한 레시피 토글 처리
    private fun setToggleRecipeForMe(){
        binding.tbRecipeForMe.setOnCheckedChangeListener { _, isChecked ->
            //유저 veganType: NONE 이면 return
            if(isChecked){
                reset()
                isForMe = true
                getRecipeForMeList(currentPage)
            }else{
                reset()
                getRecipeList(currentPage)
            }
        }
        //From VeganTest
        recipeViewModel.isFromTest.observe(this){
            if(it){
                binding.tbRecipeForMe.isChecked = true
                recipeViewModel.setIsFromTest(false)
            }else if(!binding.tbRecipeForMe.isChecked){
                getRecipeList(currentPage)
            }
        }
    }

    private fun setFabButton(){
        binding.ibFab.setOnClickListener {
            binding.rvRecipe.smoothScrollToPosition(0)
        }
    }

    //Dialog
    private fun openDialogRecipeForMe(){
        binding.ibTooltipRecipeForMe.setOnClickListener {
            TipsRecipeForMeDialog().show(childFragmentManager, "TipsRecipeForMe")
        }
    }

    //SnackBar
    private fun setSnackBar(message:String){
        val snackbar = Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.toast_scrap_action)){
                mainNavigationHandler.navigateTipsRecipeToMyRecipe()
            }
            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
        snackbar.show()
    }

    //change Bookmark
    private fun updateBookmark(isChecked:Boolean, oldItem:TipsRecipeListItem, position: Int){
        val newData = TipsRecipeListItem(
            id = oldItem.id,
            name = oldItem.name,
            veganType = oldItem.veganType,
            isBookmarked = isChecked
        )
        val oldList = recipeViewModel.recipeListState.value.data?.response
        oldList!![position] = newData

        recipeViewModel.setRecipeList(oldList)
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
}