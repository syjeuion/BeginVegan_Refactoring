package com.beginvegan.presentation.view.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.presentation.databinding.ItemNotificationBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationUnreadRvAdapter(
    private val context: Context,
    private val onItemClick:(v: View, data:Alarm, position: Int)->Unit
): ListAdapter<Alarm, NotificationUnreadRvAdapter.RecyclerViewHolder>(diffUtil){
    // DiffUtil
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Alarm>() {
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem.alarmId == newItem.alarmId
            }
            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemNotificationBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = currentList[position]
            with(binding){
                tvBadgeType.text = item.alarmType
                ivBadgeNew.isVisible = true
                tvDate.text = transferDate(item.createdDate)
                tvContent.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(context)
        )
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position!=RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                onItemClick(it, currentList[position], position)
            }
        }
    }

    private fun transferDate(date:String):String{
//        val stringToDate =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = LocalDateTime.parse(date)

//        val newDate = LocalDateTime.parse(date, stringToDate)
        val nowDate = LocalDateTime.now()

        val minutesDifference = ChronoUnit.MINUTES.between(newDate, nowDate)
        val hoursDifference = ChronoUnit.HOURS.between(newDate, nowDate)

        return when {
            minutesDifference < 60 -> "$minutesDifference 분 전"
            hoursDifference < 24 -> "$hoursDifference 시간 전"
            else -> {
                val dateToString = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                return newDate.format(dateToString)
            }
        }
    }
}