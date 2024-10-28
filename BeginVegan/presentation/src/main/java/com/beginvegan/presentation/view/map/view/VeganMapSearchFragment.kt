package com.beginvegan.presentation.view.map.view

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beginvegan.domain.model.map.HistorySearch
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentMapSearchBinding
import com.beginvegan.presentation.view.map.adapter.VeganMapSearchRVAdapter
import com.beginvegan.presentation.view.map.viewModel.VeganMapSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VeganMapSearchFragment :
    BaseFragment<FragmentMapSearchBinding>(R.layout.fragment_map_search) {

    private val viewModel: VeganMapSearchViewModel by viewModels()
    private lateinit var veganMapSearchRVAdapter: VeganMapSearchRVAdapter
    override fun init() {

        initHistory()

        showKeyBoard()

        onSearch()

        onAllDelete()

        setHistorySearchRVAdapter()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        setTabBtn()

    }

    private fun onAllDelete() {
        binding.btnAllDelete.setOnClickListener {
            viewModel.deleteAllHistory()
        }

    }

    private fun initHistory() {
        viewModel.fetchAllHistory()
    }

    private fun setHistorySearchRVAdapter() {

        veganMapSearchRVAdapter = VeganMapSearchRVAdapter()

        binding.rvSearch.apply {
            adapter = veganMapSearchRVAdapter
            itemAnimator = null
        }

        logMessage("setHistorySearchRVAdapter value = ${viewModel.searchList.value}")
        veganMapSearchRVAdapter.submitList(viewModel.searchList.value)

        veganMapSearchRVAdapter.setOnItemClickListener(object :
            VeganMapSearchRVAdapter.OnDeleteListener {
            override fun onDelete(data: HistorySearch) {
                viewModel.deleteHistory(data)
            }
        })

        veganMapSearchRVAdapter.setOnSearchListener(object :
            VeganMapSearchRVAdapter.OnSearchListener {
            override fun onSearch(data: HistorySearch) {
                binding.tieSearch.setText(data.description)
                binding.tieSearch.setSelection(data.description.length)

                viewModel.insertHistory(data.description)
                val action =
                    VeganMapSearchFragmentDirections.actionVeganMapSearchFragmentToVeganMapResultFragment(
                        searchWord = data.description
                    )
                findNavController().navigate(action)
            }

        })


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchList.collect() {
                if (it.isEmpty()) {
                    logMessage("search list empty")
                    binding.tvEmptyView.visibility = View.VISIBLE
                    binding.btnAllDelete.visibility = View.GONE
                    binding.rvSearch.visibility = View.INVISIBLE
                    veganMapSearchRVAdapter.submitList(viewModel.searchList.value)
                } else {
                    logMessage("search list not empty")
                    binding.rvSearch.visibility = View.VISIBLE
                    binding.tvEmptyView.visibility = View.GONE
                    binding.btnAllDelete.visibility = View.VISIBLE
                    veganMapSearchRVAdapter.submitList(viewModel.searchList.value)
                }
            }
        }
    }

    private fun onSearch() {
        binding.tieSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.toString().isNotBlank()) {
                    viewModel.insertHistory(v.text.toString())
                    val action =
                        VeganMapSearchFragmentDirections.actionVeganMapSearchFragmentToVeganMapResultFragment(
                            searchWord = v.text.toString()
                        )
                    findNavController().navigate(action)
                }
                true
            } else {
                false

            }
        }
    }

    private fun setTabBtn() {
        binding.ibFab.setOnClickListener {
            binding.rvSearch.smoothScrollToPosition(viewModel.searchList.value.size - 1)
        }
    }

    private fun showKeyBoard() {
        binding.tieSearch.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.tieSearch, InputMethodManager.SHOW_IMPLICIT)
    }

}