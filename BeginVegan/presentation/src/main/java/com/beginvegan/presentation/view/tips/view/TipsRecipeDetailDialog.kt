package com.beginvegan.presentation.view.tips.view

import android.graphics.Typeface
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.beginvegan.domain.model.tips.RecipeBlock
import com.beginvegan.domain.model.tips.RecipeIngredient
import com.beginvegan.domain.model.tips.TipsRecipeDetail
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.config.enumclass.Bookmarks
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.DialogRecipeDetailBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.config.enumclass.MainPages
import com.beginvegan.presentation.view.home.viewModel.HomeTipsViewModel
import com.beginvegan.presentation.view.mypage.viewModel.MyRecipeViewModel
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TipsRecipeDetailDialog:BaseDialogFragment<DialogRecipeDetailBinding>(DialogRecipeDetailBinding::inflate) {
    @Inject
    lateinit var bookmarkController: BookmarkController
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val homeViewModel: HomeTipsViewModel by activityViewModels()
    private val myRecipeViewModel: MyRecipeViewModel by activityViewModels()

    private lateinit var veganTypesKr:Array<String>
    private lateinit var veganTypesEng:Array<String>

    private var typeface: Typeface? = null
    private lateinit var nowFragmet: MainPages

    override fun init() {
        isCancelable = false
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        nowFragmet = recipeViewModel.nowFragment.value!!

        recipeViewModel.recipeDetailData.observe(this){
            setBinding(it)
        }

        setBtnClose()
        onBackPressed()
    }

    private fun setBtnClose(){
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setVeganType(type:String):String{
        veganTypesKr = requireNotNull(resources.getStringArray(R.array.vegan_type))
        veganTypesEng = requireNotNull(resources.getStringArray(R.array.vegan_types_eng))

        val index = veganTypesEng.indexOf(type)
        return veganTypesKr[index]
    }

    private fun setBinding(data: TipsRecipeDetail){
        with(binding){
            tvRecipeTitle.text = data.name
            tvVeganType.text = setVeganType(data.veganType)
            setIngredients(data.ingredients)
            setProcess(data.blocks)

            with(tbInterest){
                setOnCheckedChangeListener(null)
                isChecked = data.isBookmarked

                setOnCheckedChangeListener { _, isChecked ->
                    when(nowFragmet){
                        MainPages.HOME -> {
                            with(homeViewModel.recipeDetailPosition.value){
                                this?.item?.isBookmarked = isChecked
                                homeViewModel.setRecipeDetailPosition(this!!)
                            }
                        }
                        MainPages.TIPS -> {
                            with(recipeViewModel.recipeDetailData.value){
                                this?.isBookmarked = isChecked
                                recipeViewModel.setRecipeDetail(this!!)
                            }
                            with(recipeViewModel.recipeDetailPosition.value){
                                val newData = this?.item!!.copy(isBookmarked = isChecked)
                                recipeViewModel.updateRecipeListItem(position, newData)
                            }
                        }
                        MainPages.MYPAGE -> {
                            with(recipeViewModel.recipeDetailPosition.value){
                                val newData = this?.item!!.copy(isBookmarked = isChecked)
                                myRecipeViewModel.updateRecipeListItem(position, newData)
                            }
                        }
                        MainPages.MAP -> {}
                    }
                    viewLifecycleOwner.lifecycleScope.launch {
                        if(isChecked){
                            if(bookmarkController.postBookmark(data.id, Bookmarks.RECIPE)){
                                setSnackBar(getString(R.string.toast_scrap_done))
                            }
                        }else{
                            if(bookmarkController.deleteBookmark(data.id, Bookmarks.RECIPE)){
                                setSnackBar(getString(R.string.toast_scrap_undo))
                            }
                        }
                    }
                }
            }
        }
    }

    //Transfer Type
    private fun setIngredients(list: List<RecipeIngredient>){
        val parentLayout = binding.llIngredients
        parentLayout.removeAllViews()

        for (item in list){
            val textView = TextView(context).apply {
                text = "• ${item.name}"
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, R.color.color_text_01))
            }
            textView.typeface = typeface
            parentLayout.addView(textView)
        }
    }
    private fun setProcess(list: List<RecipeBlock>){
        val parentLayout = binding.llProcess
        parentLayout.removeAllViews()

        for (item in list){
            val textView = TextView(context).apply {
                text = "${item.sequence}. ${item.content}"
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, R.color.color_text_01))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }
            }
            textView.typeface = typeface
            parentLayout.addView(textView)
        }
    }

    private fun onBackPressed(){
        dialog?.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                Timber.d("dialog onBackPressed: dismiss()")
                dismiss()
                true
            } else {
                false
            }
        }
    }

    //SnackBar
    private fun setSnackBar(message:String){
        val snackBar = if(nowFragmet == MainPages.MYPAGE){
            Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
        }else{
            Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.toast_scrap_action)){
                    if(recipeViewModel.nowFragment.value == MainPages.HOME)
                        mainNavigationHandler.navigateHomeToMyRecipe()
                    else
                        mainNavigationHandler.navigateTipsRecipeToMyRecipe()
                }
                .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
        }
        with(snackBar){
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
            val snackBarView = view
            val params = snackBarView.layoutParams as FrameLayout.LayoutParams
            params.bottomMargin = 200
            snackBarView.layoutParams = params

            show()
        }
    }
}