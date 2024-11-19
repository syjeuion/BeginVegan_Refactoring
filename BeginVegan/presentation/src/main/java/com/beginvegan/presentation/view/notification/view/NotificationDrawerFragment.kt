package com.beginvegan.presentation.view.notification.view

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentNotificationDrawerBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.DrawerController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.view.notification.adapter.NotificationReadRvAdapter
import com.beginvegan.presentation.view.notification.adapter.NotificationUnreadRvAdapter
import com.beginvegan.presentation.view.notification.viewModel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDrawerFragment :
    BaseFragment<FragmentNotificationDrawerBinding>(FragmentNotificationDrawerBinding::inflate) {
    @Inject
    lateinit var drawerController: DrawerController
    private val notificationViewModel: NotificationViewModel by viewModels()

    private lateinit var oldRvAdapter:NotificationReadRvAdapter
    private lateinit var newRvAdapter: NotificationUnreadRvAdapter

    private lateinit var loadingDialog: LoadingDialog

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()

        setReadAlarmList()
        setUnreadAlarmList()
        setDrawerRv()
    }
    private fun setReadAlarmList() {
        val readRecyclerView = binding.rvReadNotification

        oldRvAdapter = NotificationReadRvAdapter(
            requireContext(),
            onItemClick
        )
        readRecyclerView.adapter = oldRvAdapter
        readRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUnreadAlarmList() {
        val unreadRecyclerView = binding.rvUnreadNotification

        newRvAdapter = NotificationUnreadRvAdapter(
            requireContext(),
            onItemClick
        )
        unreadRecyclerView.adapter = newRvAdapter
        unreadRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
    private val onItemClick = { v: View, data:Alarm, position: Int ->
        //아이템 클릭 시 이동
        when(data.alarmType){

        }
    }
    private fun setDrawerRv() {
        binding.ibBackUp.setOnClickListener {
            drawerController.closeDrawer()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            notificationViewModel.alarmLists.collectLatest { state ->
                when (state) {
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()

                        val unreadList = state.data.unreadAlarmList
                        if(unreadList.isEmpty()){
                            binding.tvUnreadTitle.isVisible = false
                            binding.rvUnreadNotification.isVisible = false
                            binding.vDivider.isVisible = false
                        }else{
                            newRvAdapter.submitList(unreadList)
                        }
                        val readlist = state.data.readAlarmList
                        oldRvAdapter.submitList(readlist)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> {
                        dismiss()
                        newRvAdapter.submitList(emptyList())
                        oldRvAdapter.submitList(emptyList())
                    }
                }
            }
        }

    }

    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

}