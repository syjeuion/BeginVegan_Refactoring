package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.presentation.databinding.ItemRestaurantBinding

class MyRestaurantRvAdapter(
    private val context: Context, private val list:List<MypageMyRestaurantItem>
) : RecyclerView.Adapter<MyRestaurantRvAdapter.RecyclerViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class RecyclerViewHolder(private val binding: ItemRestaurantBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = list[position]
            binding.tvRestaurantName.text = item.name
            binding.tvRestaurantType.text = item.restaurantType
            //거리 추가
            //별점 추가

            Glide.with(context)
                .load(item.thumbnail)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivRestaurantImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(list[position].restaurantId)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(restaurantId:Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}