package com.beginvegan.presentation.view.tips.view

import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beginvegan.domain.model.tips.MagazineContent
import com.beginvegan.domain.model.tips.TipsMagazineDetail
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.enumclass.Bookmarks
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentTipsMagazineDetailBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.util.setMagazineDetailToolbar
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.beginvegan.presentation.view.tips.viewModel.MagazineViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class TipsMagazineDetailFragment : BaseFragment<FragmentTipsMagazineDetailBinding>(FragmentTipsMagazineDetailBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    @Inject
    lateinit var bookmarkController: BookmarkController
    private val magazineViewModel:MagazineViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)
    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private var typeface: Typeface? = null

    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        loadingDialog = LoadingDialog.newInstance()
        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)

        magazineViewModel.magazineDetail.observe(this){
            if(loadingDialog.isAdded) loadingDialog.dismiss()
            if(it != null) setView(it)
        }
        setToolbar()
        goBackUp()
    }
    private fun setToolbar(){
        setMagazineDetailToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.magazine)
        )
    }
    private fun goBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun transferDate(date:String):String{
        val stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val newDate = LocalDateTime.parse(date, stringToDate)

        val dateToString = DateTimeFormatter.ofPattern("yyyy. MM. dd")
        return newDate.format(dateToString)
    }

    private fun setView(it: TipsMagazineDetail){
        with(binding){
            tvMagazineTitle.text = it.title
            tvWriter.text = it.editor
            tvDate.text = transferDate(it.createdDate)

            Glide.with(this@TipsMagazineDetailFragment)
                .load(it.thumbnail)
                .into(ivThumbnail)
        }
        for(content in it.magazineContents){
            createContentTextView(content)
        }
        with(binding.tbInterest){
            setOnCheckedChangeListener(null)
            isChecked = it.isBookmarked
            setOnCheckedChangeListener { _, isChecked ->
                lifecycleScope.launch {
                    if (isChecked) {
                        bookmarkController.postBookmark(it.id, Bookmarks.MAGAZINE)
                        if (mainViewModel.fromMyMagazine.value!!) {
                            Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_done), Snackbar.LENGTH_SHORT)
                                .apply {
                                    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                                    show()
                                }
                        } else {
                            setSnackBar(getString(R.string.toast_scrap_done))
                        }
                    } else {
                        bookmarkController.deleteBookmark(it.id, Bookmarks.MAGAZINE)
                        if (mainViewModel.fromMyMagazine.value!!) {
                            Snackbar.make(binding.clLayout, getString(R.string.toast_scrap_undo), Snackbar.LENGTH_SHORT)
                                .apply {
                                    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                                    show()
                                }
                        } else {
                            setSnackBar(getString(R.string.toast_scrap_undo))
                        }
                    }
                    it.isBookmarked = isChecked
                    magazineViewModel.setMagazineDetail(it)
                }
            }
        }
    }

    private fun createContentTextView(content: MagazineContent){
        val textView = TextView(context).apply {
            text = content.content
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.color_text_01))
            gravity = Gravity.CENTER
            if(content.isBold) setTypeface(null, Typeface.BOLD)
        }
        textView.typeface = typeface

        val parentLayout = binding.llMagazineContents
        parentLayout.addView(textView)
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setFromMyMagazine(false)
    }

    private fun setSnackBar(message:String){
        Snackbar.make(binding.clLayout, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.toast_scrap_action)) {
                mainNavigationHandler.navigateTipsMagazineDetailToMyMagazine()
            }
            .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
            .apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTypeface(typeface)
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).setTypeface(typeface)
                show()
            }
    }
}