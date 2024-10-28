package com.beginvegan.presentation.view.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.presentation.databinding.ItemNotificationBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationReadRvAdapter(private val list: List<Alarm>, private val context: Context) :
    RecyclerView.Adapter<NotificationReadRvAdapter.RecyclerViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecyclerViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]
            binding.tvBadgeType.text = item.alarmType
            binding.ivBadgeNew.visibility = View.INVISIBLE
            binding.tvDate.text = transferDate(item.createdDate)
            binding.tvContent.text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(context)
        )
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(it, list[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: Alarm, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private fun transferDate(date: String): String {
//        val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
////        val trimmedInput = date.substring(0, date.indexOf('.'))
//        val newDate = stringToDate.parse(date)
        val localDateTime = LocalDateTime.parse(date)

        val dateToString = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return localDateTime.format(dateToString)
    }
}