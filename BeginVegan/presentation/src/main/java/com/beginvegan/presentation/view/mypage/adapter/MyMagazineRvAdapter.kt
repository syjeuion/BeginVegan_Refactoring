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
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.presentation.databinding.ItemMagazineBinding
import kotlinx.coroutines.Job

class MyMagazineRvAdapter(
    private val context:Context,
    private val onItemClick:(magazineId:Int)->Unit,
    private val setToggleButton:(isChecked:Boolean, magazineId:Int)->Job
) :ListAdapter<MypageMyMagazineItem, MyMagazineRvAdapter.RecyclerViewHolder>(diffUtil){
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<MypageMyMagazineItem>() {
            override fun areItemsTheSame(oldItem: MypageMyMagazineItem, newItem: MypageMyMagazineItem): Boolean {
                return oldItem.magazineId == newItem.magazineId
            }
            override fun areContentsTheSame(oldItem: MypageMyMagazineItem, newItem: MypageMyMagazineItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemMagazineBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding){
                tvMagazineTitle.text = item.title
                tvDate.text = item.writeTime
                tvWriter.text = item.editor

                Glide.with(context)
                    .load(item.thumbnail)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(ivMagazineImg)

                with(tbInterest){
                    setOnCheckedChangeListener(null)
                    isChecked = true
                    setOnCheckedChangeListener { _, isChecked ->
                        setToggleButton(isChecked, item.magazineId)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemMagazineBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                onItemClick(currentList[position].magazineId)
            }
        }
    }
}