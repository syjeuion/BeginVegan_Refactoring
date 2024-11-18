package com.beginvegan.presentation.view.mypage.view

import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyMagazineFragment :
    BaseFragment<FragmentMypageMyMagazineBinding>(FragmentMypageMyMagazineBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler

    @Inject
    lateinit var bookmarkController: BookmarkController

    private val magazineViewModel: MagazineViewModel by activityViewModels()
    private val myMagazineViewModel: MyMagazineViewModel by viewModels()
    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.nav_main_graph)

    //로딩
    private lateinit var loadingDialog: LoadingDialog
    //Adapter
    private lateinit var myMagazineRvAdapter: MyMagazineRvAdapter

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()

        setToolbar()
        setBackUp()
        setFabButton()
    }

    override fun onStart() {
        super.onStart()
        setRvAdapter()
    }

    private fun setRvAdapter() {
        myMagazineViewModel.reSetVieModel() //ViewModel 값 리셋

        myMagazineRvAdapter = MyMagazineRvAdapter(
            requireContext(),
            onItemClick,
            setToggleButton
        )
        binding.rvMyMagazine.adapter = myMagazineRvAdapter
        binding.rvMyMagazine.layoutManager = LinearLayoutManager(this.context)

        setListener()
        getMyMagazineList()
    }

    /**
     * ListAdapter Lambda 함수 처리
     */
    private val onItemClick = { id: Int ->
        mainNavigationHandler.navigateMyMagazineToMagazineDetail()
        magazineViewModel.setMagazineDetail(null)
        magazineViewModel.getMagazineDetail(id)
        mainViewModel.setFromMyMagazine(true)
    }
    private val setToggleButton = { isChecked: Boolean, magazineId: Int ->
        viewLifecycleOwner.lifecycleScope.launch {
            if (isChecked) {
                if (bookmarkController.postBookmark(magazineId, Bookmarks.MAGAZINE)) {
                    setSnackBar(getString(R.string.toast_scrap_done))
                }
            } else {
                if (bookmarkController.deleteBookmark(magazineId, Bookmarks.MAGAZINE)) {
                    setSnackBar(getString(R.string.toast_scrap_undo))
                }
            }
        }
    }

    private fun setListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            myMagazineViewModel.myMagazineState.collectLatest { state ->
                when (state) {
                    is NetworkResult.Loading -> {
                        if (!loadingDialog.isAdded) loadingDialog.show(
                            childFragmentManager,
                            LOADING
                        )
                    }
                    is NetworkResult.Success -> {
                        dismiss()
                        myMagazineRvAdapter.submitList(state.data)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> {
                        dismiss()
                        myMagazineRvAdapter.submitList(emptyList())
                    }
                }
            }
        }

        myMagazineViewModel.isMagazineEmpty.observe(this) {
            setEmptyState(it)
        }
    }
    private fun getMyMagazineList() {
        viewLifecycleOwner.lifecycleScope.launch {
            myMagazineViewModel.currentPage.collectLatest {
                myMagazineViewModel.getMyMagazine()
            }
        }
    }

    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }
    private fun setSnackBar(message: String) {
        Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTypeface(ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular))
                show()
            }
    }

    private fun setEmptyState(emptyState: Boolean) {
        binding.llEmptyArea.isVisible = emptyState
        binding.btnMoveToMagazine.setOnClickListener {
            mainNavigationHandler.navigateMyMagazineToTips()
        }
    }

    override fun onStop() {
        super.onStop()
        if (loadingDialog.isAdded) loadingDialog.onDestroy()
    }

    /**
     * 기본 화면 설정
     */
    private fun setToolbar() {
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_magazine)
        )
    }
    private fun setBackUp() {
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setFabButton() {
        binding.ibFab.setOnClickListener {
            binding.rvMyMagazine.smoothScrollToPosition(0)
        }
    }
}