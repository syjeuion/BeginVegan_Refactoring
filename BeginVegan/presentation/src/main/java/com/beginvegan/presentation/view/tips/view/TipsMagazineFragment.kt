package com.beginvegan.presentation.view.tips.view

import android.graphics.Typeface
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentTipsMagazineBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.view.tips.adapter.TipsMagazineRvAdapter
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TipsMagazineFragment : BaseFragment<FragmentTipsMagazineBinding>(FragmentTipsMagazineBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController
    private val magazineViewModel : MagazineViewModel by activityViewModels()

    private lateinit var magazineRvAdapter: TipsMagazineRvAdapter

    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private var recipeList = mutableListOf<TipsMagazineItem>()
    private var currentPage = 0
    private var totalCount = 0

    private var typeface: Typeface? = null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        loadingDialog = LoadingDialog.newInstance()

        reset()
        setRvAdapter()
        setListener()
        setTabBtn()
    }
    private fun reset(){
        recipeList = mutableListOf()
        magazineViewModel.resetViewModel()
        currentPage = 0
        totalCount = 0
    }

    private fun setRvAdapter(){
        magazineRvAdapter = TipsMagazineRvAdapter(requireContext(), onItemClick, changeBookmark)
        binding.rvMagazine.adapter = magazineRvAdapter
        binding.rvMagazine.layoutManager = LinearLayoutManager(this.context)

        getMagazineList(currentPage)
    }

    /**
     * magazine rv adapter
     * click listener
     */
    private val onItemClick = {magazineId:Int ->
        mainNavigationHandler.navigateTipsToMagazineDetail()
        magazineViewModel.setMagazineDetail(null)
        magazineViewModel.getMagazineDetail(magazineId)
    }
    private val changeBookmark = {toggleButton: CompoundButton,isBookmarked: Boolean,data: TipsMagazineItem->
        viewLifecycleOwner.lifecycleScope.launch{
            when(isBookmarked){
                true -> {
                    if(bookmarkController.postBookmark(data.id, "MAGAZINE")){
                        val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_done), Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.toast_scrap_action)){
                                mainNavigationHandler.navigateTipsMagazineToMyMagazine()
                            }
                            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                        snackbar.show()
                    }
                }
                false -> {
                    if(bookmarkController.deleteBookmark(data.id, "MAGAZINE")){
                        val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_undo), Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.toast_scrap_action)){
                                mainNavigationHandler.navigateTipsMagazineDetailToMyMagazine()
                            }
                            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                        with(snackbar){
                            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun getMagazineList(page:Int){
        magazineViewModel.getMagazineList(page)
        currentPage++
    }
    private fun setListener(){
        //RecyclerView 페이징 처리
        binding.rvMagazine.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                totalCount = recyclerView.adapter?.itemCount?.minus(1)!!
                if(rvPosition == totalCount && magazineViewModel.isContinueGetList.value!!){
                    getMagazineList(currentPage)
                }
            }
        })

        //api result 받으면 setRecipeList 실행
        viewLifecycleOwner.lifecycleScope.launch {
            magazineViewModel.magazineListState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded && !loadingDialog.isVisible) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()
                        val newList = state.data?.response?.map { it.copy() }
                        magazineRvAdapter.submitList(newList)
                    }
                    is NetworkResult.Error -> {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

    override fun onPause() {
        super.onPause()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }

    private fun setTabBtn(){
        binding.ibFab.setOnClickListener {
            binding.rvMagazine.smoothScrollToPosition(0)
        }
    }
}