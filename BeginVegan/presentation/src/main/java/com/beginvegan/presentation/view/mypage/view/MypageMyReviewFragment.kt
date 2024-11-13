package com.beginvegan.presentation.view.mypage.view

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyReviewBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.util.ReviewRecommendController
import com.beginvegan.presentation.util.setContentToolbar
import com.beginvegan.presentation.view.mypage.adapter.MyReviewRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyReviewFragment : BaseFragment<FragmentMypageMyReviewBinding>(FragmentMypageMyReviewBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var reviewRecommendController: ReviewRecommendController
    private val myReviewViewModel: MyReviewViewModel by viewModels()
    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var myReviewRvAdapter: MyReviewRvAdapter
    private var myReviewList = mutableListOf<MyReview>()
    private var currentPage = 0
    private var totalCount = 0

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()

        setBackUp()
        setFabButton()
        setToolbar()
        reset()
        setRvAdapter()
    }
    private fun setToolbar(){
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_review)
        )
    }
    private fun reset(){
        myReviewList = mutableListOf()
        currentPage = 0
        totalCount = 0
        myReviewViewModel.reSetVieModel()
    }
    private fun setRvAdapter(){
        myReviewRvAdapter = MyReviewRvAdapter(requireContext(), myReviewList)
        binding.rvMyReview.adapter = myReviewRvAdapter
        binding.rvMyReview.layoutManager = LinearLayoutManager(this.context)

        myReviewRvAdapter.setOnItemClickListener(object : MyReviewRvAdapter.OnItemClickListener{
//            override fun onItemClick(id: Int) {
//                //리뷰는 이동이 없나여,,?
//            }

            override fun setToggleButton(isChecked: Boolean, reviewId: Int) {
                lifecycleScope.launch {
                    reviewRecommendController.updateReviewRecommend(reviewId)
                }
            }
        })

        setListener()
        myReviewViewModel.setMyReviewList(myReviewList)
        getMyReviewList()
    }
    private fun getMyReviewList(){
        myReviewViewModel.getMyReview(currentPage)
        currentPage++
    }
    private fun setListener(){
        //page
        binding.rvMyReview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                totalCount = recyclerView.adapter?.itemCount?.minus(1)!!
                if(rvPosition == totalCount && myReviewViewModel.isContinueGetList.value!!){
                    getMyReviewList()
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            myReviewViewModel.myReviewState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()

                        myReviewList.addAll(state.data)
                        myReviewRvAdapter.notifyItemRangeInserted(totalCount,state.data.size)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> dismiss()
                }
            }
        }

        myReviewViewModel.isReviewEmpty.observe(this){
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
            binding.rvMyReview.smoothScrollToPosition(0)
        }
    }
    private fun setEmptyState(emptyState:Boolean){
        binding.llEmptyArea.isVisible = emptyState
        //리뷰는 이동 없음, 추가될 가능성 고려해 주석처리
//        binding.btnMoveToMap.setOnClickListener {
//            mainNavigationHandler.navigateMyRestaurantToMainHome(true)
//        }
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
}