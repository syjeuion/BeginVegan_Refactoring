package com.beginvegan.presentation.view.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.ItemRecipeBinding
import kotlinx.coroutines.Job

class MyRecipeRvAdapter(
    private val context: Context,
    private val onItemClick:(item: TipsRecipeListItem, position: Int)->Unit,
    private val setToggleButton:(isBookmarked: Boolean, data: TipsRecipeListItem, position: Int)->Job,
) : ListAdapter<TipsRecipeListItem, MyRecipeRvAdapter.RecyclerViewHolder>(diffUtil)
{
    private lateinit var veganTypesKr:Array<String>
    private lateinit var veganTypesEng:Array<String>

    // DiffUtil
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TipsRecipeListItem>() {
            override fun areItemsTheSame(oldItem: TipsRecipeListItem, newItem: TipsRecipeListItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: TipsRecipeListItem, newItem: TipsRecipeListItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    //RecyclerViewHolder
    inner class RecyclerViewHolder(private val binding: ItemRecipeBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            with(binding){
                val levels = listOf(
                    tbVeganLevelMilk,
                    tbVeganLevelEgg,
                    tbVeganLevelFish,
                    tbVeganLevelChicken,
                    tbVeganLevelMeat
                )

                val item = currentList[position]
                tvRecipeName.text = item.name
                tvVeganType.text = setVeganType(item.veganType, levels)

                with(tbInterest){
                    setOnCheckedChangeListener(null)
                    isChecked = item.isBookmarked
                    setOnCheckedChangeListener { _, isChecked ->
                        setToggleButton(isChecked, item, position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(context))
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(position)
        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                onItemClick(currentList[position], position)
            }
        }
    }

    //Transfer Type
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