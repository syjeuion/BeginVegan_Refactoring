package com.beginvegan.presentation.view.home.veganTest.view

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentVeganTestResultBinding
import com.beginvegan.presentation.view.home.veganTest.viewModel.VeganTestViewModel
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VeganTestResultFragment : BaseFragment<FragmentVeganTestResultBinding>(R.layout.fragment_vegan_test_result) {

    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val viewModel: VeganTestViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)

    private lateinit var veganTypes:Array<String>
    private lateinit var resultDescriptions:Array<String>
    private lateinit var resultExplanations:Array<String>
    private var typeNum = 0

    override fun init() {
        binding.lifecycleOwner = this

        veganTypes = resources.getStringArray(R.array.vegan_type)
        resultDescriptions = resources.getStringArray(R.array.vegan_test_result_descriptions)
        resultExplanations = resources.getStringArray(R.array.vegan_test_result_explanations)


        setUI()
//        binding.tvDescription.text = 유저네임
//        binding.tvGoRecommendRecipe.text = 유저 네임

        goBackUp()
        goRecommendRecipe()
    }
    private fun setUI(){
        viewModel.userVeganTypeNum.observe(this, Observer {
            typeNum = viewModel.userVeganTypeNum.value!!
            binding.tvResultVeganType.text = veganTypes[typeNum+1]
            binding.tvVeganTypeDescription.text = resultDescriptions[typeNum]
            binding.tvResultExplanation.text = resultExplanations[typeNum]
            setIllus()
        })

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.nickName.collect { userName ->
                with(binding){
                    tvDescription.text = setUserNameColor(getString(R.string.vegan_test_result_description,userName), userName)
                    tvGoRecommendRecipe.text = setUserNameColor(getString(R.string.vegan_test_result_recommend_recipe,userName), userName)
                }
            }
        }
    }
    private fun setUserNameColor(fullString:String,userName:String):SpannableString{
        val spannableString = SpannableString(fullString)
        val start = fullString.indexOf(userName)
        val end = start + userName.length
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.color_primary))
        spannableString.setSpan(
            colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun setIllus(){
        val levels = listOf(
            { binding.includedIllusVeganLevel.milk = true },
            { binding.includedIllusVeganLevel.egg = true },
            { binding.includedIllusVeganLevel.fish = true },
            { binding.includedIllusVeganLevel.chicken = true },
            { binding.includedIllusVeganLevel.meat = true }
        )
        when(typeNum){
            0 -> return
            1 -> levels[0]()
            2 -> levels[1]()
            else -> {
                for (i in 0 until typeNum-1) {
                    levels[i]()
                }
            }
        }
    }

    //이동
    private fun goBackUp(){
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun goRecommendRecipe(){
        binding.tvBtnGoRecommendRecipe.setOnClickListener {
            mainViewModel.setFromTest(true)
            mainNavigationHandler.navigateTestResultToTips()
        }
    }
}