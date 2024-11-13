package com.beginvegan.presentation.view.notification.view

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

    private lateinit var loadingDialog: LoadingDialog

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()
        setDrawerRv()
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
                        if(loadingDialog.isAdded) loadingDialog.dismiss()

                        val unreadList = state.data.unreadAlarmList
                        setUnreadAlarmList(unreadList)
                        val readlist = state.data.readAlarmList
                        setReadAlarmList(readlist)
                    }
                    is NetworkResult.Error -> if(loadingDialog.isAdded) loadingDialog.dismiss()
                    is NetworkResult.Empty -> if(loadingDialog.isAdded) loadingDialog.dismiss()
                }
            }
        }

    }

    private fun setUnreadAlarmList(list: List<Alarm>) {
        val unreadRecyclerView = binding.rvUnreadNotification

        if (list.isEmpty()) {
            binding.tvUnreadTitle.isVisible = false
            unreadRecyclerView.isVisible = false
            binding.vDivider.isVisible = false
        } else {
            val newRvAdapter = NotificationUnreadRvAdapter(list, requireContext())
            unreadRecyclerView.adapter = newRvAdapter
            unreadRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setReadAlarmList(list: List<Alarm>) {
        val readRecyclerView = binding.rvReadNotification

        val oldRvAdapter = NotificationReadRvAdapter(list, requireContext())
        readRecyclerView.adapter = oldRvAdapter
        readRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}