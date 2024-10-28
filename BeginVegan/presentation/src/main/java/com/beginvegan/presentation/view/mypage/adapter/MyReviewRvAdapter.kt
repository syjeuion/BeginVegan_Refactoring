package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.ItemReviewBinding

class MyReviewRvAdapter(
    private val context: Context, private val list:List<MyReview>
) : RecyclerView.Adapter<MyReviewRvAdapter.RecyclerViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class RecyclerViewHolder(private val binding: ItemReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val item = list[position]
            binding.ivUserProfileImg.visibility = View.GONE
            binding.tvUserName.text = item.restaurantName
            binding.brbRating.rating = item.rate
            binding.tvDate.text = item.date
            binding.tvReviewContent.text = item.content
            binding.tvRecommend.text = context.resources.getString(R.string.btn_review_recommend,item.countRecommendation)

            for(url in item.images){
                createImageView(url, binding.llImages)
            }

            binding.tbRecommend.setOnCheckedChangeListener(null)
            binding.tbRecommend.isChecked = item.recommendation
            binding.tbRecommend.setOnCheckedChangeListener { _, isChecked ->
                listener?.setToggleButton(isChecked, item.reviewId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
//        if(position != RecyclerView.NO_POSITION){
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(list[position].reviewId)
//            }
//        }
    }

    interface OnItemClickListener{
//        fun onItemClick(reviewId:Int)
        fun setToggleButton(isChecked:Boolean, reviewId: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
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