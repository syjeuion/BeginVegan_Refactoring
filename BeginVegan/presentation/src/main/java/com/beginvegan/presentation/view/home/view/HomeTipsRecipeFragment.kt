package com.beginvegan.presentation.view.home.view

import android.graphics.Typeface
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.beginvegan.domain.model.tips.RecipeDetailPosition
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentHomeTipsRecipeBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.view.home.adapter.HomeRecipeVpAdapter
import com.beginvegan.presentation.view.home.viewModel.HomeTipsViewModel
import com.beginvegan.presentation.view.tips.view.TipsRecipeDetailDialog
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeTipsRecipeFragment: BaseFragment<FragmentHomeTipsRecipeBinding>(R.layout.fragment_home_tips_recipe){
    private lateinit var vpAdapter: HomeRecipeVpAdapter
    @Inject
    lateinit var bookmarkController: BookmarkController
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler

    private val viewModel: HomeTipsViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private var typeface:Typeface? = null

    override fun init() {
        binding.lifecycleOwner = this
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)

        viewModel.getHomeRecipeList()
        viewModel.homeRecipeListGet.observe(this){
            if(it) {
                setAdapter(viewModel.homeRecipeList.value!!)
                viewModel.setHomeRecipeListGet(false)
            }
        }
    }

    private fun setAdapter(list:List<TipsRecipeListItem>){
        vpAdapter = HomeRecipeVpAdapter(requireContext(),list)
        binding.vpTipsRecipe.adapter = vpAdapter

        binding.ciTipsRecipe.setViewPager(binding.vpTipsRecipe)

        vpAdapter.setOnItemClickListener(object : HomeRecipeVpAdapter.OnItemClickListener{
            override fun onItemClick(item: TipsRecipeListItem, position: Int) {
                openDialogRecipeDetail(item, position)
            }

            override fun changeBookmark(
                toggleButton: CompoundButton,
                isBookmarked: Boolean,
                data: TipsRecipeListItem
            ) {
                lifecycleScope.launch {
                    if(isBookmarked) {
                        bookmarkController.postBookmark(data.id, "RECIPE")
                        setSnackBar(getString(R.string.toast_scrap_done))
                    } else {
                        bookmarkController.deleteBookmark(data.id, "RECIPE")
                        setSnackBar(getString(R.string.toast_scrap_undo))
                    }
                }
            }
        })

        viewModel.recipeDetailPosition.observe(this){
            vpAdapter.notifyItemChanged(it.position)
        }
    }

    //Dialog
    private fun openDialogRecipeDetail(item: TipsRecipeListItem, position: Int){
        recipeViewModel.getRecipeDetail(item.id)
        recipeViewModel.setNowFragment("HOME")
        viewModel.setRecipeDetailPosition(RecipeDetailPosition(position, item))
        TipsRecipeDetailDialog().show(childFragmentManager, "MyRecipeDetail")
    }

    //SnackBar
    private fun setSnackBar(message:String){
        val snackbar = Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.toast_scrap_action)){
                mainNavigationHandler.navigateHomeToMyRecipe()
            }
            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
        snackbar.show()
    }
}