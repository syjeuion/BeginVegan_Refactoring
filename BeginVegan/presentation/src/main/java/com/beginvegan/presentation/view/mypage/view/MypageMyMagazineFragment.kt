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
import com.beginvegan.presentation.config.enumclass.Bookmarks
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyMagazineBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.util.setContentToolbar
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.mypage.adapter.MyMagazineRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyMagazineViewModel
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyMagazineFragment : BaseFragment<FragmentMypageMyMagazineBinding>(FragmentMypageMyMagazineBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController

    private val magazineViewModel:MagazineViewModel by activityViewModels()
    private val myMagazineViewModel: MyMagazineViewModel by viewModels()
    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.nav_main_graph)

    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var myMagazineRvAdapter: MyMagazineRvAdapter
    private var myMagazineList = mutableListOf<MypageMyMagazineItem>()
    private var currentPage = 0
    private var totalCount = 0

    private var typeface: Typeface?=null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        loadingDialog = LoadingDialog.newInstance()

        setToolbar()
        setBackUp()
        setFabButton()

        setRvAdapter()
    }
    private fun setToolbar(){
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_magazine)
        )
    }

    override fun onStart() {
        super.onStart()
        reset()
    }
    private fun reset(){
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
                        if(bookmarkController.postBookmark(magazineId, Bookmarks.MAGAZINE)){
                            setSnackBar(getString(R.string.toast_scrap_done))
                        }
                    } else {
                        if(bookmarkController.deleteBookmark(magazineId, Bookmarks.MAGAZINE)){
                            setSnackBar(getString(R.string.toast_scrap_undo))
                        }
                    }
                }
            }
        })

        setListener()
//        myMagazineViewModel.setMyMagazineList(myMagazineList)
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

        viewLifecycleOwner.lifecycleScope.launch {
            myMagazineViewModel.myMagazineState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        Timber.e("Success: state.data: ${state.data.size}")
                        dismiss()

                        myMagazineList.addAll(state.data)
                        myMagazineRvAdapter.notifyItemRangeInserted(totalCount,state.data.size)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> dismiss()
                }
            }
        }

        myMagazineViewModel.isMagazineEmpty.observe(this){
            setEmptyState(it)
        }

    }

    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
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

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }

    private fun setSnackBar(message:String ){
        Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                show()
            }
    }
}