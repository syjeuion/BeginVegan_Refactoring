package com.beginvegan.presentation.view.notification.view

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentNotificationDrawerBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.DrawerController
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

    override fun init() {
        setDrawerRv()
    }

    private fun setDrawerRv() {
        binding.ibBackUp.setOnClickListener {
            drawerController.closeDrawer()
        }

        lifecycleScope.launch {
            notificationViewModel.alarmLists.collectLatest { state ->
                when (state) {
                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        val unreadList = state.data!!.response!!.unreadAlarmList
                        setUnreadAlarmList(unreadList)
                        val readlist = state.data!!.response!!.readAlarmList
                        setReadAlarmList(readlist)
                    }

                    is NetworkResult.Error -> {

                    }
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