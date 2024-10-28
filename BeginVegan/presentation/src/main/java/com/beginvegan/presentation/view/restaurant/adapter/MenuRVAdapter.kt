package com.beginvegan.presentation.view.restaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.map.Menus
import com.beginvegan.presentation.databinding.ItemRestaurantMenuBinding

class MenuRVAdapter : ListAdapter<Menus, RecyclerView.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemRestaurantMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Menus) {
            binding.tvMenuTitle.text = data.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemRestaurantMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Menus>() {

            override fun areItemsTheSame(
                oldItem: Menus,
                newItem: Menus
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Menus,
                newItem: Menus
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}