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
import com.beginvegan.presentation.view.tips.adapter.TipsMagazineRvAdapter
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TipsMagazineFragment : BaseFragment<FragmentTipsMagazineBinding>(R.layout.fragment_tips_magazine) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController
    private val magazineViewModel : MagazineViewModel by activityViewModels()

    private lateinit var magazineRvAdapter: TipsMagazineRvAdapter
    private var collectJob: Job? = null

    private var recipeList = mutableListOf<TipsMagazineItem>()
    private var currentPage = 0
    private var totalCount = 0

    private var typeface: Typeface? = null

    override fun init() {
        binding.lifecycleOwner = this
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)

        reset()
        setRvAdapter()
        setListener()
        setTabBtn()
    }
    private fun reset(){
        collectJob?.cancel()
        recipeList = mutableListOf()
        magazineViewModel.resetViewModel()
        currentPage = 0
        totalCount = 0
    }

    private fun setRvAdapter(){
        magazineRvAdapter = TipsMagazineRvAdapter(requireContext(), recipeList)
        binding.rvMagazine.adapter = magazineRvAdapter
        binding.rvMagazine.layoutManager = LinearLayoutManager(this.context)

        magazineRvAdapter.setOnItemClickListener(object :
            TipsMagazineRvAdapter.OnItemClickListener {
            override fun onItemClick(magazineId:Int) {
                mainNavigationHandler.navigateTipsToMagazineDetail()
                magazineViewModel.setMagazineDetail(null)
                magazineViewModel.getMagazineDetail(magazineId)
            }

            override fun changeBookmark(
                toggleButton: CompoundButton,
                isBookmarked: Boolean,
                data: TipsMagazineItem
            ) { lifecycleScope.launch{
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
                                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                                snackbar.show()
                            }
                        }
                    }
            } }
        })

        getMagazineList(currentPage)
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
        collectJob = lifecycleScope.launch {
            magazineViewModel.magazineListState.collect{state->
                when(state){
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        recipeList.addAll(state.data?.response!!)
                        magazineRvAdapter.notifyItemRangeInserted(totalCount,state.data.response.size)
                    }
                    is NetworkResult.Error -> {}
                }
            }
        }
    }

    private fun setTabBtn(){
        binding.ibFab.setOnClickListener {
            binding.rvMagazine.smoothScrollToPosition(0)
        }
    }
}