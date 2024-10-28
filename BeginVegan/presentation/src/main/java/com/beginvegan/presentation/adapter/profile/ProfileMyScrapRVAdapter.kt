//package com.example.presentation.adapter.profile
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.beginvegan.R
//import com.example.beginvegan.databinding.ItemProfileMyscrapBinding
//import com.example.beginvegan.src.data.model.restaurant.Restaurant
//
//class ProfileMyScrapRVAdapter(private val context: Context, private val scrapList: ArrayList<Restaurant>): RecyclerView.Adapter<ProfileMyScrapRVAdapter.RecycleViewHolder>() {
//
//    inner class RecycleViewHolder(private val binding: ItemProfileMyscrapBinding):
//        RecyclerView.ViewHolder(binding.root){
//        fun bind(position: Int){
//            if (scrapList[position].imageUrl.isNullOrEmpty()) {
//                Glide.with(context).load(R.drawable.test_res2)
//                    .into(binding.ivRestaurantImg)
//            } else {
//                Glide.with(context).load(scrapList[position].imageUrl).into(binding.ivRestaurantImg)
//            }
//            Glide.with(context).load(scrapList[position].imageUrl).into(binding.ivRestaurantImg)
//            binding.tvRestaurantName.text = scrapList[position].name
//            binding.tvRestaurantTime.text = scrapList[position].businessHours
//            binding.tvRestaurantAddress.text = scrapList[position].address.detailAddress
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
//        val binding = ItemProfileMyscrapBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return RecycleViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = scrapList.size
//
//    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
//        holder.bind(position)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//}