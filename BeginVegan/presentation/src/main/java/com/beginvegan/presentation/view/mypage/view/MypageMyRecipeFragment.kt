package com.beginvegan.presentation.view.mypage.view

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.tips.RecipeDetailPosition
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyRecipeBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.mypage.adapter.MyRecipeRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyRecipeViewModel
import com.beginvegan.presentation.view.tips.view.TipsRecipeDetailDialog
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyRecipeFragment : BaseFragment<FragmentMypageMyRecipeBinding>(R.layout.fragment_mypage_my_recipe) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController
    private val myRecipeViewModel:MyRecipeViewModel by activityViewModels()
    private val recipeViewModel:RecipeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)

    private lateinit var myRecipeRvAdapter:MyRecipeRvAdapter
    private var currentPage = 0
    private var totalCount = 0
    private var collectJob: Job? = null

    private var typeface: Typeface?=null

    override fun init() {
        binding.lifecycleOwner = this
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)

        setBackUp()
        setFabButton()

        reset()
        setRvAdapter()
    }
    private fun reset(){
        collectJob?.cancel()
        myRecipeViewModel.resetViewModel()
        currentPage = 0
        totalCount = 0
    }
    private fun setRvAdapter(){
        myRecipeRvAdapter = MyRecipeRvAdapter(requireContext())
        binding.rvMyRecipe.adapter = myRecipeRvAdapter
        binding.rvMyRecipe.layoutManager = LinearLayoutManager(this.context)

        myRecipeRvAdapter.setOnItemClickListener(object : MyRecipeRvAdapter.OnItemClickListener{
            override fun onItemClick(item: TipsRecipeListItem, position: Int) {
                openDialogRecipeDetail(item, position)
            }

            override fun setToggleButton(isBookmarked: Boolean, data: TipsRecipeListItem, position: Int) {
                updateBookmark(isBookmarked, data, position)
                lifecycleScope.launch {
                    if(isBookmarked) {
                        if(bookmarkController.postBookmark(data.id, "RECIPE")){
                            setSnackBar(getString(R.string.toast_scrap_done))
                        }
                    } else {
                        if(bookmarkController.deleteBookmark(data.id, "RECIPE")){
                            setSnackBar(getString(R.string.toast_scrap_undo))
                        }
                    }
                }
            }
        })

        setListener()
        getMyRecipeList()
    }
    private fun getMyRecipeList(){
        myRecipeViewModel.getMyRecipe(currentPage)
        currentPage++
    }
    private fun setListener(){
        //page
        binding.rvMyRecipe.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                totalCount = recyclerView.adapter?.itemCount?.minus(1)!!
                if(rvPosition == totalCount && myRecipeViewModel.isContinueGetList.value!!){
                    getMyRecipeList()
                }
            }
        })

        collectJob = lifecycleScope.launch {
            myRecipeViewModel.myRecipesState.collect{state->
                when(state){
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        val newList = state.data?.response?.map { it.copy() }
                        myRecipeRvAdapter.submitList(newList)
                    }
                    is NetworkResult.Error -> {}
                }
            }
        }

        myRecipeViewModel.isRecipeEmpty.observe(this){
            setEmptyState(it)
        }
    }

    //BackUp
    private fun setBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setFabButton(){
        binding.ibFab.setOnClickListener {
            binding.rvMyRecipe.smoothScrollToPosition(0)
        }
    }
    private fun setEmptyState(emptyState:Boolean){
        binding.llEmptyArea.isVisible = emptyState
        binding.btnMoveToRecipe.setOnClickListener {
            //Recipe로 이동
            mainViewModel.setTipsMoveToRecipe(true)
            mainNavigationHandler.navigateMyRecipeToTips()
        }
    }

    //Dialog
    private fun openDialogRecipeDetail(item: TipsRecipeListItem, position: Int){
        recipeViewModel.getRecipeDetail(item.id)
        recipeViewModel.setNowFragment("MYPAGE")
        recipeViewModel.setRecipeDetailPosition(RecipeDetailPosition(position, item))
        TipsRecipeDetailDialog().show(childFragmentManager, "MyRecipeDetail")
    }
    //SnackBar
    private fun setSnackBar(message:String){
        val snackbar = Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
        snackbar.show()
    }

    //update Bookmark
    private fun updateBookmark(isChecked:Boolean, oldItem:TipsRecipeListItem, position: Int){
        val newData = TipsRecipeListItem(
            id = oldItem.id,
            name = oldItem.name,
            veganType = oldItem.veganType,
            isBookmarked = isChecked
        )
        val oldList = myRecipeViewModel.myRecipesState.value.data?.response
        oldList!![position] = newData

        myRecipeViewModel.setMyRecipeList(oldList)
    }
}