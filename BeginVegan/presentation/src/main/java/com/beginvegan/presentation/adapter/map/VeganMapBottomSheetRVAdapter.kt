//package com.example.presentation.adapter.map
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.beginvegan.R
//import com.example.beginvegan.databinding.ItemVeganmapRestaurantBinding
//import com.example.beginvegan.src.data.model.restaurant.NearRestaurant
//
//class VeganMapBottomSheetRVAdapter(
//    private val context: Context,
//    private val dataList: ArrayList<NearRestaurant>
//) :
//    RecyclerView.Adapter<VeganMapBottomSheetRVAdapter.DataViewHolder>() {
//    private var listener: OnItemClickListener? = null
//
//    inner class DataViewHolder(private val binding: ItemVeganmapRestaurantBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//            binding.tvVeganmapRestaurantName.text = dataList[position].name
//            binding.tvVeganmapRestaurantTime.text = dataList[position].businessHours
//            binding.ivVeganmapRestaurantAddress.text = dataList[position].address.detailAddress
//            if (dataList[position].imageUrl.isNullOrEmpty()) {
//                Glide.with(context).load(R.drawable.test_res2)
//                    .into(binding.ivVeganmapRestaurantFirst)
//            } else {
//                Glide.with(context).load(dataList[position].imageUrl)
//                    .into(binding.ivVeganmapRestaurantFirst)
//            }
//            if (dataList[position].menus[0].imageUrl.isNullOrEmpty()) {
//                Glide.with(context).load(R.drawable.test_res)
//                    .into(binding.ivVeganmapRestaurantFirst)
//            } else {
//                Glide.with(context).load(dataList[position].menus[0].imageUrl)
//                    .into(binding.ivVeganmapRestaurantFirst)
//            }
//            if (dataList[position].menus[1].imageUrl.isNullOrEmpty()) {
//                Glide.with(context).load(R.drawable.test_salad)
//                    .into(binding.ivVeganmapRestaurantFirst)
//            } else {
//                Glide.with(context).load(dataList[position].menus[1].imageUrl)
//                    .into(binding.ivVeganmapRestaurantThird)
//            }
//
//        }
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): DataViewHolder {
//        val binding = ItemVeganmapRestaurantBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return DataViewHolder(binding)
//    }
//
//
//    override fun onBindViewHolder(
//        holder: DataViewHolder,
//        position: Int
//    ) {
//        holder.bind(position)
//        if (position != RecyclerView.NO_POSITION) {
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.itemView, dataList[position], position)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = dataList.size
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    interface OnItemClickListener {
//        fun onItemClick(v: View, data: NearRestaurant, position: Int)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
//}