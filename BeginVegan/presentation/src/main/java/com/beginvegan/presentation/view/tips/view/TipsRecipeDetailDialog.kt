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
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.DialogRecipeDetailBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.view.home.viewModel.HomeTipsViewModel
import com.beginvegan.presentation.view.mypage.viewModel.MyRecipeViewModel
import com.beginvegan.presentation.view.tips.viewModel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TipsRecipeDetailDialog:BaseDialogFragment<DialogRecipeDetailBinding>(R.layout.dialog_recipe_detail) {
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
    private lateinit var nowFragmet:String

    override fun init() {
        isCancelable = false
        binding.lifecycleOwner = this
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
        veganTypesKr = resources.getStringArray(R.array.vegan_type)
        veganTypesEng = resources.getStringArray(R.array.vegan_types_eng)

        val index = veganTypesEng.indexOf(type)
        return veganTypesKr[index]
    }

    private fun setBinding(data: TipsRecipeDetail){
        binding.tvRecipeTitle.text = data.name
        binding.tvVeganType.text = setVeganType(data.veganType)
        setIngredients(data.ingredients)
        setProcess(data.blocks)

        binding.tbInterest.setOnCheckedChangeListener(null)
        binding.tbInterest.isChecked = data.isBookmarked

        binding.tbInterest.setOnCheckedChangeListener { _, isChecked ->
            when(nowFragmet){
                "HOME" -> {
                    val currentData = homeViewModel.recipeDetailPosition.value
                    currentData?.item?.isBookmarked = isChecked
                    homeViewModel.setRecipeDetailPosition(currentData!!)
                }
                "RECIPE" -> {
                    val detailData = recipeViewModel.recipeDetailData.value
                    detailData?.isBookmarked = isChecked
                    recipeViewModel.setRecipeDetail(detailData!!)

                    val currentData = recipeViewModel.recipeDetailPosition.value
                    val oldItem = currentData?.item!!
                    val newData = TipsRecipeListItem(
                        id = oldItem.id,
                        name = oldItem.name,
                        veganType = oldItem.veganType,
                        isBookmarked = isChecked
                    )
                    val oldList = recipeViewModel.recipeListState.value.data?.response
                    oldList!![currentData.position] = newData

                    recipeViewModel.setRecipeList(oldList)
                }
                "MYPAGE" -> {
                    val currentData = recipeViewModel.recipeDetailPosition.value
                    val oldItem = currentData?.item!!
                    val newData = TipsRecipeListItem(
                        id = oldItem.id,
                        name = oldItem.name,
                        veganType = oldItem.veganType,
                        isBookmarked = isChecked
                    )
                    val oldList = myRecipeViewModel.myRecipesState.value.data?.response
                    oldList!![currentData.position] = newData

                    myRecipeViewModel.setMyRecipeList(oldList)
                }
            }
            lifecycleScope.launch {
                if(isChecked){
                    if(bookmarkController.postBookmark(data.id, "RECIPE")){
                        setSnackBar(getString(R.string.toast_scrap_done))
                    }
                }else{
                    if(bookmarkController.deleteBookmark(data.id, "RECIPE")){
                        setSnackBar(getString(R.string.toast_scrap_undo))
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
        var snackbar:Snackbar
        if(nowFragmet == "MYPAGE"){
            snackbar = Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
        }else{
            snackbar = Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.toast_scrap_action)){
                    when(recipeViewModel.nowFragment.value){
                        "HOME"->{
                            mainNavigationHandler.navigateHomeToMyRecipe()
                        }
                        "RECIPE"->{
                            mainNavigationHandler.navigateTipsRecipeToMyRecipe()
                        }
                    }
                }
                .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
        }

        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = 200
        snackbarView.layoutParams = params

        snackbar.show()
    }
}