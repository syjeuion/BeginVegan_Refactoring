package com.beginvegan.presentation.view.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.presentation.databinding.ItemNotificationBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationReadRvAdapter(
    private val context: Context,
    private val onItemClick:(v: View, data: Alarm, position: Int)->Unit
) : ListAdapter<Alarm, NotificationReadRvAdapter.RecyclerViewHolder>(diffUtil) {
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

    inner class RecyclerViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding){
                tvBadgeType.text = item.alarmType
                ivBadgeNew.visibility = View.INVISIBLE
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
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                onItemClick(it,currentList[position], position)
            }
        }
    }
    private fun transferDate(date: String): String {
        val localDateTime = LocalDateTime.parse(date)

        val dateToString = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return localDateTime.format(dateToString)
    }
}