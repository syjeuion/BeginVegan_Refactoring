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
import com.beginvegan.presentation.config.enumclass.Bookmarks
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyRecipeBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.config.enumclass.MainPages
import com.beginvegan.presentation.util.setContentToolbar
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.mypage.adapter.MyRecipeRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyRecipeViewModel
import com.beginvegan.presentation.view.tips.view.TipsRecipeDetailDialog
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyRecipeFragment :
    BaseFragment<FragmentMypageMyRecipeBinding>(FragmentMypageMyRecipeBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler

    @Inject
    lateinit var bookmarkController: BookmarkController
    private val myRecipeViewModel: MyRecipeViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)

    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var myRecipeRvAdapter: MyRecipeRvAdapter

    private var typeface: Typeface? = null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        loadingDialog = LoadingDialog.newInstance()

        setToolbar()
        setBackUp()
        setFabButton()
        getMyRecipeList()
    }
    private fun setToolbar(){
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_recipe)
        )
    }

    override fun onStart() {
        super.onStart()
        setRvAdapter()
    }

    private fun setRvAdapter() {
        myRecipeViewModel.resetViewModel()

        myRecipeRvAdapter = MyRecipeRvAdapter(requireContext())
        with(binding.rvMyRecipe){
            adapter = myRecipeRvAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        myRecipeRvAdapter.setOnItemClickListener(object : MyRecipeRvAdapter.OnItemClickListener {
            override fun onItemClick(item: TipsRecipeListItem, position: Int) {
                openDialogRecipeDetail(item, position)
            }

            override fun setToggleButton(
                isBookmarked: Boolean,
                data: TipsRecipeListItem,
                position: Int
            ) {
                updateBookmark(isBookmarked, data, position)
                lifecycleScope.launch {
                    if (isBookmarked) {
                        if (bookmarkController.postBookmark(data.id, Bookmarks.RECIPE)) {
                            setSnackBar(getString(R.string.toast_scrap_done))
                        }
                    } else {
                        if (bookmarkController.deleteBookmark(data.id, Bookmarks.RECIPE)) {
                            setSnackBar(getString(R.string.toast_scrap_undo))
                        }
                    }
                }
            }
        })

        setListener()
    }

    private fun getMyRecipeList() {
        viewLifecycleOwner.lifecycleScope.launch {
            myRecipeViewModel.currentPage.collectLatest {
                myRecipeViewModel.getMyRecipe()
            }
        }
    }

    private fun setListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            myRecipeViewModel.myRecipesState.collectLatest { state ->
                when (state) {
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()
                        myRecipeRvAdapter.submitList(state.data)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> {
                        dismiss()
                        myRecipeRvAdapter.submitList(emptyList())
                    }
                }
            }
        }

        myRecipeViewModel.isRecipeEmpty.observe(this) {
            setEmptyState(it)
        }
    }
    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

    //BackUp
    private fun setBackUp() {
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setFabButton() {
        binding.ibFab.setOnClickListener {
            binding.rvMyRecipe.smoothScrollToPosition(0)
        }
    }

    private fun setEmptyState(emptyState: Boolean) {
        binding.llEmptyArea.isVisible = emptyState
        binding.btnMoveToRecipe.setOnClickListener {
            //Recipe로 이동
            mainViewModel.setTipsMoveToRecipe(true)
            mainNavigationHandler.navigateMyRecipeToTips()
        }
    }

    //Dialog
    private fun openDialogRecipeDetail(item: TipsRecipeListItem, position: Int) {
        with(recipeViewModel){
            getRecipeDetail(item.id)
            setNowFragment(MainPages.MYPAGE)
            setRecipeDetailPosition(RecipeDetailPosition(position, item))
        }
        TipsRecipeDetailDialog().show(childFragmentManager, "MyRecipeDetail")
    }

    //SnackBar
    private fun setSnackBar(message: String) {
        Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                show()
            }
    }

    //update Bookmark
    private fun updateBookmark(isChecked: Boolean, oldItem: TipsRecipeListItem, position: Int) {
        val newData = oldItem.copy(isBookmarked = isChecked)
        val oldList = (myRecipeViewModel.myRecipesState.value as? NetworkResult.Success)?.data
        oldList?.let {
            it[position] = newData
            myRecipeViewModel.setMyRecipeList(it)
        }
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
}