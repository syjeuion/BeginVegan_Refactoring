package com.beginvegan.presentation.view.home.view

import android.graphics.Typeface
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentHomeTipsMagazineBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.view.home.adapter.HomeMagazineVpAdapter
import com.beginvegan.presentation.view.home.viewModel.HomeTipsViewModel
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeTipsMagazineFragment: BaseFragment<FragmentHomeTipsMagazineBinding>(FragmentHomeTipsMagazineBinding::inflate){
    private lateinit var vpAdapter:HomeMagazineVpAdapter
    @Inject
    lateinit var bookmarkController: BookmarkController
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler

    private val viewModel:HomeTipsViewModel by viewModels()
    private val magazineViewModel:MagazineViewModel by activityViewModels()

    private var typeface : Typeface? = null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)

        viewModel.getHomeMagazineList()
        viewModel.homeMagazineList.observe(this){
            setAdapter(it)
        }
    }

    private fun setAdapter(list:List<TipsMagazineItem>){
        vpAdapter = HomeMagazineVpAdapter(requireContext(),list)
        binding.vpTipsMagazine.adapter = vpAdapter

        binding.ciTipsTab.setViewPager(binding.vpTipsMagazine)

        vpAdapter.setOnItemClickListener(object : HomeMagazineVpAdapter.OnItemClickListener{
            override fun onItemClick(magazineId: Int) {
                magazineViewModel.getMagazineDetail(magazineId)
                mainNavigationHandler.navigateHomeToMagazineDetail()
            }

            override fun changeBookmark(
                toggleButton: CompoundButton,
                isBookmarked: Boolean,
                data: TipsMagazineItem
            ) {
                lifecycleScope.launch {
                    if(isBookmarked) {
                        bookmarkController.postBookmark(data.id, "MAGAZINE")
                        val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_done), Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.toast_scrap_action)){
                                mainNavigationHandler.navigateHomeToMyMagazine()
                            }
                            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                        snackbar.show()
                    } else {
                        bookmarkController.deleteBookmark(data.id, "MAGAZINE")
                        val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_undo), Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.toast_scrap_action)){
                                mainNavigationHandler.navigateHomeToMyMagazine()
                            }
                            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                        snackbar.show()
                    }
                }
            }

        })
    }
}