package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.ItemReviewBinding
import kotlinx.coroutines.Job

class MyReviewRvAdapter(
    private val context: Context,
    private val onItemClick:(reviewId:Int)->Unit,
    private val setToggleButton:(reviewId: Int)->Job
) : ListAdapter<MyReview, MyReviewRvAdapter.RecyclerViewHolder>(diffUtil){
    // DiffUtil
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyReview>() {
            override fun areItemsTheSame(oldItem: MyReview, newItem: MyReview): Boolean {
                return oldItem.reviewId == newItem.reviewId
            }
            override fun areContentsTheSame(oldItem: MyReview, newItem: MyReview): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding){
                ivUserProfileImg.visibility = View.GONE
                tvUserName.text = item.restaurantName
                brbRating.rating = item.rate
                tvDate.text = item.date
                tvReviewContent.text = item.content
                tvRecommend.text = context.resources.getString(R.string.btn_review_recommend,item.countRecommendation)

                for(url in item.images){
                    createImageView(url,llImages)
                }
                with(tbRecommend){
                    setOnCheckedChangeListener(null)
                    isChecked = item.recommendation
                    setOnCheckedChangeListener { _, isChecked ->
                        setToggleButton(item.reviewId)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                onItemClick(currentList[position].reviewId)
            }
        }
    }

    private fun createImageView(url:String, layout:LinearLayout){
        val imageView = ImageView(context).apply {}
        Glide.with(context).load(url).into(imageView)

        val params = LinearLayout.LayoutParams(320, 320).apply {
            setMargins(12, 0, 0, 0)
        }
        layout.addView(imageView, params)
    }
}