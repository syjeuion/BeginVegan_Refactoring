package com.beginvegan.presentation.view.tips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.presentation.databinding.ItemMagazineBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TipsMagazineRvAdapter(private val context:Context,private val list:List<TipsMagazineItem>):RecyclerView.Adapter<TipsMagazineRvAdapter.RecyclerViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecyclerViewHolder(private val binding:ItemMagazineBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(position:Int){
                val item = list[position]
                binding.tvMagazineTitle.text = item.title
                binding.tvWriter.text = item.editor
                binding.tvDate.text = transferDate(item.createdDate)

                Glide.with(context)
                    .load(item.thumbnail)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.ivMagazineImg)

                binding.tbInterest.setOnCheckedChangeListener(null)
                binding.tbInterest.isChecked = item.isBookmarked
                binding.tbInterest.setOnCheckedChangeListener { toggleButton, isChecked ->
                    listener?.changeBookmark(toggleButton, isChecked, item)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemMagazineBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener{
                listener?.onItemClick(list[position].id)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(magazineId:Int)
        fun changeBookmark(toggleButton: CompoundButton, isBookmarked: Boolean, data: TipsMagazineItem)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private fun transferDate(date:String):String{
        val stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val newDate = LocalDateTime.parse(date, stringToDate)

        val dateToString = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return newDate.format(dateToString)
    }
}