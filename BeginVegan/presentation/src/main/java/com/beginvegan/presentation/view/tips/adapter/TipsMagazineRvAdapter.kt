package com.beginvegan.presentation.view.tips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.databinding.ItemMagazineBinding
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class  TipsMagazineRvAdapter(
    private val context:Context,
    private val onItemClick:(magazineId:Int)->Unit,
    private val changeBookmark:(isBookmarked: Boolean, data: TipsMagazineItem)->Job
):ListAdapter<TipsMagazineItem, TipsMagazineRvAdapter.RecyclerViewHolder>(diffUtil) {
    /**
     * diffUtill
     * are items the same - TipsRecipeListItem의 id를 비교하여 같은 item인지 확인
     * are contents the same - TipsRecipeListItem의 모든 속성 값 비교
     */
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TipsMagazineItem>() {
            override fun areItemsTheSame(oldItem: TipsMagazineItem, newItem: TipsMagazineItem)
                = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TipsMagazineItem, newItem: TipsMagazineItem)
                = oldItem == newItem
        }
    }

    inner class RecyclerViewHolder(private val binding:ItemMagazineBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(position:Int){
                val item = currentList[position]
                with(binding){
                    tvMagazineTitle.text = item.title
                    tvWriter.text = item.editor
                    tvDate.text = transferDate(item.createdDate)

                    Glide.with(context)
                        .load(item.thumbnail)
                        .transform(CenterCrop(), RoundedCorners(16))
                        .into(ivMagazineImg)

                    with(tbInterest){
                        setOnCheckedChangeListener(null)
                        isChecked = item.isBookmarked
                        setOnCheckedChangeListener { _, isChecked ->
                            changeBookmark(isChecked, item)
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
            holder.itemView.setOnClickListener{
                onItemClick(currentList[position].id)
            }
        }
    }

    /**
     * transfer date
     * 날짜 형식 변경
     */
    private fun transferDate(date:String):String{
        val stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val newDate = LocalDateTime.parse(date, stringToDate)

        val dateToString = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return newDate.format(dateToString)
    }
}