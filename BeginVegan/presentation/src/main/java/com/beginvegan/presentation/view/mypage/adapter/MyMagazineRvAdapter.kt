package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.presentation.databinding.ItemMagazineBinding

class MyMagazineRvAdapter(
    private val context:Context, private val list:List<MypageMyMagazineItem>
) :RecyclerView.Adapter<MyMagazineRvAdapter.RecyclerViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class RecyclerViewHolder(private val binding: ItemMagazineBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = list[position]
            binding.tvMagazineTitle.text = item.title
            binding.tvDate.text = item.writeTime
            binding.tvWriter.text = item.editor

            Glide.with(context)
                .load(item.thumbnail)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivMagazineImg)

            binding.tbInterest.setOnCheckedChangeListener(null)
            binding.tbInterest.isChecked = true
            binding.tbInterest.setOnCheckedChangeListener { _, isChecked ->
                listener?.setToggleButton(isChecked, item.magazineId)
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
            holder.itemView.setOnClickListener {
                listener?.onItemClick(list[position].magazineId)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(magazineId:Int)
        fun setToggleButton(isChecked:Boolean, magazineId:Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}