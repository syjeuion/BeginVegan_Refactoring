package com.beginvegan.presentation.view.home.veganTest.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentVeganTestBinding
import com.beginvegan.presentation.util.setContentToolbar
import com.beginvegan.presentation.view.home.veganTest.view.compose.CustomRadioButton
import com.beginvegan.presentation.view.home.veganTest.viewModel.VeganTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VeganTestFragment :
    BaseFragment<FragmentVeganTestBinding>(FragmentVeganTestBinding::inflate) {
    private val PATCH_TYPE = "TEST"

    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val viewModel: VeganTestViewModel by activityViewModels()

    private var selectedVeganTypeNum = 0
    private lateinit var veganTestDescriptions: Array<String>
    private lateinit var veganTypes: List<String>

    override fun init() {
        veganTestDescriptions = requireNotNull(resources.getStringArray(R.array.vegan_test_descriptions))
        veganTypes = requireNotNull(resources.getStringArray(R.array.vegan_types_eng)).map { it.toString() }

        setToolbar()
        goBackUp()
        goResult()
        binding.composeView.setContent {
            setComposeUI()
        }
    }

    private fun setToolbar() {
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.vegan_test_title)
        )
    }

    //이동
    private fun goBackUp() {
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun goResult() {
        binding.tvGoResult.setOnClickListener {
            viewModel.setUserVeganTypeNum(selectedVeganTypeNum)

            patchVeganType(selectedVeganTypeNum)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.patchVeganTypeState.collect {
                    when (it) {
                        true -> {
                            Timber.d("PatchVeganType Successful")
                            mainNavigationHandler.navigateTestToVeganTestResult()
                        }

                        false -> Timber.e("PatchVeganType Failure")
                    }
                }
            }
        }
    }

    //retrofit
    private fun patchVeganType(typeNum: Int) {
        viewModel.patchVeganType(PATCH_TYPE, veganTypes[typeNum + 1])
    }

    /**
     * Compose로 Vegan Illust 구현
     */
    @Preview
    @Composable
    private fun setComposeUI() {
        var veganType by remember { mutableStateOf(veganTypes[selectedVeganTypeNum+1]) }
        MaterialTheme {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CustomRadioButton(
                    selected = veganType == veganTypes[1],
                    description = veganTestDescriptions[0],
                    isMilk = false, isEgg = false, isFish = false, isChicken = false, isMeat = false
                ) {
                    veganType = veganTypes[1]
                    selectedVeganTypeNum = 0
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[2],
                    description = veganTestDescriptions[1],
                    isMilk = true, isEgg = false, isFish = false, isChicken = false, isMeat = false
                ) {
                    veganType = veganTypes[2]
                    selectedVeganTypeNum = 1
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[3],
                    description = veganTestDescriptions[2],
                    isMilk = false, isEgg = true, isFish = false, isChicken = false, isMeat = false
                ) {
                    veganType = veganTypes[3]
                    selectedVeganTypeNum = 2
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[4],
                    description = veganTestDescriptions[3],
                    isMilk = true, isEgg = true, isFish = false, isChicken = false, isMeat = false
                ) {
                    veganType = veganTypes[4]
                    selectedVeganTypeNum = 3
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[5],
                    description = veganTestDescriptions[4],
                    isMilk = true, isEgg = true, isFish = true, isChicken = false, isMeat = false
                ) {
                    veganType = veganTypes[5]
                    selectedVeganTypeNum = 4
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[6],
                    description = veganTestDescriptions[5],
                    isMilk = true, isEgg = true, isFish = true, isChicken = true, isMeat = false
                ) {
                    veganType = veganTypes[6]
                    selectedVeganTypeNum = 5
                }
                CustomRadioButton(
                    selected = veganType == veganTypes[7],
                    description = veganTestDescriptions[6],
                    isMilk = true, isEgg = true, isFish = true, isChicken = true, isMeat = true
                ) {
                    veganType = veganTypes[7]
                    selectedVeganTypeNum = 6
                }
            }
        }
    }


}