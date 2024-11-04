package com.beginvegan.presentation.view.mypage.view

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyMagazineBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.mypage.adapter.MyMagazineRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyMagazineViewModel
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyMagazineFragment : BaseFragment<FragmentMypageMyMagazineBinding>(R.layout.fragment_mypage_my_magazine) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController

    private val magazineViewModel:MagazineViewModel by activityViewModels()
    private val myMagazineViewModel: MyMagazineViewModel by viewModels()
    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.nav_main_graph)

    private lateinit var myMagazineRvAdapter: MyMagazineRvAdapter
    private var myMagazineList = mutableListOf<MypageMyMagazineItem>()
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
        myMagazineList = mutableListOf()
        currentPage = 0
        totalCount = 0
        myMagazineViewModel.reSetVieModel()
    }
    private fun setRvAdapter(){
        myMagazineRvAdapter = MyMagazineRvAdapter(requireContext(), myMagazineList)
        binding.rvMyMagazine.adapter = myMagazineRvAdapter
        binding.rvMyMagazine.layoutManager = LinearLayoutManager(this.context)

        myMagazineRvAdapter.setOnItemClickListener(object :MyMagazineRvAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {
                //Magazine Detail로 이동
                mainNavigationHandler.navigateMyMagazineToMagazineDetail()
                magazineViewModel.setMagazineDetail(null)
                magazineViewModel.getMagazineDetail(id)
                mainViewModel.setFromMyMagazine(true)
            }

            override fun setToggleButton(isChecked: Boolean, magazineId: Int) {
                lifecycleScope.launch {
                    if(isChecked) {
                        if(bookmarkController.postBookmark(magazineId, "MAGAZINE")){
                            val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_done), Snackbar.LENGTH_SHORT)
                            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                            snackbar.show()
                        }
                    } else {
                        if(bookmarkController.deleteBookmark(magazineId, "MAGAZINE")){
                            val snackbar = Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_undo), Snackbar.LENGTH_SHORT)
                            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                            snackbar.show()
                        }
                    }
                }
            }
        })

        setListener()
        myMagazineViewModel.setMyMagazineList(myMagazineList)
        getMyMagazineList()
    }
    private fun getMyMagazineList(){
        myMagazineViewModel.getMyMagazine(currentPage)
        currentPage++
    }
    private fun setListener(){
        //page
        binding.rvMyMagazine.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                totalCount = recyclerView.adapter?.itemCount?.minus(1)!!
                if(rvPosition == totalCount && myMagazineViewModel.isContinueGetList.value!!){
                    getMyMagazineList()
                }
            }
        })

        collectJob = lifecycleScope.launch {
            myMagazineViewModel.myMagazineState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        myMagazineList.addAll(state.data?.response!!)
                        myMagazineRvAdapter.notifyItemRangeInserted(totalCount,state.data.response.size)
                    }
                    is NetworkResult.Error -> {}
                }
            }
        }

        myMagazineViewModel.isMagazineEmpty.observe(this){
            setEmptyState(it)
        }

    }
    private fun setBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
//            mainNavigationHandler.popBackStack()
            findNavController().popBackStack()
        }
    }
    private fun setFabButton(){
        binding.ibFab.setOnClickListener {
            binding.rvMyMagazine.smoothScrollToPosition(0)
        }
    }
    private fun setEmptyState(emptyState:Boolean){
        binding.llEmptyArea.isVisible = emptyState
        binding.btnMoveToMagazine.setOnClickListener {
            mainNavigationHandler.navigateMyMagazineToTips()
        }
    }
}