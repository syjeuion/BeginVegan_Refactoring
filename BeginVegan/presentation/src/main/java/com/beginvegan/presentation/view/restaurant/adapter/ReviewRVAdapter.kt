package com.beginvegan.presentation.view.restaurant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.beginvegan.domain.model.review.RestaurantReview
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.ItemReviewBinding

class ReviewRVAdapter(private val userId: Long) :
    ListAdapter<RestaurantReview, RecyclerView.ViewHolder>(diffUtil) {

    private var editListener: OnEditClickListener? = null
    private var deleteListener: OnDeleteClickListener? = null
    private var reportListener: OnReportClickListener? = null

    interface OnEditClickListener {
        fun onEdit(data: RestaurantReview)
    }

    interface OnDeleteClickListener {
        fun onDelete(data: RestaurantReview)
    }

    interface OnReportClickListener {
        fun onReport(data: RestaurantReview)
    }

    fun setOnEditListener(listener: OnEditClickListener) {
        this.editListener = listener
    }

    fun setOnDeleteListener(listener: OnDeleteClickListener) {
        this.deleteListener = listener
    }

    fun setOnReportListener(listener: OnReportClickListener) {
        this.reportListener = listener
    }

    inner class ViewHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: RestaurantReview) {
            binding.tvUserName.text = data.user.nickname
            binding.tvDate.text = data.date
            binding.tvReviewContent.text = data.content

            Glide.with(context).load(data.user.imageUrl).skipMemoryCache(true)
                .error(R.drawable.illus_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivUserProfileImg)

            binding.brbRating.rating = data.rate

            binding.ibMenu.setOnClickListener {
                binding.ibMenu.setOnClickListener { view ->
                    showPopupMenu(context, view, data)
                }
            }

        }

        private fun showPopupMenu(context: Context, view: View, data: RestaurantReview) {
            val popupMenu = PopupMenu(context, view)
            val inflater = popupMenu.menuInflater
            if (data.user.userId == userId) {
                inflater.inflate(R.menu.menu_review_owner, popupMenu.menu)
            } else {
                inflater.inflate(R.menu.menu_review_other, popupMenu.menu)
            }

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        editListener?.onEdit(data)
                        true
                    }

                    R.id.menu_delete -> {
                        deleteListener?.onDelete(data)
                        true
                    }

                    R.id.menu_report -> {
                        reportListener?.onReport(data)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        (holder as ViewHolder).bind(context, getItem(position))
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<RestaurantReview>() {

            override fun areItemsTheSame(
                oldItem: RestaurantReview,
                newItem: RestaurantReview
            ): Boolean {
                return oldItem.reviewId == newItem.reviewId
            }

            override fun areContentsTheSame(
                oldItem: RestaurantReview,
                newItem: RestaurantReview
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}