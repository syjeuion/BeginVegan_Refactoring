package com.beginvegan.presentation.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.ItemHomeRecipeBinding
import timber.log.Timber

class HomeRecipeVpAdapter(private val context: Context, private val list: List<TipsRecipeListItem>):
    RecyclerView.Adapter<HomeRecipeVpAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    private lateinit var veganTypesKr:Array<String>
    private lateinit var veganTypesEng:Array<String>

    inner class ViewHolder(private val binding: ItemHomeRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val levels = listOf(
                binding.tbVeganLevelMilk,
                binding.tbVeganLevelEgg,
                binding.tbVeganLevelFish,
                binding.tbVeganLevelChicken,
                binding.tbVeganLevelMeat
            )

            val item = list[position]
            binding.tvRecipeName.text = item.name
            binding.tvVeganType.text = setVeganType(item.veganType, levels)

            binding.tbInterest.setOnCheckedChangeListener(null)
            Timber.d("position$position, item.isBookmarked:${item.isBookmarked}")
            binding.tbInterest.isChecked = item.isBookmarked

            binding.tbInterest.setOnCheckedChangeListener { buttonView, isChecked ->
                listener?.changeBookmark(buttonView, isChecked, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRecipeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener{
                listener?.onItemClick(list[position], position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(item: TipsRecipeListItem, position: Int)
        fun changeBookmark(toggleButton: CompoundButton, isBookmarked: Boolean, data: TipsRecipeListItem)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private fun setVeganType(type:String, levels:List<CompoundButton>):String{
        veganTypesKr = context.resources.getStringArray(R.array.vegan_type)
        veganTypesEng = context.resources.getStringArray(R.array.vegan_types_eng)

        val index = veganTypesEng.indexOf(type)
        setVeganIcon(index, levels)
        return veganTypesKr[index]
    }

    private fun setVeganIcon(index: Int, levels:List<CompoundButton>){
        for (i in 0..4) {
            when(index-1){
                0 -> levels[i].isChecked = false
                1 -> levels[i].isChecked = i<index-1
                2 -> levels[i].isChecked = i==1
                else -> levels[i].isChecked = i<index-2
            }
        }
    }
}