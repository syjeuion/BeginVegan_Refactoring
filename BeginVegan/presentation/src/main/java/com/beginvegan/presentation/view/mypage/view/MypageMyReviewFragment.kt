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

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()

        setBackUp()
        setFabButton()
        setToolbar()
    }

    override fun onStart() {
        super.onStart()
        setRvAdapter()
    }
    private fun setToolbar(){
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_review)
        )
    }
    private fun setRvAdapter(){
        myReviewViewModel.reSetVieModel()

        myReviewRvAdapter = MyReviewRvAdapter(
            requireContext(),
            onItemClick,
            setToggleButton
        )
        binding.rvMyReview.adapter = myReviewRvAdapter
        binding.rvMyReview.layoutManager = LinearLayoutManager(this.context)

        setListener()
        getMyReviewList()
    }
    private val onItemClick = { id:Int ->
        mainNavigationHandler.navigateMyReviewToMap()
        //해당하는 리뷰로 이동
    }
    private val setToggleButton = { reviewId: Int ->
        viewLifecycleOwner.lifecycleScope.launch {
            reviewRecommendController.updateReviewRecommend(reviewId)
        }
    }
    private fun getMyReviewList(){
        viewLifecycleOwner.lifecycleScope.launch {
            myReviewViewModel.currentPage.collectLatest {
                myReviewViewModel.getMyReview()
            }
        }
    }
    private fun setListener(){
        viewLifecycleOwner.lifecycleScope.launch {
            myReviewViewModel.myReviewState.collectLatest{state->
                when(state){
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()
                        myReviewRvAdapter.submitList(state.data)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> {
                        dismiss()
                        myReviewRvAdapter.submitList(emptyList())
                    }
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
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
}