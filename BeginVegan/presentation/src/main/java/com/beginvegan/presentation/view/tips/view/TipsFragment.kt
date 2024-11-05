package com.beginvegan.presentation.view.tips.view

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentMainTipsBinding
import com.beginvegan.presentation.util.DrawerController
import com.beginvegan.presentation.util.setToolbar
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.tips.adapter.TipsVpAdapter
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Tips
 * Tab - Magazine / Recipe
 */
@AndroidEntryPoint
class TipsFragment : BaseFragment<FragmentMainTipsBinding>(FragmentMainTipsBinding::inflate) {
    @Inject
    lateinit var drawerController: DrawerController
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)
    override fun init() {
        setTipsTab()
        setTipsToolbar()
        //fcm 테스트
        //mainViewModel.postFcmPush()
    }
    private fun setTipsToolbar(){
        setToolbar(
            view = binding.includedToolbar,
            title = getString(R.string.tips_title),
            backColor = ContextCompat.getColor(requireContext(), R.color.color_primary),
            contentColor = ContextCompat.getColor(requireContext(), R.color.color_white),
            isBackIcon = false,
            isNotiIcon = true
        )
    }

    private fun setTipsTab() {
        binding.vpViewpagerArea.adapter = TipsVpAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlTab, binding.vpViewpagerArea) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.magazine)
                1 -> tab.text = getString(R.string.recipe)
            }
        }.attach()

        checkMainViewModel()
        setOpenDrawer()
    }

    //이동
    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun checkMainViewModel() {
        //나를 위한 레시피
        if (mainViewModel.fromTest.value!!) {
            binding.vpViewpagerArea.post {
                binding.vpViewpagerArea.currentItem = 1
                recipeViewModel.setIsFromTest(true)
            }
        } else if (mainViewModel.tipsMoveToRecipe.value!!) {
            binding.vpViewpagerArea.post {
                binding.vpViewpagerArea.currentItem = 1
            }
        }
        mainViewModel.setFromTest(false)
        mainViewModel.setTipsMoveToRecipe(false)
    }
}