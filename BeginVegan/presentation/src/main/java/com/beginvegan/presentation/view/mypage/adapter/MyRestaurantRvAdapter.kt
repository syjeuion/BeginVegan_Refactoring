package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.databinding.ItemRestaurantBinding

class MyRestaurantRvAdapter(
    private val context: Context,
    private val onItemClick:(restaurantId:Int)->Unit
) : ListAdapter<MypageMyRestaurantItem, MyRestaurantRvAdapter.RecyclerViewHolder>(diffUtil){
    // DiffUtil
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MypageMyRestaurantItem>() {
            override fun areItemsTheSame(oldItem: MypageMyRestaurantItem, newItem: MypageMyRestaurantItem): Boolean {
                return oldItem.restaurantId == newItem.restaurantId
            }
            override fun areContentsTheSame(oldItem: MypageMyRestaurantItem, newItem: MypageMyRestaurantItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemRestaurantBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding){
                tvRestaurantName.text = item.name
                tvRestaurantType.text = item.restaurantType
                //거리 추가
                //별점 추가

                Glide.with(context)
                    .load(item.thumbnail)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(ivRestaurantImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                onItemClick(currentList[position].restaurantId)
            }
        }
    }
}