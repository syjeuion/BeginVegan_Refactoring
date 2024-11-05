package com.beginvegan.presentation.view.mypage.view

import androidx.navigation.fragment.findNavController
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentMypageEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageEditProfileFragment :
    BaseFragment<FragmentMypageEditProfileBinding>(FragmentMypageEditProfileBinding::inflate) {
    override fun init() {

        binding.ibBackUp.setOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
            findNavController().popBackStack()
        }
    }
}