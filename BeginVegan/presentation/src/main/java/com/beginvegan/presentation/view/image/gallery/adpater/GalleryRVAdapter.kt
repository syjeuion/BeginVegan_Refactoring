package com.beginvegan.presentation.view.image.gallery.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.beginvegan.presentation.databinding.ItemGalleryBinding
import com.beginvegan.presentation.view.image.gallery.model.GalleryImage

class GalleryRVAdapter() : ListAdapter<GalleryImage, GalleryRVAdapter.ViewHolder>(diffUtil) {
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, data: GalleryImage) {
            Glide.with(itemView)
                .load(data.imageUri)
                .into(binding.ivGalleryImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, currentList[position])
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, currentList[position], position)
                Log.d("check position data", currentList[position].toString())
            }
        }
    }

    override fun getItemCount(): Int = currentList.size
    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GalleryImage, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GalleryImage>() {
            override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GalleryImage,
                newItem: GalleryImage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

